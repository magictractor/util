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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import uk.co.magictractor.util.copy.CopyUtil;

/**
 *
 */
// Serializable is used to allow copies of the converters to be created and modified.
public abstract class AbstractSinglePartConverter<FROM, TO, CONVERTER extends AbstractSinglePartConverter<FROM, TO, CONVERTER>>
        implements Converter<FROM, TO>, Serializable {

    private static final long serialVersionUID = 1L;

    // TODO! sufficiently big after heroic gems to convert this to a Map
    private final List<FROM> fromValues = new ArrayList<>();
    private final List<TO> toValues = new ArrayList<>();
    private List<FROM> toNull = Collections.emptyList();

    public CONVERTER mapToNull(FROM... toNulls) {
        if (!toNull.isEmpty()) {
            throw new IllegalStateException("mapToNull() has already been called. It should be called once, but may be passed multiple values to convert to null.");
        }

        CONVERTER copy = CopyUtil.deepCopy((CONVERTER) this);
        AbstractSinglePartConverter<FROM, TO, CONVERTER> bCopy = copy;

        bCopy.toNull = List.of(toNulls);

        return copy;
    }

    public CONVERTER mapFrom(Function<FROM, FROM> fromMapper) {
        CONVERTER copy = CopyUtil.deepCopy((CONVERTER) this);
        AbstractSinglePartConverter<FROM, TO, CONVERTER> bCopy = copy;

        for (int i = 0; i < fromValues.size(); i++) {
            bCopy.fromValues.set(i, fromMapper.apply(fromValues.get(i)));
        }
        checkUnique(bCopy.fromValues);

        return copy;
    }

    public CONVERTER replaceMapping(FROM newFrom, TO existingTo) {
        CONVERTER copy = CopyUtil.deepCopy((CONVERTER) this);
        AbstractSinglePartConverter<FROM, TO, CONVERTER> bCopy = copy;

        int index = bCopy.toValues.indexOf(existingTo);
        if (index == -1) {
            throw new IllegalArgumentException("No existing mapping to value: " + existingTo);
        }
        bCopy.fromValues.set(index, newFrom);
        checkUnique(bCopy.fromValues);

        return copy;
    }

    private <T> void checkUnique(List<T> values) {
        Set<T> uniqueValues = new HashSet<>(values);
        if (values.size() != uniqueValues.size()) {
            throw new IllegalArgumentException("Mapped values are not unique");
        }
    }

    protected void add(FROM fromValue, TO toValue) {
        if (fromValues.contains(fromValue)) {
            throw new IllegalArgumentException("Already have a mapping from value " + fromValue);
        }
        if (toValues.contains(toValue)) {
            throw new IllegalArgumentException("Already have a mapping to value " + toValue);
        }

        fromValues.add(fromValue);
        toValues.add(toValue);
    }

    protected boolean containsFromValue(FROM fromValue) {
        return fromValues.contains(fromValue);
    }

    @Override
    public TO convert(FROM from) {
        int index = fromValues.indexOf(from);
        if (index == -1) {
            if (toNull.contains(from)) {
                return null;
            }
            throw new IllegalArgumentException(
                "Unable to convert to type " + toType().getSimpleName()
                        + " from value: " + from + ", expected values are " + fromValues);
        }

        return toValues.get(index);
    }

    private Class<?> toType() {
        return toValues.get(0).getClass();
    }

    @Override
    public FROM reverse(TO to) {
        int index = toValues.indexOf(to);
        if (index == -1) {
            if (to == null) {
                if (toNull.isEmpty()) {
                    throw new IllegalStateException("No values are mapped to null");
                }
                return toNull.get(0);
            }
            throw new IllegalArgumentException(
                "Unable to convert value: \"" + to + "\", expected values are " + toValues);
        }

        return fromValues.get(index);
    }

}
