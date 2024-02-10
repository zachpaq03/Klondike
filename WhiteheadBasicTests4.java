package cs3500.klondike;

import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw04.WhiteheadKlondike;

/**
 * Tests for whitehead klondike extending the controller tests.
 */
public class WhiteheadBasicTests4 extends ControllerTests {
  protected KlondikeModel factory() {
    return new WhiteheadKlondike();
  }
}
