/*
 * Copyright (c) 2009 Intuit Inc. All Rights reserved.
 * -------------------------------------------------------------------------------------------------
 *
 * File name  : QuickBaseDatabase.java
 * Created on : Jan 7, 2009
 * @author Mirko Raner
 * -------------------------------------------------------------------------------------------------
 *
 *
 * *************************************************************************************************
 */ 

package com.intuit.quickbase.api;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import org.apache.commons.httpclient.NameValuePair;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import com.intuit.quickbase.api.query.CList;
import com.intuit.quickbase.api.query.QueryExecutionMode;

import static com.intuit.quickbase.api.QuickBaseAPICall.API_DoQuery;
import static com.intuit.quickbase.api.QuickBaseAPICall.API_GetSchema;

/**
 * The class {@link QuickBaseDatabase} encapsulates access to a particular QuickBase database.
 * An existing {@link QuickBaseConnection} is necessary to create a new {@link QuickBaseDatabase}.
 * Generally, all QuickBase API operations that require a database ID (DBID) should be performed
 * using a {@link QuickBaseDatabase} object. This includes common operations such as
 * {@link QuickBaseAPICall#API_DoQuery} or {@link QuickBaseAPICall#API_DeleteRecord}.
 * Operations that do not refer to a specific database, such as
 * {@link QuickBaseAPICall#API_Authenticate} or {@link QuickBaseAPICall#API_FindDBByName} should be
 * accessed directly via a {@link QuickBaseConnection} instead.
 *
 * @author Mirko Raner
 * @version $Revision: 13 $ $Change: 747096 $
 */
public class QuickBaseDatabase
{
    private final static String CLIST = "clist"; //$NON-NLS-1$
    private final static String QUERY = "query"; //$NON-NLS-1$
    private final static String EMPTY = ""; //$NON-NLS-1$
    private final static String FMT = "fmt"; //$NON-NLS-1$
    private final static String STRUCTURED = "structured"; //$NON-NLS-1$
    private final static NameValuePair FMT_STRUCTURED = new NameValuePair(FMT, STRUCTURED);

    private QuickBaseConnection connection;
    private String dbid;

    /**
     * Creates a new {@link QuickBaseDatabase}. The mere act of creating the
     * {@link QuickBaseDatabase} object does not involve any communications over the network and
     * currently does not perform a validation of the supplied DBID.
     *
     * @param connection the {@link QuickBaseConnection}
     * @param dbid the database ID
     */
    public QuickBaseDatabase(QuickBaseConnection connection, String dbid)
    {
        this.connection = connection;
        this.dbid = dbid;
    }

    /**
     * Gets the schema information of the database.
     *
     * @return the {@link QuickBaseSchema} of the database
     * @throws QuickBaseException if a problem occurred while communicating with the database
     */
    public QuickBaseSchema getSchema() throws QuickBaseException
    {
        InputSource response = connection.execute(dbid, API_GetSchema);
//        InputStream byteStream = response.getByteStream();
//        int n = 0;
//        try {
//			while ((n = byteStream.read()) != -1)
//			{
//				System.err.write(n);
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        try
        {
            Node qdbapi = (Node)QuickBaseXPath.QDBAPI.evaluate(response, XPathConstants.NODE);
            return new QuickBaseSchema(this, qdbapi);
        }
        catch (XPathExpressionException xpathException)
        {
            throw new QuickBaseException(xpathException);
        }
    }

    /**
     * Performs an unfiltered query for all records in the database.
     * As the result of this operation may be a very large number of records the database response
     * must be handled by a {@link QuickBaseResultHandler}. The result handler receives records as
     * they become available and does not require the client code to wait until the complete
     * result set was received.
     *
     * @param resultHandler a {@link QuickBaseResultHandler} that processes the results
     * @param clist a {@link CList} that specifies which fields are included in the results
     * @throws QuickBaseException if a problem occurred while communicating with the database
     */
    public void doQuery(QuickBaseResultHandler resultHandler, CList clist, QueryExecutionMode mode) throws QuickBaseException
    {
        doQuery(resultHandler, EMPTY, clist, mode);
    }

    /**
     * Performs a query that returns matching records from the database.
     * As the result of this operation may be a very large number of records the database response
     * must be handled by a {@link QuickBaseResultHandler}. The result handler receives records as
     * they become available and does not require the client code to wait until the complete
     * result set was received.
     *
     * @param resultHandler a {@link QuickBaseResultHandler} that processes the results
     * @param query the query string
     * @param clist a {@link CList} that specifies which fields are included in the results
     * @throws QuickBaseException if a problem occurred while communicating with the database
     */
    public void doQuery(QuickBaseResultHandler resultHandler, String query, CList clist, QueryExecutionMode mode) throws QuickBaseException
    {
    	InputSource result;
    	result = connection.execute(dbid, API_DoQuery, FMT_STRUCTURED, query(query), clist(clist));
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

    	QuickBaseSAXParserThread thread = new QuickBaseSAXParserThread(result, resultHandler);
    	thread.start();
    	if (mode == QueryExecutionMode.synchronous) {
    		try {
    			thread.join();
    		} catch (InterruptedException e) {
    		}
    	}
    	
    }

    QuickBaseConnection getConnection() {
		return connection;
	}
    
    //-------------------------------------- PRIVATE SECTION -------------------------------------//

    private static NameValuePair query(String query)
    {
        return new NameValuePair(QUERY, query);
    }

    private static NameValuePair clist(CList clist)
    {
        return new NameValuePair(CLIST, clist.toString());
    }
    
}
