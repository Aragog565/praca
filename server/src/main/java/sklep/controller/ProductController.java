package sklep.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sklep.entity.User;
import sklep.service.ProductService;
import sklep.service.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sklep.service.dto.Create.CreateProductDTO;
import sklep.service.dto.Update.UpdateProductDTO;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path="/api/products")
public class ProductController {
    private static final Logger log = LoggerFactory.getLogger(SecurityController.class);
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductDTO> productRegister(
            @ModelAttribute @Validated CreateProductDTO createProductDTO,
            @AuthenticationPrincipal User authenticatedUser
    )throws IOException {
        log.debug("Request to save: product {}", createProductDTO);

        ProductDTO productDTO = productService.saveProduct(createProductDTO,authenticatedUser);

        return ResponseEntity.ok(productDTO);
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long[] ids,
            @RequestParam(defaultValue = "") String name,
            @AuthenticationPrincipal User authenticatedUser
    ){
        log.debug("Request to get: product.");

        List<ProductDTO> productDTOS  = productService.getProduct(categoryId, name, ids, authenticatedUser);

        return ResponseEntity.ok(productDTOS );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductByID(
            @PathVariable final Long id,
            @AuthenticationPrincipal User authenticatedUser
    ){
        ProductDTO productDTO = productService.getProductById(id, authenticatedUser);

        return ResponseEntity.ok(productDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable final Long id,
            @AuthenticationPrincipal User authenticatedUser
    ){
        log.debug("Request to delete: product {} by {}", id, authenticatedUser);

        productService.deleteProduct(id, authenticatedUser);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<ProductDTO> deleteProducts(
            @AuthenticationPrincipal User authenticatedUser,
            @RequestParam(required = false) Long[] ids
    ) {

        productService.deleteProducts(authenticatedUser, ids);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(
            @PathVariable final Long id,
            @ModelAttribute @Validated UpdateProductDTO updateProductDTO,
            @AuthenticationPrincipal User authenticatedUser
    )throws URISyntaxException, IOException{
        log.debug("Request to update: product {} by {}", id, authenticatedUser);

        productService.updateProduct(id, updateProductDTO, authenticatedUser);

        return ResponseEntity.noContent().build();
    }

}
