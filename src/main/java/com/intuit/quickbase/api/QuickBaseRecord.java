/*
 * Copyright (c) 2009 Intuit Inc. All Rights reserved.
 * -------------------------------------------------------------------------------------------------
 *
 * File name  : QuickBaseRecord.java
 * Created on : Jan 6, 2009
 * @author Mirko Raner
 * -------------------------------------------------------------------------------------------------
 *
 *
 * *************************************************************************************************
 */

package com.intuit.quickbase.api;

import java.util.HashMap;
import java.util.Map;

/**
 * The class {@link QuickBaseRecord} represents an individual record that was received in response
 * to a QuickBase query. The QuickBase API framework (or, more specifically, the
 * <code>QuickBaseContentHandler</code>) will create the {@link QuickBaseRecord} and set the
 * individual fields using the {@link #setField(int, String)} method. Client code can access the
 * fields as proper Java objects using the {@link #getField(QuickBaseField)} method.
 *
 * @author Mirko Raner
 * @version $Revision: 13 $ $Change: 714052 $
 */
public class QuickBaseRecord
{
    private Map<Integer, String> recordData;

    QuickBaseRecord()
    {
        recordData = new HashMap<Integer, String>();
    }

    /**
     * Sets a field in the record.
     *
     * @param fieldID the ID of the field
     * @param value the plain string value of the field (as extracted from the QuickBase response)
     */
    protected void setField(int fieldID, String value)
    {
        recordData.put(fieldID, value);
    }

    /**
     * Gets a field from the record.
     *
     * @param <$FieldType> the type parameter specifying the type that represents the field in Java
     * @param field the {@link QuickBaseField} reference that specifies which field to extract
     * @return the field value as a proper Java object
     */
    public <$FieldType> $FieldType getField(QuickBaseField<$FieldType> field)
    {
        return field.get(recordData.get(field.getID()));
    }

    /**
     * Converts the {@link QuickBaseRecord} to a string.
     *
     * @return a string representation of the record
     */
    public String toString()
    {
        return getClass().getName() + recordData;
    }
}
