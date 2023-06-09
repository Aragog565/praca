package sklep.service;

import org.springframework.stereotype.Service;
import sklep.entity.Category;
import sklep.entity.Product;
import sklep.entity.User;
import sklep.repository.CategoryRepository;
import sklep.service.dto.CategoryDTO;
import sklep.service.dto.Create.CreateCategoryDTO;
import sklep.service.exception.BadRequestException;
import sklep.service.exception.EntityAlreadyExistsException;
import sklep.service.exception.EntityNotFoundException;
import sklep.service.exception.ForbiddenException;
import sklep.service.mapper.CategoryMapper;

import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryService {

    private final static Logger log = LoggerFactory.getLogger(ParameterService.class);
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final SecurityService securityService;

    public CategoryService(CategoryRepository categoryRepository,
                           CategoryMapper categoryMapper,
                           SecurityService securityService) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.securityService = securityService;
    }

    public CategoryDTO save(CreateCategoryDTO createCategoryDTO, User user){
        log.debug(" create category");

        if(user==null) {
            throw new ForbiddenException("zaloguj się--saveProduct--ProductService");
        }
        securityService.checkAdmin(user);

        Optional<Category> category = this.categoryRepository.findByName(createCategoryDTO.getName());
        if(category.isPresent()){
            throw new EntityAlreadyExistsException("already exist ");
        }


        Category newCategory = this.categoryMapper.toCategoriesFromCreateDTO(createCategoryDTO);
        newCategory = this.categoryRepository.save(newCategory);

        return this.categoryMapper.toDto(newCategory);
    }

    public List<CategoryDTO> getCategory(){
        log.debug("Fetching all Category");

        List<Category> category = categoryRepository.findAll();



        return categoryMapper.toDto(category);
    }

}
