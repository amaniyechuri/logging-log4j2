/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */
package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.filter.CompositeFilter;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 *
 */
public class TestConfigurator {

    private static final String CONFIG_NAME = "ConfigTest";

    private static final String FILESEP = System.getProperty("file.separator");


    @Test
    public void testFromFile() throws Exception {
        LoggerContext ctx = Configurator.initialize("Test1", null, "target/test-classes/log4j2-config.xml");
        Logger logger = LogManager.getLogger("org.apache.test.TestConfigurator");
        Configuration config = ctx.getConfiguration();
        assertNotNull("No configuration", config);
        assertTrue("Incorrect Configuration. Expected " + CONFIG_NAME + " but found " + config.getName(),
            CONFIG_NAME.equals(config.getName()));
        Map<String, Appender> map = config.getAppenders();
        assertNotNull("No Appenders", map != null && map.size() > 0);
        assertTrue("Wrong configuration", map.containsKey("List"));
        Configurator.shutdown(ctx);
        config = ctx.getConfiguration();
        assertTrue("Incorrect Configuration. Expected " + DefaultConfiguration.DEFAULT_NAME + " but found " +
            config.getName(), DefaultConfiguration.DEFAULT_NAME.equals(config.getName()));
    }

    @Test
    public void testFromStream() throws Exception {
        InputStream is = new FileInputStream("target/test-classes/log4j2-config.xml");
        ConfigurationFactory.ConfigurationSource source =
            new ConfigurationFactory.ConfigurationSource(is, "target/test-classes/log4j2-config.xml");
        LoggerContext ctx = Configurator.initialize(null, source);
        Logger logger = LogManager.getLogger("org.apache.test.TestConfigurator");
        Configuration config = ctx.getConfiguration();
        assertNotNull("No configuration", config);
        assertTrue("Incorrect Configuration. Expected " + CONFIG_NAME + " but found " + config.getName(),
            CONFIG_NAME.equals(config.getName()));
        Map<String, Appender> map = config.getAppenders();
        assertNotNull("No Appenders", map != null && map.size() > 0);
        assertTrue("Wrong configuration", map.containsKey("List"));
        Configurator.shutdown(ctx);
        config = ctx.getConfiguration();
        assertTrue("Incorrect Configuration. Expected " + DefaultConfiguration.DEFAULT_NAME + " but found " +
            config.getName(), DefaultConfiguration.DEFAULT_NAME.equals(config.getName()));
    }

    @Test
    public void testFromStreamNoId() throws Exception {
        InputStream is = new FileInputStream("target/test-classes/log4j2-config.xml");
        ConfigurationFactory.ConfigurationSource source =
            new ConfigurationFactory.ConfigurationSource(is);
        LoggerContext ctx = Configurator.initialize(null, source);
        Logger logger = LogManager.getLogger("org.apache.test.TestConfigurator");
        Configuration config = ctx.getConfiguration();
        assertNotNull("No configuration", config);
        assertTrue("Incorrect Configuration. Expected " + CONFIG_NAME + " but found " + config.getName(),
            CONFIG_NAME.equals(config.getName()));
        Map<String, Appender> map = config.getAppenders();
        assertNotNull("No Appenders", map != null && map.size() > 0);
        assertTrue("Wrong configuration", map.containsKey("List"));
        Configurator.shutdown(ctx);
        config = ctx.getConfiguration();
        assertTrue("Incorrect Configuration. Expected " + DefaultConfiguration.DEFAULT_NAME + " but found " +
            config.getName(), DefaultConfiguration.DEFAULT_NAME.equals(config.getName()));
    }

    @Test
    public void testFromClassPath() throws Exception {
        LoggerContext ctx = Configurator.initialize("Test1", null, "log4j2-config.xml");
        Logger logger = LogManager.getLogger("org.apache.test.TestConfigurator");
        Configuration config = ctx.getConfiguration();
        assertNotNull("No configuration", config);
        assertTrue("Incorrect Configuration. Expected " + CONFIG_NAME + " but found " + config.getName(),
            CONFIG_NAME.equals(config.getName()));
        Map<String, Appender> map = config.getAppenders();
        assertNotNull("No Appenders", map != null && map.size() > 0);
        assertTrue("Wrong configuration", map.containsKey("List"));
        Configurator.shutdown(ctx);
        config = ctx.getConfiguration();
        assertTrue("Incorrect Configuration. Expected " + DefaultConfiguration.DEFAULT_NAME + " but found " +
            config.getName(), DefaultConfiguration.DEFAULT_NAME.equals(config.getName()));
    }

    @Test
    public void testByName() throws Exception {
        LoggerContext ctx = Configurator.initialize("-config", null, (String) null);
        Logger logger = LogManager.getLogger("org.apache.test.TestConfigurator");
        Configuration config = ctx.getConfiguration();
        assertNotNull("No configuration", config);
        assertTrue("Incorrect Configuration. Expected " + CONFIG_NAME + " but found " + config.getName(),
            CONFIG_NAME.equals(config.getName()));
        Map<String, Appender> map = config.getAppenders();
        assertNotNull("No Appenders", map != null && map.size() > 0);
        assertTrue("Wrong configuration", map.containsKey("List"));
        Configurator.shutdown(ctx);
        config = ctx.getConfiguration();
        assertTrue("Incorrect Configuration. Expected " + DefaultConfiguration.DEFAULT_NAME + " but found " +
            config.getName(), DefaultConfiguration.DEFAULT_NAME.equals(config.getName()));
    }

    @Test
    public void testReconfiguration() throws Exception {
        File file = new File("target/test-classes/log4j2-config.xml");
        file.setLastModified(System.currentTimeMillis() - 120000);
        LoggerContext ctx = Configurator.initialize("Test1", null, "target/test-classes/log4j2-config.xml");
        Logger logger = LogManager.getLogger("org.apache.test.TestConfigurator");
        Configuration config = ctx.getConfiguration();
        assertNotNull("No configuration", config);
        assertTrue("Incorrect Configuration. Expected " + CONFIG_NAME + " but found " + config.getName(),
            CONFIG_NAME.equals(config.getName()));
        Map<String, Appender> map = config.getAppenders();
        assertNotNull("No Appenders", map != null && map.size() > 0);
        assertTrue("Wrong configuration", map.containsKey("List"));

        Thread.sleep(500);
        file.setLastModified(System.currentTimeMillis());
        for (int i = 0; i < 17; ++i) {
            logger.debug("Test message " + i);
        }
        Configuration newConfig = ctx.getConfiguration();
        assertTrue("Configuration not reset", newConfig != config);
        Configurator.shutdown(ctx);
        config = ctx.getConfiguration();
        assertTrue("Incorrect Configuration. Expected " + DefaultConfiguration.DEFAULT_NAME + " but found " +
            config.getName(), DefaultConfiguration.DEFAULT_NAME.equals(config.getName()));
    }

    @Test
    public void testNoLoggers() throws Exception {
        LoggerContext ctx = Configurator.initialize("Test1", null, "bad/log4j-loggers.xml");
        Logger logger = LogManager.getLogger("org.apache.test.TestConfigurator");
        Configuration config = ctx.getConfiguration();
        assertNotNull("No configuration", config);
        assertTrue("Unexpected configuration", DefaultConfiguration.DEFAULT_NAME.equals(config.getName()));
    }

    @Test
    public void testBadStatus() throws Exception {
        LoggerContext ctx = Configurator.initialize("Test1", null, "bad/log4j-status.xml");
        Logger logger = LogManager.getLogger("org.apache.test.TestConfigurator");
        Configuration config = ctx.getConfiguration();
        assertNotNull("No configuration", config);
        assertTrue("Unexpected configuration", "XMLConfigTest".equals(config.getName()));
        LoggerConfig root = config.getLoggerConfig("");
        assertNotNull("No Root Logger", root);
        assertTrue("Expected error level, was " + root.getLevel(), Level.ERROR == root.getLevel());
    }

    @Test
    public void testBadFilterParam() throws Exception {
        LoggerContext ctx = Configurator.initialize("Test1", null, "bad/log4j-badfilterparam.xml");
        Logger logger = LogManager.getLogger("org.apache.test.TestConfigurator");
        Configuration config = ctx.getConfiguration();
        assertNotNull("No configuration", config);
        assertTrue("Unexpected configuration", "XMLConfigTest".equals(config.getName()));
        LoggerConfig lcfg = config.getLoggerConfig("org.apache.logging.log4j.test1");
        assertNotNull("No Logger", lcfg);
        Filter filter = lcfg.getFilter();
        assertNull("Unexpected Filter", filter);
    }

    @Test
    public void testNoFilters() throws Exception {
        LoggerContext ctx = Configurator.initialize("Test1", null, "bad/log4j-nofilter.xml");
        Logger logger = LogManager.getLogger("org.apache.test.TestConfigurator");
        Configuration config = ctx.getConfiguration();
        assertNotNull("No configuration", config);
        assertTrue("Unexpected configuration", "XMLConfigTest".equals(config.getName()));
        LoggerConfig lcfg = config.getLoggerConfig("org.apache.logging.log4j.test1");
        assertNotNull("No Logger", lcfg);
        Filter filter = lcfg.getFilter();
        assertNotNull("No Filter", filter);
        assertTrue("Incorrect filter", filter instanceof CompositeFilter);
        assertFalse("Unexpected filters", ((CompositeFilter) filter).hasFilters());
    }

    @Test
    public void testBadLayout() throws Exception {
        LoggerContext ctx = Configurator.initialize("Test1", null, "bad/log4j-badlayout.xml");
        Logger logger = LogManager.getLogger("org.apache.test.TestConfigurator");
        Configuration config = ctx.getConfiguration();
        assertNotNull("No configuration", config);
        assertTrue("Unexpected configuration", "XMLConfigTest".equals(config.getName()));
    }

    @Test
    public void testBadFileName() throws Exception {
        String value = FILESEP.equals("/") ? "/rootdir/test.log" : "1:/target/bad:file.log";
        System.setProperty("testfile", value);
        LoggerContext ctx = Configurator.initialize("Test1", null, "bad/log4j-badfilename.xml");
        Logger logger = LogManager.getLogger("org.apache.test.TestConfigurator");
        Configuration config = ctx.getConfiguration();
        assertNotNull("No configuration", config);
        assertTrue("Unexpected configuration", "XMLConfigTest".equals(config.getName()));
        assertTrue("Create bad appender", config.getAppenders().size() == 2);
    }

}
