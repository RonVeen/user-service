package org.ninjware.binder.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Entity
@Table(name = "PasswordInfo",
        schema = "public",
        indexes = { @Index(columnList = "internalId") }
)
public class PasswordInfo {
    @Column @Id private String internalId;
    @Column private String hash;
    @Column private String salt;
    @Column private LocalDateTime createdAt;
    @Column private String createdBy;
    @Column private LocalDateTime updatedAt;
    @Column private String updatedBy;

}
