package sklep.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sklep.entity.Category;
import sklep.entity.Product;
import sklep.entity.User;
import sklep.repository.CategoryRepository;
import sklep.repository.ProductRepository;
import sklep.service.dto.Create.CreateProductDTO;
import sklep.service.dto.ProductDTO;
import sklep.service.dto.Update.UpdateProductDTO;
import sklep.service.exception.BadRequestException;
import sklep.service.exception.EntityNotFoundException;
import sklep.service.exception.ForbiddenException;
import sklep.service.mapper.ProductMapper;
import sklep.utils.FileUploadUtil;

import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class ProductService {
    public static final String uploadDir = "photo/product/";
    private final static Logger log = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final SecurityService securityService;
    private final ParameterService parameterService;
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductService(ProductRepository productRepository,
                          ProductMapper productMapper,
                          SecurityService securityService,
                          ParameterService parameterService,
                          CategoryService categoryService,
                          CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.securityService = securityService;
        this.parameterService = parameterService;
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
    }

    public ProductDTO getProductById(Long id, User authUser){
        log.debug("Fetching user by id: {}", id);

        Product product = this.productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product with requested id doesn't exist."));


        return this.map(product, authUser);
    }

    public ProductDTO saveProduct(CreateProductDTO createProductDTO, User user)throws IOException {
        log.debug("Saving product : {}", createProductDTO);

        if(user==null){
            throw new ForbiddenException("zaloguj się--saveProduct--ProductService");
        }
        Product product = productMapper.toProductFromCreateDTO(createProductDTO);

        product.setUser(user);
        product.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        String fileName = FileUploadUtil.createFile(uploadDir, createProductDTO.getPhotoFile(), true);
        product.setPhoto(fileName);

        Category category = this.categoryRepository.findById(createProductDTO.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("not found category"));
        product.setCategory(category);

        Product newProduct = productRepository.save(product);
        this.parameterService.saveParameter(createProductDTO.getParameters(), newProduct);
        return this.map(newProduct, user);
    }

    public List<ProductDTO> getProduct(Long categoryId, String name, Long[] ids, User user){
        log.debug("Fetching all product");

        List<Product> product;

        if(ids != null && ids.length > 0){
            product = productRepository.findAllByIds(ids);
        }else{
            if(categoryId != null){
                product = productRepository.findAll(categoryId, name);
            }else{
                product = productRepository.findAll(name);
            }
        }




        return map(product,user);
    }

    public void deleteProduct(Long id, User authenticatedUser){
        log.debug("Deleting product {}", id);

        Product product = this.productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product with requested id doesn't exist."));
        if(authenticatedUser==null){
            throw new ForbiddenException("zaloguj się--deleteProduct--ProductService");
        }
        securityService.checkPermission(authenticatedUser, product.getUser().getId());
        productRepository.delete(product);
    }

    public void deleteProducts(User authenticatedUser,  Long[] ids){
        if(authenticatedUser==null){
            throw new ForbiddenException("zaloguj się");
        }
        for (Long id : ids) {
            Product product = this.productRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Product with requested id:"+id+" doesn't exist."));
            securityService.checkPermission(authenticatedUser, product.getUser().getId());
        }
        if(ids != null && ids.length > 0){
            productRepository.deleteAllById(Arrays.asList(ids));
        }
    }

    public void updateProduct(Long id, UpdateProductDTO newProductDTO, User authenticatedUser)throws IOException{
        log.debug("Updating product {} to {}", id, newProductDTO);

        Product product = this.productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product with requested id doesn't exist."));
        if(authenticatedUser==null){
            throw new ForbiddenException("zaloguj się--updateProduct--ProductService");
        }
        
        securityService.checkPermission(authenticatedUser,product.getUser().getId());
        String fileName = FileUploadUtil.updateFile(uploadDir, newProductDTO.getPhotoFile(),product.getPhotoName());
        product.setPhoto(fileName);

        product.setVat(newProductDTO.getVat());
        product.setDescription(newProductDTO.getDescription());
        product.setName(newProductDTO.getName());
        product.setNumber(newProductDTO.getNumber());
        product.setPrice(newProductDTO.getPrice());
        Product newProduct=productRepository.save(product);
        this.parameterService.saveParameter(newProductDTO.getParameters(), newProduct);

    }

    private ProductDTO map(Product product, User authUser){
        ProductDTO dto = this.productMapper.toDto(product);

        dto.setFavorite(
                authUser != null &&
                        product.getFavoriteProducts() != null &&
                            product.getFavoriteProducts().stream()
                                    .anyMatch(f -> Objects.equals(f.getUser().getId(), authUser.getId()))
        );
        return dto;
    }

    private List<ProductDTO> map(List<Product> products, User authUser){
        List<ProductDTO> dtos = this.productMapper.toDto(products);

        dtos.forEach(dto -> {
            Optional<Product> product = products.stream()
                    .filter(p -> Objects.equals(p.getId(), dto.getId()))
                    .findFirst();

            product.ifPresent(value ->
                    dto.setFavorite(
                            authUser != null &&
                                    value.getFavoriteProducts() != null &&
                                        value.getFavoriteProducts().stream()
                                                .anyMatch(f -> Objects.equals(f.getUser().getId(), authUser.getId()))
            ));

        });
        return dtos;
    }
}
