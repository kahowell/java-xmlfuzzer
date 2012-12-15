package net.kahowell.xsd.fuzzer.generator.builtin;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.kahowell.xsd.fuzzer.generator.AbstractXsdTypeValueGenerator;
import net.kahowell.xsd.fuzzer.generator.GeneratesType;

import org.apache.xmlbeans.SchemaType;

import com.google.inject.Singleton;

/**
 * Generates a value for the built-in type date.
 * 
 * Copyright (c) 2012 Kevin Howell. See LICENSE file for copying permission.
 * 
 * @author Kevin Howell
 */
@Singleton
@GeneratesType(namespace = "http://www.w3.org/2001/XMLSchema", localname = "date")
public class DateGenerator extends AbstractXsdTypeValueGenerator {

	@Override
	public String generateExampleValue(SchemaType rootType) {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}
	
	@Override
	protected String generateRandomValue(SchemaType rootType) {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date(random.nextLong()));
	}

}
