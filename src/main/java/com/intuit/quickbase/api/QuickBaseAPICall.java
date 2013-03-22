/*
 * Copyright (c) 2009 Intuit Inc. All Rights reserved.
 * -------------------------------------------------------------------------------------------------
 *
 * File name  : QuickBaseAPICall.java
 * Created on : Dec 31, 2008
 * @author Mirko Raner
 * -------------------------------------------------------------------------------------------------
 *
 *
 * *************************************************************************************************
 */

package com.intuit.quickbase.api;

/**
 * The enumeration {@link QuickBaseAPICall} provides a list of all API calls to the QuickBase HTTP
 * API.
 *
 * @author Mirko Raner
 * @version $Revision: 13 $ $Change: 714052 $
 */
public enum QuickBaseAPICall
{
    /** API_AddField. **/ API_AddField, 
    /** API_AddRecord. **/ API_AddRecord,
    /** API_AddReplaceDBPage. **/ API_AddReplaceDBPage,
    /** API_AddUserToRole. **/ API_AddUserToRole, 
    /** API_Authenticate. **/ API_Authenticate,
    /** API_ChangeRecordOwner. **/ API_ChangeRecordOwner,
    /** API_ChangeUserRole. **/ API_ChangeUserRole, 
    /** API_CloneDatabase. **/ API_CloneDatabase,
    /** API_CreateDatabase. **/ API_CreateDatabase, 
    /** API_CreateTable. **/ API_CreateTable,
    /** API_DeleteDatabase. **/ API_DeleteDatabase, 
    /** API_DeleteField. **/ API_DeleteField,
    /** API_DeleteRecord. **/ API_DeleteRecord, 
    /** API_DoQuery. **/ API_DoQuery,
    /** API_EditRecord. **/ API_EditRecord, 
    /** API_FieldAddChoices. **/ API_FieldAddChoices,
    /** API_FieldRemoveChoices. **/ API_FieldRemoveChoices,
    /** API_FindDBByName. **/ API_FindDBByName, 
    /** API_GenAddRecordForm. **/ API_GenAddRecordForm,
    /** API_GenResultsTable. **/ API_GenResultsTable, 
    /** API_GetAppDTMInfo. **/ API_GetAppDTMInfo,
    /** API_GetDBInfo. **/ API_GetDBInfo, 
    /** API_GetDBPage. **/ API_GetDBPage,
    /** API_GetDBvar. **/ API_GetDBvar, 
    /** API_GetNumRecords. **/ API_GetNumRecords,
    /** API_GetRecordAsHTML. **/ API_GetRecordAsHTML, 
    /** API_GetRecordInfo. **/ API_GetRecordInfo,
    /** API_GetRoleInfo. **/ API_GetRoleInfo, 
    /** API_GetSchema. **/ API_GetSchema,
    /** API_GetUserInfo. **/ API_GetUserInfo, 
    /** API_GetUserRole. **/ API_GetUserRole,
    /** API_GrantedDBs. **/ API_GrantedDBs, 
    /** API_ImportFromCSV. **/ API_ImportFromCSV,
    /** API_ProvisionUser. **/ API_ProvisionUser, 
    /** API_PurgeRecords. **/ API_PurgeRecords,
    /** API_RemoveUserFromRole. **/ API_RemoveUserFromRole, 
    /** API_RenameApp. **/ API_RenameApp,
    /** API_RunImport. **/ API_RunImport, 
    /** API_SendInvitation. **/ API_SendInvitation,
    /** API_SetDBvar. **/ API_SetDBvar, 
    /** API_SetFieldProperties. **/ API_SetFieldProperties,
    /** API_SignOut. **/ API_SignOut, 
    /** API_UploadFile. **/ API_UploadFile,
    /** API_UserRoles. **/ API_UserRoles
}
