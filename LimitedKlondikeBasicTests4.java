package cs3500.klondike;

import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw04.LimitedDrawKlondike;

/**
 * Tests for limited draw klondike extending the controller tests.
 */
public final class LimitedKlondikeBasicTests4 extends ControllerTests {
  protected KlondikeModel factory() {
    return new LimitedDrawKlondike(2);
  }
}
