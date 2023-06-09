import React from 'react'


/**
 * Context for global auth
 */
const AuthContext = React.createContext({
    user: null,
    login: () => {},
    logout: () => {}
})

export default AuthContext