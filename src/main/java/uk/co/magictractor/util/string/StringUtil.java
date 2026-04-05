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
package uk.co.magictractor.util.string;

import com.google.common.io.BaseEncoding;

public class StringUtil {

    private static final BaseEncoding HEX_ENCODING_SEP = BaseEncoding.base16().lowerCase().withSeparator(" ", 2);

    public static String toHex(byte b) {
        return toHex(new byte[] { b });
    }

    public static String toHex(byte[] bytes) {
        return toHex(bytes, 0, bytes.length);
    }

    public static String toHex(byte[] bytes, int offset, int length) {
        return HEX_ENCODING_SEP.encode(bytes, offset, length);
    }

    // Apache Commons has WordUtils
    public static String toCamelCase(String string) {
        int len = string.length();
        StringBuffer stringBuffer = new StringBuffer(len);
        boolean nextUpper = true;
        for (int i = 0; i < len; i++) {
            char c = string.charAt(i);
            if (Character.isLowerCase(c)) {
                if (nextUpper) {
                    c = Character.toUpperCase(c);
                    nextUpper = false;
                }
            }
            else if (Character.isUpperCase(c)) {
                if (nextUpper) {
                    nextUpper = false;
                }
                else {
                    c = Character.toLowerCase(c);
                }
            }
            else {
                // Punctuation or whitespace.
                nextUpper = true;
            }

            stringBuffer.append(c);
        }

        return stringBuffer.toString();
    }

    public static String innerClassSimpleName(Object obj) {
        return innerClassSimpleName(obj.getClass());
    }

    /**
     * For inner classes this returns {@code Outer$Inner} rather than
     * {@code Inner} that would returned by {@code Class.getSimpleName()}. If
     * {@code clazz} is not an inner class this returns the same as
     * {@code Class.getSimpleName()}, so this method may be used if the paramter
     * is maybe an inner class.
     */
    public static String innerClassSimpleName(Class<?> clazz) {
        StringBuilder classNameBuilder = new StringBuilder();
        if (clazz.getEnclosingClass() != null) {
            classNameBuilder.append(clazz.getEnclosingClass().getSimpleName());
            classNameBuilder.append('$');
        }
        classNameBuilder.append(clazz.getSimpleName());

        return classNameBuilder.toString();
    }

}
