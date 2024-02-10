package cs3500.klondike;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw04.LimitedDrawKlondike;
import cs3500.klondike.model.hw04.WhiteheadKlondike;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Tests for examplar for the model with the new game modes, limited draw
 * and whitehead.
 */
public class ExamplarExtendedModelTests {

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
  public void cannotDiscardMore() {
    KlondikeModel game = new LimitedDrawKlondike(1);
    List<Card> cards = game.getDeck();
    List<Card> acesOnly = new ArrayList<>();
    for (Card card : cards) {
      if (getRank(card) == 1) {
        acesOnly.add(card);
      }
    }
    game.startGame(acesOnly, false, 2, 1);
    assertEquals(1, game.getDrawCards().size());
    game.discardDraw();
    game.discardDraw();
    assertTrue(game.getDrawCards().isEmpty());
  }

  @Test
  public void negativeNumTimesRedrawAllowed() {
    assertThrows(IllegalArgumentException.class, () -> new LimitedDrawKlondike(-1));
  }

  @Test
  public void validRankDifColor() {
    KlondikeModel game = new WhiteheadKlondike();
    List<Card> cards = game.getDeck();
    List<Card> aceToThree = new ArrayList<>();
    for (Card card : cards) {
      if (getRank(card) == 1) {
        aceToThree.add(card);
      }
    }
    for (Card card : cards) {
      if (getRank(card) == 2) {
        aceToThree.add(card);
      }
    }
    for (Card card : cards) {
      if (getRank(card) == 3) {
        aceToThree.add(card);
      }
    }
    game.startGame(aceToThree, false, 4, 1);
    if (Objects.equals(game.getCardAt(1, 1).toString(), "2♣")) {
      assertThrows(IllegalStateException.class, () -> game.movePile(1, 1, 3));
    }
  }

  @Test
  public void anyCardInEmptyCascade() {
    KlondikeModel game = new WhiteheadKlondike();
    List<Card> cards = game.getDeck();
    List<Card> aceToThree = new ArrayList<>();
    for (Card card : cards) {
      if (getRank(card) == 1) {
        aceToThree.add(card);
      }
    }
    for (Card card : cards) {
      if (getRank(card) == 2) {
        aceToThree.add(card);
      }
    }
    for (Card card : cards) {
      if (getRank(card) == 3) {
        aceToThree.add(card);
      }
    }
    game.startGame(aceToThree, false, 4, 1);
    game.moveToFoundation(0, 0);
    game.moveDraw(0);
    assertEquals(1, game.getPileHeight(0));
  }

  @Test
  public void onlySameSuitMoveTogether() {
    KlondikeModel game = new WhiteheadKlondike();
    List<Card> cards = game.getDeck();
    List<Card> aceToFour = new ArrayList<>();
    for (Card card : cards) {
      if (getRank(card) == 1) {
        aceToFour.add(card);
      }
    }
    for (Card card : cards) {
      if (getRank(card) == 2) {
        aceToFour.add(card);
      }
    }
    for (Card card : cards) {
      if (getRank(card) == 3) {
        aceToFour.add(card);
      }
    }
    for (Card card : cards) {
      if (getRank(card) == 4) {
        aceToFour.add(card);
      }
    }
    game.startGame(aceToFour, false, 5, 1);
    if (Objects.equals(game.getCardAt(2, 2).toString(), "3♢")) {
      game.movePile(2, 1, 4);
      game.moveToFoundation(0, 0);
      assertThrows(IllegalStateException.class, () -> game.movePile(4, 2, 0));
    }
  }

}
