package net.kahowell.xsd.fuzzer.generator.builtin;

import java.sql.Date;
import java.text.SimpleDateFormat;

import net.kahowell.xsd.fuzzer.generator.AbstractXsdTypeValueGenerator;
import net.kahowell.xsd.fuzzer.generator.GeneratesType;

import org.apache.xmlbeans.SchemaType;

import com.google.inject.Singleton;

/**
 * Generates a value for the built-in type time.
 * 
 * Copyright (c) 2012 Kevin Howell. See LICENSE file for copying permission.
 * 
 * @author Kevin Howell
 */
@Singleton
@GeneratesType(namespace = "http://www.w3.org/2001/XMLSchema", localname = "time")
public class TimeGenerator extends AbstractXsdTypeValueGenerator {

	@Override
	protected String generateExampleValue(SchemaType rootType) {
		return "12:14:00";
	}
	
	@Override
	protected String generateRandomValue(SchemaType rootType) {
		return new SimpleDateFormat("HH:mm:ss").format(new Date(random.nextLong()));
	}
}
