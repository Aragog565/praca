package sklep.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sklep.entity.User;
import sklep.service.OrderService;

import sklep.service.dto.*;
import sklep.service.dto.Create.CreateOrderDTO;
import sklep.service.dto.Update.UpdateOrderDTO;

import java.util.List;


@CrossOrigin
@RestController
@RequestMapping(path="/api")
public class OrderController {
    private static final Logger log = LoggerFactory.getLogger(SecurityController.class);
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/order")
    public ResponseEntity<OrderDTO> createOrder(
            @RequestBody @Validated CreateOrderDTO createOrderDTO,
            @AuthenticationPrincipal User authenticatedUser
    ){
        log.debug("Request to save: order {}", createOrderDTO);

        OrderDTO orderDTO = orderService.saveOrder(createOrderDTO,authenticatedUser);

        return ResponseEntity.ok(orderDTO);
    }

    @GetMapping("/order")
    public ResponseEntity<List<OrderDTO>> getOrder(){
        log.debug("Request to get: order.");

        List<OrderDTO> orderDTOS  = orderService.getOrder();

        return ResponseEntity.ok(orderDTOS );
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<OrderDTO> getOrderByID(
            @PathVariable final Long id
    ){
        OrderDTO orderDTO = orderService.getOrderById(id);

        return ResponseEntity.ok(orderDTO);
    }

    @GetMapping("/user/{id}/orders")
    public ResponseEntity<List<OrderDTO>> getOrderByUserID(
            @AuthenticationPrincipal User authenticatedUser,
            @PathVariable final Long id
    ){
        List<OrderDTO> orderDTO = orderService.getOrderByUserID(id,authenticatedUser);

        return ResponseEntity.ok(orderDTO);
    }



    @DeleteMapping("/order/{id}")
    public ResponseEntity<Void> deleteOrder(
            @PathVariable final Long id,
            @AuthenticationPrincipal User authenticatedUser
    ){
        log.debug("Request to delete: order {} by {}", id, authenticatedUser);

        orderService.deleteOrder(id, authenticatedUser);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/order")
    public ResponseEntity<OrderDTO> removeOrders(
            @AuthenticationPrincipal User authenticatedUser,
            @RequestParam(required = false) Long[] ids
    ) {

        orderService.remove(authenticatedUser, ids);
        return ResponseEntity.noContent().build();
    }

//    @PutMapping("/order/{id}")
//    public ResponseEntity<Void> updateOrder(
//            @PathVariable final Long id,
//            @RequestBody @Validated UpdateOrderDTO updateOrderDTO,
//            @AuthenticationPrincipal User authenticatedUser
//    ){
//        log.debug("Request to update: Order {} by {}", id, authenticatedUser);
//
//        orderService.updateOrder(id, updateOrderDTO, authenticatedUser);
//
//        return ResponseEntity.noContent().build();
//    }

    @PutMapping("/order")
    public ResponseEntity<Void> updateOrder(
            @RequestBody @Validated UpdateOrderDTO updateOrderDTO,
            @AuthenticationPrincipal User authenticatedUser,
            @RequestParam(required = false) Long[] ids
    ){

        orderService.updateOrders(updateOrderDTO, authenticatedUser, ids);

        return ResponseEntity.noContent().build();
    }

}
