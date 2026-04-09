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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 *
 */
public abstract class AbstractSinglePartConverter<FROM, TO, CONVERTER extends AbstractSinglePartConverter<FROM, TO, CONVERTER>>
        extends AbstractConverter<FROM, TO, CONVERTER> {

    private static final long serialVersionUID = 1L;

    // TODO! sufficiently big after heroic gems to convert this to a Map
    private final List<FROM> fromValues;
    private final List<TO> toValues;
    private List<FROM> toNull = Collections.emptyList();

    protected AbstractSinglePartConverter(List<FROM> fromValues, List<TO> toValues) {
        if (fromValues.size() != toValues.size()) {
            throw new IllegalArgumentException(
                "Mismatched values, fromValue.size()=" + fromValues.size()
                        + " and toValues.size()=" + toValues.size());
        }

        this.fromValues = fromValues;
        this.toValues = toValues;
    }

    protected AbstractSinglePartConverter() {
        this.fromValues = new ArrayList<>();
        this.toValues = new ArrayList<>();
        // No initialised values so do not lock.
    }

    public CONVERTER mapToNull(FROM... toNulls) {
        checkUnlocked();

        if (!toNull.isEmpty()) {
            throw new IllegalStateException("mapToNull() has already been called. It should be called once, but may be passed multiple values to convert to null.");
        }

        this.toNull = Arrays.asList(toNulls);

        return (CONVERTER) this;
    }

    public CONVERTER mapFrom(Function<FROM, FROM> fromMapper) {
        checkUnlocked();

        for (int i = 0; i < fromValues.size(); i++) {
            fromValues.set(i, fromMapper.apply(fromValues.get(i)));
        }
        checkUnique(fromValues);

        return (CONVERTER) this;
    }

    public CONVERTER replaceMapping(FROM newFrom, TO existingTo) {
        checkUnlocked();

        int index = toValues.indexOf(existingTo);
        if (index == -1) {
            throw new IllegalArgumentException("No existing mapping to value: " + existingTo);
        }
        fromValues.set(index, newFrom);
        checkUnique(fromValues);

        return (CONVERTER) this;
    }

    // Can use new constructors in most (all?) cases instead of this
    @Deprecated
    protected void add(FROM fromValue, TO toValue) {
        checkUnlocked();

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
        lock();

        int index = fromValues.indexOf(from);
        if (index == -1) {
            if (toNull.contains(from)) {
                return null;
            }
            throw new IllegalArgumentException(
                "Unable to convert to type " + toType().getSimpleName()
                        + " from value " + from + ", expected values are " + fromValues);
        }

        return toValues.get(index);
    }

    private Class<?> toType() {
        return toValues.get(0).getClass();
    }

    public List<FROM> fromValues() {
        return fromValues;
    }

    @Override
    public FROM reverse(TO to) {
        lock();

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
