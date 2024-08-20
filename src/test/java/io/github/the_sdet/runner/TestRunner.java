package io.github.the_sdet.runner;

import org.junit.platform.suite.api.*;

import static io.cucumber.core.options.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "io.github.the_sdet.stepdefinitions, io.github.the_sdet.hooks")
@ExcludeTags("ignore")
public class TestRunner {

}
