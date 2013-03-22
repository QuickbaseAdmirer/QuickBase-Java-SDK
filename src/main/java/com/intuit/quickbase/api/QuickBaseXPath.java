/*
 * Copyright (c) 2009 Intuit Inc. All Rights reserved.
 * -------------------------------------------------------------------------------------------------
 *
 * File name  : QuickBaseXPath.java
 * Created on : Jan 7, 2009
 * @author Mirko Raner
 * -------------------------------------------------------------------------------------------------
 *
 *
 * *************************************************************************************************
 */

package com.intuit.quickbase.api;

import java.text.Format;
import java.text.MessageFormat;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * The class <code>QuickBaseXPath</code> provides some utility functions for creating
 * {@link XPathExpression}s as well as a shared repository of pre-compiled {@link XPathExpression}s
 * and pre-compiled {@link Format}s for creating parameterized {@link XPathExpression}s.
 *
 * @author Mirko Raner
 * @version $Revision: 13 $ $Change: 714052 $
 */
class QuickBaseXPath
{
    private final static XPath XPATH = XPathFactory.newInstance().newXPath();
    private final static String FORMAT_TABLE = "table[name=''{0}'']"; //$NON-NLS-1$
    private final static String FORMAT_FIELDS_FIELD = "fields/field[label=''{0}'']"; //$NON-NLS-1$

    /** The pre-compiled {@link XPathExpression} "<code>qdbapi</code>". **/
    public final static XPathExpression QDBAPI = compile("qdbapi"); //$NON-NLS-1$

    /** The pre-compiled {@link XPathExpression} "<code>qdbapi/dbid</code>". **/
    public final static XPathExpression QDBAPI_DBID = compile("qdbapi/dbid"); //$NON-NLS-1$

    /** The pre-compiled {@link XPathExpression} "<code>qdbapi/errcode</code>". **/
    public final static XPathExpression QDBAPI_ERRCODE = compile("qdbapi/errcode"); //$NON-NLS-1$

    /** The pre-compiled {@link XPathExpression} "<code>qdbapi/errtext</code>". **/
    public final static XPathExpression QDBAPI_ERRTEXT = compile("qdbapi/errtext"); //$NON-NLS-1$

    /** The pre-compiled XPath {@link Format} "<code>fields/field[label=''{0}'']</code>". **/
    public final static Format FIELDS_FIELD_LABEL = new MessageFormat(FORMAT_FIELDS_FIELD);

    /** The pre-compiled XPath {@link Format} "<code>table[name=''{0}'']</code>". **/
    public final static Format TABLE_NAME = new MessageFormat(FORMAT_TABLE);
    
    /** The pre-compiled {@link XPathExpression} "original/table_id/text()". **/
    public final static XPathExpression TABLE_ID = compile("table/original/table_id/text()");

    /**
     * Compiles an {@link XPathExpression} from a string.
     *
     * @param xpath the XPath as a string
     * @return the corresponding {@link XPathExpression}
     */
    public final static XPathExpression compile(String xpath)
    {
        try
        {
            return XPATH.compile(xpath);
        } 
        catch (XPathExpressionException exception)
        {
            throw new ExceptionInInitializerError(exception);
        }
    }

    /**
     * Compiles an {@link XPathExpression} from a {@link Format} and additional parameters.
     *
     * @param format the {@link Format} for the XPath
     * @param arguments additional arguments that need to be integrated into the expression
     * @return the corresponding {@link XPathExpression}
     */
    public final static XPathExpression compile(Format format, Object... arguments)
    {
        return compile(format.format(arguments));
    }
}
