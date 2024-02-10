package cs3500.klondike;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.CardClass;
import cs3500.klondike.model.hw02.Color;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw02.Suit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Test for the klondike model.
 */
public abstract class KlondikeModelTests {

  protected abstract KlondikeModel factory();

  KlondikeModel game = factory();
  List<Card> cards = game.getDeck();

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

  private Color getColor(Card card) {
    if (getRank(card) == 10) {
      if (card.toString().charAt(2) == '♣' || card.toString().charAt(2) == '♠') {
        return Color.black;
      } else {
        return Color.red;
      }
    } else {
      if (card.toString().charAt(1) == '♣' || card.toString().charAt(1) == '♠') {
        return Color.black;
      } else {
        return Color.red;
      }
    }
  }

  private Suit getSuit(Card card) {
    char suitChar = card.toString().charAt(card.toString().length() - 1);

    if (suitChar == '♣') {
      return Suit.club;
    } else if (suitChar == '♢') {
      return Suit.diamond;
    } else if (suitChar == '♡') {
      return Suit.heart;
    } else if (suitChar == '♠') {
      return Suit.spade;
    } else {
      throw new IllegalArgumentException("Not a suit");
    }
  }

  @Test
  public void testStartGameThrowsExceptionsNullList() {
    assertThrows(IllegalArgumentException.class, () -> game.startGame(null, true, 7, 1));
  }

  @Test
  public void testStartGameThrowsExceptionGameAlreadyStarted() {
    List<Card> cards = new ArrayList<Card>();
    assertThrows(IllegalArgumentException.class, () -> game.startGame(cards, false, 7, 1));
    assertThrows(IllegalArgumentException.class, () -> game.startGame(cards, true, 4, 2));
  }

  @Test
  public void movePileThrows() {
    assertThrows(IllegalStateException.class, () -> game.movePile(2, 3, 4));
    game.startGame(cards, true, 7, 1);
    assertThrows(IllegalArgumentException.class, () -> game.movePile(9, 1, 2));
    assertThrows(IllegalArgumentException.class, () -> game.movePile(1, 20, 2));
  }

  @Test
  public void gameIsOverWhenExpected() {
    assertThrows(IllegalStateException.class, () -> game.isGameOver());
    game.startGame(cards, true, 7, 1);
    assertFalse(game.isGameOver());
  }

  @Test
  public void isCardVisThrows() {
    assertThrows(IllegalStateException.class, () -> game.isCardVisible(3, 2));
    game.startGame(cards, false, 7, 1);
    assertThrows(IllegalArgumentException.class, () -> game.isCardVisible(10, 2));
    assertThrows(IllegalArgumentException.class, () -> game.isCardVisible(3, 33));
  }

  @Test
  public void getNumDrawTestsThrows() {
    assertThrows(IllegalStateException.class, () -> game.getNumDraw());
  }

  @Test
  public void getScoreThrows() {
    assertThrows(IllegalStateException.class, () -> game.getScore());
  }

  @Test
  public void getScoreWorksForNull() {
    game.startGame(cards, false, 7, 1);
    assertEquals(0, game.getScore());
  }

  @Test
  public void moveDrawGameNoStart() {
    assertThrows(IllegalStateException.class, () -> game.moveDraw(3));
  }

  @Test
  public void moveDrawInvalidPile() {
    game.startGame(cards, false, 7, 1);
    assertThrows(IllegalArgumentException.class, () -> game.moveDraw(9));
  }

  @Test
  public void cardIsVisAfterOneMoved() {
    List<Card> acesOnly = new ArrayList<>();
    for (Card card : cards) {
      if (getRank(card) == 1) {
        acesOnly.add(card);
      }
    }
    game.startGame(acesOnly, false, 2, 1);
    assertFalse(game.isCardVisible(1, 0));
    game.moveToFoundation(1, 0);
    assertTrue(game.isCardVisible(1, 0));
  }

  @Test
  public void getCardAtThrows() {
    List<Card> acesOnly = new ArrayList<>();
    for (Card card : cards) {
      if (getRank(card) == 1) {
        acesOnly.add(card);
      }
    }
    game.startGame(acesOnly, false, 2, 1);
    assertThrows(IllegalArgumentException.class, () -> game.getCardAt(1, 2));
    assertThrows(IllegalArgumentException.class, () -> game.getCardAt(3, 0));
    assertThrows(IllegalArgumentException.class, () -> game.getCardAt(4));
  }

  @Test
  public void movePileDoesNotWorkSameColor() {
    game.startGame(cards, false, 7, 1);
    int baseRank = getRank(game.getCardAt(6, 6));
    Color baseColor = getColor(game.getCardAt(6, 6));
    int pile = 6;
    if (baseRank == 1) {
      baseRank = getRank(game.getCardAt(5, 5));
      baseColor = getColor(game.getCardAt(5, 5));
      pile = 5;
    }
    while (getRank(game.getDrawCards().get(0)) != baseRank - 1
            && getColor(game.getDrawCards().get(0)) != baseColor) {
      game.discardDraw();
    }
    int finalPile = pile;
    assertThrows(IllegalStateException.class, () -> game.moveDraw(finalPile));
  }

  @Test
  public void gameHasNotStartedYet() {
    assertThrows(IllegalStateException.class, () -> game.moveDraw(0));
    assertThrows(IllegalStateException.class, () -> game.movePile(0, 1, 1));
    assertThrows(IllegalStateException.class, () -> game.moveToFoundation(0, 1));
    assertThrows(IllegalStateException.class, () -> game.moveDrawToFoundation(0));
    assertThrows(IllegalStateException.class, () -> game.discardDraw());
    assertThrows(IllegalStateException.class, () -> game.getNumRows());
    assertThrows(IllegalStateException.class, () -> game.getNumPiles());
    assertThrows(IllegalStateException.class, () -> game.getNumDraw());
    assertThrows(IllegalStateException.class, () -> game.isGameOver());
    assertThrows(IllegalStateException.class, () -> game.getScore());
    assertThrows(IllegalStateException.class, () -> game.getPileHeight(0));
    assertThrows(IllegalStateException.class, () -> game.isCardVisible(0, 0));
    assertThrows(IllegalStateException.class, () -> game.getCardAt(0, 0));
    assertThrows(IllegalStateException.class, () -> game.getCardAt(0));
    assertThrows(IllegalStateException.class, () -> game.getDrawCards());
    assertThrows(IllegalStateException.class, () -> game.getNumFoundations());
  }

  @Test
  public void dealCardWorksAsIntended() {
    game.startGame(cards, false, 7, 1);
    assertEquals(new CardClass(8, Suit.club), game.getDrawCards().get(0));
  }

  @Test
  public void getDeckWorks() {
    assertEquals(1, getRank(cards.get(0)));
    assertEquals(2, getRank(cards.get(6)));
    assertEquals(3, getRank(cards.get(11)));
    assertEquals(4, getRank(cards.get(13)));
    assertEquals(5, getRank(cards.get(18)));
    assertEquals(6, getRank(cards.get(21)));
    assertEquals(7, getRank(cards.get(26)));
    assertEquals(8, getRank(cards.get(30)));
    assertEquals(9, getRank(cards.get(35)));
    assertEquals(10, getRank(cards.get(39)));
    assertEquals(11, getRank(cards.get(42)));
    assertEquals(12, getRank(cards.get(47)));
    assertEquals(13, getRank(cards.get(51)));
  }

  @Test
  public void sameColorDifSuit() {
    List<Card> acesAndTwo = new ArrayList<>();
    for (Card card : cards) {
      if (getRank(card) == 2) {
        acesAndTwo.add(card);
      }
    }
    for (Card card : cards) {
      if (getRank(card) == 1) {
        acesAndTwo.add(card);
      }
    }
    game.startGame(acesAndTwo, false, 3, 1);
    assertThrows(IllegalStateException.class, () -> game.movePile(1, 1, 2));
  }

  @Test
  public void throwsIAELessThanOneCascadePileOrDrawCards() {
    assertThrows(IllegalArgumentException.class, () -> game.startGame(cards, false, 0, 1));
    assertThrows(IllegalArgumentException.class, () -> game.startGame(cards, false, -3, 1));
    assertThrows(IllegalArgumentException.class, () -> game.startGame(cards, false, 7, 0));
    assertThrows(IllegalArgumentException.class, () -> game.startGame(cards, false, 7, -4));
  }
}
