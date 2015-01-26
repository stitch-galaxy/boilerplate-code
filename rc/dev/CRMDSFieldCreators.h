#pragma once

#include "MICEXTable.h"

class CRMDSFieldCreator
{
protected:
	CRMDSFieldCreator(CString csParams)
	{
		m_csParams = csParams;
	}

public:
	static CRMDSSubjectCreator* Create(CString csParams);

protected:
	CString		m_csParams;
}

class CRMDSNumberSubjectCreator : public CRMDSFieldCreator
{
protected:
	CRMDSNumberSubjectCreator(CString csParams) : CRMDSFieldCreator(csParams)
	{
	}

}

class CRMDSFieldSubjectCreator : public CRMDSFieldCreator
{
protected:
	CRMDSFieldSubjectCreator(CString csParams) : CRMDSFieldCreator(csParams)
	{
	}

}