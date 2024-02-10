package cs3500.klondike;

import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.Color;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw02.Suit;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the examplar part of this assignment.
 */
public abstract class ExamplarModelTests {
  protected abstract KlondikeModel factory();

  KlondikeModel game = factory();
  List<Card> cards = factory().getDeck();

  /**
   * Checks if a moving a card on top of the top card in a cascade pile is legal.
   *
   * @param other the card at the top of the cascade pile.
   * @return if this move is legal.
   */
  private boolean legalMoveCascadePile(Card card, Card other) {
    return getRank(card) + 1 == getRank(other) && getColor(card) != getColor(other);
  }

  private boolean legalMoveFoundationPile(Card card, Card foundationCard) {
    if (foundationCard == null && getRank(card) == 1) {
      return true;
    }
    return getRank(card) == getRank(foundationCard) + 1
            && getSuit(card) == getSuit(foundationCard);
  }

  private int getRank(Card card) {
    if (card.toString().charAt(0) == 'A') {
      return 1;
    } else if (card.toString().charAt(0) == '1' && card.toString().charAt(1) == '0') {
      return 10;
    } else if (card.toString().charAt(0) == 'J') {
      return 11;
    } else if (card.toString().charAt(0) == 'Q') {
      return 12;
    } else if (card.toString().charAt(0) == 'K') {
      return 13;
    } else if (card.toString().charAt(0) == '2') {
      return 2;
    } else if (card.toString().charAt(0) == '3') {
      return 3;
    } else {
      return (int) card.toString().charAt(0);
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
  public void movePileThrowsForInvalidMove() {
    game.startGame(cards, false, 7, 1);
    if (!legalMoveCascadePile(game.getCardAt(0, 0),
            game.getCardAt(3, 3))) {
      assertThrows(IllegalStateException.class, () -> game.movePile(0, 1, 3));
    }
  }

  @Test
  public void movePileMoreCardsThanExist() {

    game.startGame(cards, false, 7, 1);
    assertThrows(IllegalArgumentException.class, () -> game.movePile(0, 2, 3));
  }

  @Test
  public void moveToFoundationCantCheat() {
    game.startGame(cards, false, 7, 1);
    //   Card mover = game.getCardAt(0, 0);
    //   Card foundie = game.getCardAt(0);
    //   if (!legalMoveFoundationPile(mover, foundie)) {
    assertThrows(IllegalStateException.class, () -> game.moveToFoundation(5, 0));
    //   }
  }


  @Test
  public void moveFromDrawToFoundationCanNotCheat() {
    game.startGame(cards, false, 7, 1);
    if (game.getDrawCards().get(0).toString().charAt(0) == 'A') {
      game.discardDraw();
    }
    assertThrows(IllegalStateException.class, () -> game.moveDrawToFoundation(1));
  }

  @Test
  public void moveNonAceToFoundation() {
    game.startGame(cards, false, 7, 1);
    for (int i = 0; i < 7; i++) {
      if (game.getCardAt(i, i).toString().charAt(0) != 'A') {
        int finalI = i;
        assertThrows(IllegalStateException.class, () -> game.moveToFoundation(finalI, 0));
      }
    }
  }


  @Test
  public void moveToSamePile() {
    game.startGame(cards, false, 7, 1);
    assertThrows(IllegalArgumentException.class, () -> game.movePile(3, 1, 3));
  }


  @Test
  public void twoDecks() {
    List<Card> moreCards = game.getDeck();
    moreCards.addAll(cards);
    game.startGame(moreCards, false, 12, 1);
    assertEquals(8, game.getNumFoundations());
  }

  @Test
  public void foundationDifferentSuits() {
    game = new BasicKlondike();
    int aceIndex = 0;
    int twoIndex = 0;
    for (int i = 0; i < cards.size(); i++) {
      if (getRank(cards.get(i)) == 1) {
        aceIndex = i;
        break;
      }
    }

    for (int j = 0; j < cards.size(); j++) {
      if (getRank(cards.get(j)) == 2 && getSuit(cards.get(j)) != getSuit(cards.get(aceIndex))) {
        twoIndex = j;
        break;
      }
    }

    Card temp = cards.get(28);
    cards.set(28, cards.get(aceIndex));
    cards.set(aceIndex, temp);
    Card temp2 = cards.get(29);
    cards.set(29, cards.get(twoIndex));
    cards.set(twoIndex, temp2);

    game.startGame(cards, false, 7, 1);
    int count = 0;

    game.moveDrawToFoundation(0);
    assertThrows(IllegalStateException.class, () -> game.moveDrawToFoundation(0));
  }

  @Test
  public void destPileIsEmpty() {
    game.startGame(cards, false, 7, 1);
    if (getRank(game.getCardAt(0, 0)) == 1) {
      game.moveToFoundation(0, 0);
      if (getRank(game.getCardAt(1, 1)) != 13) {
        assertThrows(IllegalStateException.class, () -> game.movePile(1, 1, 0));
      } else {
        game.movePile(1, 1, 0);
      }
    }
  }

  @Test
  public void noCardsInDraw() {
    List<Card> acesOnly = new ArrayList<>();
    for (Card card : cards) {
      if (getRank(card) == 1) {
        acesOnly.add(card);
      }
    }
    game.startGame(acesOnly, false, 2, 1);
    game.moveDrawToFoundation(0);
    assertThrows(IllegalStateException.class, () -> game.moveDraw(0));

  }

  @Test
  public void moveToFoundationInvalid() {
    List<Card> acesOnly = new ArrayList<>();
    for (Card card : cards) {
      if (getRank(card) == 1) {
        acesOnly.add(card);
      }
    }
    game.startGame(acesOnly, false, 2, 1);
    assertThrows(IllegalArgumentException.class, () -> game.moveToFoundation(0, 4));
  }

  @Test
  public void moveToFoundationWithEmptyPile() {
    List<Card> acesOnly = new ArrayList<>();
    for (Card card : cards) {
      if (getRank(card) == 1) {
        acesOnly.add(card);
      }
    }
    game.startGame(acesOnly, false, 2, 1);
    game.moveToFoundation(0, 0);
    assertThrows(IllegalStateException.class, () -> game.moveToFoundation(0, 1));
  }

  @Test
  public void moveDrawToFoundInvalidPile() {
    List<Card> acesOnly = new ArrayList<>();
    for (Card card : cards) {
      if (getRank(card) == 1) {
        acesOnly.add(card);
      }
    }
    game.startGame(acesOnly, false, 2, 1);
    assertThrows(IllegalArgumentException.class, () -> game.moveDrawToFoundation(4));
  }

  @Test
  public void getScoreAdds() {
    List<Card> acesOnly = new ArrayList<>();
    for (Card card : cards) {
      if (getRank(card) == 1) {
        acesOnly.add(card);
      }
    }
    game.startGame(acesOnly, false, 2, 1);
    game.moveToFoundation(0, 0);
    game.moveDrawToFoundation(1);
    assertEquals(2, game.getScore());
  }

  @Test
  public void isGameOverWorks() {
    List<Card> acesOnly = new ArrayList<>();
    for (Card card : cards) {
      if (getRank(card) == 1) {
        acesOnly.add(card);
      }
    }
    game.startGame(acesOnly, false, 2, 1);
    game.moveToFoundation(0, 0);
    game.moveDrawToFoundation(1);
    game.moveToFoundation(1, 2);
    game.moveToFoundation(1, 3);
    assertTrue(game.isGameOver());
  }

  @Test
  public void drawToPileNotAllowed() {
    List<Card> acesOnly = new ArrayList<>();
    for (Card card : cards) {
      if (getRank(card) == 1) {
        acesOnly.add(card);
      }
    }
    game.startGame(acesOnly, false, 2, 1);
    assertThrows(IllegalStateException.class, () -> game.moveDraw(0));
  }


  @Test
  public void getNumRowsChanges() {
    List<Card> acesOnly = new ArrayList<>();
    for (Card card : cards) {
      if (getRank(card) == 1) {
        acesOnly.add(card);
      }
    }
    game.startGame(acesOnly, false, 2, 1);
    game.moveToFoundation(1, 0);
    assertEquals(1, game.getNumRows());
  }

  @Test
  public void highestRankBaseOfFoundationPileWhenNoKing() {
    List<Card> acesOnly = new ArrayList<>();
    for (Card card : cards) {
      if (getRank(card) == 1) {
        acesOnly.add(card);
      }
    }
    game.startGame(acesOnly, false, 2, 1);
    game.moveToFoundation(0, 0);
    assertThrows(IllegalArgumentException.class, () -> game.getCardAt(0, 0));
    game.moveDraw(0);
  }

  @Test
  public void moveToFoundInvalidSrc() {
    List<Card> acesOnly = new ArrayList<>();
    for (Card card : cards) {
      if (getRank(card) == 1) {
        acesOnly.add(card);
      }
    }
    game.startGame(acesOnly, false, 2, 10);
    assertThrows(IllegalArgumentException.class, () -> game.moveToFoundation(2, 0));
  }

  @Test
  public void invalidMoveInCascade() {
    game.startGame(cards, false, 6, 3);
    assertThrows(IllegalStateException.class, () -> game.movePile(1, 1, 0));
  }

  @Test
  public void moveDrawToCascadeButSameColor() {
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

    game.startGame(acesAndTwo, false, 1, 1);
    Color twoColor = getColor(game.getCardAt(0, 0));
    boolean tester = false;
    while (!tester) {
      if (getRank(game.getDrawCards().get(0)) != 1
              || getColor(game.getDrawCards().get(0)) != twoColor) {
        game.discardDraw();
      } else {
        tester = true;
      }
    }
    assertThrows(IllegalStateException.class, () -> game.moveDraw(0));
  }

  @Test
  public void countUpdatesPlus() {
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

    game.startGame(acesAndTwo, false, 1, 1);
    Color twoColor = getColor(game.getCardAt(0, 0));
    boolean tester = false;
    while (!tester) {
      if (getRank(game.getDrawCards().get(0)) != 1
              || getColor(game.getDrawCards().get(0)) == twoColor) {
        game.discardDraw();
      } else {
        tester = true;
      }
    }
    game.moveDraw(0);
    assertEquals(2, game.getPileHeight(0));
  }

  @Test
  public void canMoveMultipleCards() {
    List<Card> acesToThree = new ArrayList<>();
    for (Card card : cards) {
      if (getRank(card) == 3) {
        acesToThree.add(card);
      }
    }

    for (Card card : cards) {
      if (getRank(card) == 2) {
        acesToThree.add(card);
      }
    }

    for (Card card : cards) {
      if (getRank(card) == 1) {
        acesToThree.add(card);
      }
    }

    assertEquals(12, acesToThree.size());

    game.startGame(acesToThree, false, 3, 1);
    Color twoColor = getColor(game.getCardAt(2, 2));
    boolean tester = false;
    while (!tester) {
      if (getRank(game.getDrawCards().get(0)) != 1
              || getColor(game.getDrawCards().get(0)) == twoColor) {
        game.discardDraw();
      } else {
        tester = true;
      }
    }
    game.moveDraw(2);
    if (getColor(game.getCardAt(0, 0)) != twoColor) {
      game.movePile(2, 2, 0);
    } else {
      game.movePile(2, 2, 1);
    }
  }


  @Test
  public void discardCyclesThrough() {
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

    assertEquals(8, acesAndTwo.size());

    game.startGame(acesAndTwo, false, 1, 1);
    boolean step1 = false;
    Suit aceSuit = null;
    while (!step1) {
      if (getRank(game.getDrawCards().get(0)) != 1) {
        game.discardDraw();
      } else {
        game.moveDrawToFoundation(0);
        aceSuit = getSuit(game.getCardAt(0));
        step1 = true;
      }
    }
    boolean step2 = false;
    while (!step2) {
      if (getRank(game.getDrawCards().get(0)) != 2
              || getSuit(game.getDrawCards().get(0)) == aceSuit) {
        game.discardDraw();
      } else {
        step2 = true;
      }
    }
  }

  @Test
  public void foundationSameSuitWrongNumber() {
    List<Card> acesToThree = new ArrayList<>();
    for (Card card : cards) {
      if (getRank(card) == 1) {
        acesToThree.add(card);
      }
    }

    for (Card card : cards) {
      if (getRank(card) == 2) {
        acesToThree.add(card);
      }
    }

    for (Card card : cards) {
      if (getRank(card) == 3) {
        acesToThree.add(card);
      }
    }

    game.startGame(acesToThree, false, 1, 1);
    game.moveToFoundation(0, 0);
    Suit suit = getSuit(game.getCardAt(0));
    boolean findThree = false;
    while (!findThree) {
      if (getRank(game.getDrawCards().get(0)) != 3
              || getSuit(game.getDrawCards().get(0)) != suit) {
        game.discardDraw();
      } else {
        findThree = true;
      }
    }
    assertThrows(IllegalStateException.class, () -> game.moveDrawToFoundation(0));
  }

  @Test
  public void nonAceInFoundation() {
    game.startGame(cards, false, 7, 1);
    boolean nonAce = false;
    while (!nonAce) {
      if (getRank(game.getDrawCards().get(0)) == 1) {
        game.discardDraw();
      } else {
        nonAce = true;
      }
    }
    assertThrows(IllegalStateException.class, () -> game.moveDrawToFoundation(0));
  }

  @Test
  public void unexpectedMutability() {
    game.startGame(cards, false, 7, 1);
    Card original = game.getCardAt(6, 6);
    try {
      game.moveToFoundation(6, 0);
    } catch (IllegalStateException e) {
      assertEquals(original, game.getCardAt(6, 6));
    }
  }
}
