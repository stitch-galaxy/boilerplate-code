//////////////////////////////////////////////////////////
//
// Module Name:
// CallStack.h
//
// Abstract:
//
//    This module defines the prototypes and constants required for the image help routines.
//    Contains debugging support routines.

//#define USE_DUMP

#pragma warning( disable : 4244)
#pragma warning( disable : 4311)
#pragma warning( disable : 4312)

void CALLBACK OnStackReporting(WORD wEventType, LPCSTR lpszMessage);
typedef void (CALLBACK* _PONSTACKREPORTING)(WORD wEventType, LPCSTR lpszMessage);

void DumpStack( PEXCEPTION_POINTERS pExcPtrs, void* const param, LPCSTR lpszDumpPath, _PONSTACKREPORTING pOnStackReporting);
// Exception handling and stack-walking:
static LONG WINAPI ExpFilter(EXCEPTION_POINTERS* pExp, DWORD dwExpCode, LPCSTR lpszDumpPath, _PONSTACKREPORTING pOnStackReporting = NULL)
{
  DumpStack(pExp, NULL, lpszDumpPath, pOnStackReporting);
  return EXCEPTION_EXECUTE_HANDLER;
}

static LONG WINAPI ExpFilter2(EXCEPTION_POINTERS* pExp, DWORD dwExpCode, LPCSTR lpszDumpPath, _PONSTACKREPORTING pOnStackReporting = NULL)
{
  DumpStack(pExp, NULL, lpszDumpPath, pOnStackReporting);
  return EXCEPTION_CONTINUE_EXECUTION;
}

#if USE_DUMP

#define RC_TRY   __try {

#define RC_EXCEPT(DumpPath, pOnStackReporting)   }  __except (ExpFilter(GetExceptionInformation(), GetExceptionCode(), DumpPath, pOnStackReporting)) { \
  }

#define RC_EXCEPT_EXT(DumpPath, pOnStackReporting)   }  __except (ExpFilter(GetExceptionInformation(), GetExceptionCode(), DumpPath, pOnStackReporting)) 


#define RC_EXCEPT_THROW(DumpPath, pOnStackReporting)   }  __except (ExpFilter2(GetExceptionInformation(), GetExceptionCode(), DumpPath, pOnStackReporting)) { \
  }

#else

#define RC_TRY  try
#define RC_EXCEPT(DumpPath, pOnStackReporting) catch(...) {}
#define RC_EXCEPT_EXT(DumpPath, pOnStackReporting) catch(...)
#define RC_EXCEPT_THROW(DumpPath, pOnStackReporting) catch(...) {}

#endif