// RC_MICEX_Feed.h : main header file for the DWRepSRV application
//

#if !defined(AFX_DWRepSRV_H__A45DA643_92C1_11D4_8739_00805F9BAE39__INCLUDED_)
#define AFX_DWRepSRV_H__A45DA643_92C1_11D4_8739_00805F9BAE39__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#ifndef __AFXWIN_H__
	#error include 'stdafx.h' before including this file for PCH
#endif

#include "resource.h"		// main symbols

/////////////////////////////////////////////////////////////////////////////
// CRC_MICEX_FeedApp:
// See RC_MICEX_Feed.cpp for the implementation of this class
//

class CRC_MICEX_FeedApp : public CWinApp
{
public:
	CRC_MICEX_FeedApp();

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CRC_MICEX_FeedApp)
	public:
	virtual BOOL InitInstance();
	//}}AFX_VIRTUAL

// Implementation

	//{{AFX_MSG(CRC_MICEX_FeedApp)
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};


/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_DWRepSRV_H__A45DA643_92C1_11D4_8739_00805F9BAE39__INCLUDED_)
