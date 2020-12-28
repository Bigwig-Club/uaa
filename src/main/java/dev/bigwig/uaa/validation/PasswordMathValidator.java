package dev.bigwig.uaa.validation;

import dev.bigwig.uaa.annotation.PasswordMatch;
import dev.bigwig.uaa.domain.dto.UserDTO;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMathValidator implements ConstraintValidator<PasswordMatch, UserDTO> {

  @Override
  public boolean isValid(UserDTO value, ConstraintValidatorContext context) {
    return value.getPassword().equals(value.getMatchPassword());
  }
}
