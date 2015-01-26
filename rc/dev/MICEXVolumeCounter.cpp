#include "stdafx.h"
#include "MICEXVolumeCounter.h"

#include "MICEXTable.h"

CMICEXVolumeCounter::CMICEXVolumeCounter()
{
	m_bUpdated	= TRUE; // publish anycase after secruties table loaded
	m_dVolume	= 0;
}

CMICEXVolumeCounter::~CMICEXVolumeCounter(void)
{
}

BOOL CMICEXVolumeCounter::Update(CMICEXTable* pTable, BYTE* pDataBuf)
{
	if (pDataBuf == NULL)
	{ // this branch is not used in current version (pDataBuf != NULL)
		BOOL bRes = FALSE;
	
		for(TABLEDATAVECTOR::iterator dataIterator = pTable->m_vTableData.begin(); dataIterator != pTable->m_vTableData.end(); dataIterator++)
		{
			bRes |= Update(pTable, (BYTE*)*dataIterator);
		}

		return bRes;
	}

	CString csVol, csBoardID, csSecCode;

	pTable->FieldValToString(pTable->GetFieldNumByName("VALTODAY"), pDataBuf, csVol);
	pTable->FieldValToString(pTable->GetFieldNumByName("SECBOARD"), pDataBuf, csBoardID);
	pTable->FieldValToString(pTable->GetFieldNumByName("SECCODE"), pDataBuf, csSecCode);
	
	csVol.TrimLeft();
	csVol.TrimRight();

	double dMICEXVol = atof((LPSTR)(LPCSTR)csVol);

	CMICEXTableKey key = CMICEXTableKey::GetSecuritiesKey(pTable, (LPCSTR)csBoardID, (LPCSTR)csSecCode);

	TABLEKEYMAPTOVOLUME::const_iterator keyIterator = m_mapVolumes.find(key);

	double dOldMICEXVol = 0;

	if (keyIterator != m_mapVolumes.end())
		dOldMICEXVol = keyIterator->second;

	if (dOldMICEXVol != dMICEXVol)
	{
		m_dVolume -= dOldMICEXVol;
		m_dVolume += dMICEXVol;

		m_mapVolumes[key] = dMICEXVol;

		m_bUpdated = TRUE;

		return TRUE;
	}

	return FALSE;
}