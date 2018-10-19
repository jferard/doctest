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

import java.io.IOException;

import com.sun.javadoc.RootDoc;

/**
 * A doclet to extract the runnable test from the javadoc.
 */
public class ExtractDocTestDoclet {
	/**
	 * Start the doclet
	 * @param root the root document
	 * @return true if success
	 * @throws IOException is an I/O exception occurs
	 */
	public static boolean start(RootDoc root) throws IOException {
		DocTestExtractor codeExtractor = new DocTestExtractor();
		codeExtractor.extract(root);
		return true;
	}
}