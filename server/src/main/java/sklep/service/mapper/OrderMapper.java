package sklep.service.mapper;

import org.mapstruct.Mapper;
import sklep.entity.Order;
import sklep.service.dto.*;
import sklep.service.dto.Create.CreateOrderDTO;
import sklep.service.dto.Update.UpdateOrderDTO;


@Mapper(componentModel = "spring")
public interface OrderMapper extends EntityMapper<Order, OrderDTO>{
    Order toOrderFromCreateDTO(CreateOrderDTO createOrderDTO);
    Order toOrderFromUpdateOrderDTO(UpdateOrderDTO updateOrderDTO);
    OrderDTO toDto(Order order);
}
