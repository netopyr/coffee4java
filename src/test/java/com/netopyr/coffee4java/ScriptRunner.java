package com.netopyr.coffee4java;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ScriptRunner extends Runner {

    private static final String[] SCRIPTS = new String[] {
            "arrays",
            "assignment",
            "booleans",
            "classes",
            "cluster",
            "comments",
            "compilation",
            "comprehensions",
            "control_flow",
            "exception_handling",
            "formatting",
            "function_invocation",
            "functions",
            "helpers",
            "importing",
            "interpolation",
            "javascript_literals",
            "numbers",
            "objects",
            "operators",
            "option_parser",
            "ranges",
            "regexps",
            "scope",
            "slicing_and_splicing",
            "soaks",
            "strings"
    };

    private final ScriptEngineFactory factory;
    private final String utils;
    private final Description description;

    public ScriptRunner(Class testClass) throws URISyntaxException, IOException {
        this.factory = new ScriptEngineManager().getEngineByName("coffeescript").getFactory();
        this.description = Description.createSuiteDescription(testClass);
        utils = new String(Files.readAllBytes(Paths.get(ScriptRunner.class.getResource("utils.coffee").toURI())));
    }

    @Override
    public Description getDescription() {
        return description;
    }

    @Override
    public void run(RunNotifier notifier) {
        for (final String scriptName : SCRIPTS) {
            final Description scriptDescription = Description.createSuiteDescription(scriptName);
            getDescription().addChild(scriptDescription);

            final CoffeeScriptEngine engine = (CoffeeScriptEngine) factory.getScriptEngine();
            engine.put("junitNotifier", notifier);
            engine.put("scriptName", scriptName);
            engine.put("scriptDescription", scriptDescription);
            engine.put("testingBrowser", true);
            try {
                engine.eval(utils);
                try (final Reader reader = new InputStreamReader(CoffeeScriptCompiler.class.getResourceAsStream("tests/" + scriptName + ".coffee"), "UTF-8")) {
                    engine.eval(reader);
                }
            }
            catch (Exception e) {
                notifier.fireTestFailure(new Failure(scriptDescription, e));
            }
        }
    }
}
