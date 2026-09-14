package org.gwtbootstrap3.extras.summernote.client;

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

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

/**
 * @author godi
 */
public interface SummernoteClientBundle extends ClientBundle {

    public static final SummernoteClientBundle INSTANCE = GWT.create(SummernoteClientBundle.class);
    static final String VERSION = "0.9.1";
    static final String LOCALE_DIR = "resource/js/locales.cache." + VERSION + "/";

    @Source("resource/js/summernote-0.9.1.min.cache.js")
    TextResource summernote();

    @Source("resource/js/locales.cache.0.9.1/summernote-ar-AR.js")
    TextResource ar_AR();

    @Source("resource/js/locales.cache.0.9.1/summernote-bg-BG.js")
    TextResource bg_BG();

    @Source("resource/js/locales.cache.0.9.1/summernote-ca-ES.js")
    TextResource ca_ES();

    @Source("resource/js/locales.cache.0.9.1/summernote-cs-CZ.js")
    TextResource cs_CZ();

    @Source("resource/js/locales.cache.0.9.1/summernote-da-DK.js")
    TextResource da_DK();

    @Source("resource/js/locales.cache.0.9.1/summernote-de-DE.js")
    TextResource de_DE();

    @Source("resource/js/locales.cache.0.9.1/summernote-es-ES.js")
    TextResource es_ES();

    @Source("resource/js/locales.cache.0.9.1/summernote-es-EU.js")
    TextResource es_EU();

    @Source("resource/js/locales.cache.0.9.1/summernote-fa-IR.js")
    TextResource fa_IR();

    @Source("resource/js/locales.cache.0.9.1/summernote-fi-FI.js")
    TextResource fi_FI();

    @Source("resource/js/locales.cache.0.9.1/summernote-fr-FR.js")
    TextResource fr_FR();

    @Source("resource/js/locales.cache.0.9.1/summernote-gl-ES.js")
    TextResource gl_ES();

    @Source("resource/js/locales.cache.0.9.1/summernote-he-IL.js")
    TextResource he_IL();

    @Source("resource/js/locales.cache.0.9.1/summernote-hr-HR.js")
    TextResource hr_HR();

    @Source("resource/js/locales.cache.0.9.1/summernote-hu-HU.js")
    TextResource hu_HU();

    @Source("resource/js/locales.cache.0.9.1/summernote-id-ID.js")
    TextResource id_ID();

    @Source("resource/js/locales.cache.0.9.1/summernote-it-IT.js")
    TextResource it_IT();

    @Source("resource/js/locales.cache.0.9.1/summernote-ja-JP.js")
    TextResource ja_JP();

    @Source("resource/js/locales.cache.0.9.1/summernote-ko-KR.js")
    TextResource ko_KR();

    @Source("resource/js/locales.cache.0.9.1/summernote-lt-LT.js")
    TextResource lt_LT();

    @Source("resource/js/locales.cache.0.9.1/summernote-lt-LV.js")
    TextResource lt_LV();

    @Source("resource/js/locales.cache.0.9.1/summernote-nb-NO.js")
    TextResource nb_NO();

    @Source("resource/js/locales.cache.0.9.1/summernote-nl-NL.js")
    TextResource nl_NL();

    @Source("resource/js/locales.cache.0.9.1/summernote-pl-PL.js")
    TextResource pl_PL();

    @Source("resource/js/locales.cache.0.9.1/summernote-pt-BR.js")
    TextResource pt_BR();

    @Source("resource/js/locales.cache.0.9.1/summernote-pt-PT.js")
    TextResource pt_PT();

    @Source("resource/js/locales.cache.0.9.1/summernote-ro-RO.js")
    TextResource ro_RO();

    @Source("resource/js/locales.cache.0.9.1/summernote-ru-RU.js")
    TextResource ru_RU();

    @Source("resource/js/locales.cache.0.9.1/summernote-sk-SK.js")
    TextResource sk_SK();

    @Source("resource/js/locales.cache.0.9.1/summernote-sl-SI.js")
    TextResource sl_SI();

    @Source("resource/js/locales.cache.0.9.1/summernote-sr-RS.js")
    TextResource sr_RS();

    @Source("resource/js/locales.cache.0.9.1/summernote-sr-RS-Latin.js")
    TextResource sr_RS_Latin();

    @Source("resource/js/locales.cache.0.9.1/summernote-sv-SE.js")
    TextResource sv_SE();

    @Source("resource/js/locales.cache.0.9.1/summernote-th-TH.js")
    TextResource th_TH();

    @Source("resource/js/locales.cache.0.9.1/summernote-tr-TR.js")
    TextResource tr_TR();

    @Source("resource/js/locales.cache.0.9.1/summernote-uk-UA.js")
    TextResource uk_UA();

    @Source("resource/js/locales.cache.0.9.1/summernote-vi-VN.js")
    TextResource vi_VN();

    @Source("resource/js/locales.cache.0.9.1/summernote-zh-CN.js")
    TextResource zh_CN();

    @Source("resource/js/locales.cache.0.9.1/summernote-zh-TW.js")
    TextResource zh_TW();
}
