/* *************************************************************************  */
/* MTESrl.h -- Header File												  */
/*																			*/
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


typedef struct Service_Info_tag
{
	int Connected_To_MICEX;
	int Session_Id;
	char MICEX_Sever_Name[33];
	char Version_Major;
	char Version_Minor;
	char Version_Build;
	char Beta_version;
	char Debug_flag;
	char Test_flag;
	int  Start_Time;
	int  Stop_Time_Min;
	int  Stop_Time_Max;
	int  Next_Event;
	int  Event_Date;
	char *b_selected;
	char *main_user;
} Service_Info;

typedef struct MTESnapTable_tag
{
	int32	m_HTable;		//	Handle of table
	char *	m_TableName;	//	char, Zero-byte terminated, Table Name
	char *	m_Params;		//	char, Zero-byte terminated, Parameters provided on open table
} MTESnapTable;


int32 WINAPI MTEOpenTable(int32 ClientIdx, char *TableName, char *Params, 
						int32 CompleteFlag, TMTEMsg **Msg);

int32 WINAPI MTEAddTable(int32 ClientIdx, int32 HTable, int32 Reference);

int32 WINAPI MTEExecTrans(int32 ClientIdx, char *TransactionName, char *Params,
							char *ResultMsg);

int32 WINAPI MTEStructure(int32 Idx, TMTEMsg **Msg);

int32 WINAPI MTEConnect(char *Params, char *ErrorMsg);

int32 WINAPI MTESoftConnect(char *Params, char *ErrorMsg);

int32 WINAPI MTELogon(int32 Idx,  int32 sync_time);

int32 WINAPI MTERefresh(int32 ClientIdx, TMTEMsg **Msg);

int32 WINAPI MTECloseTable(int32 ClientIdx, int32 HTable);

int32 WINAPI MTEDisconnect(int32 ClientIdx);

char * WINAPI MTEErrorMsg(int32 ErrorCode);

int32 WINAPI  MTEFreeBuffer(int32 ClientIdx); // dummy

int32 WINAPI MTEGetSnapshot( int32 Idx, char ** Snapshot, int *Len);

int32 WINAPI MTESetSnapshot( int32 Idx, char * Snapshot, int Len, char *Error);

int32 WINAPI MTEGetServInfo( int32 Idx, char ** ServInfo, int *Len);

char * WINAPI MTEGetVersion();

int32 WINAPI MTEGetTablesFromSnapshot( int32 Idx, char * Snapshot, int Len, MTESnapTable ** SnapTables );

int32 WINAPI MTEAddTableConst(int32 ClientIdx, int32 HTable, int32 Reference);

int32 WINAPI MTERemTableConst(int32 ClientIdx, int32 HTable);

int32 WINAPI MTEClearTablesRFList(int32 ClientIdx);

#ifdef __cplusplus
}
#endif


#endif // MTESRL_H