package net.kahowell.xsd.fuzzer.generator.builtin;

import net.kahowell.xsd.fuzzer.generator.AbstractXsdTypeValueGenerator;
import net.kahowell.xsd.fuzzer.generator.GeneratesType;

import org.apache.xmlbeans.SchemaType;

import com.google.inject.Singleton;

/**
 * Generates a value for the built-in type string.
 * 
 * Copyright (c) 2012 Kevin Howell. See LICENSE file for copying permission.
 * 
 * @author Kevin Howell
 */
@Singleton
@GeneratesType(namespace = "http://www.w3.org/2001/XMLSchema", localname = "string")
public class StringGenerator extends AbstractXsdTypeValueGenerator {

	@Override
	public String generateExampleValue(SchemaType rootType) {
		return "foobar";
	}
	
	@Override
	protected String generateRandomValue(SchemaType rootType) {
		return random.nextHexString(16); // TODO more sophisticated generation
	}

}
