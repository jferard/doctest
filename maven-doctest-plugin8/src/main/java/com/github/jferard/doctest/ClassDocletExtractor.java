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
package com.github.jferard.doctest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.ProgramElementDoc;
import com.sun.javadoc.Tag;

/**
 * Extractor for the runnable examples in a class.
 */
class ClassDocletExtractor {
	static final String SLASHES = "///";
	static final String IMPORTS = "/// imports:";
	static final String TEAR_DOWN = "/// tearDown";
	static final String SET_UP = "/// setUp";

	Map<String, List<String>> testByName;
	private ClassDoc classDoc;
	private List<String> imports;
	private List<String> tearDown;
	private List<String> setUp;

	/**
	 * @param doc
	 *            the class document
	 */
	ClassDocletExtractor(ClassDoc doc) {
		this.classDoc = doc;
		testByName = new HashMap<>();
		this.imports = new ArrayList<>();
		this.imports.add("org.junit.*");
		this.setUp = Collections.emptyList();
		this.tearDown = Collections.emptyList();
	}

	/**
	 * Extract the examples.
	 * 
	 * @throws IOException
	 */
	public void extractClass() throws IOException {
		this.extractTestsAndSetup(this.classDoc);
		for (MethodDoc m : this.classDoc.methods(false)) {
			this.extractTestsAndSetup(m);
		}
	}

	/**
	 * Add imports to the import list of the class
	 * 
	 * @param imports
	 *            the imports
	 */
	public void addImports(List<String> imports) {
		this.imports.addAll(imports);
	}

	/**
	 * Add a new test
	 * 
	 * @param testName
	 *            the name of the test
	 * @param testBody
	 *            the body of the test
	 */
	public void putTestByName(String testName, List<String> testBody) {
		List<String> l = Stream.concat(this.setUp.stream(),
				Stream.concat(testBody.stream(), this.tearDown.stream()))
				.collect(Collectors.toList());
		this.testByName.put(testName, l);
	}

	/**
	 * Add a new tearDown macro. The content will be copied at the end of every
	 * test.
	 * 
	 * @param body
	 *            the lines
	 */
	public void setTearDown(List<String> body) {
		this.tearDown = body;
	}

	/**
	 * Add a new setUp macro. The content will be copied at the beginning of
	 * every test.
	 * 
	 * @param body
	 *            the lines
	 */
	public void setSetUp(List<String> body) {
		this.setUp = body;
	}

	/**
	 * @return the writer
	 */
	public ClassCodeWriter createWriter() {
		String name = this.classDoc.qualifiedName();
		String packageName = name.substring(0, name.lastIndexOf('.'));
		String filename = name.replace('.', File.separatorChar) + "Test.java";
		String className = this.classDoc.name() + "Test";
		return new ClassCodeWriter(className, packageName, filename, imports,
				this.testByName);
	}

	private void extractTestsAndSetup(ProgramElementDoc root) {
		for (Tag t : root.inlineTags()) {
			if (t.name().equals("@code")) {
				CodeTagExtractor codeTagExtractor = new CodeTagExtractor(this);
				codeTagExtractor.extractTagData(t);
			}
		}
	}

}
