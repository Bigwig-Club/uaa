package dev.bigwig.uaa.validation;

import dev.bigwig.uaa.annotation.ValidPassword;
import java.util.Arrays;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.val;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.EnglishSequenceData;
import org.passay.IllegalSequenceRule;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.WhitespaceRule;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

  @Override
  public boolean isValid(String password, ConstraintValidatorContext context) {
    val validator = new org.passay.PasswordValidator(Arrays.asList(
      new LengthRule(8, 30),
      new CharacterRule(EnglishCharacterData.UpperCase, 1),
      new CharacterRule(EnglishCharacterData.LowerCase, 1),
      new CharacterRule(EnglishCharacterData.Special, 1),
      new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 5, false),
      new IllegalSequenceRule(EnglishSequenceData.Numerical, 5, false),
      new IllegalSequenceRule(EnglishSequenceData.USQwerty, 5, false),
      new WhitespaceRule()
    ));
    val result = validator.validate(new PasswordData(password));
    return result.isValid();
  }
}
