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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static uk.co.magictractor.util.converter.PlayRgbEnum.Green;
import static uk.co.magictractor.util.converter.PlayRgbEnum.Red;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

/**
 *
 */
public class StringEnumConverterTest {

    @Test
    public void testEnumConstructor() {
        StringEnumConverter<PlayRgbEnum> converter = new StringEnumConverter<>(PlayRgbEnum.class);

        assertThat(converter.convert("Red")).isEqualTo(Red);
        assertThat(converter.reverse(Red)).isEqualTo("Red");
    }

    @Test
    public void testEnumAndFromValuesConstructor() {
        StringEnumConverter<PlayRgbEnum> converter = new StringEnumConverter<>(PlayRgbEnum.class, "r", "g", "b");

        assertThat(converter.convert("g")).isEqualTo(Green);
        assertThat(converter.reverse(Green)).isEqualTo("g");
    }

    @Test
    public void testEnumAndFromValuesConstructor_wrongParameters() {
        assertThatThrownBy(() -> new StringEnumConverter<>(PlayRgbEnum.class, "r", "g"))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Mismatched values, fromValue.size()=2 and toValues.size()=3");
    }

    @Test
    public void testReplaceMapping() {
        StringEnumConverter<PlayRgbEnum> converter = new StringEnumConverter<>(PlayRgbEnum.class);

        converter.replaceMapping("Rouge", Red);

        assertThat(converter.convert("Rouge")).isEqualTo(Red);
        assertThat(converter.convert("Green")).isEqualTo(Green);
        checkUnknown(converter, "Red");
    }

    @Test
    public void testLock_replaceMapping() {
        StringEnumConverter<PlayRgbEnum> converter = new StringEnumConverter<>(PlayRgbEnum.class);
        converter.lock();

        checkLocked(() -> converter.replaceMapping("Rouge", Red));
    }

    private void checkUnknown(StringEnumConverter<?> converter, String unknownFromValue) {
        //  "Unable to convert to type PlayRgbEnum from value: Red, expected values are [Rouge, Green, Blue]"
        String expectedMessage = "Unable to convert to type PlayRgbEnum from value " + unknownFromValue
                + ", expected values are " + converter.fromValues();
        assertThatThrownBy(() -> converter.convert(unknownFromValue))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage(expectedMessage);
    }

    private void checkLocked(ThrowingCallable shouldRaiseThrowable) {
        String expectedMessage = "This StringEnumConverter instance may not be modified, either because it was explictly locked"
                + " or because convert() or reverse() has been called already. copy() may be used to create a modifiable copy of the converter.";
        assertThatThrownBy(shouldRaiseThrowable)
                .isExactlyInstanceOf(IllegalStateException.class)
                .hasMessage(expectedMessage);
    }

}
