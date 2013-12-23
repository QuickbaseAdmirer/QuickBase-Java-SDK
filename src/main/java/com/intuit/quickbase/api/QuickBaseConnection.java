/*
 * Copyright (c) 2009 Intuit Inc. All Rights reserved.
 * -------------------------------------------------------------------------------------------------
 *
 * File name  : QuickBaseConnection.java
 * Created on : Dec 31, 2008
 * @author Mirko Raner
 * -------------------------------------------------------------------------------------------------
 *
 *
 * *************************************************************************************************
 */

package com.intuit.quickbase.api;

import java.io.IOException;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import static com.intuit.quickbase.api.QuickBaseAPICall.API_Authenticate;
import static com.intuit.quickbase.api.QuickBaseAPICall.API_FindDBByName;

/**
 * The class {@link QuickBaseConnection} represents an HTTP connection to a QuickBase server.
 * A connection by itself is not specific to a particular database yet. The connection can be used
 * to obtain one or more {@link QuickBaseDatabase} objects. The connection object represents a
 * simulated HTTP "session", which is established by using cookies.
 *
 * @author Mirko Raner
 * @version $Revision: 13 $ $Change: 714052 $
 */
public class QuickBaseConnection
{
    private final static NameValuePair[] NO_PARAMETERS = {};
    private static String HTTPS_QB_MAIN = "https://www.quickbase.com/db/main?"; //$NON-NLS-1$
    private static String HTTPS_QB = "https://www.quickbase.com/db/"; //$NON-NLS-1$
    private final static String USERNAME = "username"; //$NON-NLS-1$
    private final static String PASSWORD = "password"; //$NON-NLS-1$
    private final static String DBNAME = "dbname"; //$NON-NLS-1$
    private final static String TICKET = "TICKET"; //$NON-NLS-1$
    private final static String ACT = "act"; //$NON-NLS-1$
    private final static String OK = "0"; //$NON-NLS-1$
    private final static int FIRST = 0;
    private final static char QUERY = '?';

    private HttpClient httpClient;
    private String appToken = null;
    
    public QuickBaseConnection(PasswordAuthentication credentials) throws QuickBaseException
    {
        httpClient = new HttpClient();
        execute(authenticate(credentials.getUserName(), credentials.getPassword()));
    }

    /**
     * Finds a database by its name. If multiple databases exist with that name only the first
     * database will be returned. In QuickBase, only database IDs are unique (but database names
     * not necessarily).
     *
     * @param databaseName the name of the data base
     * @return the {@link QuickBaseDatabase} object of the database
     * @throws QuickBaseException if no database of that name could be found
     */
    public QuickBaseDatabase findDBByName(String databaseName) throws QuickBaseException
    {
        return findDBsByName(databaseName).get(FIRST);
    }

    /**
     * Finds all databases with a given name. In QuickBase, only database IDs are unique (but
     * database names not necessarily).
     *
     * @param databaseName the name of the data base
     * @return a {@link List} of {@link QuickBaseDatabase} objects
     * @throws QuickBaseException if no database of that name could be found
     */
    public List<QuickBaseDatabase> findDBsByName(String databaseName) throws QuickBaseException
    {
        Document response = execute(method(API_FindDBByName, dbname(databaseName)));
        NodeList dbids;
        try
        {
            dbids = (NodeList)QuickBaseXPath.QDBAPI_DBID.evaluate(response, XPathConstants.NODESET);
        }
        catch (XPathExpressionException xpathException)
        {
            throw new QuickBaseException(xpathException);
        }

        // TODO: com.intuit.tcr.utilities.NodeListIterable would come in handy here:
        //
        int numberOfChildren = dbids.getLength();
        List<QuickBaseDatabase> databases = new ArrayList<QuickBaseDatabase>();
        for (int index = 0; index < numberOfChildren; index++)
        {
            Node dbidNode = dbids.item(index);
            String dbid = dbidNode.getTextContent();
            databases.add(new QuickBaseDatabase(this, dbid));
            
        }
        return databases;
    }

    /**
     * Returns the "ticket" for the current connection. The ticket is sent as a cookie to
     * authenticate QuickBase users.
     *
     * @return the ticket string or <code>null</code> if no ticket is available
     */
    public String getTicket()
    {
        for (Cookie cookie: httpClient.getState().getCookies())
        {
           if (TICKET.equals(cookie.getName()))
           {
               //return TICKET + '=' + cookie.getValue();
        	   return cookie.getValue();
           }
        }
        return null;
    }
    
    public void setAppToken(String appToken)
    {
    	this.appToken = appToken;
    }

    public void setQBLocation(String https_qb, String https_qb_main)
    {
    	QuickBaseConnection.HTTPS_QB = https_qb;
    	QuickBaseConnection.HTTPS_QB_MAIN = https_qb_main;

    }
    
    /**
     * Executes a {@link QuickBaseAPICall} for a certain database and returns the database response
     * as a SAX {@link InputSource}.
     *
     * @param dbid the database ID
     * @param call the {@link QuickBaseAPICall} to be executed
     * @param parameters {@link NameValuePair}s for additional parameters
     * @return a SAX {@link InputSource} that contains the server's response
     * @throws QuickBaseException if the execution was unsuccessful
     */
    public InputSource execute(String dbid, QuickBaseAPICall call, NameValuePair... parameters)
    throws QuickBaseException
    {
        HttpMethod method = new GetMethod(HTTPS_QB + dbid + QUERY);
        NameValuePair[] query = new NameValuePair[parameters.length+1];
        query[FIRST] = act(call);
        System.arraycopy(parameters, 0, query, 1, parameters.length);
        method.setQueryString(query);
        try
        {
            httpClient.executeMethod(method);
            //
            // TODO: evaluate the returned HTTP response code

            return new InputSource(method.getResponseBodyAsStream());
        }
        catch (IOException exception)
        {
            throw new QuickBaseException(exception);
        }
    }

    /**
     * Executes a {@link QuickBaseAPICall} for a certain QuickBase object as an XML payload
     * and returns the response as a SAX {@link InputSource}.
     * @param qbid The id of the object the call is acting upon
     * @param the {@link QuickBaseAPICall} to be executed
     * @param elements the XML elements to put into the payload
     * @return a SAX {@link InputSource} that contains the server's response
     * @throws QuickBaseException if the execution was unsuccessful
     */
    public InputSource executeXml(String qbid, QuickBaseAPICall call, String... elements)
    throws QuickBaseException
    {
    	StringBuffer xml = new StringBuffer();
    	xml.append("<qdbapi>\n");
    	
    	xml.append("<ticket>");
    	xml.append(getTicket());
    	xml.append("</ticket>");
    	
    	if (appToken != null)
    	{
    		xml.append("<apptoken>");
    		xml.append(appToken);
    		xml.append("</apptoken>");
    	}
    	
    	for (String element: elements) {
    		xml.append(element);
    		xml.append("\n");
    	}
    	xml.append("</qdbapi>\n");
    	String payload = xml.toString();
    	
        try
        {
        	PostMethod method = new PostMethod(HTTPS_QB + qbid + QUERY);
        	NameValuePair[] query = new NameValuePair[1];
            query[0] = act(call);
            method.setQueryString(query);
              
        	StringRequestEntity entity = new StringRequestEntity(payload, "application/xml", "UTF-8");
    		method.setRequestEntity(entity);
            httpClient.executeMethod(method);
            //
            // TODO: evaluate the returned HTTP response code

            return new InputSource(method.getResponseBodyAsStream());
        }
        catch (IOException exception)
        {
            throw new QuickBaseException(exception);
        }
    }
    
    /**
     * Executes a {@link QuickBaseAPICall} without additional parameters for a certain database and
     * returns the database response as a SAX {@link InputSource}.
     *
     * @param dbid the database ID
     * @param call the {@link QuickBaseAPICall} to be executed
     * @return a SAX {@link InputSource} that contains the server's response
     * @throws QuickBaseException if the execution was unsuccessful
     */
    public InputSource execute(String dbid, QuickBaseAPICall call)
    throws QuickBaseException
    {
        return execute(dbid, call, NO_PARAMETERS);
    }

    //------------------------------------- PRIVATE SECTION --------------------------------------//

    private Document execute(HttpMethod authenticate) throws QuickBaseException
    {
        try
        {
            httpClient.executeMethod(authenticate);
            Document response = getResponse(authenticate);
            String errorCode = QuickBaseXPath.QDBAPI_ERRCODE.evaluate(response);
            if (OK.equals(errorCode))
            {
                return response;
            }
            String errorText = QuickBaseXPath.QDBAPI_ERRTEXT.evaluate(response);
            errorText += " (error code " + errorCode + ')'; //$NON-NLS-1$
            throw new QuickBaseException(errorText);
        }
        catch (IOException ioException)
        {
            throw new QuickBaseException(ioException);
        }
        catch (SAXException saxException)
        {
            throw new QuickBaseException(saxException);
        }
        catch (XPathExpressionException xpathException)
        {
            throw new QuickBaseException(xpathException);
        }
        catch (ParserConfigurationException parserException)
        {
            throw new QuickBaseException(parserException);
        }
    }

    private static Document getResponse(HttpMethod method)
    throws IOException, SAXException, ParserConfigurationException
    {
        InputSource inputSource = new InputSource(method.getResponseBodyAsStream());
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        return documentBuilder.parse(inputSource);
    }

    private static HttpMethod method(QuickBaseAPICall call, NameValuePair... parameters)
    {
        HttpMethod method = new GetMethod(HTTPS_QB_MAIN);
        NameValuePair[] query = new NameValuePair[parameters.length+1];
        System.arraycopy(parameters, 0, query, 1, parameters.length);
        query[FIRST] = act(call);
        method.setQueryString(query);
        return method;
    }

    private static HttpMethod authenticate(String username, char[] password)
    {
        GetMethod authenticate = new GetMethod(HTTPS_QB_MAIN);
        return query(authenticate, act(API_Authenticate), username(username), password(password));
    }

    private static HttpMethod query(HttpMethod method, NameValuePair... parameters)
    {
        method.setQueryString(parameters);
        return method;
    }

    private static NameValuePair act(QuickBaseAPICall action)
    {
        return new NameValuePair(ACT, action.toString());
    }

    private static NameValuePair username(String username)
    {
        return new NameValuePair(USERNAME, username);
    }

    private static NameValuePair password(char[] password)
    {
        return new NameValuePair(PASSWORD, String.valueOf(password));
    }

    private static NameValuePair dbname(String dbname)
    {
        return new NameValuePair(DBNAME, dbname);
    }
}
