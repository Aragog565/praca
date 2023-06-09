import { useContext } from 'react'
import AuthContext from '../context/auth-context'

export function useAuth() {
    const authContext = useContext(AuthContext)

    const auth = authContext.user

    const setAuth = (user) => {
        if (user) {
            authContext.login(user)
            window.localStorage.setItem('token', JSON.stringify(user))
        } else {
            authContext.logout()
            window.localStorage.removeItem('token')
        }
    }

    return [auth, setAuth]
}