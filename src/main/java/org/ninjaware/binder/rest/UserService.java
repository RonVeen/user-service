package org.ninjaware.binder.rest;


import io.quarkus.arc.ArcUndeclaredThrowableException;
import org.ninjaware.binder.HashService;
import org.ninjaware.binder.SaltService;
import org.ninjware.binder.model.PasswordInfo;
import org.ninjware.binder.model.User;
import org.ninjware.binder.model.UserDTO;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.DAYS;
import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;
import static org.ninjaware.binder.rest.UserConstants.SYSTEM_USER;

@Singleton
public class UserService {

    @PersistenceContext
    EntityManager entityManager;

    private HashService hashService;
    private SaltService saltService;

    @Inject
    public UserService(HashService hashService, SaltService saltService) {
        this.hashService = hashService;
        this.saltService = saltService;
    }


    @Transactional(SUPPORTS)
    Optional<User> findUserById(String id, boolean includeDeleted) {
        TypedQuery<User> query = null;
        if (includeDeleted) {
            query = entityManager.createQuery("SELECT u FROM User u WHERE u.id=:id", User.class);
            query.setParameter("id", id);
        } else {
            query = entityManager.createQuery("SELECT u FROM User u WHERE u.id=:id and u.status <> :status", User.class);
            query.setParameter("id", id);
            query.setParameter("status", UserStatus.Deleted);
        }
        return query.getResultStream().findFirst();
    }


    @Transactional(SUPPORTS)
    private Optional<PasswordInfo> findPasswordInfoByInternalId(String internalId) {
        TypedQuery query = entityManager.createQuery("SELECT pi FROM PasswordInfo pi WHERE pi.internalId=:internalId", PasswordInfo.class);
        query.setParameter("internalId", internalId);
        return query.getResultStream().findFirst();
    }


    @Transactional(REQUIRED)
    User createUser(UserDTO dto) {
        User user = User.builder()
                .createdAt(LocalDateTime.now())
                .createdBy(SYSTEM_USER)
                .email(dto.getEmail())
                .id(UUID.randomUUID().toString())
                .internalId(UUID.randomUUID().toString())
                .memberSince(LocalDate.now().minus(139, DAYS))
                .name(dto.getName())
                .passwordChangeRequired(true)
                .status(UserStatus.Active)
                .build();
        entityManager.persist(user);
        return user;
    }

    @Transactional(REQUIRED)
    User updateUser(String id, UserDTO dto) {
        Optional<User> userOpt = findUserById(id, false);
        if (userOpt.isEmpty()) {
            return null;
        }
        User user = userOpt.get();
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setUpdatedAt(LocalDateTime.now());
        user.setUpdatedBy(SYSTEM_USER);
        entityManager.persist(user);
        return user;
    }

    @Transactional(REQUIRED)
    User deleteUser(String id) {
        Optional<User> userOpt = findUserById(id, false);
        if (userOpt.isEmpty()) {
            return null;
        }
        User user = userOpt.get();
        user.setStatus(UserStatus.Deleted);
        entityManager.persist(user);
        return user;
    }


    @Transactional(REQUIRED)
    /**
     * See https://howtodoinjava.com/java/java-security/java-aes-encryption-example/
     * See https://mvnrepository.com/artifact/org.mindrot/jbcrypt/0.4
     * See http://www.mindrot.org/projects/jBCrypt/
     * See https://blog.codinghorror.com/youre-probably-storing-passwords-incorrectly/
     */
    public boolean setPassword(String id, char[] password) {
        Optional<User> userOpt = findUserById(id, false);
        if (userOpt.isEmpty()) {
            return false;
        }
        User user = userOpt.get();

        PasswordInfo info;
        Optional<PasswordInfo> passwordInfoOpt = findPasswordInfoByInternalId(user.getInternalId());
        if (passwordInfoOpt.isEmpty()) {
            info = new PasswordInfo();
            info.setInternalId(user.getInternalId());
            info.setSalt(new String(saltService.generate()));
            info.setCreatedAt(LocalDateTime.now());
            info.setCreatedBy(SYSTEM_USER);
        } else {
            info = passwordInfoOpt.get();
        }

        String hash = hashService.hash(password, info.getSalt().getBytes());
        info.setHash(hash);
        info.setUpdatedAt(LocalDateTime.now());
        info.setUpdatedBy(SYSTEM_USER);
        try {
            entityManager.persist(info);
        } catch (ArcUndeclaredThrowableException e) {
            System.err.println(e);
        }

        return true;
    }



}
