package net.kahowell.xsd.fuzzer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import net.kahowell.xsd.fuzzer.XsdTypeValueGenerator.Strategy;
import net.kahowell.xsd.fuzzer.config.CommandLineArgumentsModule;
import net.kahowell.xsd.fuzzer.config.ConsoleOptions;
import net.kahowell.xsd.fuzzer.config.DefaultGeneratorsModule;
import net.kahowell.xsd.fuzzer.config.DefaultOptionsModule;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.google.inject.util.Modules;

/**
 * Command line interface for the generator.
 * 
 * Copyright (c) 2012 Kevin Howell. See LICENSE file for copying permission.
 * 
 * @author Kevin Howell
 * 
 */
public class XmlGenerator {
	
	private static CommandLineParser parser = new GnuParser();
	private static HelpFormatter helpFormatter = new HelpFormatter();
	private static Logger log = Logger.getLogger(XmlGenerator.class);
	
	/**
	 * Drives the application, parsing command-line arguments to determine
	 * options.
	 * 
	 * @param args command line args
	 */
	public static void main(String[] args) {
		try {
			setupLog4j();
			CommandLine commandLine = parser.parse(ConsoleOptions.OPTIONS, args);
			if (commandLine.hasOption("d")) {
				Logger.getLogger("net.kahowell.xsd.fuzzer").setLevel(Level.DEBUG);
			}
			for (Option option : commandLine.getOptions()) {
				if (option.getValue() != null) {
					log.debug("Using " + option.getDescription() + ": " + option.getValue());
				}
				else {
					log.debug("Using " + option.getDescription());
				}
			}
			
			Injector injector = Guice.createInjector(
				Modules.override(
					Modules.combine(
						new DefaultGeneratorsModule(), 
						new DefaultOptionsModule()
					)
				).with(new CommandLineArgumentsModule(commandLine))
			);
			
			log.debug(injector.getBindings());
			
			XsdParser xsdParser = injector.getInstance(XsdParser.class);
			XmlOptions xmlOptions = injector.getInstance(Key.get(XmlOptions.class, Names.named("xml save options")));
			XmlGenerator xmlGenerator = injector.getInstance(XmlGenerator.class);
			XmlGenerationOptions xmlGenerationOptions = injector.getInstance(XmlGenerationOptions.class);
			
			doPostModuleConfig(commandLine, xmlGenerationOptions);
			
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			XmlObject generatedXml = xsdParser.generateXml(commandLine.getOptionValue("root"));
			generatedXml.save(stream, xmlOptions);
			if (commandLine.hasOption("v")) {
				if (xsdParser.validate(stream)) {
					log.info("Valid XML file produced.");
				}
				else {
					log.info("Invalid XML file produced.");
					System.exit(4);
				}
			}
			xmlGenerator.showOrSave(stream);
		} 
		catch (MissingOptionException e) {
			if (e.getMissingOptions().size() != 0) {
				System.err.println("Missing argument(s): " + Arrays.toString(e.getMissingOptions().toArray()));
			}
			helpFormatter.printHelp(XmlGenerator.class.getSimpleName(), ConsoleOptions.OPTIONS);
			System.exit(1);
		}
		catch (ParseException e) {
			helpFormatter.printHelp(XmlGenerator.class.getSimpleName(), ConsoleOptions.OPTIONS);
			System.exit(2);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(3);
		}
	}

	private static void doPostModuleConfig(CommandLine commandLine, XmlGenerationOptions options) {
		if (commandLine.hasOption("s")) {
			String[] strategies = commandLine.getOptionValues("s");
			for (String strategy : strategies) {
				options.getStrategies().add(Strategy.valueOf(strategy));
			}
		}
	}

	private static void setupLog4j() {
		ConsoleAppender consoleAppender = new ConsoleAppender(new PatternLayout("[%-5p][%c{1}] %m%n"));
		consoleAppender.setTarget("System.err");
		consoleAppender.activateOptions();
		
		{
			Logger log = Logger.getLogger("net.kahowell.xsd.fuzzer");
			log.addAppender(consoleAppender);
			log.setLevel(Level.DEBUG);
			log.setAdditivity(false);
		}
		
		{
			Logger log = Logger.getRootLogger();
			log.addAppender(consoleAppender);
			log.setLevel(Level.WARN);
		}
	}

	@Inject(optional = true)
	@Named("output filename")
	private String outputFilename;
	
	private void showOrSave(ByteArrayOutputStream stream) throws IOException {
		if (outputFilename != null) {
			File file = new File(outputFilename);
			FileOutputStream fileout = new FileOutputStream(file);
			stream.writeTo(fileout);
		}
		else {
			System.out.println(stream.toString());
		}
	}
}
