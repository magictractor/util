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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import uk.co.magictractor.util.copy.CopyUtil;

/**
 * Base converter for types based on a collection of other types, such as
 * Colours (list of Colour).
 */
public abstract class AbstractMultiPartStringConverter<PART, PART_CONVERTER extends AbstractStringConverter<PART, PART_CONVERTER>, TO extends Collection<PART>, CONVERTER extends AbstractMultiPartStringConverter<PART, PART_CONVERTER, TO, CONVERTER>>
        implements Converter<String, TO>, Serializable {

    private static final long serialVersionUID = 1L;

    private PART_CONVERTER partConverter;
    private String separator = ",";
    private boolean trim = true;

    private transient Splitter partSplitter;
    private transient Joiner partJoiner;

    protected AbstractMultiPartStringConverter(PART_CONVERTER partConverter) {
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
        return getPartJoiner().join(to.stream().map(partConverter::reverse).toList());
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
        return (CONVERTER) CopyUtil.deepCopyAndModify(this, copy -> copy.separator = separator);
    }

    @SuppressWarnings("unchecked")
    public CONVERTER trimParts(boolean trimParts) {
        return (CONVERTER) CopyUtil.deepCopyAndModify(this, copy -> copy.trim = trimParts);
    }

    @SuppressWarnings("unchecked")
    public CONVERTER fromCamelCase() {
        return (CONVERTER) CopyUtil.deepCopyAndModify(this,
            copy -> copy.setPartConverter(partConverter.fromCamelCase()));
    }

    @SuppressWarnings("unchecked")
    public CONVERTER fromUpperCase() {
        return (CONVERTER) CopyUtil.deepCopyAndModify(this,
            copy -> copy.setPartConverter(partConverter.fromUpperCase()));
    }

    @SuppressWarnings("unchecked")
    public CONVERTER fromLowerCase() {
        return (CONVERTER) CopyUtil.deepCopyAndModify(this,
            copy -> copy.setPartConverter(partConverter.fromLowerCase()));
    }

    //    private void setSeparator(String separator) {
    //        partSplitter = Splitter.on(separator);
    //        if (trim) {
    //            partSplitter = partSplitter.trimResults();
    //        }
    //        partJoiner = Joiner.on(separator);
    //    }

    private void setPartConverter(PART_CONVERTER partConverter) {
        this.partConverter = partConverter;
    }

}
