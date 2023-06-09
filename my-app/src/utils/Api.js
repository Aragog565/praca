import axios, { AxiosError } from 'axios';
import { useAuth } from '../hooks/use-auth';

export const api = "http://localhost:8080/api";

export async function get(route, headers = {}){
    return (await axios.get(
        `${api}/${route}`,
        {
            headers: {
                Authorization: `Bearer ${getToken()}`,
                ...headers
            }
        }
    ).catch(handleResponse))
}

export async function post(route, data, headers = {}){
    return (await axios.post(
        `${api}/${route}`,
        data,
        {
            headers: {
                Authorization: `Bearer ${getToken()}`,
                ...headers
            }
        }
    ).catch(handleResponse))
}

export async function remove(route,params={}, headers = {}){
    const paramString = Object.entries(params)
                        .map(param => `${param[0]}=${param[1]}`)
                        .join('&')
    return (await axios.delete(
        `${api}/${route}?${paramString}`,
        {
            headers: {
                Authorization: `Bearer ${getToken()}`,
                ...headers
            }
        }
    ).catch(handleResponse))
}

export async function put(route, data, headers = {}){
    return (await axios.put(
        `${api}/${route}`,
        data,
        {
            headers: {
                Authorization: `Bearer ${getToken()}`,
                ...headers
            }
        }
    ).catch(handleResponse))
}

function handleResponse(res){
    if(res?.response?.status == 403){
        window.localStorage.removeItem('token')
        window.location.href = '/logowanie'

    }
    throw new AxiosError(res.message, res.code, res.config, res.request, res.response)
}

function getToken(){
    const user = JSON.parse(window.localStorage.getItem('token')) ?? null
    return user?.token || null;
}