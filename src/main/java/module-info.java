/*
 * Copyright (C) 2019-2023 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */


/**
 * FB mini module.
 */
module de.powerstat.fb.mini
 {
  exports de.powerstat.fb.mini;

  requires transitive java.xml;

  requires org.apache.logging.log4j;
  requires transitive de.powerstat.validation;

  requires transitive org.apache.httpcomponents.httpclient;
  requires org.apache.httpcomponents.httpcore;
  requires org.apache.commons.codec;

 }
