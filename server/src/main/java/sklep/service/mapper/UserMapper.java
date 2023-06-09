package sklep.service.mapper;

import sklep.entity.User;
import sklep.service.dto.Create.RegisterDTO;
import sklep.service.dto.Update.UpdateUserDTO;
import sklep.service.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<User, UserDTO> {
    User toUserFromRegisterDTO(RegisterDTO registerDTO);
    User toUserFromUpdateUserDTO(UpdateUserDTO updateUserDTO);
}
