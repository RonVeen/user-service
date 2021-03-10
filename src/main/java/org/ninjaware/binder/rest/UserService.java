package org.ninjaware.binder.rest;


import org.ninjware.binder.model.User;
import org.ninjware.binder.model.UserDTO;

import javax.inject.Singleton;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.Objects.isNull;
import static org.ninjaware.binder.rest.UserConstants.SYSTEM_USER;

@Singleton
public class UserService {

    private static Map<String, User> users = new HashMap<>();

    User getUserById(String id, boolean includeDeleted) {
        User user = users.get(id);
        if (isNull(user)) {
            return null;
        }
        if (!includeDeleted && user.getStatus().equals(UserStatus.Deleted)) {
            return null;
        }
        return users.get(id);
    }

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
        users.put(user.getId(), user);
        return user;
    }

    User updateUser(String id, UserDTO dto) {
        User user = users.get(id);
        if (isNull(user)) {
            return null;
        }
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setUpdatedAt(LocalDateTime.now());
        user.setUpdatedBy(SYSTEM_USER);
        users.put(user.getId(), user);
        return user;
    }

    User deleteUser(String id) {
        User user = users.get(id);
        if (isNull(user)) {
            return null;
        }
        user.setStatus(UserStatus.Deleted);
        return user;
    }



}
