package cs3500.klondike;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.view.KlondikeTextualView;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the klondike textual view class.
 */
public class ViewTest {

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

  @Test
  public void toStringWorksBeginning() {
    BasicKlondike game = new BasicKlondike();
    KlondikeTextualView view = new KlondikeTextualView(game);
    List<Card> cards = game.getDeck();
    List<Card> acesAndTwo = new ArrayList<>();
    for (Card card : cards) {
      if (getRank(card) == 1) {
        acesAndTwo.add(card);
      }
    }
    for (Card card : cards) {
      if (getRank(card) == 2) {
        acesAndTwo.add(card);
      }
    }
    game.startGame(acesAndTwo, false, 3, 2);
    String expected = new String("Draw: 2♡, 2♠\n" +
            "Foundation: <none>, <none>, <none>, <none>\n" +
            " A♣  ?  ?\n" +
            "    A♠  ?\n" +
            "       2♢");

    assertEquals(expected, view.toString());
  }

  @Test
  public void toStringWorksCardsInFound() {
    BasicKlondike game2 = new BasicKlondike();
    KlondikeTextualView view2 = new KlondikeTextualView(game2);
    List<Card> cards = game2.getDeck();
    List<Card> acesAndTwo = new ArrayList<>();
    for (Card card : cards) {
      if (getRank(card) == 1) {
        acesAndTwo.add(card);
      }
    }
    for (Card card : cards) {
      if (getRank(card) == 2) {
        acesAndTwo.add(card);
      }
    }
    game2.startGame(acesAndTwo, false, 3, 2);
    game2.moveToFoundation(0, 0);
    game2.moveToFoundation(1, 1);
    String expected = new String("Draw: 2♡, 2♠\n" +
            "Foundation: A♣, A♠, <none>, <none>\n" +
            "  X A♢  ?\n" +
            "        ?\n" +
            "       2♢");

    assertEquals(expected, view2.toString());
  }

  @Test
  public void toStringWorksNonFullDraw() {
    BasicKlondike game3 = new BasicKlondike();
    KlondikeTextualView view3 = new KlondikeTextualView(game3);
    List<Card> cards = game3.getDeck();
    List<Card> acesAndTwo = new ArrayList<>();
    for (Card card : cards) {
      if (getRank(card) == 1) {
        acesAndTwo.add(card);
      }
    }
    for (Card card : cards) {
      if (getRank(card) == 2) {
        acesAndTwo.add(card);
      }
    }
    game3.startGame(acesAndTwo, false, 3, 2);
    game3.moveToFoundation(0, 0);
    game3.moveToFoundation(1, 1);
    game3.discardDraw();
    game3.moveDrawToFoundation(1);
    String expected = new String("Draw: 2♡\n" +
            "Foundation: A♣, 2♠, <none>, <none>\n" +
            "  X A♢  ?\n" +
            "        ?\n" +
            "       2♢");

    assertEquals(expected, view3.toString());
  }
}
