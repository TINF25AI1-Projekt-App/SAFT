/*
* Copyright 2025 SAFT Authors and Contributors
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package de.dhbw.core.parser;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.ResponseBody;

/**
 * Parser interface for converting an HTTP response body into a target type.
 *
 * @param <T>	The type produced by the parser
 */
public interface ResponseParser<T> {

	/**
	 * Parses a request response.
	 *
	 * @param body			The request response
	 * @return				Parsed result
	 * @throws IOException	If the url was invalid
	 */
	T parse(@NotNull ResponseBody body) throws IOException;
}
