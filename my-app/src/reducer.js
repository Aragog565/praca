export const reducer = (state, action) => {
    switch (action.type) {
        case 'login':
            return {...state, user: action.user  }
        case 'logout':
            return {...state, user: null }
        default:
            throw new Error('Action: ' + action.type + ' Not Found')
    }
}

export const initialState = {
    user: JSON.parse(window.localStorage.getItem('token')) ?? null
}