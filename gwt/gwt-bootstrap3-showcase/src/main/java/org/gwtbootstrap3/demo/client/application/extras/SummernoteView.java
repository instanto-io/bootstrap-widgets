package org.gwtbootstrap3.demo.client.application.extras;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.client.ui.html.Paragraph;
import org.gwtbootstrap3.extras.summernote.client.event.SummernoteBlurEvent;
import org.gwtbootstrap3.extras.summernote.client.event.SummernoteChangeEvent;
import org.gwtbootstrap3.extras.summernote.client.event.SummernoteEnterEvent;
import org.gwtbootstrap3.extras.summernote.client.event.SummernoteFocusEvent;
import org.gwtbootstrap3.extras.summernote.client.event.SummernoteImageUploadEvent;
import org.gwtbootstrap3.extras.summernote.client.event.SummernoteImageUploadEvent.ImageFile;
import org.gwtbootstrap3.extras.summernote.client.event.SummernoteInitEvent;
import org.gwtbootstrap3.extras.summernote.client.event.SummernoteKeyDownEvent;
import org.gwtbootstrap3.extras.summernote.client.event.SummernoteKeyUpEvent;
import org.gwtbootstrap3.extras.summernote.client.event.SummernotePasteEvent;
import org.gwtbootstrap3.extras.summernote.client.ui.Summernote;
import org.gwtbootstrap3.extras.summernote.client.ui.base.DefaultHintHandler;
import org.gwtbootstrap3.extras.summernote.client.ui.base.SummernoteFontName;
import org.gwtbootstrap3.extras.summernote.client.ui.base.SummernoteLanguage;
import org.gwtbootstrap3.extras.summernote.client.ui.base.Toolbar;
import org.gwtbootstrap3.extras.summernote.client.ui.base.ToolbarButton;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author godi
 * @author Xiaodong Sun
 */
public class SummernoteView extends Composite {

    @UiField Summernote customToolbar;
    @UiField Summernote apiTest;
    @UiField ListBox languageBox;
    @UiField Summernote languageNote;
    @UiField Summernote hintWords;
    @UiField Summernote hintEmoji;
    @UiField Summernote events;
    @UiField Button clearLogButton;
    @UiField FlowPanel logRow;

    @UiHandler("setCode")
    void setCode(final ClickEvent event) {
        apiTest.setCode("<b>This is custom code. </b><u>OH YA</u>");
    }

    @UiHandler("getCode")
    void getCode(final ClickEvent event) {
        Window.alert(apiTest.getCode());
    }

    @UiHandler("clear")
    void clear(final ClickEvent event) {
        apiTest.clear();
    }

    @UiHandler("isEmpty")
    void isEmpty(final ClickEvent event) {
        Window.alert("" + apiTest.isEmpty());
    }

    @UiHandler("enable")
    void enable(final ClickEvent event) {
        apiTest.setEnabled(true);
    }

    @UiHandler("disable")
    void disable(final ClickEvent event) {
        apiTest.setEnabled(false);
    }

    @UiHandler("reset")
    void reset(final ClickEvent event) {
        apiTest.reset();
    }

    @UiHandler("clearLogButton")
    public void handleClearLog(final ClickEvent event) {
        logRow.clear();
    }

    @UiHandler("languageBox")
    void onChangeLanguage(final ChangeEvent event) {
        String language = languageBox.getSelectedValue();
        languageNote.setLanguage(SummernoteLanguage.valueOf(language));
        languageNote.reconfigure();
    }

    @UiHandler("events")
    void onSummernoteInit(final SummernoteInitEvent event) {
        final Paragraph logEntry = new Paragraph();
        logEntry.setText("Init Event Fired!");
        logRow.add(logEntry);
    }

    @UiHandler("events")
    void onSummernoteEnter(final SummernoteEnterEvent event) {
        final Paragraph logEntry = new Paragraph();
        logEntry.setText("Enter Event Fired!");
        logRow.add(logEntry);
    }

    @UiHandler("events")
    void onSummernoteFocus(final SummernoteFocusEvent event) {
        final Paragraph logEntry = new Paragraph();
        logEntry.setText("Focus Event Fired!");
        logRow.add(logEntry);
    }

    @UiHandler("events")
    void onSummernoteBlur(final SummernoteBlurEvent event) {
        final Paragraph logEntry = new Paragraph();
        logEntry.setText("Blur Event Fired!");
        logRow.add(logEntry);
    }

    @UiHandler("events")
    void onSummernoteKeyUp(final SummernoteKeyUpEvent event) {
        final Paragraph logEntry = new Paragraph();
        logEntry.setText("Key Up Event Fired. The key code is:" + event.getNativeEvent().getKeyCode());
        logRow.add(logEntry);
    }

    @UiHandler("events")
    void onSummernoteKeyDown(final SummernoteKeyDownEvent event) {
        final Paragraph logEntry = new Paragraph();
        logEntry.setText("Key Down Event Fired. The key code is:" + event.getNativeEvent().getKeyCode());
        logRow.add(logEntry);
    }

    @UiHandler("events")
    public void onSummernotePaste(final SummernotePasteEvent event) {
        final Paragraph logEntry = new Paragraph();
        logEntry.setText("Paste Event Fired!");
        logRow.add(logEntry);
    }

    @UiHandler("events")
    public void onSummernoteImageUpload(final SummernoteImageUploadEvent event) {
        final Paragraph logEntry = new Paragraph();
        StringBuilder sb = new StringBuilder("Image Upload Event Fired:");
        JsArray<ImageFile> images = event.getImages();
        for (int i = 0; i < images.length(); i++) {
            sb.append("<br>").append(images.get(i).getMetadata());
        }
        logEntry.setHTML(sb.toString());
        logRow.add(logEntry);
        events.insertImages(images);
    }

    @UiHandler("events")
    public void onSummernoteChange(final SummernoteChangeEvent event) {
        final Paragraph logEntry = new Paragraph();
        logEntry.setText("Change Event Fired!");
        logRow.add(logEntry);
    }

    interface Binder extends UiBinder<Widget, SummernoteView> {
    }


    private static final Binder BINDER = GWT.create(Binder.class);
    public SummernoteView() {

        initWidget(BINDER.createAndBindUi(this));

        // Hint for words
        hintWords.setHint("\\b(\\w{1,})$", new DefaultHintHandler() {

            List<String> words = new ArrayList<>(
                    Arrays.asList("apple", "orange", "watermelon", "lemon"));

            @Override
            public String[] onSearch(String keyword) {
                List<String> result = new ArrayList<>(0);
                for (String word : words) {
                    if (word.indexOf(keyword) == 0)
                        result.add(word);
                }
                return result.toArray(new String[0]);
            }
        });

        // Bundled suggestions keep the example usable offline, without a GitHub API request.
        hintEmoji.setHint(":([\\-+\\w]+)$", new DefaultHintHandler() {
            private final String[] names = {"smile", "heart", "thumbsup"};
            private final String[] symbols = {"\uD83D\uDE04", "\u2764", "\uD83D\uDC4D"};
            @Override public String[] onSearch(String keyword) {
                List<String> result = new ArrayList<>();
                for (String name : names) if (name.startsWith(keyword)) result.add(name);
                return result.toArray(new String[0]);
            }
            private String symbol(String name) {
                for (int i = 0; i < names.length; i++) if (names[i].equals(name)) return symbols[i];
                return name;
            }
            @Override public String getTemplate(String item) { return symbol(item) + " :" + item + ":"; }
            @Override public Node getContent(String item) { return Document.get().createTextNode(symbol(item)); }
        });

        customToolbar.setToolbar(new Toolbar()
            .addGroup(ToolbarButton.FONT_NAME, ToolbarButton.FONT_SIZE)
            .addGroup(ToolbarButton.UNDO, ToolbarButton.REDO)
            .addGroup(ToolbarButton.CODE_VIEW)
            .addGroup(ToolbarButton.TABLE));


        // Language
        SummernoteLanguage defaultLanguage = languageNote.getLanguage();
        for (SummernoteLanguage language : SummernoteLanguage.values()) {
            languageBox.addItem(language.getCode(), language.name());
        }
        languageBox.setSelectedIndex(defaultLanguage.ordinal());
    }
}