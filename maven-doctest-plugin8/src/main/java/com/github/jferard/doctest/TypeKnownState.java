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
 * The state when the type is known.
 */
class TypeKnownState implements ExtractorState {
	private String name;

	/**
	 * @param name the name of the state
	 */
	public TypeKnownState(String name) {
		this.name = name;
	}

	/** {@inheritDoc} */
	@Override
	public void process(CodeTagExtractor extractor, String line) {
		String trimmedLine = line.trim();
		if (trimmedLine.startsWith(ClassDocletExtractor.IMPORTS)) {
			extractor.addImports(Arrays.asList(trimmedLine.substring(
					ClassDocletExtractor.IMPORTS.length()).trim().split(",")));
		} else if (trimmedLine.startsWith(ClassDocletExtractor.SLASHES)) {
			throw new IllegalStateException("Only imports allowed in "+this.name);
		} else {
			extractor.addBodyLine(line);
			extractor.setState(new BodyState());
		}
	}

}
