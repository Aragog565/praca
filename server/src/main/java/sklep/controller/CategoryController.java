package sklep.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sklep.entity.User;
import sklep.service.CategoryService;
import sklep.service.dto.CategoryDTO;
import sklep.service.dto.Create.CreateCategoryDTO;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path="/api/categories")
public class CategoryController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> addCategory(
            @RequestBody @Validated CreateCategoryDTO createCategoryDTO,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        log.debug("Request to save: Category " );

        CategoryDTO categoryDTO = categoryService.save(createCategoryDTO, authenticatedUser);

        return ResponseEntity.ok(categoryDTO);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getCategory(){
        log.debug("Request to get: category.");

        List<CategoryDTO> categoryDTOS  = categoryService.getCategory();

        return ResponseEntity.ok(categoryDTOS );
    }


}
