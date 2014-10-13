package com.netopyr.coffee4java;

import javax.script.Compilable;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.stream.Collectors;

class CoffeeScriptCompiler {

    private static final String COFFEE_SCRIPT_JS = "coffee-script.js";
    private static final String COFFEE_SCRIPT_NAMESPACE = "CoffeeScript";
    private static final String COMPILE_METHOD_NAME = "compile";

    private final Invocable javaScriptEngine;
    private final Object coffeeScript;

    private boolean printJavaScript = false;

    CoffeeScriptCompiler(ScriptEngine scriptEngine) {
        this.javaScriptEngine = (Invocable)scriptEngine;

        try (final Reader reader = new BufferedReader(new InputStreamReader(CoffeeScriptCompiler.class.getResourceAsStream(COFFEE_SCRIPT_JS)))) {
            final Compilable compiler = (Compilable)scriptEngine;
            compiler.compile(reader).eval();
            coffeeScript = scriptEngine.get(COFFEE_SCRIPT_NAMESPACE);
        } catch (ScriptException | IOException e) {
            throw new Error("Preparation of the CoffeeScript compiler failed", e);
        }
    }

    boolean isPrintJavaScript() {
        return printJavaScript;
    }

    void setPrintJavaScript(boolean printJavaScript) {
        this.printJavaScript = printJavaScript;
    }

    String compile(String script) throws ScriptException {
        try {
            final String result = (String) javaScriptEngine.invokeMethod(coffeeScript, COMPILE_METHOD_NAME, script);
            if (printJavaScript) {
                System.out.println(result);
            }
            return result;
        } catch (NoSuchMethodException ex) {
            // Oh, oh, this should not happen. (I know, famous last words...)
            throw new Error(ex);
        }
    }

    String compile(Reader reader) throws ScriptException {
        final String script;
        try (final BufferedReader bufferedReader = new BufferedReader(reader)) {
            script = bufferedReader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new ScriptException(e);
        }

        return compile(script);
    }
}
