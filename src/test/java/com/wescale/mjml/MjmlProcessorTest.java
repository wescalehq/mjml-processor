package com.wescale.mjml;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import javax.script.ScriptException;
import java.io.IOException;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;

public class MjmlProcessorTest {

	private MjmlProcessor mjmlProcessor;

	@Before
	public void setMjmlProcessor() throws ScriptException, IOException {

		mjmlProcessor = MjmlProcessor.create();
	}

	@Test
	public void process() throws Exception {

		String mjml = IOUtils.resourceToString("/simple_template.mjml", Charset.forName("UTF-8"));
		String expectedHtml = IOUtils.resourceToString("/simple_template.html", Charset.forName("UTF-8"));
		String actualHtml = mjmlProcessor.process(mjml).getHtml();

		assertEquals(expectedHtml, actualHtml);
	}
}