import { combineReducers } from 'redux';

import { LOGON, LOGOUT } from "../actions/actions";

import { routerReducer } from 'react-router-redux'

function sessionReducer(state = { isLogged: false }, action: any) {
    switch (action.type) {
        case LOGON:
            return { isLogged: true };
        case LOGOUT:
            return { isLogged: false };
    }
    return state;
}

const reducers = combineReducers(
    {
        session: sessionReducer,
        routing: routerReducer,
    }
);

export default reducers