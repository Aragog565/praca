package sklep.service.annotation;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import sklep.service.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UpdateEmailConstraintValidator implements ConstraintValidator<ValidUpdateEmail, String> {

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        if(email==null){
            return true;
        }
        return EmailValidator.getInstance().isValid(email);
    }
}
