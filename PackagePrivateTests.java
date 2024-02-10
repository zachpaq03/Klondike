package cs3500.klondike.model.hw04;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.CardClass;
import cs3500.klondike.model.hw02.Color;
import cs3500.klondike.model.hw02.Suit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Tests for package private methods.
 */
public class PackagePrivateTests {
  BasicKlondike game = new BasicKlondike();
  List<Card> cards = game.getDeck();


  @Test
  public void shuffleCardsWorksNormal() {
    game.shuffleCards(cards);
    assertEquals(52, cards.size());
    for (int i = 1; i <= 13; i++) {
      for (Suit suit : Suit.values()) {
        assertTrue(cards.contains(new CardClass(i, suit)));
      }
    }
  }

  @Test
  public void shuffleCardsWorksFunky() {
    List<Card> empty = new ArrayList<>();
    game.shuffleCards(empty);
    assertEquals(0, empty.size());
    empty.add(new CardClass(1, Suit.heart));
    game.shuffleCards(empty);
    assertEquals(new CardClass(1, Suit.heart), empty.get(0));
  }

  @Test
  public void dealCardsWorks() {
    List<Card> before = new ArrayList<>(cards);
    game.startGame(cards, false, 7, 1);
    assertEquals(before, cards);
    for (int i = 6; i > -1; i--) {
      assertEquals(i + 1, game.getPileHeight(i));
    }
    assertEquals(cards.get(0), game.getCardAt(0, 0));
    assertEquals(cards.get(7), game.getCardAt(1, 1));
    assertEquals(cards.get(28), game.getDrawCards().get(0));
    for (int i = 0; i < game.getNumPiles(); i++) {
      assertTrue(game.isCardVisible(i, i));
    }
    assertFalse(game.isCardVisible(1, 0));
  }

  @Test
  public void getRankWorks() {
    for (int i = 1; i <= 13; i++) {
      assertEquals(i, game.getRank(cards.get((i - 1) * 4)));
    }
  }

  @Test
  public void getSuitWorks() {
    assertEquals(Suit.club, game.getSuit(cards.get(0)));
    assertEquals(Suit.diamond, game.getSuit(cards.get(1)));
    assertEquals(Suit.heart, game.getSuit(cards.get(2)));
    assertEquals(Suit.spade, game.getSuit(cards.get(3)));
  }

  @Test
  public void validDeckWorks() {
    List<Card> tester1 = new ArrayList<>(cards);
    tester1.remove(51);
    assertFalse(game.validDeck(tester1));
    List<Card> tester2 = new ArrayList<>(cards);
    tester2.remove(51);
    tester2.add(new CardClass(13, Suit.club));
    assertFalse(game.validDeck(tester2));
    List<Card> tester3 = new ArrayList<>(cards);
    List<Card> removers3 = new ArrayList<>();
    for (Card card : tester3) {
      if (game.getRank(card) == 5) {
        removers3.add(card);
      }
    }
    tester3.removeAll(removers3);
    assertFalse(game.validDeck(tester3));
    List<Card> tester4 = new ArrayList<>(cards);
    List<Card> removers4 = new ArrayList<>();
    for (Card card : tester4) {
      if (game.getRank(card) > 10) {
        removers4.add(card);
      }
    }
    tester4.removeAll(removers4);
    assertTrue(game.validDeck(tester4));
    List<Card> tester5 = new ArrayList<>(cards);
    List<Card> removers5 = new ArrayList<>();
    for (Card card : tester5) {
      if (game.getSuit(card) == Suit.club) {
        removers5.add(card);
      }
    }
    tester5.removeAll(removers5);
    assertTrue(game.validDeck(tester5));
  }

  @Test
  public void canCascadeBeDealtWorks() {
    assertThrows(IllegalArgumentException.class, () -> game.startGame(cards, false, 100, 1));
    assertThrows(IllegalArgumentException.class, () -> game.startGame(cards, false, 10, 1));
    List<Card> empty = new ArrayList<>();
    assertThrows(IllegalArgumentException.class, () -> game.startGame(empty, false, 1, 1));
    game.startGame(cards, false, 9, 1); // the max amount of piles allowed works
  }

  @Test
  public void numAcesWorks() {
    assertEquals(4, game.numAces(cards));
    List<Card> empty = new ArrayList<>();
    assertEquals(0, game.numAces(empty));
    List<Card> twoDeck = new ArrayList<>(cards);
    twoDeck.addAll(cards);
    assertEquals(8, game.numAces(twoDeck));
  }

  @Test
  public void getHighestRankWorks() {
    assertEquals(13, game.getHighestRank(cards));
    List<Card> tester = new ArrayList<>(cards);
    List<Card> removers = new ArrayList<>();
    for (Card card : cards) {
      if (game.getRank(card) > 10) {
        removers.add(card);
      }
    }
    tester.removeAll(removers);
    assertEquals(10, game.getHighestRank(tester));
    List<Card> empty = new ArrayList<>();
    assertEquals(0, game.getHighestRank(empty));
  }

  @Test
  public void setUpMethodsWork() {
    game.startGame(cards, false, 7, 1);
    assertEquals(7, game.getNumRows());
    assertEquals(4, game.getNumFoundations());
  }

  @Test
  public void getColorWorks() {
    Assert.assertEquals(Color.red, game.getColor(cards.get(1)));
    assertEquals(Color.red, game.getColor(cards.get(2)));
    assertEquals(Color.black, game.getColor(cards.get(0)));
    assertEquals(Color.black, game.getColor(cards.get(3)));
  }

  // Not testing legal move methods because examplar tests already do that.
  // Not testing gameNotStarted because it is already being tested in the Klondike
  // ModelTests file.
}
