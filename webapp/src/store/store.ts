import { createStore, applyMiddleware, combineReducers, Store, compose } from 'redux'
import reducers from '../reducers/reducers'
import { syncHistoryWithStore, routerReducer } from 'react-router-redux'

interface IntlState {
    locale: string,
    messages: any
}

interface SessionState {
    isLogged: boolean
}

export interface State {
    intl: IntlState,
    session: SessionState
}

const initialState: State = {
    intl: {
        locale: "ru",
        messages: {
            'app.greeting': 'Привет!',
        }
    },
    session: {
        isLogged: false
    }

}



const storeEnhancers: any = [
]

function configureStore(initialState: State): Store {
    if (__DEVTOOLS__) {
        const DevTools = require<ModuleInterface>("../components/DevTools").default;
        const enhancer = compose(DevTools.instrument());
        return createStore(
            reducers,
            initialState,
            enhancer)
    }
    return createStore(reducers,
        initialState);

}


export default configureStore(initialState);