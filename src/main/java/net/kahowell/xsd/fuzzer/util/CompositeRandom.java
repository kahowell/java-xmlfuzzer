package net.kahowell.xsd.fuzzer.util;

import java.util.Collection;
import java.util.Random;

import org.apache.commons.math3.random.RandomData;

import com.google.inject.Inject;

/**
 * Wraps {@link java.util.Random} and {@link RandomData} in the same class.
 * This allows convenient access to methods of both.
 * 
 * Copyright (c) 2012 Kevin Howell. See LICENSE file for copying permission.
 * 
 * @author Kevin Howell
 */
@SuppressWarnings("serial")
public class CompositeRandom extends Random implements RandomData {
	@Inject
	Random random;
	
	@Inject
	RandomData randomData;

	public double nextExponential(double arg0) {
		return randomData.nextExponential(arg0);
	}

	public double nextGaussian(double arg0, double arg1) {
		return randomData.nextGaussian(arg0, arg1);
	}

	public String nextHexString(int arg0) {
		return randomData.nextHexString(arg0);
	}

	public int nextInt(int arg0, int arg1) {
		if (arg0 == arg1) {
			return arg0;
		}
		return randomData.nextInt(arg0, arg1);
	}

	public long nextLong(long arg0, long arg1) {
		return randomData.nextLong(arg0, arg1);
	}

	public int[] nextPermutation(int arg0, int arg1) {
		return randomData.nextPermutation(arg0, arg1);
	}

	public long nextPoisson(double arg0) {
		return randomData.nextPoisson(arg0);
	}

	public Object[] nextSample(Collection<?> arg0, int arg1) {
		return randomData.nextSample(arg0, arg1);
	}

	public String nextSecureHexString(int arg0) {
		return randomData.nextSecureHexString(arg0);
	}

	public int nextSecureInt(int arg0, int arg1) {
		return randomData.nextSecureInt(arg0, arg1);
	}

	public long nextSecureLong(long arg0, long arg1) {
		return randomData.nextSecureLong(arg0, arg1);
	}

	public double nextUniform(double arg0, double arg1) {
		return randomData.nextUniform(arg0, arg1);
	}

	public double nextUniform(double arg0, double arg1, boolean arg2) {
		return randomData.nextUniform(arg0, arg1, arg2);
	}
}
