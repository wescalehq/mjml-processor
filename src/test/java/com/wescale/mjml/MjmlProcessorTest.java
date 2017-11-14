package com.wescale.mjml;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import javax.script.ScriptException;
import java.io.IOException;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

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
}
