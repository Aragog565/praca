package sklep.config;

import sklep.service.exception.EntityAlreadyExistsException;
import sklep.service.exception.EntityNotFoundException;
import sklep.service.exception.ForbiddenException;
import sklep.service.exception.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@ControllerAdvice
public class ApiExceptionHandler {



    @ExceptionHandler({
            EntityAlreadyExistsException.class,
            EntityNotFoundException.class,
            ForbiddenException.class,
            BadRequestException.class,
    })
    public ResponseEntity<Object> handleCustomException(HttpServletRequest req, Exception ex){


        HttpStatus status;
        if(ex instanceof EntityAlreadyExistsException){
            status = HttpStatus.BAD_REQUEST;
        }else if(ex instanceof EntityNotFoundException){
            status = HttpStatus.NOT_FOUND;
        }else if(ex instanceof BadRequestException){
            status = HttpStatus.BAD_REQUEST;
        }else{
            status = HttpStatus.FORBIDDEN;
        }

        String body = ex.getMessage();

        return new ResponseEntity<>(
                Collections.singletonMap("message", body),
                status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleArgumentNotValidException(HttpServletRequest req, MethodArgumentNotValidException ex){

        HttpStatus status = HttpStatus.BAD_REQUEST;
        HashMap<String, List<String>> body = new HashMap<>();
        for(FieldError error : ex.getFieldErrors()){
            List<String> messages = Collections.emptyList();
            if(error.getDefaultMessage() != null) {
                messages = Arrays.asList(error.getDefaultMessage().split(","));
            }
            body.put(error.getField(), messages);
        }

        return new ResponseEntity<>(
                Collections.singletonMap("message", body.size() > 0 ? body : ex.getMessage()),
                status);
    }
}
