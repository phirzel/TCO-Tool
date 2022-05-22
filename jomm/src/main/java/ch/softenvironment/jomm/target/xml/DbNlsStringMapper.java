package ch.softenvironment.jomm.target.xml;

/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */

import ch.softenvironment.jomm.DbObjectServer;
import ch.softenvironment.jomm.datatypes.DbNlsString;
import ch.softenvironment.jomm.serialize.InterlisSerializer;
import ch.softenvironment.jomm.serialize.ObjectWriter;
import ch.softenvironment.jomm.serialize.Serializer;
import ch.softenvironment.util.BeanReflector;
import ch.softenvironment.util.StringUtils;
import ch.softenvironment.util.Tracer;
import java.util.ArrayList;
import java.util.List;

/**
 * Mapping-Utility to encode and decode a DbNlsString to/from an XML-Instance. The structure of a DbNlsString is always the same, therefore no Visitor is necessary her.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
class DbNlsStringMapper implements ObjectWriter /* , Visitor */ {

    // DbNlsString tag's in XML-instance
    private static final String SE_TRANSLATION = "translation";

    private DbNlsString nlsString = null;
    // private List translation = null;
    private List<String> entry = null;

    /**
     * Return whether given tag represents a DbNlsString definition.
     *
     * @param tag
     * @return boolean
     * @see SE_NLS
     */
    protected static boolean isNlsString(String tag) {
        return (tag != null) && tag.equals(XmlMapper.SE_NLS);
    }

    /**
     * Constructor for decoding an XML-instance.
     *
     * @param server
     * @see #handleStart()
     * @see #handleEnd()
     */
    protected DbNlsStringMapper(DbObjectServer server) {
        super();
        try {
            nlsString = (DbNlsString) server.createInstance(DbNlsString.class);
        } catch (Exception e) {
            Tracer.getInstance().runtimeError(null, e);
        }
    }

    /**
     * Constructor for encoding an XML-instance.
     *
     * @see #writeObject()
     */
    protected DbNlsStringMapper() {
        super();
    }

    /**
     * Decode all beginning DbNlsString relevant elements in an XML-Instance. Design Pattern: State
     *
     * @param element
     */
    protected void handleStart(String element) {
        if (element.equals(SE_TRANSLATION)) {
            // [0..*] XmlSerializer.SE_NLS_TRANSLATION will follow
            // translation = new ArrayList();
        } else if (element.equals(XmlMapper.SE_NLS_TRANSLATION)) {
            // a single language/country/nlsText translation entry will follow
            entry = new ArrayList<String>(3);
            entry.add(null); // [0]=language
            entry.add(null); // [1]=country
            entry.add(null); // [2]=nlsText
            // translation.add(entry);
        }
        /*
         * else if
         * (element(BeanReflector.convertPropertyName(DbNlsString.ATTRIBUTE_NLS_TEXT
         * ))) { // nlsText[1] = will follow } else if
         * (element(DbNlsString.PROPERTY_LANGUAGE)) { // language[1] = will
         * follow } else if (element(DbNlsString.PROPERTY_COUNTRY)) { // country
         * [0..1] = will follow }
         */
    }

    /**
     * Decode all ending DbNlsString relevant elements in an XML-instance. Design Pattern: State
     *
     * @param element
     * @param content (finally known)
     * @return
     */
    protected DbNlsString handleEnd(String element, StringBuffer content) {
        String value = content == null ? null : content.toString();
        if (element.equals(XmlMapper.SE_NLS)) {
            // done DbNlsString is now completely read
            return nlsString;
        } else if (element.equals(SE_TRANSLATION)) {
            // translation is now complete
            // TODO NYI: multi-translation
            // nlsString.add(entry)
            // Locale loc = new Locale((String)entry.get(0) /*language*/,
            // (String)entry.get(1) /*country*/);
            nlsString.setValue(entry.get(2) /* , loc */);
        } else if (element.equals(XmlMapper.SE_NLS_TRANSLATION)) {
            // a single language/country/nlsText translation entry will follow
            // translation.add(entry);
        } else if (element.equals(BeanReflector.convertPropertyName(DbNlsString.ATTRIBUTE_NLS_TEXT))) {
            // nlsText[1]
            entry.set(2, value);
        } else if (element.equals(DbNlsString.PROPERTY_LANGUAGE)) {
            entry.set(0, value);
        } else if (element.equals(DbNlsString.PROPERTY_COUNTRY)) {
            entry.set(1, value);
        }
        return null;
    }

    /**
     * Write a DbNlsString into XML-Stream.
     *
     * @param element Property representing the given nlsString
     */
    @Override
    public void writeObject(Object object, Serializer cb) throws java.io.IOException {
        DbNlsString nlsString = (DbNlsString) object;
        InterlisSerializer ser = (InterlisSerializer) cb;

        java.util.Iterator<String> translations = nlsString.getAllValuesByLanguage().keySet().iterator();
        if (translations.hasNext()) {
            ser.startElement(nlsString.getObjectServer().getMapper().getTargetNlsName());
            ser.startElement(SE_TRANSLATION);
            while (translations.hasNext()) {
                // TODO key == language => might change soon
                String language = translations.next();
                String nlsText = nlsString.getTranslation(language);
                if (!StringUtils.isNullOrEmpty(nlsText)) {
                    ser.startElement(nlsString.getObjectServer().getMapper().getTargetNlsTranslationName());
                    // TODO not multi-language able overwrites entries with new
                    // current language
                    ser.element(DbNlsString.PROPERTY_LANGUAGE, language);
                    // TODO Country
                    ser.element(BeanReflector.convertPropertyName(DbNlsString.ATTRIBUTE_NLS_TEXT), nlsText);
                    ser.endElement(/*
                     * nlsString.getObjectServer().getMapper().
                     * getTargetNlsTranslationName()
                     */);
                }
            }
            ser.endElement(SE_TRANSLATION);
            ser.endElement(nlsString.getObjectServer().getMapper().getTargetNlsName());
        }
        /*
         * java.util.Iterator languages =
         * nlsString.getAllValues().keySet().iterator(); if
         * (languages.hasNext()) {
         * ser.startElement(nlsString.getObjectServer().getMapper
         * ().getTargetNlsName()); ser.startElement(SE_TRANSLATION); while
         * (languages.hasNext()) { String language = (String)languages.next();
         * Map countries = (Map)nlsString.getAllValues().get(language); Iterator
         * iterator = countries.keySet().iterator(); while (iterator.hasNext())
         * { String country = (String)iterator.next();
         * ser.startElement(nlsString
         * .getObjectServer().getMapper().getTargetNlsTranslationName()); //TODO
         * not multi-language able overwrites entries with new current language
         * ser.element(DbNlsString.PROPERTY_LANGUAGE, language);
         *
         * if (!country.equals(DbNlsString.NO_COUNTRY)) {
         * ser.element(DbNlsString.PROPERTY_COUNTRY, country); }
         * ser.element(BeanReflector
         * .convertPropertyName(DbNlsString.ATTRIBUTE_NLS_TEXT),
         * (String)countries.get(country)); ser.endElement(); //
         * nlsString.getObjectServer().getMapper().getTargetNlsTranslationName()
         * // } } ser.endElement(SE_TRANSLATION);
         * ser.endElement(nlsString.getObjectServer
         * ().getMapper().getTargetNlsName()); }
         */
    }
}