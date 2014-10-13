package com.netopyr.coffee4java;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import java.util.Arrays;
import java.util.List;

/**
 * A {@code CoffeeScriptEngineFactory} is an implementation of {@code ScriptEngineFactory}
 * that generates {@code ScriptEngines} to run CoffeeScript.
 * <p>
 * Usually one does not use {@code CoffeeScriptEngineFactory} directly, but instead it is
 * recommended to request an instance of {@code ScriptEngine}
 * from {@code ScriptEngineManager}, e.g.
 * <p>
 * <pre>
 *     ScriptEngineManager manager = new ScriptEngineManager();
 *     CoffeeScriptEngine engine = (CoffeeScriptEngine)manager.getEngineByName("CoffeeScript");
 * </pre>
 *
 * @author Michael Heinrichs
 * @version 1.0.0
 *
 * @see javax.script.ScriptEngineFactory
 */
public class CoffeeScriptEngineFactory implements ScriptEngineFactory {

    private static final String ENGINE_NAME      = "CoffeeScript";
    private static final String ENGINE_VERSION   = "1.0.0";
    private static final List<String> EXTENSIONS = Arrays.asList("coffee", "coffeescript");
    private static final List<String> MIME_TYPES = Arrays.asList("application/coffeescript", "text/coffeescript");
    private static final List<String> NAMES      = Arrays.asList("CoffeeScript", "coffeescript");
    private static final String LANGUAGE_NAME    = "CoffeeScript";
    private static final String LANGUAGE_VERSION = "1.8.0";

    private ScriptEngineFactory javascriptFactory;

    private ScriptEngineFactory getJavascriptFactory() {
        if (javascriptFactory == null) {
            javascriptFactory = new ScriptEngineManager().getEngineByName("javascript").getFactory();
        }
        return javascriptFactory;
    }

    @Override
    public String getEngineName() {
        return ENGINE_NAME;
    }

    @Override
    public String getEngineVersion() {
        return ENGINE_VERSION;
    }

    @Override
    public List<String> getExtensions() {
        return EXTENSIONS;
    }

    @Override
    public List<String> getMimeTypes() {
        return MIME_TYPES;
    }

    @Override
    public List<String> getNames() {
        return NAMES;
    }

    @Override
    public String getLanguageName() {
        return LANGUAGE_NAME;
    }

    @Override
    public String getLanguageVersion() {
        return LANGUAGE_VERSION;
    }

    @Override
    public Object getParameter(String key) {
        switch (key) {
            case ScriptEngine.ENGINE:
                return getEngineName();
            case ScriptEngine.ENGINE_VERSION:
                return getEngineVersion();
            case ScriptEngine.NAME:
                return getNames().get(0);
            case ScriptEngine.LANGUAGE:
                return getLanguageName();
            case ScriptEngine.LANGUAGE_VERSION:
                return getLanguageVersion();
        }
        return getJavascriptFactory().getParameter(key);
    }

    @Override
    public String getMethodCallSyntax(String obj, String m, String... args) {
        return String.format("%s.%s(%s))", obj, m, String.join(",", args));
    }

    @Override
    public String getOutputStatement(String toDisplay) {
        return "print(" + toDisplay + ")";
    }

    @Override
    public String getProgram(String... statements) {
        return String.join("\n", statements);
    }

    @Override
    public ScriptEngine getScriptEngine() {
        final ScriptEngine javascript = getJavascriptFactory().getScriptEngine();
        final CoffeeScriptCompiler compiler = new CoffeeScriptCompiler(javascript);
        return new CoffeeScriptEngine(this, javascript, compiler);
    }
}
