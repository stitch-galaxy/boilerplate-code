export const LOGON : String = "LOGON";
export const LOGOUT : String = "LOGOUT";

export function logout()
{
    return {
        type: LOGOUT
    }
}

export function logon()
{
    return {
        type: LOGON
    }
}