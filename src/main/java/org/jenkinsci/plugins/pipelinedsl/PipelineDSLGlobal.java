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
package org.jenkinsci.plugins.pipelinedsl;

import groovy.lang.Binding;
import hudson.Extension;
import org.jenkinsci.plugins.scriptsecurity.sandbox.whitelists.ProxyWhitelist;
import org.jenkinsci.plugins.scriptsecurity.sandbox.whitelists.StaticWhitelist;
import org.jenkinsci.plugins.workflow.cps.CpsScript;
import org.jenkinsci.plugins.workflow.cps.CpsThread;
import org.jenkinsci.plugins.workflow.cps.GlobalVariable;

import java.io.File;
import java.io.IOException;

public abstract class PipelineDSLGlobal extends GlobalVariable {

    public abstract String getFunctionName();

    @Override
    public String getName() {
        return getFunctionName();
    }



    @Override
    public Object getValue(CpsScript script) throws Exception {
        Binding binding = script.getBinding();
        Object pipelineDSL;

        if (binding.hasVariable(getName())) {
            pipelineDSL = binding.getVariable(getName());
        } else {
            CpsThread c = CpsThread.current();
            if (c == null)
                throw new IllegalStateException("Expected to be called from CpsThread");

            File converterGroovy = new File(getClass().getClassLoader().getResource(getFunctionName() + ".groovy").getFile());
            pipelineDSL = c.getExecution()
                    .getShell()
                    .getClassLoader()
                    .parseClass(converterGroovy)
                    .newInstance();
            binding.setVariable(getName(), pipelineDSL);
        }

        return pipelineDSL;
    }

}
