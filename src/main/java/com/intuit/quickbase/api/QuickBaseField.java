/*
 * Copyright (c) 2009 Intuit Inc. All Rights reserved.
 * -------------------------------------------------------------------------------------------------
 *
 * File name  : QuickBaseField.java
 * Created on : Dec 31, 2008
 * @author Mirko Raner
 * -------------------------------------------------------------------------------------------------
 *
 *
 * *************************************************************************************************
 */

package com.intuit.quickbase.api;

/**
 * The class {@link QuickBaseField} represents a field in a QuickBase record. Its objects carry
 * the ID and type information of a particular field and can be used as accessors to extract field
 * data from a record. A {@link QuickBaseField} does not contain or represent data of a particular
 * field; it represents the abstract concept of a particular field inside arbitrary records.
 * <p/>
 * To create a {@link QuickBaseField}, it is necessary to specify the field's QuickBase ID and a
 * {@link QuickBaseFieldResolver} instance that is capable of translating the plain string results
 * received from QuickBase into instances of the corresponding Java type.
 *
 * @param <$FieldType> the Java type of the field (for example,
 * <code>QuickBaseField&lt;Integer&gt;</code> would be a field that can be represented in Java
 * as an {@link Integer} or <code>int</code>)
 *
 * @author Mirko Raner
 * @version $Revision: 13 $ $Change: 747096 $
 */
public final class QuickBaseField<$FieldType>
{
    private int id;
    private QuickBaseFieldResolver<$FieldType> resolver;

    /**
     * Creates a new {@link QuickBaseField} with the specified ID and resolver.
     *
     * @param id the field ID used by QuickBase
     * @param resolver a {@link QuickBaseFieldResolver} that can translate strings to instances of
     * the correct data type
     */
    public QuickBaseField(int id, QuickBaseFieldResolver<$FieldType> resolver)
    {
        this.id = id;
        this.resolver = resolver;
    }

    /**
     * Gets the ID of the field.
     *
     * @return the field ID
     */
    public int getID()
    {
        return id;
    }

    /**
     * Resolves the given string into an instance of the corresponding Java type (using the
     * field's {@link QuickBaseFieldResolver}).
     *
     * @param string the plain string content as received from QuickBase
     * @return a Java object representing the field value
     */
    public $FieldType get(String string)
    {
        return resolver.resolve(string);
    }

    /**
     * Converts a field value object to a string. This method is mainly used to compose queries
     * based on actual TCR objects. The user code will ideally work with business objects, but
     * QuickBase queries require a string representation. For example:
     * <small><pre>
     * TestCaseQuery<TCRID> getQueryMatching(TCRID id)
     * {
     *     return new TestCaseQuery<TCRID>(IS, id);
     * }
     * </pre></small>
     *
     * @param field a business object stored in a QuickBase field
     * @return a string representation of that business object, suitable for use in a QuickBase
     * query string
     */
    @SuppressWarnings("unchecked")
    public String toString(Object field) // TODO: this should not use Object as parameter type!
    {
        return resolver.toString(($FieldType)field);
    }
}
