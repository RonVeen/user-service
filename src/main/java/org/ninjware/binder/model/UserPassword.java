package org.ninjware.binder.model;

import lombok.Builder;
import lombok.Setter;
import lombok.Getter;

@Builder
@Getter
@Setter
public class UserPassword {
    private char[] password;
    private String salt;
}
