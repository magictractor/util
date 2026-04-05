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
package uk.co.magictractor.util.string;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class StringUtilTest {

    @Test
    public void testToCamelCase_singleWordLower() {
        assertThat(StringUtil.toCamelCase("wibble")).isEqualTo("Wibble");
    }

    @Test
    public void testToCamelCase_singleWordUpper() {
        assertThat(StringUtil.toCamelCase("WIBBLE")).isEqualTo("Wibble");
    }

    @Test
    public void testToCamelCase_singleWordCamel() {
        assertThat(StringUtil.toCamelCase("Wibble")).isEqualTo("Wibble");
    }

    @Test
    public void testToCamelCase_multipleWordsLower() {
        assertThat(StringUtil.toCamelCase("foo bar ding")).isEqualTo("Foo Bar Ding");
    }

    @Test
    public void testToCamelCase_multipleWordsUPPER() {
        assertThat(StringUtil.toCamelCase("FOO BAR DING")).isEqualTo("Foo Bar Ding");
    }

    @Test
    public void testToCamelCase_multipleWordsMixedAndOddSpacing() {
        assertThat(StringUtil.toCamelCase("  FoO bAr   DinG  ")).isEqualTo("  Foo Bar   Ding  ");
    }

    @Test
    public void testToCamelCase_hyphen() {
        assertThat(StringUtil.toCamelCase("ultra-rare")).isEqualTo("Ultra-Rare");
    }

    @Test
    public void testToCamelCase_commas() {
        assertThat(StringUtil.toCamelCase("foo,bar,ding")).isEqualTo("Foo,Bar,Ding");
    }

    @Test
    public void testToCamelCase_singleLetters() {
        assertThat(StringUtil.toCamelCase("n b a")).isEqualTo("N B A");
    }

    @Test
    public void testToCamelCase_initials() {
        assertThat(StringUtil.toCamelCase("n.b.a.")).isEqualTo("N.B.A.");
    }

}
