package sklep.service.mapper;
import org.mapstruct.Mapper;
import sklep.entity.Product;
import sklep.service.dto.ProductDTO;
import sklep.service.dto.Create.CreateProductDTO;
import sklep.service.dto.Update.UpdateProductDTO;

@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<Product, ProductDTO> {
    Product toProductFromCreateDTO(CreateProductDTO createProductDTO);
    Product toProductFromUpdateProductDTO(UpdateProductDTO updateProductDTO);

}
