package org.gwtbootstrap3.extras.tagsinput.client.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * #%L
 * GwtBootstrap3
 * %%
 * Copyright (C) 2013 - 2016 GwtBootstrap3
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.gwtbootstrap3.extras.tagsinput.client.ui.base.MultiValueTagsInput;
import org.gwtbootstrap3.extras.typeahead.client.base.StringDataset;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;

/**
 * Wrapper for Bootstrap Tags Input component with multi value support.
 *
 * @author Marko Nikolić <marko.nikolic@iten.rs>
 */
public class MVTagsInput extends MultiValueTagsInput<String> {

    /** Wraps an element already in the document and starts its lifecycle. */
    public static MVTagsInput wrap(Element element) {
        MVTagsInput widget = new MVTagsInput(element);
        widget.attachToDocument();
        return widget;
    }

    public MVTagsInput() {
    }

    public MVTagsInput(StringDataset dataset) {
        super(dataset);
    }

    public MVTagsInput(Element e) {
        super(e);
    }

    public MVTagsInput(Element e, StringDataset dataset) {
        super(e, dataset);
    }
    
    @Override
    public void add(String tag) {
        if (isAttached())
            super.add(tag);
        else {
            OptionElement option = Document.get().createOptionElement();
            option.setValue(tag);
            option.setInnerText(tag);
            option.setSelected(true);
            getElement().appendChild(option);
        }
    }
    
    @Override
    public List<String> getValue() {
        if (isAttached())
            return super.getValue();
        else {
            List<String> value = new ArrayList<String>();
            SelectElement select = getElement().cast();
            for (int i = 0; i < select.getOptions().getLength(); i++) {
                OptionElement option = select.getOptions().getItem(i);
                if (option.isSelected()) value.add(option.getValue());
            }
            return value;
        }
    }

    @Override
    public void removeAll() {
        if (isAttached()) {
            super.removeAll();
        } else {
            getElement().removeAllChildren();
        }
    }

    public void setValue(String value) {
        removeAll();
        if (!value.isEmpty()) add(Arrays.asList(value.split(",")));
    }
}
