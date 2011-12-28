; Script generated by the Inno Setup Script Wizard.
; SEE THE DOCUMENTATION FOR DETAILS ON CREATING INNO SETUP SCRIPT FILES!

#define MyAppName "MailBySims"
#define MyAppVerName "MailBySims 1.0"
#define MyAppPublisher "SimsCoprs"
#define MyAppExeName "mbs.exe"

[Setup]
; NOTE: The value of AppId uniquely identifies this application.
; Do not use the same AppId value in installers for other applications.
; (To generate a new GUID, click Tools | Generate GUID inside the IDE.)
#include "Setup.version.inc"
AppId={{58DDDC3E-DA55-441E-A0CE-3258FA410B79}
AppName={#MyAppName}
AppVerName={#MyAppVerName} {#VERSION_INFO}

AppPublisher={#MyAppPublisher}
DefaultDirName={pf}\{#MyAppName}
DisableDirPage=yes
DefaultGroupName={#MyAppName}
AllowNoIcons=yes
OutputDir=./
OutputBaseFilename=setup
SetupIconFile=.././Images/logo_appli.ico
Compression=lzma
SolidCompression=yes
;logo � utiliser sur la page d'acceuil du setup
;164*313
WizardImageFile=.././Images/logo_appli_WizardImageFile.bmp
;logo � utiliser dans les fenetre suivantes      111*113
WizardSmallImageFile=.././Images/logo_appli_SmallWizardImageFile.bmp


[Languages]
Name: "french"; MessagesFile: "compiler:Languages\French.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked
Name: "quicklaunchicon"; Description: "{cm:CreateQuickLaunchIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Files]
Source: ".././mbs.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: ".././mbs.jar"; DestDir: "{app}"; Flags: ignoreversion
Source: ".././lib/activation.jar"; DestDir: {app}/lib; Flags: ignoreversion;
Source: ".././lib/deltasync.jar"; DestDir: {app}/lib; Flags: ignoreversion;
Source: ".././lib/jaybird_full_2_1_6.jar"; DestDir: {app}/lib; Flags: ignoreversion;
Source: ".././lib/mail.jar"; DestDir: {app}/lib; Flags: ignoreversion;
Source: ".././lib/libdeltasync/*"; DestDir: "{app}/lib/libdeltasync"; Flags: ignoreversion
Source: ".././lib/Firebird-2.0.3.12981-1-Win32.exe"; DestDir: "{app}/lib"; Flags: ignoreversion

Source: ".././Images/*"; DestDir: "{app}/Images"; Flags: ignoreversion recursesubdirs createallsubdirs
Source: ".././template/*"; DestDir: "{app}/template"; Flags: ignoreversion recursesubdirs createallsubdirs
; NOTE: Don't use "Flags: ignoreversion" on any shared system files

[Icons]
Name: "{group}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"
Name: "{group}\{cm:UninstallProgram,{#MyAppName}}"; Filename: "{uninstallexe}"
Name: "{commondesktop}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: desktopicon
Name: "{userappdata}\Microsoft\Internet Explorer\Quick Launch\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: quicklaunchicon

[Run]
Filename: "{app}\{#MyAppExeName}"; Description: "{cm:LaunchProgram,{#MyAppName}}"; Flags: nowait postinstall skipifsilent



