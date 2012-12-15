package net.kahowell.xsd.fuzzer.generator.builtin;

import net.kahowell.xsd.fuzzer.generator.AbstractXsdTypeValueGenerator;
import net.kahowell.xsd.fuzzer.generator.GeneratesType;

import org.apache.xmlbeans.SchemaType;

import com.google.inject.Singleton;

/**
 * Generates a value for the built-in type gDay.
 * 
 * Copyright (c) 2012 Kevin Howell. See LICENSE file for copying permission.
 * 
 * @author Kevin Howell
 */
@Singleton
@GeneratesType(namespace = "http://www.w3.org/2001/XMLSchema", localname = "gDay")
public class GDayGenerator extends AbstractXsdTypeValueGenerator {

	@Override
	protected String generateExampleValue(SchemaType rootType) {
		return "31";
	}
	
	@Override
	protected String generateRandomValue(SchemaType rootType) {
		return Integer.toString(random.nextInt(30) + 1);
	}
}
