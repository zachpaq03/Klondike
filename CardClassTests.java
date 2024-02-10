package cs3500.klondike;

import org.junit.Test;

import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.CardClass;
import cs3500.klondike.model.hw02.Suit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the class CardClass.
 */
public class CardClassTests {

  @Test
  public void toStringWorks() {
    Card card = new CardClass(10, Suit.club);
    assertEquals("10♣", card.toString());
  }

  @Test
  public void equalsTesting() {
    Card card1 = new CardClass(12, Suit.diamond);
    Card card2 = new CardClass(12, Suit.diamond);
    assertTrue(card1.equals(card2));
  }

  @Test
  public void sameHashCode() {
    Card card1 = new CardClass(12, Suit.diamond);
    Card card2 = new CardClass(12, Suit.diamond);
    assertEquals(card1.hashCode(), card2.hashCode());
  }
}
