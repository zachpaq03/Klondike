package cs3500.klondike;

import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw04.LimitedDrawKlondike;

/**
 * Tests for limited draw klondike extending the model tests.
 */
public final class LimitedKlondikeBasicTests2 extends KlondikeModelTests {
  protected KlondikeModel factory() {
    return new LimitedDrawKlondike(2);
  }
}
