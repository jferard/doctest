/**
/* 
This file is part of Java DocTest Maven Plugin.
	Copyright (C) 2018 Julien Férard

Java DocTest Maven Plugin is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Java DocTest Maven Plugin is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Java DocTest Maven Plugin.  If not, see <https://www.gnu.org/licenses/>. 
*/
package com.github.jferard.doctestexample;

/**
 * A simple calculator with some runnable examples.
 * 
 * <pre>
 * {@code
 * /// setUp
 * Calculator c = new Calculator(0);
 * }</pre>
 * 
 * <pre>
 * {@code
 * /// imports: java.lang.Math
 * /// 1
 * Assert.assertEquals(-100, c.subtract(100), 0.01);
 * Assert.assertEquals(100, c.negate(), 0.01);
 * Assert.assertEquals(377, c.add(277), 0.01);
 * Assert.assertEquals(Math.PI, c.divide(120), 0.01);
 * }
 * </pre>
 */
public class Calculator {
	private float value;

	/**
	 * Init the calculator
	 * @param value the initial value
	 */
	public Calculator(int value) {
		this.value = value;
	}

	/**
	 * <pre>{@code
	 * /// 2
	 * /// imports: java.util.Random
	 * Float f = new Random().nextFloat();
	 * Assert.assertEquals(f, c.add(f), 0.01);
	 * }</pre>
	 * 
	 * @param n
	 *            the term to add
	 * @return the new value
	 */
	public float add(float n) {
		this.value += n;
		return this.value;
	}

	/**
	 * @param n
	 *            the term to subtract
	 * @return the new value
	 */
	public float subtract(float n) {
		this.value -= n;
		return this.value;
	}

	/**
	 * @param n
	 *            the factor to multiply
	 * @return the new value
	 */
	public float multiply(float n) {
		this.value *= n;
		return this.value;
	}

	/**
	 * @param n
	 *            the factor of the division
	 * @return the new value
	 */
	public float divide(float n) {
		this.value /= n;
		return this.value;
	}

	/**
	 * @return the new value
	 */
	public float negate() {
		this.value = -this.value;
		return this.value;
	}

}
