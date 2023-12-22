package me.skillissue.permissionsystem.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class StringUtilsTest {
  private final StringUtils stringUtils = new StringUtils();

  @Test
  void parseTimeReturnsCorrectMillisecondsForSingleUnit() {
    long result = StringUtils.parseTime("1s");
    assertEquals(1000, result);
  }

  @Test
  void parseTimeReturnsCorrectMillisecondsForMultipleUnits() {
    long result = StringUtils.parseTime("1d1M1s");
    assertEquals(2592000000L + 86400000 + 1000, result);
  }

  @Test
  void parseTimeReturnsZeroForEmptyString() {
    long result = StringUtils.parseTime("");
    assertEquals(0, result);
  }

  @Test
  void parseTimeReturnsZeroForInvalidUnit() {
    long result = StringUtils.parseTime("1x");
    assertEquals(0, result);
  }

  @Test
  void parseTimeReturnsCorrectMillisecondsForMixedOrderUnits() {
    long result = StringUtils.parseTime("1s1d1M");
    assertEquals(2592000000L + 86400000 + 1000, result);
  }

  @Test
  void parseTimeReturnsCorrectMillisecondsForLargeNumberOfDays() {
    long result = StringUtils.parseTime("350d");
    assertEquals(86400000L * 350, result);
  }

  @Test
  void formatMessageReturnsSameMessageWhenNoPlaceholderPresent() {
    String result = StringUtils.formatMessage("Hello, World!");
    assertEquals("Hello, World!", result);
  }

  @Test
  void formatMessageReplacesPlaceholderWithConfigMessage() {
    Config.getInstance().messages.put("test", "Test Message");
    String result = StringUtils.formatMessage("Hello, %test!");
    assertEquals("Hello, Test Message!", result);
  }

  @Test
  void formatMessageReplacesMultiplePlaceholdersWithConfigMessages() {
    Config.getInstance().messages.put("test", "Test Message");
    Config.getInstance().messages.put("example", "Example Message");
    String result = StringUtils.formatMessage("Hello, %test! This is an %example.");
    assertEquals("Hello, Test Message! This is an Example Message.", result);
  }

  @Test
  void formatMessageReturnsSameMessageWhenPlaceholderNotInConfig() {
    String result = StringUtils.formatMessage("Hello, %notInConfig!");
    assertEquals("Hello, %notInConfig!", result);
  }

  @Test
  void formatMessageReplacesNestedPlaceholdersWithConfigMessages() {
    Config.getInstance().messages.put("test", "Test Message");
    Config.getInstance().messages.put("nested", "%test");
    String result = StringUtils.formatMessage("Hello, %nested!");
    assertEquals("Hello, Test Message!", result);
  }

  @Test
  void formatTimeReturnsZeroSecondsForZeroTime() {
    String result = StringUtils.formatTime(0, 1);
    assertEquals("Lifetime", result);
  }

  @Test
  void formatTimeReturnsCorrectFormatForSingleUnit() {
    String result = StringUtils.formatTime(System.currentTimeMillis() + 1000, 1);
    assertEquals("1s", result);
  }

  @Test
  void formatTimeReturnsCorrectFormatForMultipleUnits() {
    String result =
        StringUtils.formatTime(System.currentTimeMillis() + 2592000000L + 86400000 + 1000, 3);
    assertEquals("1M1d1s", result);
  }

  @Test
  void formatTimeReturnsCorrectFormatForLargeNumberOfDays() {
    String result = StringUtils.formatTime(System.currentTimeMillis() + 86400000L * 350, 1);
    assertEquals("11M", result);
  }

  @Test
  void formatTimeReturnsCorrectFormatForMixedOrderUnits() {
    String result =
        StringUtils.formatTime(System.currentTimeMillis() + 2592000000L + 86400000 + 1000, 3);
    assertEquals("1M1d1s", result);
  }

  @Test
  void formatTimeReturnsCorrectFormatForAccuracyLimit() {
    String result =
        StringUtils.formatTime(System.currentTimeMillis() + 2592000000L + 86400000 + 1000, 2);
    assertEquals("1M1d", result);
  }

  @BeforeAll
  static void setup() {
    new Config();
  }
}
