/*
 * Copyright (c) 2009 Intuit Inc. All Rights reserved.
 * -------------------------------------------------------------------------------------------------
 *
 * File name  : CList.java
 * Created on : Jan 5, 2009
 * @author Mirko Raner
 * -------------------------------------------------------------------------------------------------
 *
 *
 * *************************************************************************************************
 */

package com.intuit.quickbase.api.query;

import com.intuit.quickbase.api.QuickBaseField;

/**
 * The class {@link CList} represents the equivalent of a <code>clist</code> ("column list")
 * argument as defined by the QuickBase HTTP API. A <code>clist</code> consists of a number of
 * column IDs separated by dots. For example, the <code>clist</code> <code>3.130.162</code> would
 * specify that columns 3, 130, and 162 should be included in the result set.
 * {@link CList} objects can be created from raw IDs or from {@link QuickBaseField}s (which already
 * contain the column ID implicitly).
 *
 * @author Mirko Raner
 * @version $Revision: 13 $ $Change: 714052 $
 */
public class CList
{
    private int[] columns;

    /**
     * Creates a new {@link CList} from raw column IDs.
     *
     * @param columns the column IDs
     */
    public CList(int... columns)
    {
        this.columns = columns;
    }

    /**
     * Creates a new {@link CList} from a list of {@link QuickBaseField}s.
     *
     * @param fields the {@link QuickBaseField}s whose corresponding columns should be included
     */
    public CList(QuickBaseField<?>... fields)
    {
        this(convertFieldsToIDs(fields));
    }

    /**
     * Converts the {@link CList} to a string. The result of this method can be directly passed to
     * the QuickBase HTTP API.
     *
     * @return a string of the form "&lt;<i>column ID 1</i>&gt;<code>.</code>&lt;<i>column ID
     * 2</i>&gt;<code>.</code> ... <code>.</code>&lt;<i>column ID </i>n&gt;"
     */
    public String toString()
    {
        StringBuffer string = new StringBuffer();
        for (int column: columns)
        {
            if (string.length() > 0)
            {
                string.append('.');
            }
            string.append(column);
        }
        return string.toString();
    }

    //-------------------------------------- PRIVATE SECTION -------------------------------------//

    private static int[] convertFieldsToIDs(QuickBaseField<?>[] fields)
    {
        int numberOfFields = fields.length;
        int[] id = new int[numberOfFields];
        for (int index = 0; index < numberOfFields; index++)
        {
            id[index] = fields[index].getID();
        }
        return id;
    }
}
