package sklep.service.mapper;



import org.mapstruct.Mapper;
import sklep.entity.Rate;
import sklep.service.dto.Create.CreateRateDTO;
import sklep.service.dto.RateDTO;

@Mapper(componentModel = "spring")
public interface RateMapper extends EntityMapper<Rate, RateDTO>{
    Rate toRateFromCreatedRateDTO(CreateRateDTO createRateDTO);
    Rate toEntity(RateDTO rateDTO);
    RateDTO toDto(Rate rate);

}
