package net.kahowell.xsd.fuzzer.generator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import net.kahowell.xsd.fuzzer.XsdTypeValueGenerator;

import com.google.inject.BindingAnnotation;

/**
 * Binding annotation that specifies which qualified type a 
 * {@link XsdTypeValueGenerator} is able to generate.
 * 
 * Copyright (c) 2012 Kevin Howell. See LICENSE file for copying permission.
 * 
 * @author Kevin Howell
 */
@BindingAnnotation
@Retention(RetentionPolicy.RUNTIME)
public @interface GeneratesType {
	/**
	 * @return namespace of the type
	 */
	public String namespace();
	/**
	 * @return localname of the type
	 */
	public String localname();
}