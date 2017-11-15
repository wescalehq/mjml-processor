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

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import javax.script.ScriptException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import static java.util.concurrent.TimeUnit.SECONDS;

public class MjmlProcessorTest {

    private MjmlProcessor mjmlProcessor;

    @Before
    public void setMjmlProcessor() throws ScriptException, IOException {

        mjmlProcessor = MjmlProcessor.create();
    }

    @Test
    public void shouldProcessSimpleTemplate() throws Exception {

        String mjml = IOUtils.resourceToString("/simple_template.mjml", Charset.forName("UTF-8"));
        String expectedHtml = IOUtils.resourceToString("/simple_template.html", Charset.forName("UTF-8"));
        String actualHtml = mjmlProcessor.process(mjml).getHtml();

        assertEquals(expectedHtml, actualHtml);
    }

    @Test
    public void shouldProcessOnePageTemplate() throws IOException {

        String mjml = IOUtils.resourceToString("/one_page.mjml", Charset.forName("UTF-8"));
        String actualHtml = mjmlProcessor.process(mjml).getHtml();

        assertNotNull(actualHtml);
    }

    @Test
    public void sameHtmlShouldYieldEqualResult() throws IOException {

        String mjml = IOUtils.resourceToString("/simple_template.mjml", Charset.forName("UTF-8"));

        assertEquals(mjmlProcessor.process(mjml), mjmlProcessor.process(mjml));
        assertEquals(mjmlProcessor.process(mjml).hashCode(), mjmlProcessor.process(mjml).hashCode());
    }

    @Test
    public void differentHtmlShouldYieldDifferentResult() throws IOException {

        String simple = IOUtils.resourceToString("/simple_template.mjml", Charset.forName("UTF-8"));
        String onePage = IOUtils.resourceToString("/one_page.mjml", Charset.forName("UTF-8"));

        assertNotEquals(mjmlProcessor.process(simple), mjmlProcessor.process(onePage));
        assertNotEquals(mjmlProcessor.process(simple).hashCode(), mjmlProcessor.process(onePage).hashCode());
    }

    public class ConsumerThread extends Thread {
        private int threadNumber;

        public ConsumerThread(int threadNumber) {
            this.threadNumber = threadNumber;
        }

        @Override public void run() {
            try {
                String threadName = Thread.currentThread().getName();
                int templateNumber = (threadNumber % 8) + 1; // there are currently only 8 templates in test resources
                String mjmlTemplateFilename = "template" + templateNumber + ".mjml";
                String htmlOutputFilename = "template" + templateNumber + ".html";
                System.out.println(threadName + " is trying to process " + mjmlTemplateFilename);

                String mjml = IOUtils.resourceToString("/" + mjmlTemplateFilename, StandardCharsets.UTF_8);
                String expectedHtml = IOUtils.resourceToString("/" + htmlOutputFilename, StandardCharsets.UTF_8);
                String actualHtml = mjmlProcessor.process(mjml).getHtml();

                assertEquals(expectedHtml, actualHtml);
                Thread.currentThread().interrupt();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    public void multipleThreadsUsingTheSameMjmlProcessor() throws Exception {
        final Integer numberOfThreads = 16;
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

        for(int i=1; i <= numberOfThreads; i++) {
            executor.execute(new ConsumerThread(i));
        }

        executor.shutdown();
        executor.awaitTermination(120, SECONDS);
    }
}
