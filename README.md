[![Build Status](https://travis-ci.org/wescalehq/mjml-processor.svg?branch=master)](https://travis-ci.org/wescalehq/mjml-processor)
[![](https://jitpack.io/v/wescalehq/mjml-processor.svg)](https://jitpack.io/#wescalehq/mjml-processor)
 [ ![Download](https://api.bintray.com/packages/wescalehq/oss/mjml-processor/images/download.svg) ](https://bintray.com/wescalehq/oss/mjml-processor/_latestVersion)
[![codecov](https://codecov.io/gh/wescalehq/mjml-processor/branch/master/graph/badge.svg)](https://codecov.io/gh/wescalehq/mjml-processor)

## Overview

This projects aims to bring [mjml](https://mjml.io/) (mailjet markup language) to
the java world. Because mjml is only provided as a bunch of javascript packages, the only
way to access it from Java is to run the original javascript code within [Nashorn](http://www.oracle.com/technetwork/articles/java/jf14-nashorn-2126515.html)
and to bridge calls between these two.

## Get It

Releases are maintained via [jcenter](https://bintray.com/wescalehq/oss/mjml-processor). Snapshots are available via [jitpack](https://jitpack.io/#wescalehq/mjml-processor).

### Gradle

```gradle
repositories {
    jcenter()
}

dependencies {
	compile 'com.wescale:mjml-processor:CURRENT_VERSION'
}
```

### Maven

```xml
<dependency>
    <groupId>com.wescale</groupId>
    <artifactId>mjml-processor</artifactId>
    <version>1.0.2</version>
</dependency>
...
<repositories>
    <repository>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
        <id>jcenter</id>
        <name>bintray</name>
        <url>https://jcenter.bintray.com</url>
    </repository>
</repositories>
<pluginRepositories>
    <pluginRepository>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
        <id>jcenter</id>
        <name>bintray-plugins</name>
        <url>https://jcenter.bintray.com</url>
    </pluginRepository>
</pluginRepositories>
...
```

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
You could even think of using an object pool to handle multiple threads processing mjml markup.
