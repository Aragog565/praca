import { useState } from "react"
import { useGet } from "./useGet";
import { useEffect } from "react";

export function useBasket(){
    const {data ,error, loading, refetch} = useGet('products',{ids: (JSON.parse(window.localStorage.getItem('basket'))?.map(item => item.id)||[]).join(',')},[])
    const [basket, setBasket]= useState(JSON.parse(window.localStorage.getItem('basket'))||[]);
    function addProduct(id,number){
        const productIds = new Set(JSON.parse(window.localStorage.getItem('basket')));
        const ids = Array.from(productIds).map(item => item.id);

        if(!ids.includes(id)){
            productIds.add({id,number})
            setBasket(prev => ([...prev, {id,number}]))
            window.localStorage.setItem('basket', JSON.stringify([...productIds]))
        }
    }

    function removeProduct(id){
        const productIds = new Set(JSON.parse(window.localStorage.getItem('basket')));
        productIds.forEach(productInBasket => productInBasket.id == id ? productIds.delete(productInBasket): null)
        const index = basket.findIndex((item)=>item.id == id)
        
        if(index >= 0){
            setBasket(prev => {
                const copy = [...prev]
                copy.splice(index,1)
                return copy;
            })
            window.localStorage.setItem('basket', JSON.stringify([...productIds]))
        }
    }
    function removerProducts(ids){
        const productIds = new Set(JSON.parse(window.localStorage.getItem('basket')));
        productIds.forEach(productInBasket => ids.includes(productInBasket.id) ? productIds.delete(productInBasket): null)
        setBasket(prev => {
            const copy = [...prev]
            window.localStorage.setItem('basket', JSON.stringify([...productIds]))
            return copy.filter((item) => !ids.includes(item.id))
        })

    }

    function isProductInBasket(id){

        const ids = Array.from(basket).map(item => item.id);
        const index = ids.find((item)=>item == id)
        return index != null;
        
    }

    function updateProductBasket(id, number){
        const productIds = new Set(JSON.parse(window.localStorage.getItem('basket')));
        productIds.forEach(product => product.id == id ? product.number=number:null);
        setBasket([...productIds]);
        window.localStorage.setItem('basket', JSON.stringify([...productIds]));
    }

    useEffect(() => {
        const ids = JSON.parse(window.localStorage.getItem('basket'))?.map(item => item.id)||[];
        if(ids.length > 0){
            refetch({ids: ids.join(',')})
        }
    }, [basket])

    return {
        basket:(JSON.parse(window.localStorage.getItem('basket'))?.map(item => item.id)||[]).length>0 ? data.map(d => ({...d, basketCount: basket.find(b => b.id == d.id)?.number || 0})):[], 
        error, 
        loading, 
        addProductToBasket: addProduct, 
        removeProductBasket: removeProduct, 
        isProductInBasket,
        updateProductBasket,
        removerProducts
    }
}