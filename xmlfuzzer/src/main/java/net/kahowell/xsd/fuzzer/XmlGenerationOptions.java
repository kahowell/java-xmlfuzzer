package net.kahowell.xsd.fuzzer;

import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;

import net.kahowell.xsd.fuzzer.XsdTypeValueGenerator.Strategy;
import net.kahowell.xsd.fuzzer.config.DefaultGeneratorsModule;
import net.kahowell.xsd.fuzzer.generator.XsdType;

import org.apache.xmlbeans.SchemaType;

import com.google.inject.ConfigurationException;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

/**
 * Class that serves as a central location of XML generation options.
 * 
 * Copyright (c) 2012 Kevin Howell. See LICENSE file for copying permission.
 * 
 * @author Kevin Howell
 */
public class XmlGenerationOptions {
	
	@Inject
	Injector injector;
	
	@Inject
	@Named("generate all attributes")
	protected boolean generateAllAttributes;
	
	@Inject
	@Named("generate required attributes only")
	protected boolean generateRequiredAttributesOnly;
	
	@Inject
	@Named("output filename")
	protected String outputFilename;
	
	@Inject
	@Named("hard element limit")
	private Integer elementLimit;
	
	protected Set<Strategy> strategies = new HashSet<Strategy>();
	
	protected Strategy collectionSizeStrategy;
	
	/**
	 * @return whether the application is currently set to generate all 
	 * attributes, regardless of whether they are mandatory 
	 */
	public final boolean getGenerateAllAttributes() {
		return generateAllAttributes;
	}
	
	/**
	 * @return whether the application is currently set to generate only
	 * required attributes, always ignoring optional ones.
	 */
	public final boolean getGenerateRequiredAttributesOnly() {
		return generateRequiredAttributesOnly;
	}
	
	/**
	 * @return the current output filename for the generated XML.
	 */
	public final String getOutputFilename() {
		return outputFilename;
	}
	
	/**
	 * Looks up a generator for a type. The generator must first be bound to
	 * the given type via Guice.
	 * 
	 * @param name the qualified name of the type
	 * @return a generator for the given type, or <code>null</code> if none is
	 * registered for the given type
	 * 
	 * @see DefaultGeneratorsModule
	 */
	public final XsdTypeValueGenerator getGeneratorForType(QName name) {
		try {
			return injector.getInstance(Key.get(XsdTypeValueGenerator.class, XsdType.named(name)));
		} catch (ConfigurationException e) { // will try to generate with simpler generators.
			return null;
		}
	}

	/**
	 * Finds the generator for an anonymous type. The identity of the type is
	 * based on the identifier that XMLBeans generates.
	 * 
	 * @param name identity of the type (XMLBeans' toString)
	 * @return the generator, or <code>null</code> if none is registered for the
	 * type
	 * @see SchemaType#toString()
	 */
	public XsdTypeValueGenerator getGeneratorForAnonymousType(String name) {
		try {
			return injector.getInstance(Key.get(XsdTypeValueGenerator.class, Names.named(name)));
		} catch (ConfigurationException e) {
			return null;
		}
	}

	/**
	 * @return the configured strategies for generating data
	 */
	public Set<Strategy> getStrategies() {
		return strategies;
	}
	
	/**
	 * @return the configured strategy for picking a collection size
	 */
	public Strategy getCollectionSizeStrategy() {
		return collectionSizeStrategy;
	}

	/**
	 * Sets the strategy for picking a collection size
	 * @param strategy desired strategy
	 */
	public void setCollectionSizeStrategy(Strategy strategy) {
		collectionSizeStrategy = strategy;
	}

	/**
	 * @return the current configured hard element limit (per collection)
	 */
	public Integer getElementLimit() {
		return elementLimit;
	}

	/**
	 * Sets the hard element limit (per collection)
	 * @param elementLimit the maximum number of elements
	 */
	public void setElementLimit(Integer elementLimit) {
		this.elementLimit = elementLimit;
	}
}
