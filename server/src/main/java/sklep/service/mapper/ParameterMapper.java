package sklep.service.mapper;

import org.mapstruct.Mapper;
import sklep.entity.Parameters;
import sklep.service.dto.Create.CreateParameterDTO;
import sklep.service.dto.ParameterDTO;

import java.util.Set;


@Mapper(componentModel = "spring")
public interface ParameterMapper extends EntityMapper<Parameters, ParameterDTO>{
    Parameters toParameterFromCreateDTO(CreateParameterDTO createParameterDTO);

    Set<Parameters> toParameterFromCreateDTO(Set<CreateParameterDTO> createParameterDTO);
}
