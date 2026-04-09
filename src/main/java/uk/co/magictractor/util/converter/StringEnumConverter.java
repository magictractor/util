/**
 * Copyright 2026 Ken Dobson
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
package uk.co.magictractor.util.converter;

import java.util.function.Function;

/**
 *
 */
public class StringEnumConverter<TO extends Enum<TO>> extends AbstractStringEnumConverter<TO, StringEnumConverter<TO>> {

    private static final long serialVersionUID = 1L;

    public StringEnumConverter(Class<TO> enumType) {
        super(enumType);
    }

    public StringEnumConverter(Class<TO> enumType, String... fromValues) {
        super(enumType, fromValues);
    }

    public StringEnumConverter(Class<TO> enumType, Function<String, String> fromMapper) {
        super(enumType, fromMapper);
    }

}
