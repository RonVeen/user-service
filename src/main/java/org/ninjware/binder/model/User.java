package org.ninjware.binder.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.ninjaware.binder.rest.UserStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
public class User {

    private String id;
    private String internalId;
    private UserStatus status;
    private String email;
    private String name;
    private boolean passwordChangeRequired;
    private LocalDate memberSince;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;

}
