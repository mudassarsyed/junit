package com.browserstack;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class LocalTest extends BrowserStackJUnitTest {

  @Test
  public void test() throws Exception {
    driver.get("http://bs-local.com:45691/check");

    assertTrue(driver.getPageSource().contains("Up and running"));
  }
}
