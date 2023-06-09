package sklep.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sklep.entity.Order;
import sklep.entity.OrderProduct;
import sklep.repository.OrderProductRepository;
import sklep.repository.OrderRepository;
import sklep.repository.ProductRepository;
import sklep.service.dto.Create.CreateOrderProductDTO;
import sklep.service.exception.BadRequestException;
import sklep.service.exception.ConflictException;
import sklep.service.exception.ForbiddenException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderProductService {
    private final static Logger log = LoggerFactory.getLogger(OrderProductService.class);
    private final OrderProductRepository orderProductRepository;
    private final SecurityService securityService;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderProductService(OrderProductRepository orderProductRepository,
                               SecurityService securityService,
                               ProductRepository productRepository, OrderRepository orderRepository) {
        this.orderProductRepository = orderProductRepository;
        this.securityService = securityService;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public void saveOrderProducts(List<CreateOrderProductDTO> orderProductDTOS, Order order){
        log.debug("Updating: orderProduct {} in order {}", orderProductDTOS, order);

        List<Long> ids = new ArrayList<>();

        List<OrderProduct> ops = this.productRepository.findAllById(
                orderProductDTOS.stream()
                        .map(CreateOrderProductDTO::getProductId)
                        .collect(Collectors.toList())
        ).stream().map(p -> {

            Long quantity = orderProductDTOS.stream()
                    .filter(o -> Objects.equals(o.getProductId(), p.getId()))
                    .findFirst()
                    .map(CreateOrderProductDTO::getQuantity)
                    .orElse(0L);

            if(quantity <= p.getNumber()){
                p.setNumber(p.getNumber()-quantity);
            }else {
                ids.add(p.getId());
            }

            return new OrderProduct(p, order, quantity);

        }).collect(Collectors.toList());

        if(ids.size() > 0){
            throw new BadRequestException("not enough product: "+ids);
        }

        order.setOrderProduct(ops);
    }

}
