package cs3500.klondike;

import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.KlondikeModel;

/**
 * Tests for basic klondike extending the Examplar controller tests.
 */
public class BasicKlondikeTests3 extends ExamplarControllerTests {
  protected KlondikeModel factory() {
    return new BasicKlondike();
  }
}
