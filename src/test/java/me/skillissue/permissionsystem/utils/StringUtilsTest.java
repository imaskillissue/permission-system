package me.skillissue.permissionsystem.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import me.skillissue.permissionsystem.utils.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class StringUtilsTest {
  private final StringUtils stringUtils = new StringUtils();

  @Test
  void parseTimeReturnsCorrectMillisecondsForSingleUnit() {
    long result = stringUtils.parseTime("1s");
    assertEquals(1000, result);
  }

  @Test
  void parseTimeReturnsCorrectMillisecondsForMultipleUnits() {
    long result = stringUtils.parseTime("1d1M1s");
    assertEquals(2592000000L + 86400000 + 1000, result);
  }

  @Test
  void parseTimeReturnsZeroForEmptyString() {
    long result = stringUtils.parseTime("");
    assertEquals(0, result);
  }

  @Test
  void parseTimeReturnsZeroForInvalidUnit() {
    long result = stringUtils.parseTime("1x");
    assertEquals(0, result);
  }

  @Test
  void parseTimeReturnsCorrectMillisecondsForMixedOrderUnits() {
    long result = stringUtils.parseTime("1s1d1M");
    assertEquals(2592000000L + 86400000 + 1000, result);
  }

  @Test
  void parseTimeReturnsCorrectMillisecondsForLargeNumberOfDays() {
    long result = stringUtils.parseTime("350d");
    assertEquals(86400000L * 350, result);
  }

  @Test
  void formatMessageReturnsSameMessageWhenNoPlaceholderPresent() {
    String result = stringUtils.formatMessage("Hello, World!");
    assertEquals("Hello, World!", result);
  }

  @Test
  void formatMessageReplacesPlaceholderWithConfigMessage() {
    Config.getInstance().messages.put("test", "Test Message");
    String result = stringUtils.formatMessage("Hello, %test!");
    assertEquals("Hello, Test Message!", result);
  }

  @Test
  void formatMessageReplacesMultiplePlaceholdersWithConfigMessages() {
    Config.getInstance().messages.put("test", "Test Message");
    Config.getInstance().messages.put("example", "Example Message");
    String result = stringUtils.formatMessage("Hello, %test! This is an %example.");
    assertEquals("Hello, Test Message! This is an Example Message.", result);
  }

  @Test
  void formatMessageReturnsSameMessageWhenPlaceholderNotInConfig() {
    String result = stringUtils.formatMessage("Hello, %notInConfig!");
    assertEquals("Hello, %notInConfig!", result);
  }

  @Test
  void formatMessageReplacesNestedPlaceholdersWithConfigMessages() {
    Config.getInstance().messages.put("test", "Test Message");
    Config.getInstance().messages.put("nested", "%test");
    String result = stringUtils.formatMessage("Hello, %nested!");
    assertEquals("Hello, Test Message!", result);
  }
  @Test
void formatTimeReturnsZeroSecondsForZeroTime() {
  String result = stringUtils.formatTime(0, 1);
  assertEquals("Lifetime", result);
}

@Test
void formatTimeReturnsCorrectFormatForSingleUnit() {
  String result = stringUtils.formatTime(1000, 1);
  assertEquals("1s", result);
}

@Test
void formatTimeReturnsCorrectFormatForMultipleUnits() {
  String result = stringUtils.formatTime(2592000000L + 86400000 + 1000, 3);
  assertEquals("1M1d1s", result);
}

@Test
void formatTimeReturnsCorrectFormatForLargeNumberOfDays() {
  String result = stringUtils.formatTime(86400000L * 350, 1);
  assertEquals("11M", result);
}

@Test
void formatTimeReturnsCorrectFormatForMixedOrderUnits() {
  String result = stringUtils.formatTime(2592000000L + 86400000 + 1000, 3);
  assertEquals("1M1d1s", result);
}

@Test
void formatTimeReturnsCorrectFormatForAccuracyLimit() {
  String result = stringUtils.formatTime(2592000000L + 86400000 + 1000, 2);
  assertEquals("1M1d", result);
}

  @BeforeAll
  static void setup() {
    new Config();
  }
}
