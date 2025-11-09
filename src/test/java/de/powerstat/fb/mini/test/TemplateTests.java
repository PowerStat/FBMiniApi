/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini.test;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import nl.jqno.equalsverifier.*;

import de.powerstat.fb.mini.AIN;
import de.powerstat.fb.mini.ApplyMask;
import de.powerstat.fb.mini.Functions;
import de.powerstat.fb.mini.Metadata;
import de.powerstat.fb.mini.ScenarioType;
import de.powerstat.fb.mini.Temperature;
import de.powerstat.fb.mini.Template;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * Template tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR"})
final class TemplateTests
 {
  /**
   * Default constructor.
   */
  /* default */ TemplateTests()
   {
    super();
   }


  /**
   * Test correct Template.
   */
  @Test
  /* default */ void testTemplateCorrect1()
   {
    final AIN identifier = AIN.of("000000000000");
    final long id = 0;
    final EnumSet<Functions> functionbitmask = null;
    final boolean autocreate = false;
    final long applymaskField = 0;
    final String name = "test";
    final Metadata metadata = Metadata.of(0, ScenarioType.UNDEFINED);
    final List<AIN> devices = null;
    final List<AIN> triggers = null;
    final List<AIN> subtemplates = null;
    final EnumSet<ApplyMask> applymask = null;
    final Template cleanTemplate = Template.of(identifier, id, functionbitmask, autocreate, applymaskField, name, metadata, devices, triggers, subtemplates, applymask);
    assertEquals("000000000000", cleanTemplate.stringValue(), "Template not as expected"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test correct Template.
   */
  @Test
  /* default */ void testTemplateCorrect2()
   {
    final AIN identifier = AIN.of("000000000000");
    final long id = 0;
    final EnumSet<Functions> functionbitmask = EnumSet.allOf(Functions.class);
    final boolean autocreate = false;
    final long applymaskField = 0;
    final String name = "test";
    final Metadata metadata = Metadata.of(-1, ScenarioType.COMING);
    final List<AIN> devices = new ArrayList<>();
    final List<AIN> triggers = new ArrayList<>();
    final List<AIN> subtemplates = new ArrayList<>();
    final EnumSet<ApplyMask> applymask = EnumSet.allOf(ApplyMask.class);
    final Template cleanTemplate = Template.of(identifier, id, functionbitmask, autocreate, applymaskField, name, metadata, devices, triggers, subtemplates, applymask);
    assertEquals("000000000000", cleanTemplate.stringValue(), "Template not as expected"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test Template with wrong template name lengths.
   *
   * @param name Template name
   */
  @ParameterizedTest
  @ValueSource(strings = {"1", "1234567890123456789012345678901234567890"})
  /* default */ void testTemplateNameLength1(final String name)
   {
    final AIN identifier = AIN.of("000000000000");
    final long id = 0;
    final EnumSet<Functions> functionbitmask = null;
    final boolean autocreate = false;
    final long applymaskField = 0;
    final Metadata metadata = Metadata.of(-1, ScenarioType.COMING);
    final List<AIN> devices = null;
    final List<AIN> triggers = null;
    final List<AIN> subtemplates = null;
    final EnumSet<ApplyMask> applymask = null;
    final Template cleanTemplate = Template.of(identifier, id, functionbitmask, autocreate, applymaskField, name, metadata, devices, triggers, subtemplates, applymask);
    assertEquals("000000000000", cleanTemplate.stringValue(), "Template not as expected"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test Template with wrong template name lengths.
   *
   * @param name Template name
   */
  @ParameterizedTest
  @ValueSource(strings = {"", "12345678901234567890123456789012345678901"})
  /* default */ void testTemplateNameLength2(final String name)
   {
    final AIN identifier = AIN.of("000000000000");
    final long id = 0;
    final EnumSet<Functions> functionbitmask = null;
    final boolean autocreate = false;
    final long applymaskField = 0;
    final Metadata metadata = Metadata.of(-1, ScenarioType.COMING);
    final List<AIN> devices = null;
    final List<AIN> triggers = null;
    final List<AIN> subtemplates = null;
    final EnumSet<ApplyMask> applymask = null;
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final Template cleanTemplate = */ Template.of(identifier, id, functionbitmask, autocreate, applymaskField, name, metadata, devices, triggers, subtemplates, applymask);
     }, "Illegal argument exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test wrong Template.
   */
  @Test
  /* default */ void testTemplateWrong1()
   {
    final AIN identifier = null;
    final long id = 0;
    final EnumSet<Functions> functionbitmask = EnumSet.noneOf(Functions.class);
    final boolean autocreate = false;
    final long applymaskField = 0;
    final String name = "test";
    final Metadata metadata = Metadata.of(-1, ScenarioType.COMING);
    final List<AIN> devices = new ArrayList<>();
    final List<AIN> triggers = new ArrayList<>();
    final List<AIN> subtemplates = new ArrayList<>();
    final EnumSet<ApplyMask> applymask = EnumSet.noneOf(ApplyMask.class);
    assertThrows(NullPointerException.class, () ->
     {
      /* final Template cleanTemplate = */ Template.of(identifier, id, functionbitmask, autocreate, applymaskField, name, metadata, devices, triggers, subtemplates, applymask);
     }, "Null pointer exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test wrong Template.
   */
  @Test
  /* default */ void testTemplateWrong2()
   {
    final AIN identifier = AIN.of("000000000000");
    final long id = -1;
    final EnumSet<Functions> functionbitmask = EnumSet.noneOf(Functions.class);
    final boolean autocreate = false;
    final long applymaskField = 0;
    final String name = "test";
    final Metadata metadata = Metadata.of(-1, ScenarioType.COMING);
    final List<AIN> devices = new ArrayList<>();
    final List<AIN> triggers = new ArrayList<>();
    final List<AIN> subtemplates = new ArrayList<>();
    final EnumSet<ApplyMask> applymask = EnumSet.noneOf(ApplyMask.class);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final Template cleanTemplate = */ Template.of(identifier, id, functionbitmask, autocreate, applymaskField, name, metadata, devices, triggers, subtemplates, applymask);
     }, "Illegal argument exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test wrong Template.
   */
  @Test
  /* default */ void testTemplateWrong3()
   {
    final AIN identifier = AIN.of("000000000000");
    final long id = 0;
    final EnumSet<Functions> functionbitmask = EnumSet.noneOf(Functions.class);
    final boolean autocreate = false;
    final long applymaskField = -1;
    final String name = "test";
    final Metadata metadata = Metadata.of(-1, ScenarioType.COMING);
    final List<AIN> devices = new ArrayList<>();
    final List<AIN> triggers = new ArrayList<>();
    final List<AIN> subtemplates = new ArrayList<>();
    final EnumSet<ApplyMask> applymask = EnumSet.noneOf(ApplyMask.class);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final Template cleanTemplate = */ Template.of(identifier, id, functionbitmask, autocreate, applymaskField, name, metadata, devices, triggers, subtemplates, applymask);
     }, "Illegal argument exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test wrong Template.
   */
  @Test
  /* default */ void testTemplateWrong4()
   {
    final AIN identifier = AIN.of("000000000000");
    final long id = 0;
    final EnumSet<Functions> functionbitmask = EnumSet.noneOf(Functions.class);
    final boolean autocreate = false;
    final long applymaskField = 0;
    final String name = null;
    final Metadata metadata = Metadata.of(-1, ScenarioType.COMING);
    final List<AIN> devices = new ArrayList<>();
    final List<AIN> triggers = new ArrayList<>();
    final List<AIN> subtemplates = new ArrayList<>();
    final EnumSet<ApplyMask> applymask = EnumSet.noneOf(ApplyMask.class);
    assertThrows(NullPointerException.class, () ->
     {
      /* final Template cleanTemplate = */ Template.of(identifier, id, functionbitmask, autocreate, applymaskField, name, metadata, devices, triggers, subtemplates, applymask);
     }, "Null pointer exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test wrong Template.
   */
  @Test
  /* default */ void testTemplateWrong5()
   {
    final AIN identifier = AIN.of("000000000000");
    final long id = 0;
    final EnumSet<Functions> functionbitmask = EnumSet.noneOf(Functions.class);
    final boolean autocreate = false;
    final long applymaskField = 0;
    final String name = "test";
    final Metadata metadata = null;
    final List<AIN> devices = new ArrayList<>();
    final List<AIN> triggers = new ArrayList<>();
    final List<AIN> subtemplates = new ArrayList<>();
    final EnumSet<ApplyMask> applymask = EnumSet.noneOf(ApplyMask.class);
    assertThrows(NullPointerException.class, () ->
     {
      /* final Template cleanTemplate = */ Template.of(identifier, id, functionbitmask, autocreate, applymaskField, name, metadata, devices, triggers, subtemplates, applymask);
     }, "Null pointer exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test stringValue.
   */
  @Test
  /* default */ void testStringValue()
   {
    final AIN identifier = AIN.of("000000000000");
    final long id = 0;
    final EnumSet<Functions> functionbitmask = null;
    final boolean autocreate = false;
    final long applymaskField = 0;
    final String name = "test";
    final Metadata metadata = Metadata.of(-1, ScenarioType.COMING);
    final List<AIN> devices = null;
    final List<AIN> triggers = null;
    final List<AIN> subtemplates = null;
    final EnumSet<ApplyMask> applymask = null;
    final Template template = Template.of(identifier, id, functionbitmask, autocreate, applymaskField, name, metadata, devices, triggers, subtemplates, applymask);
    assertEquals("000000000000", template.stringValue(), "Template not as expected"); //$NON-NLS-1$
   }


  /**
   * Test getIdentifier.
   */
  @Test
  /* default */ void testGetIdentifier()
   {
    final AIN identifier = AIN.of("000000000000");
    final long id = 0;
    final EnumSet<Functions> functionbitmask = null;
    final boolean autocreate = false;
    final long applymaskField = 0;
    final String name = "test";
    final Metadata metadata = Metadata.of(-1, ScenarioType.COMING);
    final List<AIN> devices = null;
    final List<AIN> triggers = null;
    final List<AIN> subtemplates = null;
    final EnumSet<ApplyMask> applymask = null;
    final Template template = Template.of(identifier, id, functionbitmask, autocreate, applymaskField, name, metadata, devices, triggers, subtemplates, applymask);
    assertEquals("000000000000", template.getIdentifier().stringValue(), "Identifier not as expected"); //$NON-NLS-1$
   }


  /**
   * Test getId.
   */
  @Test
  /* default */ void testGetId()
   {
    final AIN identifier = AIN.of("000000000000");
    final long id = 1;
    final EnumSet<Functions> functionbitmask = null;
    final boolean autocreate = false;
    final long applymaskField = 0;
    final String name = "test";
    final Metadata metadata = Metadata.of(-1, ScenarioType.COMING);
    final List<AIN> devices = null;
    final List<AIN> triggers = null;
    final List<AIN> subtemplates = null;
    final EnumSet<ApplyMask> applymask = null;
    final Template template = Template.of(identifier, id, functionbitmask, autocreate, applymaskField, name, metadata, devices, triggers, subtemplates, applymask);
    assertEquals(1L, template.getId(), "Id not as expected"); //$NON-NLS-1$
   }


  /**
   * Test getFunctionbitmask.
   */
  @Test
  /* default */ void testGetFunctionbitmask()
   {
    final AIN identifier = AIN.of("000000000000");
    final long id = 0;
    final EnumSet<Functions> functionbitmask = null;
    final boolean autocreate = false;
    final long applymaskField = 0;
    final String name = "test";
    final Metadata metadata = Metadata.of(-1, ScenarioType.COMING);
    final List<AIN> devices = null;
    final List<AIN> triggers = null;
    final List<AIN> subtemplates = null;
    final EnumSet<ApplyMask> applymask = null;
    final Template template = Template.of(identifier, id, functionbitmask, autocreate, applymaskField, name, metadata, devices, triggers, subtemplates, applymask);
    assertEquals(EnumSet.noneOf(Functions.class), template.getFunctionbitmask(), "Functionbitmask not as expected"); //$NON-NLS-1$
   }


  /**
   * Test getAutocreate.
   */
  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  /* default */ void testGetAutocreate(boolean autocreate)
   {
    final AIN identifier = AIN.of("000000000000");
    final long id = 0;
    final EnumSet<Functions> functionbitmask = null;
    final long applymaskField = 0;
    final String name = "test";
    final Metadata metadata = Metadata.of(-1, ScenarioType.COMING);
    final List<AIN> devices = null;
    final List<AIN> triggers = null;
    final List<AIN> subtemplates = null;
    final EnumSet<ApplyMask> applymask = null;
    final Template template = Template.of(identifier, id, functionbitmask, autocreate, applymaskField, name, metadata, devices, triggers, subtemplates, applymask);
    assertEquals(autocreate, template.getAutocreate(), "createauto not as expected"); //$NON-NLS-1$
   }


  /**
   * Test getApplymaskField.
   */
  @Test
  /* default */ void testGetApplymaskField()
   {
    final AIN identifier = AIN.of("000000000000");
    final long id = 0;
    final EnumSet<Functions> functionbitmask = null;
    final boolean autocreate = false;
    final long applymaskField = 1;
    final String name = "test";
    final Metadata metadata = Metadata.of(-1, ScenarioType.COMING);
    final List<AIN> devices = null;
    final List<AIN> triggers = null;
    final List<AIN> subtemplates = null;
    final EnumSet<ApplyMask> applymask = null;
    final Template template = Template.of(identifier, id, functionbitmask, autocreate, applymaskField, name, metadata, devices, triggers, subtemplates, applymask);
    assertEquals(1, template.getApplymaskField(), "ApplymaskField not as expected"); //$NON-NLS-1$
   }


  /**
   * Test getName.
   */
  @Test
  /* default */ void testGetName()
   {
    final AIN identifier = AIN.of("000000000000");
    final long id = 0;
    final EnumSet<Functions> functionbitmask = null;
    final boolean autocreate = false;
    final long applymaskField = 0;
    final String name = "test";
    final Metadata metadata = Metadata.of(-1, ScenarioType.COMING);
    final List<AIN> devices = null;
    final List<AIN> triggers = null;
    final List<AIN> subtemplates = null;
    final EnumSet<ApplyMask> applymask = null;
    final Template template = Template.of(identifier, id, functionbitmask, autocreate, applymaskField, name, metadata, devices, triggers, subtemplates, applymask);
    assertEquals("test", template.getName(), "Name not as expected"); //$NON-NLS-1$
   }


  /**
   * Test getMetadata.
   */
  @Test
  /* default */ void testGetMetadata()
   {
    final AIN identifier = AIN.of("000000000000");
    final long id = 0;
    final EnumSet<Functions> functionbitmask = null;
    final boolean autocreate = false;
    final long applymaskField = 0;
    final String name = "test";
    final Metadata metadata = Metadata.of(-1, ScenarioType.COMING);
    final List<AIN> devices = null;
    final List<AIN> triggers = null;
    final List<AIN> subtemplates = null;
    final EnumSet<ApplyMask> applymask = null;
    final Template template = Template.of(identifier, id, functionbitmask, autocreate, applymaskField, name, metadata, devices, triggers, subtemplates, applymask);
    assertEquals(metadata, template.getMetadata(), "Metadata not as expected"); //$NON-NLS-1$
   }


  /**
   * Test getDevices.
   */
  @Test
  /* default */ void testGetDevices()
   {
    final AIN identifier = AIN.of("000000000000");
    final long id = 0;
    final EnumSet<Functions> functionbitmask = null;
    final boolean autocreate = false;
    final long applymaskField = 0;
    final String name = "test";
    final Metadata metadata = Metadata.of(-1, ScenarioType.COMING);
    final List<AIN> devices = new ArrayList<>();
    final AIN identifier2 = AIN.of("000000000001");
    devices.add(identifier2);
    final List<AIN> triggers = null;
    final List<AIN> subtemplates = null;
    final EnumSet<ApplyMask> applymask = null;
    final Template template = Template.of(identifier, id, functionbitmask, autocreate, applymaskField, name, metadata, devices, triggers, subtemplates, applymask);
    assertTrue(template.getDevices().contains(identifier2), "Devices not as expected"); //$NON-NLS-1$
   }


  /**
   * Test getTriggers.
   */
  @Test
  /* default */ void testGetTriggers()
   {
    final AIN identifier = AIN.of("000000000000");
    final long id = 0;
    final EnumSet<Functions> functionbitmask = null;
    final boolean autocreate = false;
    final long applymaskField = 0;
    final String name = "test";
    final Metadata metadata = Metadata.of(-1, ScenarioType.COMING);
    final List<AIN> devices = null;
    final List<AIN> triggers = new ArrayList<>();
    final AIN identifier2 = AIN.of("000000000001");
    triggers.add(identifier2);
    final List<AIN> subtemplates = null;
    final EnumSet<ApplyMask> applymask = null;
    final Template template = Template.of(identifier, id, functionbitmask, autocreate, applymaskField, name, metadata, devices, triggers, subtemplates, applymask);
    assertTrue(template.getTriggers().contains(identifier2), "Triggers not as expected"); //$NON-NLS-1$
   }


  /**
   * Test getSubtemplates.
   */
  @Test
  /* default */ void testGetSubtemplates()
   {
    final AIN identifier = AIN.of("000000000000");
    final long id = 0;
    final EnumSet<Functions> functionbitmask = null;
    final boolean autocreate = false;
    final long applymaskField = 0;
    final String name = "test";
    final Metadata metadata = Metadata.of(-1, ScenarioType.COMING);
    final List<AIN> devices = null;
    final List<AIN> triggers = null;
    final List<AIN> subtemplates = new ArrayList<>();
    final AIN identifier2 = AIN.of("000000000001");
    subtemplates.add(identifier2);
    final EnumSet<ApplyMask> applymask = null;
    final Template template = Template.of(identifier, id, functionbitmask, autocreate, applymaskField, name, metadata, devices, triggers, subtemplates, applymask);
    assertTrue(template.getSubtemplates().contains(identifier2), "Subtemplates not as expected"); //$NON-NLS-1$
   }


  /**
   * Test getApplymask.
   */
  @Test
  /* default */ void testGetApplymask()
   {
    final AIN identifier = AIN.of("000000000000");
    final long id = 0;
    final EnumSet<Functions> functionbitmask = null;
    final boolean autocreate = false;
    final long applymaskField = 0;
    final String name = "test";
    final Metadata metadata = Metadata.of(-1, ScenarioType.COMING);
    final List<AIN> devices = null;
    final List<AIN> triggers = null;
    final List<AIN> subtemplates = null;
    final EnumSet<ApplyMask> applymask = null;
    final Template template = Template.of(identifier, id, functionbitmask, autocreate, applymaskField, name, metadata, devices, triggers, subtemplates, applymask);
    assertEquals(EnumSet.noneOf(ApplyMask.class), template.getApplymask(), "Functionbitmask not as expected"); //$NON-NLS-1$
   }


  /**
   * Equalsverifier.
   */
  @Test
  public void equalsContract()
   {
    EqualsVerifier.forClass(Template.class).withNonnullFields("identifier").verify();
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final AIN identifier = AIN.of("000000000000");
    final long id = 0;
    final EnumSet<Functions> functionbitmask = null;
    final boolean autocreate = false;
    final long applymaskField = 0;
    final String name = "test";
    final Metadata metadata = Metadata.of(-1, ScenarioType.COMING);
    final List<AIN> devices = null;
    final List<AIN> triggers = null;
    final List<AIN> subtemplates = null;
    final EnumSet<ApplyMask> applymask = null;
    final Template template = Template.of(identifier, id, functionbitmask, autocreate, applymaskField, name, metadata, devices, triggers, subtemplates, applymask);
    assertEquals("Template[identifier=AIN[ain=000000000000], id=0, functionbitmask=[], autocreate=false, applymaskField=0, name=test, metadata=Metadata[icon=-1, type=COMING], devices=[], triggers=[], subtemplates=[], applymask=[]]", template.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo()
   {
    final AIN identifier = AIN.of("000000000000");
    final long id = 0;
    final EnumSet<Functions> functionbitmask = null;
    final boolean autocreate = false;
    final long applymaskField = 0;
    final String name = "test";
    final Metadata metadata = Metadata.of(-1, ScenarioType.COMING);
    final List<AIN> devices = null;
    final List<AIN> triggers = null;
    final List<AIN> subtemplates = null;
    final EnumSet<ApplyMask> applymask = null;

    final Template template1 = Template.of(identifier, id, functionbitmask, autocreate, applymaskField, name, metadata, devices, triggers, subtemplates, applymask);
    final Template template2 = Template.of(identifier, id, functionbitmask, autocreate, applymaskField, name, metadata, devices, triggers, subtemplates, applymask);
    final Template template3 = Template.of(AIN.of("000000000001"), id, functionbitmask, autocreate, applymaskField, name, metadata, devices, triggers, subtemplates, applymask); //$NON-NLS-1$
    final Template template4 = Template.of(AIN.of("000000000002"), id, functionbitmask, autocreate, applymaskField, name, metadata, devices, triggers, subtemplates, applymask); //$NON-NLS-1$
    final Template template5 = Template.of(identifier, id, functionbitmask, autocreate, applymaskField, name, metadata, devices, triggers, subtemplates, applymask);
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(template1.compareTo(template2) == -template2.compareTo(template1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(template1.compareTo(template3) == -template3.compareTo(template1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((template4.compareTo(template3) > 0) && (template3.compareTo(template1) > 0) && (template4.compareTo(template1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((template1.compareTo(template2) == 0) && (Math.abs(template1.compareTo(template5)) == Math.abs(template2.compareTo(template5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((template1.compareTo(template2) == 0) && template1.equals(template2), "equals") //$NON-NLS-1$
    );
   }

 }
