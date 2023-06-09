import { useEffect } from "react"
import { useState } from "react"
import { useGet } from "./useGet";

export const PRODUCTS_LIMIT = 4;

export function useRecentProducts() {
    const ids = (JSON.parse(window.localStorage.getItem('recentProducts')) || []);
    const { data, error: errorRecentProducts, loading: loadingRecentProducts }= useGet('products', {ids: ids.join(',')}, [])
    
    function addProduct(id){
        let productIds = new Set(JSON.parse(window.localStorage.getItem('recentProducts')));
        productIds.add(id)
        while(productIds.size > PRODUCTS_LIMIT){
            productIds.delete(productIds.values().next().value)
        }
        window.localStorage.setItem('recentProducts', JSON.stringify([...productIds]))
    }

    return {recentProducts: ids.length > 0 ? data : [], errorRecentProducts, loadingRecentProducts, addProduct}
}