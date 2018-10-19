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
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/** A printer for the generated class code */
class ClassCodeWriter {
	private String className;
	private String filename;
	private String packageName;
	private List<String> imports;
	private Map<String, List<String>> testByName;

	/**
	 * @param className
	 *            the name of the TEST class
	 * @param packageName
	 *            the name of the package
	 * @param filename the name of the TEST file
	 * @param imports a list of imports
	 * @param testByName a map name -> test as a list of lines
	 */
	public ClassCodeWriter(String className, String packageName,
			String filename, List<String> imports,
			Map<String, List<String>> testByName) {
		this.className = className;
		this.packageName = packageName;
		this.filename = filename;
		this.imports = imports;
		this.testByName = testByName;
	}

	/**
	 * Print the class file
	 * @throws IOException
	 */
	public void print() throws IOException {
		File f = new File(filename);
		if (new File(f.getParent()).mkdirs()) {
			try (Writer fw = new FileWriter(f)) {
				writeClassFile(fw);
			}
		}
	}

	private void writeClassFile(Writer fw) throws IOException {
		writePackageName(fw);
		writeImports(fw);
		writeClass(fw);
	}

	private void writePackageName(Writer fw) throws IOException {
		fw.write("package " + packageName + ";\n");
		fw.write("\n");
	}
	
	private void writeImports(Writer fw) throws IOException {
		for (String imp : imports) {
			fw.write("import " + imp + ";\n");
		}
		fw.write("\n");
	}

	private void writeClass(Writer fw) throws IOException {
		fw.write("public class " + this.className + " {\n");
		for (Entry<String, List<String>> entry : this.testByName
				.entrySet()) {
			writeTest(fw, entry);
		}
		fw.write("}\n");
	}

	private void writeTest(Writer fw, Entry<String, List<String>> entry)
			throws IOException {
		fw.write("\t@Test\n");
		fw.write("\tpublic void " + entry.getKey() + "() {\n");
		for (String line : entry.getValue()) {
			fw.write("\t\t" + line + "\n");
		}
		fw.write("\t}\n\n");
	}

}
