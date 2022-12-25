package ch.softenvironment.util;

public interface I18nTranslator { //TODO HIP
    default String getResourceString(String property) {
        return "<NON-Translated>";
    }
}
