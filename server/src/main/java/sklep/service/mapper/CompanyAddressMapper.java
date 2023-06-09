package sklep.service.mapper;

import org.mapstruct.Mapper;
import sklep.entity.Address;
import sklep.entity.CompanyAddress;

import sklep.service.dto.CompanyAddressDTO;
import sklep.service.dto.Create.CreateCompanyAddressDTO;
import sklep.service.dto.Update.UpdateAddressDTO;
import sklep.service.dto.Update.UpdateCompanyAddressDTO;

@Mapper(componentModel = "spring")
public interface CompanyAddressMapper extends EntityMapper<CompanyAddress, CompanyAddressDTO>{

    CompanyAddress toCompanyAddressFromCreateDTO(CreateCompanyAddressDTO createCompanyAddressDTO);
    CompanyAddress toCompanyAddressFromUpdateCompanyAddressDTO(UpdateCompanyAddressDTO updateCompanyAddressDTO);

}
