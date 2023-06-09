import { useGet } from "./useGet"

export function useCategories(){
    const {
        data: category,
        error: errorCategory,
        loading: loadingCategory
    } = useGet('categories', {}, [])
    return {category, errorCategory, loadingCategory};
}
