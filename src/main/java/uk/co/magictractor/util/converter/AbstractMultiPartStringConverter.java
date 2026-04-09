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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

/**
 * Base converter for types based on a collection of other types, such as
 * Colours (list of Colour).
 */
//public abstract class AbstractMultiPartStringConverter<PART, PART_CONVERTER extends AbstractStringConverter<PART, PART_CONVERTER>, TO extends Collection<PART>, CONVERTER extends AbstractMultiPartStringConverter<PART, PART_CONVERTER, TO, CONVERTER>>
//extends AbstractConverter<String, TO, CONVERTER> {
public abstract class AbstractMultiPartStringConverter<PART, TO extends Collection<PART>, CONVERTER extends AbstractMultiPartStringConverter<PART, TO, CONVERTER>>
        extends AbstractConverter<String, TO, CONVERTER> {

    private static final long serialVersionUID = 1L;

    private Converter<String, PART> partConverter;
    private String separator = ",";
    private boolean trim = true;

    private transient Splitter partSplitter;
    private transient Joiner partJoiner;

    protected AbstractMultiPartStringConverter(Converter<String, PART> partConverter) {
        this.partConverter = partConverter;
    }

    @Override
    public TO convert(String from) {
        List<PART> parts = new ArrayList<>();
        Iterator<String> partIterator = getPartSplitter().split(from).iterator();
        while (partIterator.hasNext()) {
            String partString = partIterator.next();
            PART part = partConverter.convert(partString);
            parts.add(part);
        }

        return from(parts);
    }

    public abstract TO from(List<PART> parts);

    @Override
    public String reverse(TO to) {
        return getPartJoiner().join(to.stream().map(partConverter::reverse).collect(Collectors.toList()));
    }

    private Splitter getPartSplitter() {
        if (partSplitter == null) {
            partSplitter = Splitter.on(separator);
            if (trim) {
                partSplitter = partSplitter.trimResults();
            }
        }
        return partSplitter;
    }

    private Joiner getPartJoiner() {
        if (partJoiner == null) {
            partJoiner = Joiner.on(separator);
        }
        return partJoiner;
    }

    @SuppressWarnings("unchecked")
    public CONVERTER separator(String separator) {
        checkUnlocked();
        this.separator = separator;

        return (CONVERTER) this;
    }

    @SuppressWarnings("unchecked")
    public CONVERTER trimParts(boolean trimParts) {
        checkUnlocked();
        this.trim = trimParts;

        return (CONVERTER) this;
    }

    //    @SuppressWarnings("unchecked")
    //    public CONVERTER fromCamelCase() {
    //        setPartConverter(partConverter.copy().fromCamelCase());
    //
    //        return (CONVERTER) this;
    //    }

    //    @SuppressWarnings("unchecked")
    //    public CONVERTER fromUpperCase() {
    //        setPartConverter(partConverter.copy().fromUpperCase());
    //
    //        return (CONVERTER) this;
    //    }

    //    @SuppressWarnings("unchecked")
    //    public CONVERTER fromLowerCase() {
    //        setPartConverter(partConverter.copy().fromLowerCase());
    //
    //        return (CONVERTER) this;
    //    }

    //    private void setSeparator(String separator) {
    //        partSplitter = Splitter.on(separator);
    //        if (trim) {
    //            partSplitter = partSplitter.trimResults();
    //        }
    //        partJoiner = Joiner.on(separator);
    //    }

    //    private void setPartConverter(PART_CONVERTER partConverter) {
    //        checkUnlocked();
    //        this.partConverter = partConverter;
    //    }

}
