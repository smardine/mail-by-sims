;*******************************************************************************
; Fichier de mod�le pour ajout de version aux scripts inno setup.;
;*******************************************************************************
#define VERSION_INFO "${version.point}"
#define VERSION_FICHIER "${version.buildinfo}"
VersionInfoVersion={#VERSION_INFO}
VersionInfoTextVersion={#VERSION_FICHIER}