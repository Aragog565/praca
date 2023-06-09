package sklep.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sklep.entity.User;
import sklep.service.FavoriteProductService;
import sklep.service.RateService;
import sklep.service.dto.Create.CreateFavoriteProductDTO;
import sklep.service.dto.Create.CreateRateDTO;
import sklep.service.dto.FavoriteProductDTO;
import sklep.service.dto.ProductDTO;
import sklep.service.dto.RateDTO;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path="/api/products/")
public class FavoriteProductController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final FavoriteProductService favoriteProductService;

    @Autowired
    public FavoriteProductController(FavoriteProductService favoriteProductService) {
        this.favoriteProductService = favoriteProductService;
    }

    @PostMapping("{productID}/favorite")
    public ResponseEntity<FavoriteProductDTO> addFavoriteProduct(
            @PathVariable final Long productID,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        log.debug("Request to save: Rate  in product {}",  productID);
        FavoriteProductDTO favoriteProductDTO = favoriteProductService.save(authenticatedUser,productID);
        return ResponseEntity.ok(favoriteProductDTO);

    }

    @DeleteMapping("{productID}/favorite")
    public ResponseEntity<FavoriteProductDTO> removeFavoriteProduct(
            @PathVariable final Long productID,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        log.debug("Request to save: Rate  in product {}",  productID);
        favoriteProductService.remove(authenticatedUser,productID);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("favorite")
    public ResponseEntity<FavoriteProductDTO> removeFavoriteProducts(
            @AuthenticationPrincipal User authenticatedUser,
            @RequestParam(required = false) Long[] ids
    ) {

        favoriteProductService.remove(authenticatedUser, ids);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("favorite")
    public ResponseEntity<List<FavoriteProductDTO>> getFavoriteProduct(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long[] ids,
            @RequestParam(defaultValue = "") String name,
            @AuthenticationPrincipal User authenticatedUser
    ){
        log.debug("Request to get: FavoriteProduct.");

        List<FavoriteProductDTO> favoriteProductDTOS = favoriteProductService.getFavoriteProduct(categoryId, name, ids, authenticatedUser);
        return ResponseEntity.ok(favoriteProductDTOS );
    }

}
