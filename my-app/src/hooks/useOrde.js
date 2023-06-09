import { useGet } from "./useGet"

export function useOrde(id){
    const {
        data: order ,
        updateData: updateOrder  , 
        error: errorOrder, 
        loading: loadingOrder,
        removeItems:  removeOrders,
        updateDataArray: updateOrderArray
    } = useGet(`user/${id}/orders`, {})
    return {order, errorOrder, loadingOrder,updateOrder, removeOrders, updateOrderArray};

}