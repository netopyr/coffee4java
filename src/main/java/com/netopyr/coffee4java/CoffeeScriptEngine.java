package com.netopyr.coffee4java;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import java.io.Reader;

/**
 * A {@code CoffeeScriptEngine} is an implementation of JSR-223 for CoffeeScript.
 * <p>
 * In the current implementation, a given CoffeeScript program is translated into JavaScript
 * source code, which is executed using the default JavaScript engine (Nashorn in Java 8).
 * <p>
 * For debugging purposes it is possible to view the generated JavaScript code by calling
 * {@link #toJavaScript(String)} or {@link #toJavaScript(java.io.Reader)}.
 *
 * @author Michael Heinrichs
 * @version 1.0.0
 *
 * @see javax.script.ScriptEngine
 * @see javax.script.Compilable
 * @see javax.script.Invocable
 */
public class CoffeeScriptEngine implements ScriptEngine, Compilable, Invocable {

    private final CoffeeScriptEngineFactory factory;
    private final ScriptEngine javascript;
    private final CoffeeScriptCompiler compiler;

    CoffeeScriptEngine(CoffeeScriptEngineFactory factory, ScriptEngine javascript, CoffeeScriptCompiler compiler) {
        this.factory = factory;
        this.javascript = javascript;
        this.compiler = compiler;
    }

    /**
     * Translate a given CoffeeScript program into JavaScript.
     *
     * @param script The source of the script.
     * @return The source code of the generated JavaScript program.
     * @throws ScriptException if an error occurs in the script.
     */
    public String toJavaScript(String script) throws ScriptException {
        return compiler.compile(script);
    }

    /**
     * Same as {@link #toJavaScript(String)} except that the source of the script is
     * provided as a <code>Reader</code>
     *
     * @param reader The source of the script.
     * @return The source code of the generated JavaScript program.
     * @throws ScriptException if an error occurs in the script.
     */
    public String toJavaScript(Reader reader) throws ScriptException {
        return compiler.compile(reader);
    }

    @Override
    public Object eval(String script, ScriptContext context) throws ScriptException {
        return javascript.eval(compiler.compile(script), context);
    }

    @Override
    public Object eval(Reader reader, ScriptContext context) throws ScriptException {
        return javascript.eval(compiler.compile(reader), context);
    }

    @Override
    public Object eval(String script) throws ScriptException {
        return javascript.eval(compiler.compile(script));
    }

    @Override
    public Object eval(Reader reader) throws ScriptException {
        return javascript.eval(compiler.compile(reader));
    }

    @Override
    public Object eval(String script, Bindings bindings) throws ScriptException {
        return javascript.eval(compiler.compile(script), bindings);
    }

    @Override
    public Object eval(Reader reader, Bindings bindings) throws ScriptException {
        return javascript.eval(compiler.compile(reader), bindings);
    }

    @Override
    public void put(String key, Object value) {
        javascript.put(key, value);
    }

    @Override
    public Object get(String key) {
        return javascript.get(key);
    }

    @Override
    public Bindings getBindings(int scope) {
        return javascript.getBindings(scope);
    }

    @Override
    public void setBindings(Bindings bindings, int scope) {
        javascript.setBindings(bindings, scope);
    }

    @Override
    public Bindings createBindings() {
        return javascript.createBindings();
    }

    @Override
    public ScriptContext getContext() {
        return javascript.getContext();
    }

    @Override
    public void setContext(ScriptContext context) {
        javascript.setContext(context);
    }

    @Override
    public ScriptEngineFactory getFactory() {
        return factory;
    }

    @Override
    public CompiledScript compile(String script) throws ScriptException {
        return ((Compilable)javascript).compile(compiler.compile(script));
    }

    @Override
    public CompiledScript compile(Reader script) throws ScriptException {
        return ((Compilable)javascript).compile(compiler.compile(script));
    }

    @Override
    public Object invokeMethod(Object obj, String name, Object... args) throws ScriptException, NoSuchMethodException {
        return ((Invocable)javascript).invokeMethod(obj, name, args);
    }

    @Override
    public Object invokeFunction(String name, Object... args) throws ScriptException, NoSuchMethodException {
        return ((Invocable)javascript).invokeFunction(name, args);
    }

    @Override
    public <T> T getInterface(Class<T> clazz) {
        return ((Invocable)javascript).getInterface(clazz);
    }

    @Override
    public <T> T getInterface(Object obj, Class<T> clazz) {
        return ((Invocable)javascript).getInterface(obj, clazz);
    }
}
