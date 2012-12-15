package net.kahowell.xsd.fuzzer.generator;

import java.util.Arrays;

import javax.xml.datatype.DatatypeFactory;

import net.kahowell.xsd.fuzzer.XmlGenerationOptions;
import net.kahowell.xsd.fuzzer.XsdTypeValueGenerator;
import net.kahowell.xsd.fuzzer.util.CompositeRandom;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlAnySimpleType;
import org.apache.xmlbeans.XmlCursor;

import com.google.inject.Inject;

/**
 * Provides many convenience functions for a type generator. By extending this
 * class and implementing just a single method (see below), one can create a
 * simple custom value generator.
 * 
 * <p>
 * A generator with this class can simply implement one or more of the generate
 * value methods to provide a functional generator. This class provides strategy
 * utilization functionality. One (or more) of the following methods should be
 * implemented (each corresponds to a given strategy):
 * <ul>
 * 	<li>generateMaximumValue</li>
 * 	<li>generateMinimumValue</li>
 * 	<li>generateRandomValue</li>
 * 	<li>generateExampleValue</li>
 * </ul>
 * </p>
 * 
 * Copyright (c) 2012 Kevin Howell. See LICENSE file for copying permission.
 * 
 * @author Kevin Howell
 *
 */
public abstract class AbstractXsdTypeValueGenerator implements XsdTypeValueGenerator {
	
	@Inject
	protected CompositeRandom random;
	
	@Inject
	protected XmlGenerationOptions options;
	
	@Inject
	protected DatatypeFactory datatypeFactory;
	
	/**
	 * Generates a value of the given type. If the value has enumerations, it
	 * will pick an enumeration, otherwise, it will try all strategies, ignoring
	 * unimplemented strategies.
	 * 
	 * @param rootType
	 * @return the value of the type
	 */
	public String generateValue(SchemaType rootType) {
		if (rootType.getEnumerationValues() != null) {
			return ((XmlAnySimpleType) random.nextSample(Arrays.asList(rootType.getEnumerationValues()), 1)[0]).getStringValue();
		}
		for (Strategy strategy : options.getStrategies()) {
			try {
				return generateValueForStrategy(rootType, strategy);
			} catch (UnsupportedOperationException e) {}
		}
		for (Strategy strategy : Strategy.values()) {
			try {
				return generateValueForStrategy(rootType, strategy);
			} catch (UnsupportedOperationException e) {}
		}
		throw new UnsupportedOperationException("No value generation implemented for this generator!");
	}

	/**
	 * Generates the value for a type using the specified strategy
	 * 
	 * @param type the schema type
	 * @param strategy the strategy to use
	 * @return a generated value, as a string
	 */
	public String generateValueForStrategy(SchemaType type, Strategy strategy) {
		switch (strategy) {
		case EXAMPLE:
			return generateExampleValue(type);
		case RANDOM:
			return generateRandomValue(type);
		case MINIMUM:
			return generateMinimumValue(type);
		case MAXIMUM:
			return generateMaximumValue(type);
		}
		throw new UnsupportedOperationException("Unimplemented strategy: " + strategy);
	}
	
	protected String generateMaximumValue(SchemaType rootType) {
		throw new UnsupportedOperationException("Maximum value generation strategy not yet implemented.");
	}

	protected String generateMinimumValue(SchemaType rootType) {
		throw new UnsupportedOperationException("Minimum value generation strategy not yet implemented.");
	}

	protected String generateRandomValue(SchemaType rootType) {
		throw new UnsupportedOperationException("Random value generation strategy not yet implemented.");
	}

	protected String generateExampleValue(SchemaType rootType) {
		throw new UnsupportedOperationException("Example value generation strategy not yet implemented.");
	}

	public void generateValue(XmlCursor cursor, SchemaType rootType) {
		cursor.insertChars(generateValue(rootType));
	}
	
	protected long getMinLength(SchemaType type) {
		XmlAnySimpleType minLength = type.getFacet(SchemaType.FACET_MIN_LENGTH);
		if (minLength != null) {
			return Long.parseLong(minLength.getStringValue());
		}
		return 0;
	}
	
	protected long getMaxLength(SchemaType type) {
		XmlAnySimpleType maxLength = type.getFacet(SchemaType.FACET_MAX_LENGTH);
		if (maxLength != null) {
			return Long.parseLong(maxLength.getStringValue());
		}
		return 256; // XXX reasonable length restriction?
	}
	
	protected long chooseLength(SchemaType type) {
		return random.nextLong(getMinLength(type), getMaxLength(type));
	}
}
