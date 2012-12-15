package net.kahowell.xsd.fuzzer.config;

import java.util.ArrayList;
import java.util.List;

import net.kahowell.xsd.fuzzer.XsdTypeValueGenerator;
import net.kahowell.xsd.fuzzer.generator.AbstractXsdTypeValueGenerator;
import net.kahowell.xsd.fuzzer.generator.GeneratesType;
import net.kahowell.xsd.fuzzer.generator.XsdType;
import net.kahowell.xsd.fuzzer.generator.builtin.AnyUriGenerator;
import net.kahowell.xsd.fuzzer.generator.builtin.Base64Binary;
import net.kahowell.xsd.fuzzer.generator.builtin.BooleanGenerator;
import net.kahowell.xsd.fuzzer.generator.builtin.DateGenerator;
import net.kahowell.xsd.fuzzer.generator.builtin.DateTimeGenerator;
import net.kahowell.xsd.fuzzer.generator.builtin.DecimalGenerator;
import net.kahowell.xsd.fuzzer.generator.builtin.DoubleGenerator;
import net.kahowell.xsd.fuzzer.generator.builtin.DurationGenerator;
import net.kahowell.xsd.fuzzer.generator.builtin.FloatGenerator;
import net.kahowell.xsd.fuzzer.generator.builtin.GDayGenerator;
import net.kahowell.xsd.fuzzer.generator.builtin.GMonthDay;
import net.kahowell.xsd.fuzzer.generator.builtin.GMonthGenerator;
import net.kahowell.xsd.fuzzer.generator.builtin.GYearGenerator;
import net.kahowell.xsd.fuzzer.generator.builtin.GYearMonthGenerator;
import net.kahowell.xsd.fuzzer.generator.builtin.HexBinaryGenerator;
import net.kahowell.xsd.fuzzer.generator.builtin.IntegerGenerator;
import net.kahowell.xsd.fuzzer.generator.builtin.QNameGenerator;
import net.kahowell.xsd.fuzzer.generator.builtin.StringGenerator;
import net.kahowell.xsd.fuzzer.generator.builtin.TimeGenerator;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

/**
 * Sets up default (builtin) generators.
 * 
 * Copyright (c) 2012 Kevin Howell. See LICENSE file for copying permission.
 * 
 * @author Kevin Howell
 * @see AbstractXsdTypeValueGenerator
 */
public class DefaultGeneratorsModule extends AbstractModule {
	
	private static List<Class<? extends XsdTypeValueGenerator>> typeGenerators = new ArrayList<Class<? extends XsdTypeValueGenerator>>();
	
	static {
		typeGenerators.add(AnyUriGenerator.class);
		typeGenerators.add(Base64Binary.class);
		typeGenerators.add(BooleanGenerator.class);
		typeGenerators.add(DateGenerator.class);
		typeGenerators.add(DateTimeGenerator.class);
		typeGenerators.add(DecimalGenerator.class);
		typeGenerators.add(DoubleGenerator.class);
		typeGenerators.add(DurationGenerator.class);
		typeGenerators.add(FloatGenerator.class);
		typeGenerators.add(GDayGenerator.class);
		typeGenerators.add(GMonthDay.class);
		typeGenerators.add(GMonthGenerator.class);
		typeGenerators.add(GYearGenerator.class);
		typeGenerators.add(GYearMonthGenerator.class);
		typeGenerators.add(HexBinaryGenerator.class);
		typeGenerators.add(IntegerGenerator.class);
		typeGenerators.add(QNameGenerator.class);
		typeGenerators.add(StringGenerator.class);
		typeGenerators.add(TimeGenerator.class);
	};
	
	@Override
	protected void configure() {
		for (Class<? extends XsdTypeValueGenerator> generatorClass : typeGenerators) {
			bind(XsdTypeValueGenerator.class).annotatedWith(XsdType.fromAnnotation(generatorClass.getAnnotation(GeneratesType.class))).to(generatorClass).in(Singleton.class);
		}
	}

}
