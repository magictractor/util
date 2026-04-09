/**
 * Copyright 2019 Ken Dobson
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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 */
public abstract class AbstractStringEnumConverter<TO extends Enum<TO>, CONVERTER extends AbstractStringEnumConverter<TO, CONVERTER>>
        extends AbstractStringConverter<TO, CONVERTER> {

    private static final long serialVersionUID = 1L;

    private static <ENUM extends Enum<ENUM>> List<ENUM> enumValueList(Class<ENUM> enumType) {
        return Arrays.asList(enumType.getEnumConstants());
    }

    private static <ENUM extends Enum<ENUM>> List<String> enumNameList(Class<ENUM> enumType) {
        return Stream.of(enumType.getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    private static <ENUM extends Enum<ENUM>> List<String> enumNameListMapped(Class<ENUM> enumType, Function<String, String> fromMapper) {
        return Stream.of(enumType.getEnumConstants())
                .map(Enum::name)
                .map(fromMapper)
                .collect(Collectors.toList());
    }

    protected AbstractStringEnumConverter(Class<TO> enumType) {
        //        for (TO enumValue : enumType.getEnumConstants()) {
        //            add(enumValue.name(), enumValue);
        //        }
        super(enumNameList(enumType), enumValueList(enumType));
    }

    protected AbstractStringEnumConverter(Class<TO> enumType, String... fromValues) {
        //        TO[] values = enumType.getEnumConstants();
        //        int len = values.length;
        //        if (fromValues.length != len) {
        //            throw new IllegalArgumentException("Wrong number of fromValues, expected " + values.length + " to match the enum but got " + fromValues.length);
        //        }
        //        for (int i = 0; i < len; i++) {
        //            add(fromValues[i], values[i]);
        //        }
        super(Arrays.asList(fromValues), enumValueList(enumType));
    }

    protected AbstractStringEnumConverter(Class<TO> enumType, Function<String, String> fromMapper) {
        //        TO[] values = enumType.getEnumConstants();
        //        int len = values.length;
        //        if (fromValues.length != len) {
        //            throw new IllegalArgumentException("Wrong number of fromValues, expected " + values.length + " to match the enum but got " + fromValues.length);
        //        }
        //        for (int i = 0; i < len; i++) {
        //            add(fromValues[i], values[i]);
        //        }
        super(enumNameListMapped(enumType, fromMapper), enumValueList(enumType));
    }

    /**
     * No-args constructor. {@code add()} should be used to add all mappings.
     */
    protected AbstractStringEnumConverter() {
        // Do nothing.
    }

    protected AbstractStringEnumConverter(Map<String, TO> map) {
        for (Map.Entry<String, TO> entry : map.entrySet()) {
            add(entry.getKey(), entry.getValue());
        }
        //super(map.key)
    }

}
