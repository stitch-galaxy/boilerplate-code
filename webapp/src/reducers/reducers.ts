import { combineReducers } from 'redux';

import { LOGON, LOGOUT } from "../actions/actions";

function sessionReducer(state = { isLogged: false }, action: any) {
    switch (action.type) {
        case LOGON:
            return { isLogged: true };
        case LOGOUT:
            return { isLogged: false };
    }
    return state;
}

const rootReducer = combineReducers(
    {
        session: sessionReducer
    }
);

export default rootReducer