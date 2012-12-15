package net.kahowell.xsd.fuzzer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import net.kahowell.xsd.fuzzer.util.CompositeRandom;
import net.kahowell.xsd.fuzzer.util.FakeXmlCursorFactory;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.SchemaGlobalElement;
import org.apache.xmlbeans.SchemaLocalAttribute;
import org.apache.xmlbeans.SchemaParticle;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Driver for XML generation.
 * 
 * Copyright (c) 2012 Kevin Howell. See LICENSE file for copying permission.
 * 
 * @author Kevin Howell
 */
public class XsdParser {

	private static final Logger log = Logger.getLogger(XsdParser.class);
	
	@Inject
	@Named("xml schema options")
	private XmlOptions schemaOptions;
	
	@Inject
	@Named("xml save options")
	private XmlOptions xmlGenerationOptions;
	
	@Inject
	private XmlGenerationOptions options = new XmlGenerationOptions();
	
	@Inject
	private CompositeRandom random;
	
	private SchemaTypeSystem schemaTypeSystem;
	
	private SchemaType rootSchemaType;
	
	private List<XmlError> errorList = new LinkedList<XmlError>();
	
	/**
	 * Constructs an XsdParser for a schema at the given URL.
	 * 
	 * @param url URL of the schema
	 * @throws XmlException
	 * @throws IOException
	 */
	@Inject
	public XsdParser(@Named("schema url") String url) throws XmlException, IOException {
		XmlObject schema = XmlObject.Factory.parse(new URL(url));
		schemaTypeSystem = XmlBeans.compileXsd(new XmlObject[] {schema}, XmlBeans.getBuiltinTypeSystem(), schemaOptions);
	}

	/**
	 * Generates an XML document, starting with the passed element as the root
	 * element.
	 * 
	 * @param rootElementName
	 * @return the generated XML document
	 */
	public XmlObject generateXml(String rootElementName) {
		SchemaGlobalElement root = findRoot(rootElementName);
		XmlObject element = schemaTypeSystem.newInstance(root.getType(), xmlGenerationOptions);
		XmlCursor cursor = element.newCursor();
		cursor.toNextToken();
		log.debug("creating element with name " + root.getName());
		cursor.beginElement(root.getName());
		// generate attributes and content
		generateAttributes(cursor, root.getType());
		generateContents(cursor, root.getType(), root.getType());
		cursor.toNextToken();
		return element;
	}

	private void generateContents(XmlCursor cursor, SchemaType elementType, SchemaType rootType) {
		if (!elementType.isAnonymousType()) {
			XsdTypeValueGenerator generator = options.getGeneratorForType(elementType.getName());
			if (generator != null) {
				generator.generateValue(cursor, rootType);
				return;
			}
		}
		else {
			XsdTypeValueGenerator generator = options.getGeneratorForAnonymousType(elementType.toString());
			if (generator != null) {
				generator.generateValue(cursor, rootType);
				return;
			}
		}
		if (elementType.isURType()) {
			throw new RuntimeException("UR types not supported yet. Wanna fix me?");
		}
		else if (elementType.isSimpleType()) {
			generateContentForSimpleType(cursor, elementType, rootType);
		}
		else {
			generateContentForComplexType(cursor, elementType, rootType);
		}
	}

	private void generateContentForComplexType(XmlCursor cursor, SchemaType elementType, SchemaType rootType) {
		switch (elementType.getContentType()) {
		case SchemaType.NOT_COMPLEX_TYPE:
			throw new RuntimeException("Expected a complex type. Passed a non-complex type. Shame on you!");
		case SchemaType.EMPTY_CONTENT:
			return;
		case SchemaType.SIMPLE_CONTENT:
			elementType.getBaseType();
			generateContentForSimpleType(cursor, elementType, rootType);
			break;
		case SchemaType.ELEMENT_CONTENT:
			generateElementContent(cursor, elementType.getContentModel());
			break;
		case SchemaType.MIXED_CONTENT:
			throw new RuntimeException("Mixed content not supported yet. Wanna fix me?");
		}
	}

	private void generateContentForSimpleType(XmlCursor cursor, SchemaType elementType, SchemaType rootType) {
		log.debug("generating content for simple type " + elementType);
		switch (elementType.getSimpleVariety()) {
		case SchemaType.NOT_SIMPLE:
			throw new RuntimeException("Expected a simple type. Passed a non-simple type. Shame on you!");
		case SchemaType.ATOMIC:
			generateContentForAtomicType(cursor, elementType, rootType);
			break;
		case SchemaType.UNION:
			
		case SchemaType.LIST:
			
		}
	}

	private void generateContentForAtomicType(XmlCursor cursor, SchemaType elementType, SchemaType rootType) {
		SchemaType baseType = elementType.getBaseType();
		generateContents(cursor, baseType, rootType);
	}

	private void generateElementContent(XmlCursor cursor, SchemaParticle contentModel) {
		if (contentModel.getName() != null) {
			log.debug("generating element content of type " + contentModel.getName());
		}
		switch (contentModel.getParticleType()) {
		case SchemaParticle.ALL:
			break;
		case SchemaParticle.CHOICE:
			break;
		case SchemaParticle.SEQUENCE:
			generateSequence(cursor, contentModel);
			break;
		case SchemaParticle.ELEMENT:
			generateElement(cursor, contentModel);
		}
	}

	private void generateSequence(XmlCursor cursor, SchemaParticle contentModel) {
		log.debug("Begin sequence");
		for (SchemaParticle particle : contentModel.getParticleChildren()) {
			generateElementContent(cursor, particle);
		}
		log.debug("End sequence");
	}

	private void generateElement(XmlCursor cursor, SchemaParticle contentModel) {
		log.debug("generating element with name " + contentModel.getName() + " and type " + contentModel.getType());
		for (int i = 0; i < chooseNumberOfElements(contentModel); i++) {
			cursor.beginElement(contentModel.getName());
			generateAttributes(cursor, contentModel.getType());
			generateContents(cursor, contentModel.getType(), contentModel.getType());
			cursor.toNextToken();
		}
	}

	private int chooseNumberOfElements(SchemaParticle contentModel) {
		int max = contentModel.getIntMaxOccurs();
		if (max > options.getElementLimit()) {
			max = options.getElementLimit();
		}
		return random.nextInt(contentModel.getIntMinOccurs(), max);
	}

	private void generateAttributes(XmlCursor cursor, SchemaType type) {
		if (type.getAttributeModel() == null) {
			log.debug("No attributes for type " + type);
			return;
		}
		log.debug("generating attributes for type " + type);
		for (SchemaLocalAttribute attribute : type.getAttributeModel().getAttributes()) {
			if (attribute.getUse() == SchemaLocalAttribute.REQUIRED || options.getGenerateAllAttributes() || (!options.getGenerateRequiredAttributesOnly() && random.nextBoolean())) {
				log.debug("generating attribute " + attribute.getName());
				cursor.insertAttribute(attribute.getName());
				XmlCursor dummy = FakeXmlCursorFactory.getFakeXmlCursor();
				generateContents(dummy, attribute.getType(), attribute.getType());
				cursor.push();
				cursor.toPrevToken();
				cursor.setTextValue(dummy.toString()); // toString gets value
				cursor.pop();
			}
		}
	}

	private SchemaGlobalElement findRoot(String rootElementName) {
		for (SchemaGlobalElement element : schemaTypeSystem.globalElements()) {
			if (element.getName().getLocalPart().equals(rootElementName)) {
				return element;
			}
		}
		throw new RuntimeException("Could not find specified root.");
	}

	/**
	 * Validates a stream of bytes as an XML against the configured schema.
	 * 
	 * @param stream generated XML
	 * @return <code>true</code> if the XML is valid, <code>false</code>
	 * otherwise
	 * @see XmlObject#validate()
	 */
	public boolean validate(ByteArrayOutputStream stream) {
		try {
			XmlObject doc = schemaTypeSystem.parse(stream.toString(), rootSchemaType, null);
			boolean returnBool = doc.validate(new XmlOptions().setErrorListener(errorList));
			logValidationErrors();
			return returnBool;
		} catch (XmlException e) {
			throw new RuntimeException("Error validating", e);
		}
	}

	private void logValidationErrors() {
		for (XmlError error : errorList) {
			log.info("Validation error: " + error.getMessage());
		}
		errorList.clear();
	}

}
