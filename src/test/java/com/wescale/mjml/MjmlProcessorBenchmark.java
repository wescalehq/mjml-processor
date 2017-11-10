package com.wescale.mjml;

import org.apache.commons.io.FileUtils;

import javax.script.*;
import java.io.File;
import java.io.IOException;

public class MjmlProcessorBenchmark {

    public static void main(String[] args) throws ScriptException, IOException, NoSuchMethodException {
        ClassLoader classLoader = MjmlProcessorBenchmark.class.getClassLoader();

        MjmlProcessor mjmlProcessor = MjmlProcessor.create();

        // process template
        File templateFile = new File("src/test/resources/complex_template.mjml");
        String template = FileUtils.readFileToString(templateFile);

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
