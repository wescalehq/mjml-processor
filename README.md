[![Build Status](https://travis-ci.org/wescalehq/mjml-processor.svg?branch=master)](https://travis-ci.org/wescalehq/mjml-processor)
[![](https://jitpack.io/v/wescalehq/mjml-processor.svg)](https://jitpack.io/#wescalehq/mjml-processor)

## Overview

This projects aims to bring [mjml](https://mjml.io/) (mailjet markup language) to
the java world. Because mjml is only provided as a bunch of javascript packages, the only
way to access it from Java is to run the original javascript code within [Nashorn](http://www.oracle.com/technetwork/articles/java/jf14-nashorn-2126515.html)
and to bridge calls between these two.

## Usage

Include the package and use it as follows

```java
import com.wescale.mjml;

// ...

MjmlProcessor mjmlProcessor = MjmlProcessor.create();
String template = "<mjml><mj-body><mj-text>Hello World</mj-text></mj-body></mjml>";
MjmlProcessorResult mjmlProcessorResult = mjmlProcessor.process(template);
String html = mjmlProcessorResult.getHtml();
```

Keep a newly created instance of ` MjmlProcessor` as long as possible. Read the performance section for details.

## Performance

You should be aware that creating an instance of `MjmlProcessor` creates a new Nashorn instance,
loads about 4MB of javascript and parses/compiles this into bytecode. This leads to two consequences
* creating an instance of `MjmlProcessor` lasts a quite amount of time (few seconds)
* the first calls to this code are rather slow because the JVM optimizes the javascript bytecode over time

This means that you *should really* take care of *not* throwing away this instance quickly. Create it and
hold it as long as you can and the performance of calling `process` will increase dramatically over time.
