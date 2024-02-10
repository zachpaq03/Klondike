package cs3500.klondike;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw04.LimitedDrawKlondike;
import cs3500.klondike.model.hw04.WhiteheadKlondike;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the new game modes added, limited draw and whitehead.
 * Testing things that are different from the basic version of the game.
 */
public class HW4Tests {

  private int getRank(Card card) {
    char firstChar = card.toString().charAt(0);

    switch (firstChar) {
      case 'A':
        return 1;
      case '2':
        return 2;
      case '3':
        return 3;
      case '4':
        return 4;
      case '5':
        return 5;
      case '6':
        return 6;
      case '7':
        return 7;
      case '8':
        return 8;
      case '9':
        return 9;
      case '1':
        return 10;
      case 'J':
        return 11;
      case 'Q':
        return 12;
      case 'K':
        return 13;
      default:
        return (int) firstChar;
    }
  }

  @Test
  public void allCardsFaceUp() {
    KlondikeModel game = new WhiteheadKlondike();
    game.startGame(game.getDeck(), false, 7, 1);
    for (int i = 0; i < game.getNumPiles(); i++) {
      for (int j = 0; j <= i; j++) {
        assertTrue(game.isCardVisible(i, j));
      }
    }
  }

  @Test
  public void throwsISEAfterTooManyDD() {
    KlondikeModel game = new LimitedDrawKlondike(0);
    List<Card> cards = game.getDeck();
    List<Card> acesOnly = new ArrayList<>();
    for (Card card : cards) {
      if (getRank(card) == 1) {
        acesOnly.add(card);
      }
    }
    game.startGame(acesOnly, false, 2, 1);
    game.discardDraw();
    assertThrows(IllegalStateException.class, game::discardDraw);
  }
}
