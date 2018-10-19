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

import java.util.Arrays;

/**
 * The start state. Finds the type of the {@literal @code} tag and the imports,
 * or declare this tag as "not a test".
 */
class HeaderState implements ExtractorState {

	/** {@inheritDoc} */
	@Override
	public void process(CodeTagExtractor extractor, String line) {
		String trimmedLine = line.trim();
		if (trimmedLine.startsWith(ClassDocletExtractor.SET_UP)) {
			extractor.setSetUpState();
		} else if (trimmedLine.startsWith(ClassDocletExtractor.TEAR_DOWN)) {
			extractor.setTearDownState();
		} else if (trimmedLine.startsWith(ClassDocletExtractor.IMPORTS)) {
			extractor.addImports(Arrays.asList(trimmedLine
					.substring(ClassDocletExtractor.IMPORTS.length()).trim()
					.split(",")));
		} else if (trimmedLine.startsWith(ClassDocletExtractor.SLASHES)) {
			String title = trimmedLine.substring(
					ClassDocletExtractor.SLASHES.length()).trim();
			extractor.setMethodState(title);
		} else {
			extractor.setNotATestState();
		}
	}
}
