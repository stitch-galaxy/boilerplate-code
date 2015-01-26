// File ControlStructures.h
// Definition of the control structures, which used to control Service

typedef struct _tagMCXFeedConfig {
	int nMICEXRefreshInterval; // secs
	int nMICEX_ERROR_RefreshInterval; // secs
	int nLogBufferLength;
	int nMICEXLogoffHour;
	int nMICEXLogoffMin;
	int nMICEXLogonHour;
	int nMICEXLogonMin;
	int nMICEXStartMarksHour;
	int nMICEXStartMarksMin;
	int nMICEXStopMarksHour;
	int nMICEXStopMarksMin;

	char szMICEXConnectCOMPort[64];
	char szMICEXConnectCOMRate[64];
	char szMICEXConnectCOMTimeOut[64];
	BOOL fRestartFeed;
	char szUserChangedConfig[255];
	BOOL fLogOnMon;
	BOOL fLogOnTue;
	BOOL fLogOnWed;
	BOOL fLogOnThu;
	BOOL fLogOnFri;
	BOOL fLogOnSat;
	BOOL fLogOnSun;
} MCXFEEDCONFIG, *PMCXFEEDCONFIG;

enum MCXJob_IDs {
	MCXJOB_NONE,
	MCXJOB_RESTART_MICEX_LINK,
	MCXJOB_DUMP_SECURITY
};


typedef struct _tagMCXFeedJobRequest {
	int nJob_id;
	char szUserRequested[255];
} MCXJOBREQUEST, *PMCXJOBREQUEST;