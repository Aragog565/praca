package sklep.service.mapper;

import org.mapstruct.Mapper;
import sklep.entity.Address;
import sklep.entity.User;
import sklep.service.dto.AddressDTO;
import sklep.service.dto.Create.CreateAddressDTO;
import sklep.service.dto.Update.UpdateAddressDTO;
import sklep.service.dto.Update.UpdateUserDTO;

@Mapper(componentModel = "spring")
public interface AddressMapper extends EntityMapper<Address, AddressDTO>{

    Address toAddressFromCreateDTO(CreateAddressDTO createAddressDTO);
    Address toAddressFromUpdateAddressDTO(UpdateAddressDTO updateAddressDTO);

}
