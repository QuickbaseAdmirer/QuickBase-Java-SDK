/*
 * Copyright (c) 2009 Intuit Inc. All Rights reserved.
 * -------------------------------------------------------------------------------------------------
 *
 * File name  : QuickBaseSAXParserThread.java
 * Created on : Jan 7, 2009
 * @author Mirko Raner
 * -------------------------------------------------------------------------------------------------
 *
 *
 * *************************************************************************************************
 */

package com.intuit.quickbase.api;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * The class <code>QuickBaseSAXParserThread</code> provides a background processing thread that
 * parses the {@link InputSource} for a QuickBase result set and sends the individual XML records to
 * a <code>QuickBaseContentHandler</code>, which in turn forwards them to a client-supplied
 * {@link QuickBaseResultHandler}.
 *
 * @author Mirko Raner
 * @version $Revision: 13 $ $Change: 714052 $
 */
class QuickBaseSAXParserThread extends Thread
{
    private InputSource response;
    private QuickBaseResultHandler resultHandler;

    /**
     * Creates a new {@link QuickBaseSAXParserThread}.
     *
     * @param response the {@link InputSource} received from QuickBase
     * @param resultHandler the client-supplied {@link QuickBaseResultHandler}
     */
    QuickBaseSAXParserThread(InputSource response, QuickBaseResultHandler resultHandler)
    {
        this.response = response;
        this.resultHandler = resultHandler;
    }

    /**
     * Performs the parsing.
     *
     * @see Runnable#run()
     */
    public void run()
    {
        try
        {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse(response, new QuickBaseContentHandler(resultHandler));
        }
        catch (ParserConfigurationException parserConfigurationException)
        {
            resultHandler.handleException(new QuickBaseException(parserConfigurationException));
        }
        catch (SAXException saxException)
        {
            resultHandler.handleException(new QuickBaseException(saxException));
        }
        catch (IOException ioException)
        {
            resultHandler.handleException(new QuickBaseException(ioException));
        }
    }
}
