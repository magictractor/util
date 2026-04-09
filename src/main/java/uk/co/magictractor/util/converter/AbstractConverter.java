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

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import uk.co.magictractor.util.copy.CopyUtil;

/**
 *
 */
// Serializable is used to allow copies of the converters to be created and modified.
public abstract class AbstractConverter<FROM, TO, CONVERTER extends AbstractConverter<FROM, TO, CONVERTER>>
        implements Converter<FROM, TO>, Serializable {

    private static final long serialVersionUID = 1L;

    private boolean locked;

    /**
     * <p>
     * Mark the converter as locked.
     * </p>
     * <p>
     * Implementations should lock the converter when {@code convert()} or
     * {@code reverse()} is used, and methods that modify the converter's
     * mappings should throw an {@IllegalStateExeception()} is the converter is
     * locked.
     * </p>
     */
    public CONVERTER lock() {
        locked = true;
        return (CONVERTER) this;
    }

    public boolean isLocked() {
        return locked;
    }

    protected void checkUnlocked() {
        if (locked) {
            throw new IllegalStateException("This " + getClass().getSimpleName()
                    + " instance may not be modified, either because it was explictly locked"
                    + " or because convert() or reverse() has been called already."
                    + " copy() may be used to create a modifiable copy of the converter.");
        }
    }

    public CONVERTER copy() {
        CONVERTER copy = CopyUtil.deepCopy((CONVERTER) this);
        ((AbstractConverter) copy).locked = false;
        initCopy(copy);
        return copy;
    }

    protected void initCopy(CONVERTER copy) {
        // Do nothing. Implementations may override to modify copied fields.
    }

    protected <T> void checkUnique(List<T> values) {
        Set<T> uniqueValues = new HashSet<>(values);
        if (values.size() != uniqueValues.size()) {
            throw new IllegalArgumentException("Mapped values are not unique");
        }
    }

}
