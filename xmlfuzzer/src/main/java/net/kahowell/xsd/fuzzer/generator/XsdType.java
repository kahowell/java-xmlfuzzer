package net.kahowell.xsd.fuzzer.generator;

import java.lang.annotation.Annotation;
import java.text.MessageFormat;

import javax.xml.namespace.QName;

import net.kahowell.xsd.fuzzer.XsdTypeValueGenerator;
import net.kahowell.xsd.fuzzer.config.DefaultGeneratorsModule;

/**
 * Provides functions for selecting an {@link XsdTypeValueGenerator} using
 * Guice.
 * 
 * Copyright (c) 2012 Kevin Howell. See LICENSE file for copying permission.
 * 
 * @author Kevin Howell
 */
public class XsdType {
	
	private static GeneratesType typeFromParts(final String namespace, final String localname) {
		return new GeneratesType() {
			
			public Class<? extends Annotation> annotationType() {
				return GeneratesType.class;
			}

			public String namespace() {
				return namespace;
			}

			public String localname() {
				return localname;
			}

			@Override
			public int hashCode() {
				final int prime = 31;
				int result = 1;
				result = prime * result
						+ ((localname() == null) ? 0 : localname().hashCode());
				result = prime * result
						+ ((namespace() == null) ? 0 : namespace().hashCode());
				return result;
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj)
					return true;
				if (obj == null)
					return false;
				if (!(obj instanceof GeneratesType))
					return false;
				GeneratesType type = (GeneratesType) obj;
				return type.namespace().equals(namespace()) && type.localname().equals(localname());
			}
			
			public String toString() {
				return MessageFormat.format("T={1}@{0}", namespace(), localname());
			}
		};
	}
	
	/**
	 * Constructs a {@link GeneratesType} instance from a {@link QName}.
	 * 
	 * @param name the qualified name
	 * @return a {@link GeneratesType} instance for the given qualified name
	 */
	public static GeneratesType named(QName name) {
		return typeFromParts(name.getNamespaceURI(), name.getLocalPart());
	}
	
	/**
	 * Constructs a {@link GeneratesType} instance from an annotation instance.
	 * This is used to assist Guice in binding the generators. (It returns an
	 * instance that can be compared against another instance).
	 * 
	 * @param annotation instance of the annotation
	 * @return a {@link GeneratesType} instance for the given qualified name.
	 * @see DefaultGeneratorsModule#configure(com.google.inject.Binder)
	 */
	public static GeneratesType fromAnnotation(GeneratesType annotation) {
		return typeFromParts(annotation.namespace(), annotation.localname());
	}
}
