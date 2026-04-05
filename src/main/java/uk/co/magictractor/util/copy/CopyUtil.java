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
package uk.co.magictractor.util.copy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.function.Consumer;

import uk.co.magictractor.util.exception.ExceptionUtil;

/**
 *
 */
public final class CopyUtil {

    private CopyUtil() {
    }

    public static <T> T deepCopyAndModify(T object, Consumer<T>... modifiers) {
        T copy = deepCopy(object);
        for (Consumer<T> modifier : modifiers) {
            modifier.accept(copy);
        }

        return copy;
    }

    // Use serialisation to create a copy of this converter.
    public static <T> T deepCopy(T object) {
        return (T) ExceptionUtil.call(() -> deepCopy0(object));
    }

    @SuppressWarnings("unchecked")
    private static Object deepCopy0(Object object) throws IOException, ClassNotFoundException {
        // TODO! check typical size in use
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream(1024);
        new ObjectOutputStream(byteOutputStream).writeObject(object);

        InputStream byteInputStream = new ByteArrayInputStream(byteOutputStream.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(byteInputStream);

        return ois.readObject();

        // TODO! flush and close streams?
    }

}
