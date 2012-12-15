package net.kahowell.xsd.fuzzer.generator.builtin;

import net.kahowell.xsd.fuzzer.generator.AbstractXsdTypeValueGenerator;
import net.kahowell.xsd.fuzzer.generator.GeneratesType;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.util.Base64;

import com.google.inject.Singleton;

/**
 * Generates a value for the built-in type base64Binary.
 * 
 * Copyright (c) 2012 Kevin Howell. See LICENSE file for copying permission.
 * 
 * @author Kevin Howell
 */
@Singleton
@GeneratesType(namespace = "http://www.w3.org/2001/XMLSchema", localname = "base64Binary")
public class Base64Binary extends AbstractXsdTypeValueGenerator {

	private static byte buffer[] = new byte[4];
	
	@Override
	protected String generateExampleValue(SchemaType rootType) {
		return "aGVsbG8="; // hello
	}
	
	@Override
	protected String generateRandomValue(SchemaType rootType) {
		random.nextBytes(buffer);
		return new String(Base64.encode(buffer));
	}
}
