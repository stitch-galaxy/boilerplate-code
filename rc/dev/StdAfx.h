// stdafx.h : include file for standard system include files,
//  or project specific include files that are used frequently, but
//      are changed infrequently
//

#if !defined(AFX_STDAFX_H__A45DA647_92C1_11D4_8739_00805F9BAE39__INCLUDED_)
#define AFX_STDAFX_H__A45DA647_92C1_11D4_8739_00805F9BAE39__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#define WINVER 0x0500
#define _WIN32_WINNT WINVER 
#define VC_EXTRALEAN		// Exclude rarely-used stuff from Windows headers

#include <afxwin.h>         // MFC core and standard components
#include <afxext.h>         // MFC extensions
#include <afxdtctl.h>		// MFC support for Internet Explorer 4 Common Controls
#ifndef _AFX_NO_AFXCMN_SUPPORT
#include <afxcmn.h>			// MFC support for Windows Common Controls
#endif // _AFX_NO_AFXCMN_SUPPORT

#import <msxml3.dll> named_guids

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#pragma warning(disable:4786)

#include <Winsvc.h>
#include <iostream>
#include <vector>
#include <map>
using namespace std ;

#include "MTESrl.h"
#include "MCXFeedService_Int.h"

#include "NTServiceEventLogMsg.h"

#import "C:\Program Files\Common Files\System\ADO\msado15.dll" no_namespace rename("EOF", "EndOfFile")
#include <ole2.h>

enum LogSectionsEnum
{
	lNone,
	lSystem,
	lConfig,
	lTrade,
	lSecurity,
	lAlive,
	lVolume,
	lUstrade,
	lTibrv
};

#endif // !defined(AFX_STDAFX_H__A45DA647_92C1_11D4_8739_00805F9BAE39__INCLUDED_)
