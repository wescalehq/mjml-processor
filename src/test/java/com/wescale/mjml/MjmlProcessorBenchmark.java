/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wescale.mjml;

import org.apache.commons.io.FileUtils;

import javax.script.ScriptException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MjmlProcessorBenchmark {

    public static void main(String[] args) throws ScriptException, IOException, NoSuchMethodException {
        ClassLoader classLoader = MjmlProcessorBenchmark.class.getClassLoader();

        MjmlProcessor mjmlProcessor = MjmlProcessor.create();

        // process template
        File templateFile = new File("src/test/resources/complex_template.mjml");
        String template = FileUtils.readFileToString(templateFile, StandardCharsets.UTF_8);

        // warm up
        System.out.println("Warm up ...");
        for (int i = 0; i < 100; i++) {
            mjmlProcessor.process(template);
            System.out.println(i);
        }

        long startTime = System.currentTimeMillis();

        System.out.println("Starting loop");
        for (int i = 0; i < 100; i++) {
            mjmlProcessor.process(template);
            System.out.println(i);
        }

        long estimatedTime = System.currentTimeMillis() - startTime;

        System.out.println(estimatedTime);
    }
}
