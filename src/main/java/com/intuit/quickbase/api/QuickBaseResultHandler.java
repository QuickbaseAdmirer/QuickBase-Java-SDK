/*
 * Copyright (c) 2009 Intuit Inc. All Rights reserved.
 * -------------------------------------------------------------------------------------------------
 *
 * File name  : QuickBaseResultHandler.java
 * Created on : Jan 6, 2009
 * @author Mirko Raner
 * -------------------------------------------------------------------------------------------------
 *
 *
 * *************************************************************************************************
 */

package com.intuit.quickbase.api;

/**
 * The {@link QuickBaseResultHandler} interface defines an event-driven way of receiving query
 * results from QuickBase. Large result sets can take a few seconds (or maybe even minutes) to
 * be transferred from the QuickBase server to the client. Waiting for the complete transfer of the
 * result set to finish can incur significant delays for the user. To provide a quick and
 * interactive user experience, the {@link QuickBaseResultHandler} provides a way to receive
 * QuickBase records as they become available. As soon as the response for an individual record
 * was processed the {@link #handleRecord(QuickBaseRecord)} method will be called (even if the
 * transmission of the complete result set is far from complete). Client code can already use the
 * incoming results (for example, to populate a table or other UI elements) while result
 * transmission still continues in the background.
 *
 * @author Mirko Raner
 * @version $Revision: 13 $ $Change: 714052 $
 */
public interface QuickBaseResultHandler
{
    /**
     * Receives a {@link QuickBaseRecord}.
     *
     * @param record the next record in the result set
     */
    public abstract void handleRecord(QuickBaseRecord record);

    /**
     * Provides a notification about an exception that was encountered during processing.
     *
     * @param exception the {@link QuickBaseException}
     */
    public abstract void handleException(QuickBaseException exception);

    /**
     * Provides a notification that all records in the result set were processed.
     */
    public abstract void done();
}
