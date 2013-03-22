/*
 * Copyright (c) 2009 Intuit Inc. All Rights reserved.
 * -------------------------------------------------------------------------------------------------
 *
 * File name  : QuickBaseContentHandler.java
 * Created on : Jan 6, 2009
 * @author Mirko Raner
 * -------------------------------------------------------------------------------------------------
 *
 *
 * *************************************************************************************************
 */

package com.intuit.quickbase.api;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * The class <code>QuickBaseContentHandler</code> is a SAX content handler that forwards query
 * results to a {@link QuickBaseResultHandler}. Currently, the class can only handle results in
 * the "structured" format.
 *
 * @author Mirko Raner
 * @version $Revision: 13 $ $Change: 747096 $
 */
class QuickBaseContentHandler extends DefaultHandler
{
    private final static String ERRCODE = "errcode"; //$NON-NLS-1$
    private final static String ERRTEXT = "errtext"; //$NON-NLS-1$
    private final static String ERRDETAIL = "errdetail"; //$NON-NLS-1$
    private final static String RECORD = "record"; //$NON-NLS-1$
    private final static String ID = "id"; //$NON-NLS-1$
    private final static String F = "f"; //$NON-NLS-1$

    private QuickBaseResultHandler resultHandler;
    private QuickBaseRecord currentRecord;
    private StringBuffer currentField;
    private StringBuffer error;
    private int currentFieldID;

    QuickBaseContentHandler(QuickBaseResultHandler resultHandler)
    {
        this.resultHandler = resultHandler;
    }

    /**
     * Processes the beginning of a new element. This method handles <code>&lt;record&gt;</code>
     * and <code>&lt;f&gt;</code> (i.e., field) elements as well as all elements that are nested
     * inside <code>&lt;f&gt;</code> elements (for example, <code>&lt;br&gt;</code> elements that
     * are part of multi-line field content).
     *
     * @param uri the current URI
     * @param localName the local name of the element (if any)
     * @param name the qualified name of the element
     * @param attributes the list of element attributes (can be empty but never <code>null</code>)
     * @throws SAXException if a problem was encountered during XML processing
     */
    public void startElement(String uri, String localName, String name, Attributes attributes)
    throws SAXException
    {
        if (RECORD.equals(name))
        {
            currentRecord = new QuickBaseRecord();
        }
        else if (F.equals(name))
        {
            currentField = new StringBuffer();
            currentFieldID = Integer.parseInt(attributes.getValue(ID));
        }
        else if (ERRCODE.equals(name) || ERRTEXT.equals(name) || ERRDETAIL.equals(name))
        {
            error = new StringBuffer();
        }
        else if (currentField != null)
        {
            // Some <f id="..."> elements may contain additional nested elements such as <url> or
            // <BR>; these elements should be incorporated into the field content:
            //
            appendNestedTagToCurrentField(name, attributes);
        }
    }

    /**
     * Processes text content inside or between elements. Text content is ignored outside of
     * <code>&lt;f&gt;</code> elements.
     *
     * @param characters an array containing the text
     * @param start the index in the array where the text begins
     * @param length the length of the text
     * @throws SAXException if a problem was encountered during XML processing
     */
    public void characters(char[] characters, int start, int length) throws SAXException
    {
        if (currentField != null)
        {
            currentField.append(characters, start, length);
        }
        if (error != null)
        {
            if (error.length() > 0)
            {
                error.append(' ');
            }
            error.append(characters, start, length);
        }
    }

    /**
     * Processes the end of an element. This method will close previously opened
     * <code>&lt;record&gt;</code> and <code>&lt;f&gt;</code> elements and will also process
     * end tags (i.e., <code>&lt;/</code>&nbsp;...&nbsp;<code>&gt;</code>) that are nested inside
     * <code>&lt;f&gt;</code> elements.
     *
     * @param uri the current URI
     * @param localName the local name of the element (if any)
     * @param name the qualified name of the element
     * @throws SAXException if a problem was encountered during XML processing
     */
    public void endElement(String uri, String localName, String name) throws SAXException
    {
        if (RECORD.equals(name))
        {
            resultHandler.handleRecord(currentRecord);
            currentRecord = null;
        }
        else if (F.equals(name))
        {
            currentRecord.setField(currentFieldID, currentField.toString());
            currentField = null;
        }
        else if (currentField != null)
        {
            // Some <f id="..."> elements may contain additional nested elements such as <url> or
            // <BR>; these elements should be incorporated into the field content:
            //
            appendNestedTagToCurrentField(name, null);
        }
    }

    /**
     * Handles the end of document processing and notifies the {@link QuickBaseResultHandler} that
     * processing is finished.
     *
     * @throws SAXException if a problem was encountered during XML processing
     */
    public void endDocument() throws SAXException
    {
        if (error != null)
        {
            resultHandler.handleException(new QuickBaseException(error.toString()));
        }
        resultHandler.done();
    }

    //------------------------------------- PRIVATE SECTION --------------------------------------//

    private void appendNestedTagToCurrentField(String tag, Attributes attributes)
    {
        if (attributes == null)
        {
            if (currentField.toString().endsWith('<' + tag + '>'))
            {
                // Combine adjacent opening and closing tags if possible:
                //
                currentField.insert(currentField.length()-1, '/');
            }
            else
            {
                currentField.append('<').append('/').append(tag).append('>');
            }
        }
        else
        {
            currentField.append('<').append(tag);
            int numberOfAttributes = attributes.getLength();
            if (numberOfAttributes > 0)
            {
                currentField.append(' ');
            }
            for (int index = 0; index < numberOfAttributes; index++)
            {
                currentField.append(attributes.getQName(index)).append('=');
                currentField.append('"').append(attributes.getValue(index)).append('"');
            }
            currentField.append('>');
        }
    }
}
