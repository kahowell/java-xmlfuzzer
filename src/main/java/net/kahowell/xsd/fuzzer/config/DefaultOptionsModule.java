package net.kahowell.xsd.fuzzer.config;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import net.kahowell.xsd.fuzzer.XmlGenerationOptions;
import net.kahowell.xsd.fuzzer.XmlGenerator;

import org.apache.commons.math3.random.RandomData;
import org.apache.commons.math3.random.RandomDataImpl;
import org.apache.xmlbeans.XmlOptions;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

/**
 * Guice module to setup default options for {@link XmlGenerator}
 * 
 * Copyright (c) 2012 Kevin Howell. See LICENSE file for copying permission.
 * 
 * @author Kevin Howell
 *
 */
public class DefaultOptionsModule extends AbstractModule {

	private static class DefaultSchemaXmlOptionsProvider implements Provider<XmlOptions> {

		public XmlOptions get() {
			XmlOptions options = new XmlOptions();
			options.setCompileDownloadUrls();
			return options;
		}
	}
	
	private static class DefaultSaveXmlOptionsProvider implements Provider<XmlOptions> {

		public XmlOptions get() {
			XmlOptions options = new XmlOptions();
			options.setCharacterEncoding("UTF-8");
			options.setSavePrettyPrint();
			options.setSavePrettyPrintIndent(2);
			options.setUseDefaultNamespace();
			return options;
		}
		
	}
	
	@Override
	protected void configure() {
		bindConstant().annotatedWith(Names.named("generate all attributes")).to(true);
		bindConstant().annotatedWith(Names.named("generate required attributes only")).to(false);
		bindConstant().annotatedWith(Names.named("output filename")).to("output.xml");
		bindConstant().annotatedWith(Names.named("hard element limit")).to(20);
		bindConstant().annotatedWith(Names.named("offline")).to(false);
		bind(XmlOptions.class).annotatedWith(Names.named("xml schema options")).toProvider(DefaultSchemaXmlOptionsProvider.class).in(Singleton.class);
		bind(XmlOptions.class).annotatedWith(Names.named("xml save options")).toProvider(DefaultSaveXmlOptionsProvider.class).in(Singleton.class);
		bind(RandomData.class).to(RandomDataImpl.class).in(Singleton.class);
		bind(XmlGenerationOptions.class).in(Singleton.class);
		try {
			bind(DatatypeFactory.class).toInstance(DatatypeFactory.newInstance());
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

}
