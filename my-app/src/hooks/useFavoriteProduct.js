import { useGet } from "./useGet"

export function useFavoriteProduct(){
    const {
        data: favoriteProduct ,
        updateData: updateFavoriteProduct , 
        error: errorFavoriteProduct, 
        loading: loadingFavoriteProduct,
        removeItems:  removeFavoriteProducts
    } = useGet(`products/favorite`, {})
    return {favoriteProduct, updateFavoriteProduct, errorFavoriteProduct, loadingFavoriteProduct, removeFavoriteProducts};

}