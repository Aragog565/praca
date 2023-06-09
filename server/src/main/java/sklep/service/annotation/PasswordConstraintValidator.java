package sklep.service.annotation;

import com.google.common.base.Joiner;
import org.passay.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword,String>{
    @Override
    public void initialize(ValidPassword constraintAnnotation) {}

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        final PasswordValidator passwordValidator = new PasswordValidator(Arrays.asList(
                new LengthRule(7,20),
                new UppercaseCharacterRule(1),
                new SpecialCharacterRule(1),
                new DigitCharacterRule(1),
                new WhitespaceRule()));
        final RuleResult result = passwordValidator.validate(new PasswordData(password));
        if(result.isValid()) {
            return true;
        }
        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext
                .buildConstraintViolationWithTemplate(Joiner.on(",")
                        .join(passwordValidator.getMessages(result))).addConstraintViolation();

        return false;
    }
}
