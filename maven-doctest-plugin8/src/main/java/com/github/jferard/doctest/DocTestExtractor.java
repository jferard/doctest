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

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;

/**
 * An extractor for the runnable examples in a package.
 */
class DocTestExtractor {
	/**
	 * Extract the runnable examples
	 * 
	 * @param root
	 *            the root document of the doclet
	 * @throws IOException 
	 */
	public void extract(RootDoc root) throws IOException {
		for (ClassDoc c : root.classes()) {
			ClassDocletExtractor e = new ClassDocletExtractor(c);
			e.extractClass();
			e.createWriter().print();
		}
	}
}
