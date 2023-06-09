package sklep.service.annotation;


import sklep.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UpdateUsernameConstraintValidator implements ConstraintValidator<ValidUpdateName, String> {
    private final UserService userService;

    @Autowired
    public UpdateUsernameConstraintValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        if(name == null){
            return true;
        }
        if( name.length() < 3){
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Imię użytkownika powinna mieć co najmniej 3 znaki.").addConstraintViolation();
            return false;
        }

        return true;
    }
}
