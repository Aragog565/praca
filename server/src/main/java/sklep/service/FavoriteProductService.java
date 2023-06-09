package sklep.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sklep.entity.FavoriteProduct;
import sklep.entity.Product;
import sklep.entity.User;
import sklep.repository.FavoriteProductRepository;
import sklep.repository.ProductRepository;
import sklep.repository.UserRepository;
import sklep.service.dto.FavoriteProductDTO;
import sklep.service.dto.ProductDTO;
import sklep.service.exception.BadRequestException;
import sklep.service.exception.EntityNotFoundException;
import sklep.service.exception.ForbiddenException;
import sklep.service.exception.NoContentException;
import sklep.service.mapper.FavoriteProductMapper;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class FavoriteProductService {
    private final static Logger log = LoggerFactory.getLogger(FavoriteProductService.class);

    private final FavoriteProductMapper favoriteProductMapper;
    private final FavoriteProductRepository favoriteProductRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final SecurityService securityService;

    @Autowired
    public FavoriteProductService(FavoriteProductMapper rateMapper,
                                  FavoriteProductRepository rateRepository,
                                  UserRepository userRepository,
                                  ProductRepository productRepository,
                                  SecurityService securityService) {
        this.favoriteProductMapper = rateMapper;
        this.favoriteProductRepository = rateRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.securityService = securityService;
    }


    public FavoriteProductDTO save( User authenticatedUser, Long productID){
        log.debug("Saving: Favorite in product {}",  productID);
        Product product = productRepository.findById(productID)
                .orElseThrow(() -> new EntityNotFoundException("Product with requested id doesn't exists."));
        if(authenticatedUser==null){
            throw new ForbiddenException("zaloguj się");
        }

        FavoriteProduct favoriteProduct = favoriteProductRepository.findByProduct_IdAndUser_IdOrderByCreatedAtDesc(productID, authenticatedUser.getId());

        if(favoriteProduct!=null){
            throw new BadRequestException("dodane kiedyś");
        }

        favoriteProduct = new FavoriteProduct();
        favoriteProduct.setCreatedAt(new Timestamp((System.currentTimeMillis())));
        favoriteProduct.setProduct(product);
        favoriteProduct.setUser(authenticatedUser);
        favoriteProduct.setLastModificationAt(new Timestamp(System.currentTimeMillis()));
        return favoriteProductMapper.toDto(favoriteProductRepository.save(favoriteProduct));
    }

    public void remove(User authenticatedUser, Long productID){
        log.debug("Saving: Rate in product {}",  productID);
        Product product = productRepository.findById(productID)
                .orElseThrow(() -> new EntityNotFoundException("Product with requested id doesn't exists."));
        if(authenticatedUser==null){
            throw new ForbiddenException("zaloguj się");
        }

        FavoriteProduct favoriteProduct = favoriteProductRepository.findByProduct_IdAndUser_IdOrderByCreatedAtDesc(productID, authenticatedUser.getId());

        if(favoriteProduct==null){
            throw new BadRequestException("nie jest polubiony");
        }
        favoriteProductRepository.delete(favoriteProduct);
    }

    public void remove(User authenticatedUser,  Long[] ids){
        if(authenticatedUser==null){
            throw new ForbiddenException("zaloguj się");
        }
        if(ids != null && ids.length > 0){
            favoriteProductRepository.deleteAllById(Arrays.asList(ids));
        }
    }
    public List<FavoriteProductDTO> getFavoriteProduct(Long categoryId, String name, Long[] ids, User user){

        List<FavoriteProduct> favoriteProducts;
        if(ids != null && ids.length > 0){
            favoriteProducts =  favoriteProductRepository.findAllByIds(ids);
        }else{
            if(categoryId != null){
                favoriteProducts =  favoriteProductRepository.findAll(categoryId, name);
            }if(name != ""){
                favoriteProducts =  favoriteProductRepository.findAll(name);
            }else {
                favoriteProducts = favoriteProductRepository.findAllByUser_IdOrderByCreatedAtDesc(user.getId());
            }
        }


        return favoriteProductMapper.toDto(favoriteProducts);
    }
}
