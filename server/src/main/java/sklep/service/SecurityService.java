package sklep.service;

import org.springframework.stereotype.Service;
import sklep.config.Constants;
import sklep.entity.Role;
import sklep.entity.User;
import sklep.service.exception.ForbiddenException;

import java.util.Objects;
import java.util.Set;

@Service
public class SecurityService {
    public void checkPermission(User authenticatedUser,Long userID){
        if(authenticatedUser == null ||
                (!Objects.equals(authenticatedUser.getRole().getName(), Constants.Role.ADMIN.name)&&
                !Objects.equals(authenticatedUser.getId(), userID))
        ){
            throw new ForbiddenException("nie ma uprawnień--SecurityService");
        }

    }

    public void checkAdmin(User authenticatedUser){
        if(authenticatedUser == null || !Objects.equals(authenticatedUser.getRole().getName(), Constants.Role.ADMIN.name)){
            throw new ForbiddenException("nie ma uprawnień--SecurityService");
        }
    }
}
