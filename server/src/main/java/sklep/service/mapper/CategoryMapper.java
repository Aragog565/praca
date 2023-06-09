package sklep.service.mapper;

import org.mapstruct.Mapper;
import sklep.entity.Category;
import sklep.service.dto.CategoryDTO;
import sklep.service.dto.Create.CreateCategoryDTO;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends EntityMapper<Category, CategoryDTO>{

    Category toCategoriesFromCreateDTO(CreateCategoryDTO createCategoryDTO);

}
