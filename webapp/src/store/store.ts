import { createStore, applyMiddleware, Store } from 'redux'
import rootReducer from '../reducers/reducers'

interface SessionState {
    isLogged : boolean
}

export interface State {
    session : SessionState
}

const initialState : State = {
    session : {
        isLogged : false
    }
    
}

function configureStore(initialState: State) : Store {
    return createStore(
        rootReducer,
        initialState)
}


export default configureStore(initialState);