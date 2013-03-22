/*
 * Copyright (c) 2009 Intuit Inc. All Rights reserved.
 * -------------------------------------------------------------------------------------------------
 *
 * File name  : QuickBaseAuthenticator.java
 * Created on : Jan 7, 2009
 * @author Mirko Raner
 * -------------------------------------------------------------------------------------------------
 *
 *
 * *************************************************************************************************
 */

package com.intuit.quickbase.api;

import java.net.PasswordAuthentication;

/**
 * The interface {@link QuickBaseAuthenticator} describes a simple login mechanism for QuickBase.
 * It is modeled after {@link java.net.Authenticator} but only returns a simple
 * {@link PasswordAuthentication} consisting of a user name and a password; no additional
 * information is supported.
 * <p/>
 * To communicate with QuickBase, client code must provide an implementation of
 * {@link QuickBaseAuthenticator}. A common implementation is to show a dialog to the user that
 * requests the user name and password. The authenticator implementation typically depends on the
 * UI framework that is being used by the client code, for example, Swing or SWT.
 *
 * @author Mirko Raner
 * @version $Revision: 13 $ $Change: 714052 $
 */
public interface QuickBaseAuthenticator
{
    /**
     * Returns the user credentials for logging in to QuickBase.
     *
     * @return a {@link PasswordAuthentication} or <code>null</code> if authentication was
     * unsuccessful or was canceled by the user
     */
    public abstract PasswordAuthentication login();
}
