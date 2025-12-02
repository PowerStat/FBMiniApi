/*
 * Copyright (C) 2019-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */


/**
 * FB mini module test.
 */
open module de.powerstat.fb.mini
 {
  exports de.powerstat.fb.mini;

  requires transitive java.xml;
  // opens java.xml to org.mockito;

  requires org.apache.logging.log4j;
  requires transitive de.powerstat.validation;

  requires transitive org.apache.httpcomponents.httpclient;
  requires org.apache.httpcomponents.httpcore;
  requires org.apache.commons.codec;
  requires transitive javatuples;
  requires com.google.gson;
  // opens de.powerstat.fb.mini to com.google.gson;
  requires org.checkerframework.checker.qual;
  requires org.jmolecules.ddd;

  requires com.github.spotbugs.annotations;
  requires org.junit.jupiter.api;
  requires org.junit.jupiter.params;
  requires org.junit.platform.launcher;
  requires org.junit.platform.suite.api;
  requires org.mockito;
  requires nl.jqno.equalsverifier;

  // requires org.jmolecules.ddd;
  // requires io.cucumber.java;
  // requires io.cucumber.junit.platform.engine;

 }
