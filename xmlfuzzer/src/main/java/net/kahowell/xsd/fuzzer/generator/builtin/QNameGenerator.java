package net.kahowell.xsd.fuzzer.generator.builtin;

import java.text.MessageFormat;

import net.kahowell.xsd.fuzzer.generator.AbstractXsdTypeValueGenerator;
import net.kahowell.xsd.fuzzer.generator.GeneratesType;

import org.apache.xmlbeans.SchemaType;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Generates a value for the built-in type QName.
 * 
 * Copyright (c) 2012 Kevin Howell. See LICENSE file for copying permission.
 * 
 * @author Kevin Howell
 */
@Singleton
@GeneratesType(namespace = "http://www.w3.org/2001/XMLSchema", localname = "QName")
public class QNameGenerator extends AbstractXsdTypeValueGenerator {

	@Inject
	AnyUriGenerator uriGenerator;
	
	@Inject
	StringGenerator nameGenerator;
	
	@Override
	protected String generateExampleValue(SchemaType rootType) {
		return MessageFormat.format("{0}:{1}", uriGenerator.generateValue(null), nameGenerator.generateValue(null));
	}
	
}
