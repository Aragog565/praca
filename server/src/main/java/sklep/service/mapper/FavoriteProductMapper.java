package sklep.service.mapper;

import org.mapstruct.Mapper;
import sklep.entity.FavoriteProduct;
import sklep.entity.Rate;
import sklep.service.dto.Create.CreateFavoriteProductDTO;
import sklep.service.dto.Create.CreateRateDTO;
import sklep.service.dto.FavoriteProductDTO;
import sklep.service.dto.RateDTO;

@Mapper(componentModel = "spring")
public interface FavoriteProductMapper extends EntityMapper<FavoriteProduct, FavoriteProductDTO> {
    FavoriteProduct toFavoriteProductFromCreatedFavoriteProductDTO(CreateFavoriteProductDTO createFavoriteProductDTO);
    FavoriteProduct toEntity(FavoriteProductDTO favoriteProductDTO);
    FavoriteProductDTO toDto(FavoriteProduct favoriteProduct);

}
