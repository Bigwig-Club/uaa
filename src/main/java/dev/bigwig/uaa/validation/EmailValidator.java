package dev.bigwig.uaa.validation;

import dev.bigwig.uaa.annotation.ValidEmail;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.val;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

  private final static String EMAIL_PATTERN = "^([A-Za-z0-9_\\-.\\u4e00-\\u9fa5])+@([A-Za-z0-9_\\-.])+\\.([A-Za-z]{2,8})$";

  @Override
  public boolean isValid(String email, ConstraintValidatorContext context) {
    return validateEmail(email);
  }

  private boolean validateEmail(final String email) {
    val pattern = Pattern.compile(EMAIL_PATTERN);
    val matcher = pattern.matcher(email);
    return matcher.matches();
  }
}
