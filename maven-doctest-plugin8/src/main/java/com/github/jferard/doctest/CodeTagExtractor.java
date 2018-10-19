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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sun.javadoc.Tag;

/**
 * An extractor for a single {@literal @code} tag. This is a finite state
 * machine.
 */
class CodeTagExtractor {
	private enum Type {
		PENDING, NOT_A_TEST, SET_UP, TEAR_DOWN, A_TEST
	}

	private Type bodyType;
	private String title;
	private ExtractorState state;
	private List<String> body;
	private ClassDocletExtractor classCodeExtractor;

	/**
	 * @param classCodeExtractor
	 *            the parent
	 */
	public CodeTagExtractor(ClassDocletExtractor classCodeExtractor) {
		this.classCodeExtractor = classCodeExtractor;
		state = new HeaderState();
		this.body = new ArrayList<>();
		this.bodyType = Type.PENDING;
	}

	/**
	 * Extract the data from the {@literal @code} tag: header (name, type,
	 * imports) and body
	 * 
	 * @param tag
	 *            the tag
	 */
	public void extractTagData(Tag tag) {
		processTag(tag);
		updateParent();
	}

	/**
	 * Change the state
	 * 
	 * @param newState
	 *            the new state
	 */
	public void setState(ExtractorState newState) {
		this.state = newState;
	}

	/**
	 * Add new imports to the parent
	 * 
	 * @param imports
	 *            the imports
	 */
	public void addImports(List<String> imports) {
		this.classCodeExtractor.addImports(imports);
	}

	/**
	 * Add a line to the ody of the test
	 * 
	 * @param line
	 *            the line
	 */
	public void addBodyLine(String line) {
		this.body.add(line);
	}

	/**
	 * Set this {@literal @code} tag as a setUp.
	 */
	public void setSetUpState() {
		updateType(Type.SET_UP);
		this.setState(new TypeKnownState("setUp"));
	}

	/**
	 * Set this {@literal @code} tag as a tearDown.
	 */
	public void setTearDownState() {
		updateType(Type.TEAR_DOWN);
		this.setState(new TypeKnownState("tearDown"));
	}

	/**
	 * Set this {@literal @code} tag as a method.
	 */
	public void setMethodState(String title) {
		updateType(Type.A_TEST);
		this.title = title;
		this.setState(new TypeKnownState("method "+ title));
	}

	/**
	 * Set this {@literal @code} tag as "not a test". A normal code example.
	 */
	public void setNotATestState() {
		updateType(Type.NOT_A_TEST);
		this.setState(new NotATestState());
	}

	private void processTag(Tag tag) {
		String text = tag.text().trim();
		for (String line : Arrays.asList(text.split("\\R"))) {
			this.state.process(this, line);
		}
	}

	private void updateParent() {
		switch (this.bodyType) {
		case NOT_A_TEST:
		case PENDING:
			return;
		case SET_UP:
			this.classCodeExtractor.setSetUp(this.body);
			break;
		case TEAR_DOWN:
			this.classCodeExtractor.setTearDown(this.body);
			break;
		case A_TEST:
			String testName = "test" + toTitleCase(title);
			this.classCodeExtractor.putTestByName(testName, this.body);
			break;
		default:
			throw new IllegalStateException();
		}
	}

	private String toTitleCase(String title) {
		return title.substring(0, 1).toUpperCase()
							+ title.substring(1);
	}
	
	private void updateType(Type newType) {
		if (this.bodyType != Type.PENDING)
			throw new IllegalStateException();
		this.bodyType = newType;
	}
}
