// RC_MICEX_Feed.cpp : Defines the class behaviors for the application.
//

#include "stdafx.h"
#include "RC_MICEX_Feed.h"
#include "RCITService.h"
#include "DllUtils.h"
#include "RC_ConfigInt_Int.h"
#include "FEEDConfig.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif


/////////////////////////////////////////////////////////////////////////////
// CRC_MICEX_FeedApp

BEGIN_MESSAGE_MAP(CRC_MICEX_FeedApp, CWinApp)
	//{{AFX_MSG_MAP(CRC_MICEX_FeedApp)
	//}}AFX_MSG
	ON_COMMAND(ID_HELP, CWinApp::OnHelp)
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CRC_MICEX_FeedApp construction

CRC_MICEX_FeedApp::CRC_MICEX_FeedApp()
{
}

/////////////////////////////////////////////////////////////////////////////
// The one and only CRC_MICEX_FeedApp object

CRC_MICEX_FeedApp theApp;

/////////////////////////////////////////////////////////////////////////////
// CRC_MICEX_FeedApp initialization

BOOL CRC_MICEX_FeedApp::InitInstance()
{

	// Standard initialization
#if(_MFC_VER < 0x0700)
#ifdef _AFXDLL
	Enable3dControls();			// Call this when using MFC in a shared DLL
#else
	Enable3dControlsStatic();	// Call this when linking to MFC statically
#endif
#endif

	CString csProfile;
	string path;
	string sFeedName;
	string serviceFileName;
	string serviceName;
	string displayName;
	string feedWindowTitle;

	ServiceUtils::GetProgramPath(path, ServiceUtils::fullPath | ServiceUtils::fileName);
	ServiceUtils::GetProgramPath(serviceFileName, ServiceUtils::fileName);

	csProfile.Format("%s.config.xml", path.c_str());

	if(LoadConfigFile((LPCSTR)csProfile))
	{
		sFeedName = CFEEDConfig::GetParamStringAtStart("serviceName");
		feedWindowTitle = CFEEDConfig::GetParamStringAtStart("feedWindowTitle");
		SaveAndCloseConfigFile();
	}
	else
		return FALSE;
	
	if(sFeedName.length() == 0 || feedWindowTitle.length() == 0)
		return FALSE;

	serviceName = serviceFileName;
	displayName = serviceFileName;

	if(sFeedName.length() != 0)
	{
		feedWindowTitle.append(" - ");
		feedWindowTitle.append(sFeedName);

		serviceName.append("_");
		serviceName.append(sFeedName);

		displayName.append(" - ");
		displayName.append(sFeedName);
	}

	HWND hwndMyAnotherWindow = FindWindow(NULL, feedWindowTitle.c_str());
	if(hwndMyAnotherWindow != NULL) 
	{
		::PostMessage(hwndMyAnotherWindow, WM_SYSCOMMAND, SC_RESTORE, NULL);
		return FALSE;
	}

	// create the service-object
	CRCITService* serv = new CRCITService(serviceFileName.c_str(), serviceName.c_str(), displayName.c_str(), csProfile);
	
	// RegisterService() checks the parameterlist for predefined switches
	// (such as -d or -i etc.; see NTService.h for possible switches) and
	// starts the service's functionality.
	// You can use the return value from "RegisterService()"
	// as your exit-code.
	serv->RegisterService(m_lpCmdLine);

	delete serv;

	return FALSE;

	// Since the dialog has been closed, return FALSE so that we exit the
	//  application, rather than start the application's message pump.
	//return FALSE;
}
