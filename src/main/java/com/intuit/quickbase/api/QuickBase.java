/*
 * Copyright (c) 2009 Intuit Inc. All Rights reserved.
 * -------------------------------------------------------------------------------------------------
 *
 * File name  : QuickBase.java
 * Created on : Jan 7, 2009
 * @author Mirko Raner
 * -------------------------------------------------------------------------------------------------
 *
 *
 * *************************************************************************************************
 */

package com.intuit.quickbase.api;

/**
 * The class {@link QuickBase} can be considered the primary entry point into the QuickBase API.
 * It provides the only way for client code to obtain a {@link QuickBaseConnection} to a database.
 * A {@link QuickBaseAuthenticator} is required to log in to QuickBase. The authenticator can either
 * be set globally in advance, using {@link #setAuthenticator(QuickBaseAuthenticator)}, or it can
 * be supplied when obtaining a connection using {@link #getConnection(QuickBaseAuthenticator)}.
 *
 * @author Mirko Raner
 * @version $Revision: 13 $ $Change: 714052 $
 */
public class QuickBase
{
    private static QuickBaseAuthenticator authenticator;

    private QuickBase()
    {
        super();
    }

    /**
     * Obtains a new {@link QuickBaseConnection} using the global authenticator that was set by
     * calling {@link #setAuthenticator(QuickBaseAuthenticator)}.
     *
     * @return a new {@link QuickBaseConnection}
     * @throws QuickBaseException if the connection to the database could not be established
     */
    public static QuickBaseConnection getConnection() throws QuickBaseException
    {
        return getConnection(authenticator);
    }

    /**
     * Obtains a new {@link QuickBaseConnection} using the supplied
     * {@link #setAuthenticator(QuickBaseAuthenticator)}.
     *
     * @param qba the {@link QuickBaseAuthenticator}
     * @return a new {@link QuickBaseConnection}
     * @throws QuickBaseException if the connection to the database could not be established
     */
    public static QuickBaseConnection getConnection(QuickBaseAuthenticator qba)
    throws QuickBaseException
    {
        if (qba == null)
        {
            throw new QuickBaseException("no QuickBaseAuthenticator specified"); //$NON-NLS-1$
        }
        return new QuickBaseConnection(qba.login());
    }

    /**
     * Gets the current global {@link QuickBaseAuthenticator} that was set using
     * {@link #setAuthenticator(QuickBaseAuthenticator)}.
     *
     * @return the {@link QuickBaseAuthenticator}, or <code>null</code> if none was set
     */
    public static QuickBaseAuthenticator getAuthenticator()
    {
        return authenticator;
    }

    /**
     * Sets the {@link QuickBaseAuthenticator} to be used for obtaining connections via
     * {@link #getConnection()}.
     *
     * @param authenticator the global {@link QuickBaseAuthenticator} to be used
     */
    public static void setAuthenticator(QuickBaseAuthenticator authenticator)
    {
        QuickBase.authenticator = authenticator;
    }
}
