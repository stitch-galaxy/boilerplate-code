/* *************************************************************************  */
/* MTESrl.h -- Header File												  */
/* MICEX EPTS version																		*/
/* *************************************************************************  */


#ifndef MTESRL_H
#define MTESRL_H


#include "tsmrerr.h"
#ifdef WIN32
#include	<windows.h>
#else
#define WINAPI 
#endif


#if WIN32
typedef long				int32;
#include <stdlib.h>
#else

typedef int					int32;
#ifndef _MAX_PATH
#define _MAX_PATH 512
#endif
#endif

#include "MTEErrors.h"

#ifdef __cplusplus
extern "C" {
#endif

// All that typedefs are example only.
// But you can try to use it as underlaying structure for results of MTEStructure call.
// That structures are used in library for integrity checking

typedef struct TMTEMsg_tag
{
	long DataLen;
//	char *Data; // all other bytes are Data and you can address it as 
				//"char *Data = (char *)(TMTEMsg_pointer + 1);" 
				// Or you can add data field as:
				// char Data[1];
				// then you can address to data field as TMTEMsg_pointer->Data
} TMTEMsg;

typedef struct TMTERow_tag
{
	char NumFields;
	int32 DataLen;
	char *FieldsNumbers;
	char *Data;
} TMTERowStruct;


typedef struct TMTETable_tag
{
	int32 Ref;
	int32 NumRows;
	TMTERowStruct *rows;
} TMTETableStruct;

typedef struct TMTETables_tag
{
	int32 NumTables;
	TMTETableStruct *tables;
} TMTETablesStruct;


#define		ZLIB_COMPRESSED		0x1		// session is using ZLIB compression
#define		FLAG_ENCRYPTED		0x2             // session traffic is encrypted
#define		FLAG_SIGNING_ON		0x4		// session transactios are digitally signed

typedef struct TConnectionStats_tag
{
	int32 Size;		// must be set to sizeof(TConnectionStats) prior to call
	DWORD Properties;	// ZLIB_COMPRESSED, FLAG_ENCRYPTED, FLAG_SIGNING_ON
	DWORD SentPackets;	// number of packets sent in the current session
	DWORD RecvPackets;	// number of packets received in the current session
	DWORD SentBytes;	// number of bytes sent in the current session
	DWORD RecvBytes;	// number of bytes received in the current session
} TConnectionStats; 


int32 WINAPI MTEOpenTable(int32 ClientIdx, char *TableName, char *Params, 
						int32 CompleteFlag, TMTEMsg **Msg);

int32 WINAPI MTEAddTable(int32 ClientIdx, int32 HTable, int32 Reference);

int32 WINAPI MTEExecTrans(int32 ClientIdx, char *TransactionName, char *Params,
							char *ResultMsg);

int32 WINAPI MTEStructure(int32 Idx, TMTEMsg **Msg);

int32 WINAPI MTEConnect(char *Params, char *ErrorMsg);

int32 WINAPI MTERefresh(int32 ClientIdx, TMTEMsg **Msg);

int32 WINAPI MTECloseTable(int32 ClientIdx, int32 HTable);

int32 WINAPI MTEDisconnect(int32 ClientIdx);

char * WINAPI MTEErrorMsg(int32 ErrorCode);

int32 WINAPI  MTEFreeBuffer(int32 ClientIdx); // dummy

int32 WINAPI MTEGetSnapshot( int32 Idx, char ** Snapshot, int *Len);

int32 WINAPI MTESetSnapshot( int32 Idx, char * Snapshot, int Len, char *Error);

int32 WINAPI MTEConnectionStats(int32 Idx, TConnectionStats *Stats);

#ifdef __cplusplus
}
#endif


#endif // MTESRL_H