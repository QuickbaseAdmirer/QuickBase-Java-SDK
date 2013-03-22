/*
 * Copyright (c) 2009 Intuit Inc. All Rights reserved.
 * -------------------------------------------------------------------------------------------------
 *
 * File name  : QuickBaseFieldResolver.java
 * Created on : Dec 31, 2008
 * @author Mirko Raner
 * -------------------------------------------------------------------------------------------------
 *
 *
 * *************************************************************************************************
 */

package com.intuit.quickbase.api;

/**
 * The interface {@link QuickBaseFieldResolver} defines a method for translating QuickBase string
 * responses to Java types and vice versa. An implementation of a {@link QuickBaseFieldResolver} is
 * necessary for creating a {@link QuickBaseField}.
 *
 * @param <$FieldType> the type that represents the field in Java
 * @see QuickBaseField
 *
 * @author Mirko Raner
 * @version $Revision: 13 $ $Change: 747096 $
 */
public interface QuickBaseFieldResolver<$FieldType>
{
    /**
     * Resolves a string to a Java object.
     *
     * @param string a string (as received from QuickBase)
     * @return a Java object that corresponds to the input string
     */
    public abstract $FieldType resolve(String string);

    /**
     * Converts a field value to a string that is suitable for use as part of a QuickBase query.
     *
     * @param field the field value
     * @return the corresponding string
     */
    public abstract String toString($FieldType field);
}
