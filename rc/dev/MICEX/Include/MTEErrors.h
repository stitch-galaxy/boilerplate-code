/* *************************************************************************  */
/* MTEErrors.h -- Header File                                                  */
/*                                                                            */
/* *************************************************************************  */

#ifndef MTEERRORS_HPP
#define MTEERRORS_HPP


#define MTE_OK				0
#define MTE_CONFIG			-1
#define MTE_SRVUNAVAIL		-2
#define MTE_LOGERROR		-3
#define MTE_INVALIDCONNECT	-4
#define MTE_NOTCONNECTED	-5
#define MTE_WRITE			-6
#define MTE_READ			-7
#define MTE_TSMR			-8
#define MTE_NOMEMORY		-9
#define MTE_ZLIB			-10
#define MTE_PKTINPROGRESS	-11
#define MTE_PKTNOTSTARTED	-12
#define MTE_FATALERROR		-13 
#define MTE_INVALIDHANDLE	-14
#define MTE_DSROFF			-15
 #define MTE_ERRUNKNOWN		-16
#define MTE_WRONGPARAM		-17
#define MTE_REJECTION		-18
#define MTE_TEUNAVAIL		-19
#define MTE_NOTLOGGEDIN     -20
#define MTE_WRONGVERSION    -21

//Don't forget update this define
#define _MTELastError		22


#ifdef __cplusplus
extern "C" {
#endif

char * GetErrorStr( int ecode );
char * GetShortErrorStr( int ecode );

#ifdef __cplusplus
}
#endif

#endif