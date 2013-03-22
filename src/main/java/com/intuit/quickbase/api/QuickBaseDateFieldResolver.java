package com.intuit.quickbase.api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This resolves java Date fields to Strings used by QuickBase.  The client needs to specify
 * a suitable format string to be used for the Dates.
 * 
 * @author Brad Brown
 */
public class QuickBaseDateFieldResolver implements QuickBaseFieldResolver<Date> {

	private SimpleDateFormat simpleDateFormat;
	
	public QuickBaseDateFieldResolver(String formatStr) {
		simpleDateFormat = new SimpleDateFormat(formatStr);
	}
	
	@Override
	public Date resolve(String str) {
		try 
		{
			return simpleDateFormat.parse(str);
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	@Override
	public String toString(Date field) {
		if (field == null) {
			return null;
		}
		return simpleDateFormat.format(field);
	}
	
}
