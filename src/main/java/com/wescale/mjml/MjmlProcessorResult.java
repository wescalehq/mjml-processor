package com.wescale.mjml;

import jdk.nashorn.api.scripting.JSObject;

public class MjmlProcessorResult {

    private String html;
    private JSObject errors;


    MjmlProcessorResult(Object result) {
        errors = (JSObject) ((JSObject) result).getMember("errors");
        html = (String) ((JSObject) result).getMember("html");
    }

    public String getHtml() {
        return html;
    }

    public JSObject getErrors() {
        return errors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MjmlProcessorResult that = (MjmlProcessorResult) o;

        return html != null ? html.equals(that.html) : that.html == null;
    }

    @Override
    public int hashCode() {
        return html != null ? html.hashCode() : 0;
    }
}
