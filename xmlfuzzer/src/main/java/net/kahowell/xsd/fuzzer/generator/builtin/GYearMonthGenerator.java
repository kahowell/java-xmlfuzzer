package net.kahowell.xsd.fuzzer.generator.builtin;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.kahowell.xsd.fuzzer.generator.AbstractXsdTypeValueGenerator;
import net.kahowell.xsd.fuzzer.generator.GeneratesType;

import org.apache.xmlbeans.SchemaType;

import com.google.inject.Singleton;

/**
 * Generates a value for the built-in type gYearMonth.
 * 
 * Copyright (c) 2012 Kevin Howell. See LICENSE file for copying permission.
 * 
 * @author Kevin Howell
 */
@Singleton
@GeneratesType(namespace = "http://www.w3.org/2001/XMLSchema", localname = "gYearMonth")
public class GYearMonthGenerator extends AbstractXsdTypeValueGenerator {

	@Override
	protected String generateExampleValue(SchemaType rootType) {
		return "1979-01";
	}
	
	@Override
	protected String generateRandomValue(SchemaType rootType) {
		return new SimpleDateFormat("yyyy-MM").format(new Date(random.nextLong()));
	}
}
