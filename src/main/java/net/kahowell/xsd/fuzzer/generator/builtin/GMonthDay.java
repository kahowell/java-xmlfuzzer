package net.kahowell.xsd.fuzzer.generator.builtin;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.kahowell.xsd.fuzzer.generator.AbstractXsdTypeValueGenerator;
import net.kahowell.xsd.fuzzer.generator.GeneratesType;

import org.apache.xmlbeans.SchemaType;

import com.google.inject.Singleton;

/**
 * Generates a value for the built-in type gMonthDay.
 * 
 * Copyright (c) 2012 Kevin Howell. See LICENSE file for copying permission.
 * 
 * @author Kevin Howell
 */
@Singleton
@GeneratesType(namespace = "http://www.w3.org/2001/XMLSchema", localname = "gMonthDay")
public class GMonthDay extends AbstractXsdTypeValueGenerator {

	@Override
	protected String generateExampleValue(SchemaType rootType) {
		return "12-25";
	}
	
	@Override
	protected String generateRandomValue(SchemaType rootType) {
		return new SimpleDateFormat("MM-dd").format(new Date(random.nextLong()));
	}
}
