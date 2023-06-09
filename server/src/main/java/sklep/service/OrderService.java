package sklep.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sklep.config.Constants;
import sklep.entity.FavoriteProduct;
import sklep.entity.Order;
import sklep.entity.Product;
import sklep.entity.User;
import sklep.repository.OrderRepository;
import sklep.service.dto.*;
import sklep.service.dto.Create.CreateOrderDTO;
import sklep.service.dto.Update.UpdateOrderDTO;
import sklep.service.exception.BadRequestException;
import sklep.service.exception.EntityNotFoundException;
import sklep.service.exception.ForbiddenException;
import sklep.service.mapper.OrderMapper;
import sklep.utils.ObjectOverwrite;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class OrderService {
    private final static Logger log = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final SecurityService securityService;
    private final OrderProductService orderProductService;


    public OrderService(OrderRepository orderRepository,
                        OrderMapper orderMapper,
                        SecurityService securityService, OrderProductService orderProductService) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.securityService = securityService;
        this.orderProductService = orderProductService;
    }

    public OrderDTO saveOrder(CreateOrderDTO createOrderDTO, User authenticatedUser){
        log.debug("Saving order : {}", createOrderDTO);

        if(authenticatedUser==null){
            throw new ForbiddenException("zaloguj się--saveOrder--OrderService");
        }

        Order order = orderMapper.toOrderFromCreateDTO(createOrderDTO);

        order.setUser(authenticatedUser);
        order.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        order.setDateOfPayment(new Timestamp(System.currentTimeMillis()));
        order.setShippingDate(new Timestamp(System.currentTimeMillis()+ Constants.ONE_DAY_IN_MS));
        order.setCompletionDate(new Timestamp(System.currentTimeMillis()+Constants.ONE_DAY_IN_MS*2));
        order.setLastModificationAt(new Timestamp(System.currentTimeMillis()));

        this.orderProductService.saveOrderProducts(createOrderDTO.getProducts(), order);

        order = orderRepository.save(order);


        return orderMapper.toDto(order);
    }

    public List<OrderDTO> getOrder(){
        log.debug("Fetching all order");

        List<Order> order = orderRepository.findAll();

        return orderMapper.toDto(order);
    }

    public OrderDTO getOrderById(Long id){
        log.debug("Fetching order by id: {}", id);

        Order order = this.orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order with requested id doesn't exist."));

        return this.orderMapper.toDto(order);
    }

    public List<OrderDTO> getOrderByUserID(Long id, User authenticatedUser){
        if(authenticatedUser==null){
            throw new ForbiddenException("zaloguj się");
        }

        securityService.checkPermission(authenticatedUser, id);

        List<Order> order = this.orderRepository.findByUser_IdOrderByCreatedAtDesc(id);

        return this.orderMapper.toDto(order);
    }

    public void deleteOrder(Long id, User authenticatedUser){
        log.debug("Deleting Order {}", id);

        Order order = this.orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order with requested id doesn't exist."));
        if(authenticatedUser==null){
            throw new ForbiddenException("zaloguj się--deleteOrder--OrderService");
        }
        securityService.checkPermission(authenticatedUser, order.getUser().getId());

        orderRepository.delete(order);
    }

    public void remove(User authenticatedUser,  Long[] ids){
        if(authenticatedUser==null){
            throw new ForbiddenException("zaloguj się");
        }
        for(Long id: ids){
            Order order = this.orderRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Order with requested id:"+id+" doesn't exist."));
            securityService.checkPermission(authenticatedUser, order.getUser().getId());
        }


        if(ids != null && ids.length > 0){
            orderRepository.deleteAllById(Arrays.asList(ids));
        }
    }

    public void updateOrder(Long id, UpdateOrderDTO newOrderDTO, User authenticatedUser){
        log.debug("Updating product {} to {}", id, newOrderDTO);

        Order order = this.orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order with requested id doesn't exist."));
        if(authenticatedUser==null){
            throw new ForbiddenException("zaloguj się--updateOrder--OrderService");
        }
        securityService.checkPermission(authenticatedUser,order.getUser().getId());
        Order updatedOrder = orderMapper.toOrderFromUpdateOrderDTO(newOrderDTO);
        ObjectOverwrite.map(order, updatedOrder);

        order.setLastModificationAt(new Timestamp(System.currentTimeMillis()));
        orderRepository.save(order);
    }
    @Transactional
    public void updateOrders(UpdateOrderDTO newOrderDTO, User authenticatedUser, Long[] ids ){
        if(authenticatedUser==null){
            throw new ForbiddenException("zaloguj się");
        }
        if(ids != null){
            List<Order> orders = this.orderRepository.findAllByIds(ids);
            boolean cancelled = newOrderDTO.getCancelled();
            for(Order order:orders){
                order.setCancelled(cancelled);
                order.setLastModificationAt(new Timestamp(System.currentTimeMillis()));
            }
            orderRepository.saveAll(orders);
        }
    }

}
