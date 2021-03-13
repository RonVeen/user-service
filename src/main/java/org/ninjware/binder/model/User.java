package org.ninjware.binder.model;

import lombok.*;
import org.ninjaware.binder.rest.UserStatus;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "user",
        schema = "public",
        indexes = {
        @Index(columnList = "id"),
        @Index(name = "internalId", columnList = "internalId"),
})
public class User {
    @Id @Column(unique = true, updatable = false, nullable = false) private String id;
    @Column private String internalId;
    @Enumerated(EnumType.STRING) private UserStatus status;
    @Column private String email;
    @Column private String name;
    @Column private boolean passwordChangeRequired;
    @Column private LocalDate memberSince;
    @Column private LocalDateTime lastLogin;
    @Column private LocalDateTime createdAt;
    @Column private String createdBy;
    @Column private LocalDateTime updatedAt;
    @Column private String updatedBy;
}
