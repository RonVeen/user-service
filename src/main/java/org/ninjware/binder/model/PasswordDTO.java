package org.ninjware.binder.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PasswordDTO {
  private char[] password;
}
