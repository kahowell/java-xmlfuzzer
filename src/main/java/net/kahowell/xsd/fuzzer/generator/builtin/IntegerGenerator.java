package net.kahowell.xsd.fuzzer.generator.builtin;

import net.kahowell.xsd.fuzzer.generator.AbstractXsdTypeValueGenerator;
import net.kahowell.xsd.fuzzer.generator.GeneratesType;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlAnySimpleType;

import com.google.inject.Singleton;

/**
 * Generates a value for the built-in type integer.
 * 
 * Copyright (c) 2012 Kevin Howell. See LICENSE file for copying permission.
 * 
 * @author Kevin Howell
 */
@Singleton
@GeneratesType(namespace = "http://www.w3.org/2001/XMLSchema", localname = "integer")
public class IntegerGenerator extends AbstractXsdTypeValueGenerator {
	
	private static final Logger log = Logger.getLogger(IntegerGenerator.class);
	
	@Override
	protected String generateExampleValue(SchemaType rootType) {
		return "42";
	}
	
	@Override
	protected String generateRandomValue(SchemaType rootType) {
		String value = Long.toString(random.nextLong(getMinimumValue(rootType), getMaximumValue(rootType)));
		if (value.length() > getMaxLength(rootType)) {
			log.warn("truncating integer value. it may no longer comply with any min/max value facets.");
			value = value.substring(0, (int) getMaxLength(rootType));
		}
		else if (value.length() < getMinLength(rootType)) {
			StringBuilder sb = new StringBuilder();
			if (value.startsWith("-")) {
				sb.append("-");
				value = value.substring(1);
			}
			while (sb.length() + value.length() < getMinLength(rootType)) {
				sb.append("0");
			}
			sb.append(value);
			value = sb.toString();
		}
		return value;
	}
	
	protected long getMaximumValue(SchemaType rootType) {
		XmlAnySimpleType maxExclusive = rootType.getFacet(SchemaType.FACET_MAX_EXCLUSIVE);
		XmlAnySimpleType maxInclusive = rootType.getFacet(SchemaType.FACET_MAX_INCLUSIVE);
		if (maxExclusive != null && maxInclusive != null) {
			log.warn("Both max exclusive and max inclusive are present. Ignoring max exclusive");
		}
		if (maxInclusive != null) {
			return Long.parseLong(maxInclusive.getStringValue());
		}
		if (maxExclusive != null) {
			return Long.parseLong(maxExclusive.getStringValue()) - 1;
		}
		return Long.MAX_VALUE;
	}
	
	protected long getMinimumValue(SchemaType rootType) {
		XmlAnySimpleType minExclusive = rootType.getFacet(SchemaType.FACET_MIN_EXCLUSIVE);
		XmlAnySimpleType minInclusive = rootType.getFacet(SchemaType.FACET_MIN_INCLUSIVE);
		if (minExclusive != null && minInclusive != null) {
			log.warn("Both min exclusive and min inclusive are present. Ignoring min exclusive");
		}
		if (minInclusive != null) {
			return Long.parseLong(minInclusive.getStringValue());
		}
		if (minExclusive != null) {
			return Long.parseLong(minExclusive.getStringValue()) + 1;
		}
		return Long.MIN_VALUE;
	}
}
