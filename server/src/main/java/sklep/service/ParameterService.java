package sklep.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sklep.entity.Parameters;
import sklep.entity.Product;
import sklep.repository.ParameterRepository;
import sklep.repository.ProductRepository;
import sklep.service.dto.Create.CreateParameterDTO;
import sklep.service.mapper.ParameterMapper;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ParameterService {

    private final static Logger log = LoggerFactory.getLogger(ParameterService.class);
    private final ParameterRepository parameterRepository;
    private final ParameterMapper parameterMapper;
    private final SecurityService securityService;

    private final ProductRepository productRepository;

    @Autowired
    public ParameterService(ParameterRepository parameterRepository,
                            ParameterMapper parameterMapper,
                            SecurityService securityService,
                            ProductRepository productRepository) {
        this.parameterRepository = parameterRepository;
        this.parameterMapper = parameterMapper;
        this.securityService = securityService;
        this.productRepository = productRepository;
    }

    public void saveParameter(List<CreateParameterDTO> parameterDTOS, Product product ){
        log.debug("Updating: parameters {} in product {}", parameterDTOS, product);

        if(parameterDTOS == null || product == null) return;

        removeParameters(product);
        for(CreateParameterDTO param : parameterDTOS){
            Parameters parameter = this.parameterMapper.toParameterFromCreateDTO(param);
            parameter.setProduct(product);
            this.parameterRepository.save(parameter);
        }
    }

    public void removeParameters(Product product){
        log.debug("Removing: tags in product {}", product);

        this.parameterRepository.deleteAll(product.getParameters());
    }

}
