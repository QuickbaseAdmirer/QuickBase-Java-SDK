/**
 * 
 */
package com.intuit.quickbase.api;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple implementation of {@link QuickBaseResultHandler} that simply accumulates all
 * result records into a list.  If an exception is report it is simply retained here.
 * The implementation is synchronized, but clients should not access the record list until
 * isDone() is true.
 * 
 * @author Brad Brown
 */
public class QuickBaseSimpleResultHandler implements QuickBaseResultHandler {

	private List<QuickBaseRecord> records;
	private boolean done;
	private QuickBaseException exception;
	
	private Object lock;
	
	
	public QuickBaseSimpleResultHandler() {
		records = new ArrayList<QuickBaseRecord>();
		done = false;
		lock = new Object();
		exception = null;
	}

	@Override
	public void handleRecord(QuickBaseRecord record) {
		synchronized(lock) {
			records.add(record);
		}
	}

	
	@Override
	public void handleException(QuickBaseException exception) {
		synchronized(lock) {
			this.exception = exception;
		}
	}

	@Override
	public void done() {
		synchronized(lock) {
			done = true;
		}
	}

	public List<QuickBaseRecord> getRecords() {
		synchronized(lock) {
			return records;
		}
	}

	public boolean isDone() {
		synchronized(lock) {
			return done;
		}
	}
	
	public QuickBaseException getException() {
		synchronized(lock) {
			return this.exception;
		}
	}
}
