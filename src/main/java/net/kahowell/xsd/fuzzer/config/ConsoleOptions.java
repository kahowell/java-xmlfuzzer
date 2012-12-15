package net.kahowell.xsd.fuzzer.config;

import net.kahowell.xsd.fuzzer.XmlGenerator;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * Command line options for {@link XmlGenerator}.
 * 
 * Copyright (c) 2012 Kevin Howell. See LICENSE file for copying permission.
 * 
 * @author Kevin Howell
 */
public class ConsoleOptions {

	/**
	 * The set of command line options for XML generation.
	 * @see Options
	 */
	public static final Options OPTIONS = new Options();
	
	static {
		{
			Option option = new Option("u", "url", true, "schema url");
			OPTIONS.addOption(option);
		}
		{
			Option option = new Option("f", "file", true, "schema file");
			OPTIONS.addOption(option);
		}
		{
			Option option = new Option("r", "root", true, "root element");
			option.setRequired(true);
			OPTIONS.addOption(option);
		}
		{
			Option option = new Option("o", "output-file", true, "xml output filename");
			OPTIONS.addOption(option);
		}
		{
			Option option = new Option("nd", "no-download", false, "work offline (don't download external referenced schemas)");
			OPTIONS.addOption(option);
		}
		{
			Option option = new Option("d", "debug", false, "debug");
			OPTIONS.addOption(option);
		}
		{
			Option option = new Option("v", "validate", false, "validate XML against XSD after generation, and don't output if not valid.");
			OPTIONS.addOption(option);
		}
		{
			Option option = new Option("s", "strategies", true, "list of strategies separated by commas. valid strategies: EXAMPLE, RANDOM, MINIMUM, MAXIMUM");
			option.setValueSeparator(',');
			OPTIONS.addOption(option);
		}
	}

}
