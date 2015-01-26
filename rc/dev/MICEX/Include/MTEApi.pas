unit MTEApi;

interface

uses
  Windows;
  
const
  // Коды ошибок, возвращаемые функциями MTExxxx
  MTE_OK             = 0;
  MTE_CONFIG         = -1;
  MTE_SRVUNAVAIL     = -2;
  MTE_LOGERROR       = -3;
  MTE_INVALIDCONNECT = -4;
  MTE_NOTCONNECTED   = -5;
  MTE_WRITE          = -6;
  MTE_READ           = -7;
  MTE_TSMR           = -8;
  MTE_NOMEMORY       = -9;
  MTE_ZLIB           = -10;
  MTE_PKTINPROGRESS  = -11;
  MTE_PKTNOTSTARTED  = -12;
  MTE_LOGON          = -13;
  MTE_INVALIDHANDLE  = -14;
  MTE_DSROFF         = -15;
  MTE_UNKNOWN        = -16;
  MTE_BADPTR         = -17;
  MTE_TRANSREJECTED  = -18;
  MTE_TOOSLOWCONNECT = -19;
  MTE_CRYPTO_ERROR   = -20;

  // Атрибуты входных и выходных полей
  ffKey     = $01;                // Ключевое поле
  ffSecCode = $02;                // Поле содержит код инструмента

  // Атрибуты таблиц
  tfUpdateable    = $01;          // Обновляемая
  tfClearOnUpdate = $02;          // Очищать при обновлении

type
  PMTEMsg = ^TMTEMsg;
  TMTEMsg = record
    DataLen: Integer;
    Data: record end;
  end;

  PMTERow = ^TMTERow;
  TMTERow = packed record
    FldCount: Byte;
    RowLen: Integer;
    RowData: record end;
  end;

  PMTETable = ^TMTETable;
  TMTETable = record
    Ref: Integer;
    RowCount: Integer;
    TblData: TMTERow;
  end;

  PMTETables = ^TMTETables;
  TMTETables = record
    TblCount: Integer;
    Tables: TMTETable;
  end;

  TErrorMsg = array [0..255] of Char;

  TTEFieldType = (ftChar, ftInteger, ftFixed, ftFloat, ftDate, ftTime);
  TTEEnumKind = (ekCheck, ekGroup, ekCombo);

  TMTEConnStats = record
    Size: Integer;
    Properties: DWORD;
    SentPackets: DWORD;
    RecvPackets: DWORD;
    SentBytes: DWORD;
    RecvBytes: DWORD;
  end;

function MTEConnect(Params, ErrorMsg: LPSTR): Integer; stdcall;
function MTEDisconnect(Idx: Integer): Integer; stdcall;
function MTEStructure(Idx: Integer; var Msg: PMTEMsg): Integer; stdcall;
function MTEExecTrans(Idx: Integer; TransName, Params, ResultMsg: LPSTR): Integer; stdcall;
function MTEOpenTable(Idx: Integer; TableName, Params: LPSTR; Complete: BOOL;
  var Msg: PMTEMsg): Integer; stdcall;
function MTECloseTable(Idx, HTable: Integer): Integer; stdcall;
function MTEAddTable(Idx, HTable, Ref: Integer): Integer; stdcall;
function MTERefresh(Idx: Integer; var Msg: PMTEMsg): Integer; stdcall;
function MTEErrorMsg(ErrCode: Integer): PChar; stdcall;
function MTEFreeBuffer(Idx: Integer): Integer; stdcall;
function MTEGetSnapshot(Idx: Integer; var Snapshot: PChar; var Len: Integer): Integer; stdcall;
function MTESetSnapshot(Idx: Integer; Snapshot: PChar; Len: Integer; ErrorMsg: LPSTR): Integer; stdcall;
function MTEGetExtData(Idx: Integer; DataSetName, ExtFileName: LPSTR; var Msg: PMTEMsg): Integer; stdcall;
function MTEExecTransIP(Idx: Integer; TransName, Params, ResultMsg: LPSTR; ClientIP: Integer): Integer; stdcall;
function MTEConnectionStats(Idx: Integer; var Stats: TMTEConnStats): Integer; stdcall;

type
  TMTESrlAddOn = (msaExecTransIP, msaGetExtData, msaConnState);
  TMTESrlAddOns = set of TMTESrlAddOn;

function GetMTESrlAddons: TMTESrlAddOns;

implementation

type
  TMTEExecTransIP = function (Idx: Integer; TransName, Params, ResultMsg: LPSTR; ClientIP: Integer): Integer; stdcall;
  TMTEGetExtData = function (Idx: Integer; DataSetName, ExtFileName: LPSTR; var Msg: PMTEMsg): Integer; stdcall;
  TMTEConnectionStats = function (Idx: Integer; var Stats: TMTEConnStats): Integer; stdcall;

var
  _MTEExecTransIP: TMTEExecTransIP = nil;
  _MTEGetExtData: TMTEGetExtData = nil;
  _MTEConnectionStats: TMTEConnectionStats = nil;
  MTESrlLoaded: Boolean = False;

const
  micexdll = 'mtesrl.dll';

function MTEConnect; external micexdll;
function MTEDisconnect; external micexdll;
function MTEStructure; external micexdll;
function MTEExecTrans; external micexdll;
function MTEOpenTable; external micexdll;
function MTECloseTable; external micexdll;
function MTEAddTable; external micexdll;
function MTERefresh; external micexdll;
function MTEErrorMsg; external micexdll;
function MTEFreeBuffer; external micexdll;
function MTEGetSnapshot; external micexdll;
function MTESetSnapshot; external micexdll;

resourcestring
  rsNoExecTransIP = 'MTEExecTransIP() function is not supported by MTESRL library';
  rsNoGetExtData  = 'MTEGetExtData() function is not supported by MTESRL library';

procedure LoadMTESrl;
var
  H: THandle;
begin
  if not MTESrlLoaded then
  begin
    MTESrlLoaded := True;
    H := GetModuleHandle(micexdll);
    if H <> 0 then
    begin
      _MTEExecTransIP := GetProcAddress(H, 'MTEExecTransIP');
      _MTEGetExtData := GetProcAddress(H, 'MTEGetExtData');
      _MTEConnectionStats := GetProcAddress(H, 'MTEConnectionStats');
    end;
  end;
end;

function MTEExecTransIP(Idx: Integer; TransName, Params, ResultMsg: LPSTR; ClientIP: Integer): Integer;
begin
  LoadMTESrl;
  if Assigned(_MTEExecTransIP) then
    Result := _MTEExecTransIP(Idx, TransName, Params, ResultMsg, ClientIp)
  else
  begin
    Result := MTE_TRANSREJECTED;
    lstrcpyn(ResultMsg, PChar(rsNoExecTransIP), 256);
  end;
end;

var
  TmpBuf: array [0..255] of Char;

function MTEGetExtData(Idx: Integer; DataSetName, ExtFileName: LPSTR; var Msg: PMTEMsg): Integer; stdcall;
begin
  LoadMTESrl;
  if Assigned(_MTEGetExtData) then
    Result := _MTEGetExtData(Idx, DataSetName, ExtFileName, Msg)
  else
  begin
    Result := MTE_TRANSREJECTED;
    PInteger(@TmpBuf)^ := Length(rsNoGetExtData);
    lstrcpyn(TmpBuf + SizeOf(Integer), PChar(rsNoGetExtData), SizeOf(TmpBuf) - SizeOf(Integer));
    Msg := @TmpBuf;
  end;
end;

function MTEConnectionStats(Idx: Integer; var Stats: TMTEConnStats): Integer; stdcall;
begin
  LoadMTESrl;
  if Assigned(_MTEConnectionStats) then
    Result := _MTEConnectionStats(Idx, Stats)
  else
  begin
    Result := MTE_TRANSREJECTED;
  end;
end;

function GetMTESrlAddons: TMTESrlAddOns;
begin
  LoadMTESrl;
  Result := [];
  if Assigned(_MTEExecTransIP) then Include(Result, msaExecTransIP);
  if Assigned(_MTEGetExtData) then Include(Result, msaGetExtData);
  if Assigned(_MTEConnectionStats) then Include(Result, msaConnState);
end;

end.
