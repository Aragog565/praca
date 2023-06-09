import { useGet } from "./useGet"

export function useProduct(id){
    const {
        data: product ,
        updateData: updateProduct , 
        error: errorProduct, 
        loading: loadingProduct
    } = useGet(`products/${id}`, {})
    return {product, updateProduct, errorProduct, loadingProduct};

}