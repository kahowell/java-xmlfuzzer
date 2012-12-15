package net.kahowell.xsd.fuzzer;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlCursor;

/**
 * Interface for generating XML snippets in an XML document based on an XSD 
 * specified type.
 * 
 * Copyright (c) 2012 Kevin Howell. See LICENSE file for copying permission.
 * 
 * @author Kevin Howell
 * 
 * @see SchemaType
 *
 */
public interface XsdTypeValueGenerator {
	/**
	 * Strategy for picking values
	 */
	public enum Strategy {
		/** Use an example value (think foobar) */
		EXAMPLE,
		/** Use a random value */
		RANDOM,
		/** Use the minimum valid value */
		MINIMUM,
		/** Use the maximum valid value */
		MAXIMUM
	}
	
	/**
	 * Generate a value of the given type, writing the results to cursor
	 * 
	 * @param cursor cursor used to write the value
	 * @param type type of the value
	 */
	public void generateValue(XmlCursor cursor, SchemaType type);
}
