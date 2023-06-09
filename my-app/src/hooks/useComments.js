import { useGet } from "./useGet"

export function useComments(id){
    const {data: comments, updateData: addComment, error: errorComment, loading: loadingComment} = useGet(`products/${id}/comments`, {}, [])
    return {comments, addComment, errorComment, loadingComment};

}