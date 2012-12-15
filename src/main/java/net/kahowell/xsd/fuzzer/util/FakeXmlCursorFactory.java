package net.kahowell.xsd.fuzzer.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.text.MessageFormat;

import org.apache.xmlbeans.XmlCursor;

/**
 * Produces fake XmlCursor objects that are simply used to capture characters 
 * produced during value generation. This assumes that the only XmlCursor method
 * called is {@link XmlCursor#insertChars(String)}.
 * 
 * Copyright (c) 2012 Kevin Howell. See LICENSE file for copying permission.
 * 
 * @author Kevin Howell
 * @see XmlCursor
 */
public class FakeXmlCursorFactory {	
	
	/**
	 * Via proxy, returns a fake XmlCursor which will simply gather the 
	 * characters inserted in a StringBuffer, then return the contents via 
	 * <code>toString</code>
	 * 
	 * @return the dummy xml cursor
	 */
	public static XmlCursor getFakeXmlCursor() {
		final StringBuffer buffer = new StringBuffer();
		return (XmlCursor) Proxy.newProxyInstance(
			FakeXmlCursorFactory.class.getClassLoader(), 
			new Class[] {XmlCursor.class},
			new InvocationHandler() {
				
				public Object invoke(Object proxy, Method method, Object[] args)
						throws Throwable {
					if ("insertChars".equals(method.getName())) {
						buffer.append(args[0]);
						return null;
					}
					else if ("toString".equals(method.getName())) {
						return buffer.toString();
					}
					else {
						String message = MessageFormat.format("Tried to invoke method {0} on fake XmlCursor (probably when trying to generate an attribute value).", method.getName());
						throw new UnsupportedOperationException(message);
					}
				}
			}
		);
	}

}
