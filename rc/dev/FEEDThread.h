#pragma once

class CRCFEEDService;

class CFEEDThread
{
public:
	CFEEDThread(CString csThreadName);
	virtual ~CFEEDThread(void);

public:
	void Start();
	void Stop();
	void Pause();
	void Continue();

	BOOL StartAndWait();
	void WaitFinalyState();

protected:
	static DWORD WINAPI ThreadMainStatic(CFEEDThread* lpMain);

	DWORD ThreadMain();

protected:
	virtual BOOL RunBegin() = 0;
	virtual void RunStep() = 0;
	virtual void RunEnd() = 0;

protected:
	void StopProcess();

protected:
	CString				m_csThreadName;

protected:
	DWORD	m_iThreadID;
	HANDLE	m_hThreadHnd;

	HANDLE	m_hStop;
	HANDLE	m_hPause;
	HANDLE	m_hContinue;

public:
	BOOL	m_bIsRunning;
	HANDLE	m_hThreadReady;
	HANDLE	m_hThreadStoped;
};
