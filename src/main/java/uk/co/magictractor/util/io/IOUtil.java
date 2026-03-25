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
package uk.co.magictractor.util.io;

import java.io.BufferedReader;
import java.io.Reader;

/**
 *
 */
public final class IOUtil {

    private IOUtil() {
    }

    /**
     * <p>
     * Returns the given {@code Reader} either cast to {@code BufferedReader} if
     * it is already an instance, or wrapped by a new {@code BufferedReader}
     * instance.
     * </p>
     * <p>
     * <p>
     * This avoids buffering a {@code Reader} more than once that can happen
     * when using the typical pattern of simply wrapping a {@code Reader} with a
     * {@code BufferedReader}.
     * </p>
     */
    // It feels like this should be available in an existing third-party lib, but I haven't found it.
    public static BufferedReader bufferedReader(Reader reader) {
        if (reader instanceof BufferedReader) {
            return (BufferedReader) reader;
        }
        return new BufferedReader(reader);
    }

}
