import { useGet } from "./useGet"

export function useProducts(){
    return useGet('products', {}, [])
}