# JavaDocTest

Copyright (C) J. Férard 2018

A Maven DocTest (see https://docs.python.org/3.7/library/doctest.html) for Java plugin **proof of concept**.

**Not designed to be used in exploitation.**

**Uses the old com.sun.javadoc API**

## Installation

    cd doctest
    mvn clean install

## Quick Test

    cd DocTestExample # or any project of your own with doctests.
    mvn clean com.github.jferard:doctest-maven-plugin:0.0.1-SNAPSHOT:doctest test

Output:

    ...
    -------------------------------------------------------
     T E S T S
    -------------------------------------------------------
    Running com.github.jferard.doctest.CalculatorTest
    Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.078 sec

    Results :

    Tests run: 2, Failures: 0, Errors: 0, Skipped: 0

## POM configuration
To execute automatically the plugin:

    <plugins>
        ...
        <plugin>
            <groupId>com.github.jferard</groupId>
            <artifactId>doctest-maven-plugin</artifactId>
            <version>0.0.1-SNAPSHOT</version>
            <executions>
                <execution>
                    <id>doctest</id>
                    <phase>generate-test-sources</phase>
                    <goals>
                        <goal>doctest</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>

Now, a:

    mvn clean test

Will generate and execute the test.

*Note: I added an `example` profile with those lines to the POM of `DocTestExample`. You can check it using:*

    mvn -P example clean test

## How does it work ?

The `DocTest` plugin:
* add a new test directory source for the generated test class files;
* starts the Maven Javadoc Plugin a custom doclet that extracts the test from the javadoc and builds a test class.

The example class `Calculator` contains the following lines:

    /**
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

        ...

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
    ...


The generated test class is:

    package com.github.jferard.doctest;

    import org.junit.*;
    import java.lang.Math;
    import java.util.Random;

    public class CalculatorTest {
        @Test
        public void test2() {
             Calculator c = new Calculator(0);
             Float f = new Random().nextFloat();
             Assert.assertEquals(f, c.add(f), 0.01);
        }

        @Test
        public void test1() {
             Calculator c = new Calculator(0);
             Assert.assertEquals(-100, c.subtract(100), 0.01);
             Assert.assertEquals(100, c.negate(), 0.01);
             Assert.assertEquals(377, c.add(277), 0.01);
             Assert.assertEquals(Math.PI, c.divide(120), 0.01);
        }

    }

## POM
To use automatically Since the heart of the Java DocTest is a simple doclet, you can add those plugins to the pom:

    <build>
        <plugins>
            ...
            <plugin>
                <groupId>com.github.jferard</groupId>
                <artifactId>doctest-maven-plugin</artifactId>
                <version>0.0.1-SNAPSHOT</version>
                <executions>
                    <execution>
                        <id>doctest</id>
                        <phase>generate-test-sources</phase>
                        <goals>
                            <goal>doctest</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
