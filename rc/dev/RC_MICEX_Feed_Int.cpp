#include "stdafx.h"
#include "RCITService.h"

#include "FEEDConfig.h"
#include "FHLogger.h"

#include "MCXControlStructures.h"

BOOL StartRPCListen()
{
    RPC_STATUS status;
    UCHAR szProtocolSequence[80];
	lstrcpy((LPSTR)szProtocolSequence, "ncacn_np");
    LPSTR pszSecurity = NULL; /*Security not implemented */
    UCHAR szEndpoint[255];
	wsprintf((LPSTR)szEndpoint, "\\pipe\\%s_Feed", (LPCSTR)CFEEDConfig::s_pConfigInstance->m_csServiceName);
    UINT    cMinCalls      = 1;
    UINT    cMaxCalls      = 20;
    UINT    fDontWait      = TRUE;
    if(status = RpcServerUseProtseqEp(szProtocolSequence, cMaxCalls, szEndpoint, pszSecurity))  {
		LOG_ERROR(SECTION(lSystem), "Unable to start RPC Server");
        return FALSE;
    }
    if (status = RpcServerRegisterIf(MCXFeedService_Int_v1_0_s_ifspec, NULL, NULL))  {
		LOG_ERROR(SECTION(lSystem), "Unable to start RPC Server");
        return FALSE;
    }
    if (status = RpcServerListen(cMinCalls, cMaxCalls, fDontWait))  {
		LOG_ERROR(SECTION(lSystem), "Unable to start RPC Server");
        return FALSE;
    }
	return TRUE;
}

void StopRPCListen(void)
{
    RPC_STATUS status;
    if (status = RpcMgmtStopServerListening(NULL)) {
		LOG_ERROR(SECTION(lSystem), "Unable to stop RPC Server");
       return;
    }
 
    if (status = RpcServerUnregisterIf(NULL, NULL, FALSE)) {
		LOG_ERROR(SECTION(lSystem), "Unable to stop RPC Server");
       return;;
    }
}

	
void __RPC_FAR * __RPC_USER midl_user_allocate(size_t len)
{
    return(malloc(len));
}
 void __RPC_USER midl_user_free(void __RPC_FAR * ptr)
{
    free(ptr);
}

int MCXFeedService_GetServiceState(byte* pStatus, int nSize)
{
	return -1;
}

int MCXFeedService_PutJobRequest(byte* pRequest, int nSize)
{
	if(nSize != sizeof(MCXJOBREQUEST))
		return -1;

	switch(((PMCXJOBREQUEST)pRequest)->nJob_id) 
	{
		case MCXJOB_RESTART_MICEX_LINK:
			break;
		case MCXJOB_DUMP_SECURITY: 
			break;
		default:
			break;
	}
	return 0;
}

int MCXFeedService_GetConfig(byte* pStatus, int nSize)
{
	return 1;
}

int MCXFeedService_SetConfig(byte* pStatus, int nSize)
{
	return 1;
}


int MCXFeedService_GetLogList(byte* pList, int nSize, __int64 nLastRec)
{
	return CFHLogger::GetLogList(pList, nSize, nLastRec);
}

int MCXFeedService_GetSecList(byte* pList, int nSize)
{
	return 0;
}

