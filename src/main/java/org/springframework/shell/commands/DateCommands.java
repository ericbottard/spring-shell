/*
 * Copyright 2011-2012 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.shell.commands;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

/**
 * Commands related to the dates
 * 
 */
@Component
public class DateCommands implements CommandMarker {

	@CliCommand(value = { "date" }, help = "Displays the local date and time")
	public String date() {
		return DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.US).format(new Date());
	}

	@CliCommand(value = "stream")
	public String foo(@CliOption(key = "")
	String a, @CliOption(key = "def")
	String def) {
		System.out.format("name = %s%ndef = %s%n", a, def);
		return "ok";
	}
}
