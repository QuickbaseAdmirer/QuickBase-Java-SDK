/*
 * Copyright (c) 2009 Intuit Inc. All Rights reserved.
 * -------------------------------------------------------------------------------------------------
 *
 * File name  : QuickBaseTable.java
 * Created on : Jan 7, 2009
 * @author Mirko Raner
 * -------------------------------------------------------------------------------------------------
 *
 *
 * *************************************************************************************************
 */

package com.intuit.quickbase.api;

import java.util.List;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import org.apache.commons.httpclient.NameValuePair;
import org.w3c.dom.Node;

/**
 * The class {@link QuickBaseTable} represents a table in a QuickBase database.
 * The {@link #getField(String, QuickBaseFieldResolver)} method allows client code to create
 * {@link QuickBaseField}s that can be used to access individual fields in the table's records.
 *
 * @author Mirko Raner
 * @version $Revision: 13 $ $Change: 714052 $
 */
public class QuickBaseTable
{
    private final static String ID = "id"; //$NON-NLS-1$

    private QuickBaseDatabase database;
    private Node table;
    private String tableId;

    QuickBaseTable(QuickBaseDatabase database, Node table, String tableId)
    {
    	this.database = database;
        this.table = table;
        this.tableId = tableId;
    }

    /**
     * Creates a new {@link QuickBaseField}.
     *
     * @param name the field label
     * @param resolver the {@link QuickBaseFieldResolver} for resolving the field's contents
     * @param <$FieldType> the type parameter for the type that represents the field in Java
     * @return a {@link QuickBaseField} object representing the requested field
     * @throws QuickBaseException if a problem occurred during processing
     */
    public <$FieldType> QuickBaseField<$FieldType> getField(String name,
    QuickBaseFieldResolver<$FieldType> resolver) throws QuickBaseException
    {
        Node node = null;
        try
        {
            XPathExpression xpath = QuickBaseXPath.compile(QuickBaseXPath.FIELDS_FIELD_LABEL, name);
            node = (Node)xpath.evaluate(table, XPathConstants.NODE);
            if (node == null) {
            	throw new QuickBaseException("Field " + name + " was not found in table " + tableId);
            }
        }
        catch (XPathExpressionException xpathException)
        {
            throw new QuickBaseException(xpathException);
        }
        Node idAttribute = node.getAttributes().getNamedItem(ID);
        if (idAttribute == null || idAttribute.getNodeValue() == null) {
        	throw new QuickBaseException("Field " + name + " was not found in table " + tableId);
        }
		int id = Integer.parseInt(idAttribute.getNodeValue());
        return new QuickBaseField<$FieldType>(id, resolver);
    }

    /**
     * Returns the {@link QuickBaseDatabase} that owns this table.
     */
	public QuickBaseDatabase getDatabase() {
		return database;
	}

	/**
	 * Adds a record whose field values are specified in a {@link QuickBaseRecordBuilder} to
	 * this table.
	 * 
	 * @param recordBuilder
	 * @throws QuickBaseException if an error occurrs communicating with QuickBase.
	 */
	public void addRecord(QuickBaseRecordBuilder recordBuilder) throws QuickBaseException {
		
		if (!recordBuilder.getTable().tableId.equals(tableId)) {
			throw new IllegalArgumentException("RecordBuilder is not setup for use with this table");
		}
		List<NameValuePair> fieldValues = recordBuilder.getFieldValues();
		String[] elements = new String[fieldValues.size()];
		for (int i = 0; i < fieldValues.size(); i++) {
			NameValuePair fieldValue = fieldValues.get(i);
			
			StringBuffer b = new StringBuffer();
			b.append("<field fid=\"");
			b.append(fieldValue.getName());
			b.append("\">");
			b.append(fieldValue.getValue());
			b.append("</field>");
			
			elements[i] = b.toString();
		}
		
//		InputSource result;
//		result = 
		database.getConnection().executeXml(tableId, QuickBaseAPICall.API_AddRecord, elements);

//    	InputStream byteStream = result.getByteStream();
//    	int n = 0;
//    	try {
//    		while ((n = byteStream.read()) != -1)
//    		{
//    			System.err.write(n);
//    		}
//    	} catch (IOException e) {
//    		// TODO Auto-generated catch block
//    		e.printStackTrace();
//    	}
	}
	   
    
}
