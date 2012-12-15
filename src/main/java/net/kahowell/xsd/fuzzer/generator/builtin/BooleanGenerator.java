package net.kahowell.xsd.fuzzer.generator.builtin;

import java.util.Arrays;

import net.kahowell.xsd.fuzzer.generator.AbstractXsdTypeValueGenerator;
import net.kahowell.xsd.fuzzer.generator.GeneratesType;

import org.apache.xmlbeans.SchemaType;

import com.google.inject.Singleton;

/**
 * Generates a value for the built-in type boolean.
 * 
 * Copyright (c) 2012 Kevin Howell. See LICENSE file for copying permission.
 * 
 * @author Kevin Howell
 */
@Singleton
@GeneratesType(namespace = "http://www.w3.org/2001/XMLSchema", localname = "boolean")
public class BooleanGenerator extends AbstractXsdTypeValueGenerator {

	private static final String[] TRUE_FALSE_ARRAY = {"true", "false"};

	@Override
	public String generateRandomValue(SchemaType rootType) {
		// TODO apply restrictions
		return (String) random.nextSample(Arrays.asList(TRUE_FALSE_ARRAY), 1)[0];
	}
	
	@Override
	protected String generateExampleValue(SchemaType rootType) {
		return "true";
	}

}
