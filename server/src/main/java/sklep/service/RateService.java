package sklep.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sklep.entity.Product;
import sklep.entity.Rate;
import sklep.entity.User;
import sklep.repository.ProductRepository;
import sklep.repository.RateRepository;
import sklep.repository.UserRepository;
import sklep.service.dto.Create.CreateRateDTO;
import sklep.service.dto.RateDTO;
import sklep.service.exception.EntityNotFoundException;
import sklep.service.exception.ForbiddenException;
import sklep.service.mapper.RateMapper;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class RateService {
    private final static Logger log = LoggerFactory.getLogger(RateService.class);

    private final RateMapper rateMapper;
    private final RateRepository rateRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final SecurityService securityService;

    @Autowired
    public RateService(RateMapper rateMapper,
                       RateRepository rateRepository,
                       UserRepository userRepository,
                       ProductRepository productRepository,
                       SecurityService securityService) {
        this.rateMapper = rateMapper;
        this.rateRepository = rateRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.securityService = securityService;
    }

    public RateDTO save(CreateRateDTO createdRateDTO, User authenticatedUser, Long productID){
        log.debug("Saving: Rate {} in product {}", createdRateDTO, productID);
        Product product = productRepository.findById(productID)
                .orElseThrow(() -> new EntityNotFoundException("Product with requested id doesn't exists."));
        if(authenticatedUser==null){
            throw new ForbiddenException("zaloguj się--saveProduct--ProductService");
        }
        Rate rate = rateRepository.findByProduct_IdAndUser_IdOrderByCreatedAtDesc(productID,authenticatedUser.getId());

        if(rate==null){
            rate = rateMapper.toRateFromCreatedRateDTO(createdRateDTO);
            rate.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            rate.setProduct(product);
            rate.setUser(authenticatedUser);
        }else{
            rate.setValue(createdRateDTO.getValue());
        }

        rate.setLastModificationAt(new Timestamp(System.currentTimeMillis()));
        return rateMapper.toDto(rateRepository.save(rate));
    }

}
