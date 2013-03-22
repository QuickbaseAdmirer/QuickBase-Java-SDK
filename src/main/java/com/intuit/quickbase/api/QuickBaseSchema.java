/*
 * Copyright (c) 2009 Intuit Inc. All Rights reserved.
 * -------------------------------------------------------------------------------------------------
 *
 * File name  : QuickBaseSchema.java
 * Created on : Jan 5, 2009
 * @author Mirko Raner
 * -------------------------------------------------------------------------------------------------
 *
 *
 * *************************************************************************************************
 */

package com.intuit.quickbase.api;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;

/**
 * The class {@link QuickBaseSchema} represents the database schema of a QuickBase database.
 * The schema can be used to find tables and meta-information of a given {@link QuickBaseDatabase}.
 *
 * TODO: add more schema-related methods beside just getTable(...)
 *
 * @see QuickBaseDatabase#getSchema()
 *
 * @author Mirko Raner
 * @version $Revision: 13 $ $Change: 714052 $
 */
public class QuickBaseSchema
{
	private QuickBaseDatabase database;
    private Node schema;

    QuickBaseSchema(QuickBaseDatabase database, Node schema)
    {
    	this.database = database;
        this.schema = schema;
    }

    /**
     * Gets a {@link QuickBaseTable} with the specified name.
     *
     * @param tableName the name of the table
     * @return the corresponding {@link QuickBaseTable} object
     * @throws QuickBaseException if an error occurred while processing the QuickBase response
     */
    public QuickBaseTable getTable(String tableName) throws QuickBaseException
    {
        try
        {
            XPathExpression xpath = QuickBaseXPath.compile(QuickBaseXPath.TABLE_NAME, tableName);
            Node table = (Node)xpath.evaluate(schema, XPathConstants.NODE);
            
            String tableId = (String)QuickBaseXPath.TABLE_ID.evaluate(schema, XPathConstants.STRING);
            if (tableId == null) {
            	throw new QuickBaseException("Unable to determine table id for table " + tableName);
            }
            
            return new QuickBaseTable(database, table, tableId);
        }
        catch (XPathExpressionException xpathException)
        {
            throw new QuickBaseException(xpathException);
        }
    }


    /**
     * Returns the {@link QuickBaseDatabase} that owns this schema.
     */
	public QuickBaseDatabase getDatabase() {
		return database;
	}
    
    
}
