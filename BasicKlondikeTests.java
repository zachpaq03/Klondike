package cs3500.klondike;

import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.KlondikeModel;

/**
 * Tests for basic klondike extending the Examplar model tests.
 */
public class BasicKlondikeTests extends ExamplarModelTests {
  protected KlondikeModel factory() {
    return new BasicKlondike();
  }
}
