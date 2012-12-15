package net.kahowell.xsd.fuzzer.generator.builtin;

import net.kahowell.xsd.fuzzer.generator.AbstractXsdTypeValueGenerator;
import net.kahowell.xsd.fuzzer.generator.GeneratesType;

import org.apache.xmlbeans.SchemaType;

import com.google.inject.Singleton;

/**
 * Generates a value for the built-in type float.
 * 
 * Copyright (c) 2012 Kevin Howell. See LICENSE file for copying permission.
 * 
 * @author Kevin Howell
 */
@Singleton
@GeneratesType(namespace = "http://www.w3.org/2001/XMLSchema", localname = "float")
public class FloatGenerator extends AbstractXsdTypeValueGenerator {

	@Override
	public String generateExampleValue(SchemaType rootType) {
		return "3.14";
	}
	
	@Override
	protected String generateRandomValue(SchemaType rootType) {
		// TODO handle facets
		return Float.toString((float) random.nextDouble());
	}

}
