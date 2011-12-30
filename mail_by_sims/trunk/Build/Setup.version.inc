;*******************************************************************************
; Fichier de modele pour ajout de version aux scripts inno setup.;
;*******************************************************************************
#define VERSION_INFO "0.0.1.272"
#define VERSION_FICHIER "${version.buildinfo}"
VersionInfoVersion={#VERSION_INFO}
VersionInfoTextVersion={#VERSION_FICHIER}