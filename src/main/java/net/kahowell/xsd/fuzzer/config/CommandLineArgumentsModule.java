package net.kahowell.xsd.fuzzer.config;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingOptionException;
import org.apache.log4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

/**
 * Guice module that parses command line options.
 * 
 * Copyright (c) 2012 Kevin Howell. See LICENSE file for copying permission.
 * 
 * @author Kevin Howell
 */
public class CommandLineArgumentsModule extends AbstractModule {

	private CommandLine commandLine;
	private static Logger log = Logger.getLogger(CommandLineArgumentsModule.class);
	
	/**
	 * Construct a Guice module from the given command-line arguments.
	 * 
	 * @param commandLine
	 */
	public CommandLineArgumentsModule(CommandLine commandLine) {
		this.commandLine = commandLine;
	}
	
	@Override
	protected void configure() {
		if (commandLine.hasOption("nd")) {
			log.debug("not downloading urls");
			bindConstant().annotatedWith(Names.named("offline")).to(true);
		}
		if (commandLine.hasOption("o")) {
			String filename = commandLine.getOptionValue("o");
			log.info("outputting to file: " + filename);
			bindConstant().annotatedWith(Names.named("output filename")).to(filename);
		}
		if (commandLine.hasOption("url")) {
			bindConstant().annotatedWith(Names.named("schema url")).to(commandLine.getOptionValue("url"));
		}
		else if (commandLine.hasOption("file")) {
			bindConstant().annotatedWith(Names.named("schema url")).to(new File(commandLine.getOptionValue("file")).toURI().toString());
		}
		else {
			throw new RuntimeException(new MissingOptionException("Must specify schema file or schema url."));
		}
	}

}
