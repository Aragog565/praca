package sklep.service.annotation;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import sklep.service.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailConstraintValidator implements ConstraintValidator<ValidEmail, String> {
    private final UserService userService;

    @Autowired
    public EmailConstraintValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {

        if(userService.existsByEmail(email)){
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Użytkownik z tym adresem e-mail już istnieje.").addConstraintViolation();
            return false;
        }
        return EmailValidator.getInstance().isValid(email);
    }
}
