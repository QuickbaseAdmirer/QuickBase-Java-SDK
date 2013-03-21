/*
 * Copyright (c) 2009 Intuit Inc. All Rights reserved.
 * -------------------------------------------------------------------------------------------------
 *
 * File name  : QuickBaseException.java
 * Created on : Dec 31, 2008
 * @author Mirko Raner
 * -------------------------------------------------------------------------------------------------
 *
 *
 * *************************************************************************************************
 */

package com.intuit.quickbase.api;

/**
 * The class {@link QuickBaseException} is an all-purpose exception class used for reporting with
 * accessing QuickBase. The exceptions can carry a message, a nested root cause exception, or both.
 *
 * @author Mirko Raner
 * @version $Revision: 13 $ $Change: 714052 $
 */
public class QuickBaseException extends Exception
{
    final static long serialVersionUID = -6517798552605593430L;

    /**
     * Creates a new {@link QuickBaseException} without a specific message or nested exception.
     */
    public QuickBaseException()
    {
        super();
    }

    /**
     * Creates a new {@link QuickBaseException} with a specific message.
     *
     * @param message the exception message
     */
    public QuickBaseException(String message)
    {
        super(message);
    }

    /**
     * Creates a new {@link QuickBaseException} with a nested exception.
     *
     * @param exception the nested exception
     */
    public QuickBaseException(Exception exception)
    {
        super(exception);
    }

    /**
     * Creates a new {@link QuickBaseException} with a message and a nested exception.
     *
     * @param message the exception message
     * @param exception the nested exception
     */
    public QuickBaseException(String message, Exception exception)
    {
        super(message, exception);
    }
}
