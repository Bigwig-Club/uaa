package dev.bigwig.uaa.domain.dto;

import dev.bigwig.uaa.annotation.PasswordMatch;
import dev.bigwig.uaa.annotation.ValidPassword;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
@PasswordMatch
public class UserDTO {

  @NotNull
  @NotBlank
  @Size(min = 4, max = 50, message = "用户名长度必须在 4-50 之间")
  private String username;
  @NotNull
  @ValidPassword
  private String password;
  private String matchPassword;
  @Email
  @NotNull
  private String email;
  @NotNull
  @NotBlank
  @Size(min = 4, max = 50, message = "姓名长度必须在 4-50 之间")
  private String name;
}
