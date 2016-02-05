/*
 * The MIT License
 *
 * Copyright (c) 2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jenkinsci.plugins.simplebuild;

import groovy.lang.Binding;
import hudson.Extension;
import org.jenkinsci.plugins.scriptsecurity.sandbox.whitelists.ProxyWhitelist;
import org.jenkinsci.plugins.scriptsecurity.sandbox.whitelists.StaticWhitelist;
import org.jenkinsci.plugins.workflow.cps.CpsScript;
import org.jenkinsci.plugins.workflow.cps.GlobalVariable;

import java.io.IOException;

@Extension
public class SimpleBuildDSL extends GlobalVariable {
    @Override
    public String getName() {
        return "simpleBuild";
    }

    @Override
    public Object getValue(CpsScript script) throws Exception {
        Binding binding = script.getBinding();
        Object travisPipelineConverter;

        // If we already have a travisPipelineConverter defined, reuse it. Otherwise, load it from the DSL groovy and
        // add it to the binding.
        if (binding.hasVariable(getName())) {
            travisPipelineConverter = binding.getVariable(getName());
        } else {
            travisPipelineConverter = script.getClass()
                    .getClassLoader()
                    .loadClass("org.jenkinsci.plugins.simplebuild.SimpleBuild")
                    .getConstructor(CpsScript.class)
                    .newInstance(script);

            binding.setVariable(getName(), travisPipelineConverter);
        }

        return travisPipelineConverter;
    }

    @Extension
    public static class MiscWhitelist extends ProxyWhitelist {
        /**
         * Methods to add to the script-security whitelist for this plugin to work.
         *
         * @throws IOException
         */
        public MiscWhitelist() throws IOException {
            super(new StaticWhitelist(
                    "new org.yaml.snakeyaml.Yaml",
                    "method org.yaml.snakeyaml.Yaml load java.lang.String",
                    "method java.util.Map containsKey java.lang.Object",
                    "method java.lang.Class isInstance java.lang.Object",
                    "staticMethod org.codehaus.groovy.runtime.ScriptBytecodeAdapter castToType java.lang.Object java.lang.Class",
                    "staticMethod org.codehaus.groovy.runtime.DefaultGroovyMethods toSet java.util.Collection"
            ));
        }
    }
}
