java-xmlfuzzer
==============

Extensible XML generator/fuzzer that reads an XSD file and tries to generate random, but valid XML.

Written using the following libraries:
- log4j
- Google Guice
- Apache Commons-CLI
- Apache Commons-Math - for random functions
- Apache XMLBeans

TODO:
- review overall implementation to make sure i got all xsd entities (ex. sequences, all, etc.)
- take xsd facets into consideration on lots of builtin type generators
- document how to add custom type generators
- document command-line usage with examples
- come up with cool name
