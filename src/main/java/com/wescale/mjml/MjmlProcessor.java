package com.wescale.mjml;

import jdk.nashorn.api.scripting.JSObject;

import javax.script.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class MjmlProcessor {

    private ScriptEngine javascriptEngine;
    private JSObject mjml2html;

    /**
     * Factory method that returns a new and initialized MjmlProcessor instance.
     *
     * @return MjmlProcessor
     */
    public static MjmlProcessor create() {
        return new MjmlProcessor().initialize();
    }

    /**
     * For instances not created via the factory method, this initializes a newly created MjmlProcessor.
     *
     * @return MjmlProcessor
     */
    public MjmlProcessor initialize() {
        setupJavascriptEngine();
        setupMethodReference(compileJavascript(loadMjmlResource()));
        return this;
    }

    /**
     * Processes the given mjml template.
     *
     * @param mjml
     * @return MjmlProcessorResult
     */
    public synchronized MjmlProcessorResult process(String mjml) { /* synchronized because of js multithreading issues */
        return new MjmlProcessorResult(mjml2html.call(null, mjml));
    }

    private CompiledScript compileJavascript(Reader mjmlFileReader) {
        try {
            CompiledScript script = ((Compilable) javascriptEngine).compile(mjmlFileReader);
            script.eval();
            return script;

        } catch (ScriptException e) {
            throw new IllegalStateException("Cannot.. ", e);
        }
    }

    private Reader loadMjmlResource() {
        InputStream resource = this.getClass().getClassLoader().getResourceAsStream("mjml.js");

        try {
            return new BufferedReader(new InputStreamReader(resource));
        } catch (NullPointerException e) {
            throw new RuntimeException("Cannot load mjml resource!", e);
        }
    }

    private void setupJavascriptEngine() {
        try {
            javascriptEngine = new ScriptEngineManager().getEngineByName("nashorn");
            javascriptEngine.eval("var global = this;"); // the mjml javascript expects 'global = { .. }' to be present
        } catch (ScriptException e) {
            throw new RuntimeException("Cannot setup javascript engine!", e);
        }
    }

    private void setupMethodReference(CompiledScript script) {
        // Dig into the javascript context and grab "global.mjml.mjml2html"
        mjml2html = (JSObject) ((JSObject) ((JSObject) script.getEngine().get("global")).getMember("mjml")).getMember("mjml2html");
    }
}