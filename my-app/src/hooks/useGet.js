import { useEffect, useState } from "react"
import { get } from '../utils/Api' 

export function useGet(route, params = {}, defaultState = null, errorMessage = `${route} error`){
    const [data, setData] = useState(defaultState)
    const [error, setError] = useState(null)
    const [loading, setLoading] = useState(true)
    let wait = false;

    function updateData(item){
        if(Array.isArray(data)){
            const index=data.findIndex(d => d.id == item.id)
            if(index >= 0){
                data[index]=item
                setData([...data])
            }else{
                setData((prev) => [item, ...prev]);
            }
        }else{
            setData(item);
        }
    }

    function updateDataArray(items){
        setData(items)
    }

    function removeItems(items){
        if(Array.isArray(data)){
            const copy = [...data].filter(item => !items.includes(item.id))
            setData(copy)
        }

    }

    async function fetch(newParams = null){
        if(wait){
            return;
        }
        try{
            setLoading(true)
            wait = true
            const p = !newParams ? params : newParams;
            const paramString = Object.entries(p)
                .map(param => `${param[0]}=${param[1]}`)
                .join('&')
            const response = await get(`${route}?${paramString}`)
            setData(response.data)
        }catch(err){
            setError(errorMessage)
        }finally{
            setLoading(false)
            wait = false
        }
    }

    useEffect(() => {
        fetch()
    }, [])

    return { data, updateData, error, loading ,removeItems, refetch: fetch,updateDataArray}
}