package com.intuit.quickbase.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.NameValuePair;

/**
 * The class {@link QuickBaseRecordBuilder} allows you to construct a record image, field by field,
 * ready for storing into a {@link QuickBaseTable}.
 * 
 *
 * @author Brad Brown
 * @version $Revision: 13 $ $Change: 714052 $
 */
public class QuickBaseRecordBuilder {

	private QuickBaseTable table;
	
	// This is a list of <field id, value> pairs
	private List<NameValuePair> fieldValues;
	
	public QuickBaseRecordBuilder(QuickBaseTable table) {
		this.table = table;
		this.fieldValues = new ArrayList<NameValuePair>();
	}
	
	public <$FieldType> void addField(String name, $FieldType value, QuickBaseFieldResolver<$FieldType> resolver) throws QuickBaseException {
		QuickBaseField<$FieldType> field = table.getField(name, resolver);
		String strValue = field.toString(value);
		NameValuePair pair = new NameValuePair(String.valueOf(field.getID()), strValue);
		fieldValues.add(pair);
	}

	public void addField(String name, String value) throws QuickBaseException {
		addField(name, value, QuickBaseStandardFieldResolvers.STRING_RESOLVER);
	}
	
	public void addField(String name, Integer value) throws QuickBaseException {
		addField(name, value, QuickBaseStandardFieldResolvers.INTEGER_RESOLVER);
	}
	
	public void addField(String name, Long value) throws QuickBaseException {
		addField(name, value, QuickBaseStandardFieldResolvers.LONG_RESOLVER);
	}
	
	public void addField(String name, Boolean value) throws QuickBaseException {
		addField(name, value, QuickBaseStandardFieldResolvers.BOOLEAN_RESOLVER);
	}

	List<NameValuePair> getFieldValues() {
		return fieldValues;
	}

	public QuickBaseTable getTable() {
		return table;
	}
	
	
}
