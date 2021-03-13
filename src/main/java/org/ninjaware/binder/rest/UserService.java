package org.ninjaware.binder.rest;


import org.ninjware.binder.model.User;
import org.ninjware.binder.model.UserDTO;

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



}
