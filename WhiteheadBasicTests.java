package cs3500.klondike;

import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw04.WhiteheadKlondike;

import static org.junit.Assert.assertTrue;

/**
 * Tests for whitehead klondike extending the Examplar model tests.
 */
public class WhiteheadBasicTests extends ExamplarModelTests {
  protected KlondikeModel factory() {
    return new WhiteheadKlondike();
  }

  //The following tests are specific to basic klondike and limited draw,
  //rather than adding this test to those two classes, I decided to just
  //return it in this class.

  @Override
  public void canMoveMultipleCards() {
    assertTrue(true);
  }

  @Override
  public void countUpdatesPlus() {
    assertTrue(true);
  }

  @Override
  public void destPileIsEmpty() {
    assertTrue(true);
  }

  @Override
  public void moveDrawToCascadeButSameColor() {
    assertTrue(true);
  }
}
