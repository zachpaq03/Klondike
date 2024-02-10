package cs3500.klondike;

import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import cs3500.klondike.controller.KlondikeController;
import cs3500.klondike.controller.KlondikeTextualController;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Tests the implementation of the klondike textual view controller.
 */
public abstract class ControllerTests {

  protected abstract KlondikeModel factory();

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
  public void playGameThrowsIAE() {
    KlondikeModel game = factory();

    Readable in = new StringReader("input");
    Appendable out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    assertThrows(IllegalArgumentException.class, () -> controller.playGame(
            null, game.getDeck(), false, 7, 1));
  }

  @Test
  public void invalidInput() {
    KlondikeModel game = factory();

    Readable in = new StringReader("Invalid Input");
    Appendable out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    List<Card> cards = game.getDeck();
    assertThrows(IllegalStateException.class, () ->  controller.playGame(game, cards, false, 2, 1));
  }

  //*
  @Test
  public void controllerThrowsISE() {
    KlondikeModel game = factory();

    Readable in = new StringReader("input");
    Appendable out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    assertThrows(IllegalStateException.class, () -> controller.playGame(
            game, game.getDeck(), false, 20, 1));
    assertThrows(IllegalStateException.class, () -> controller.playGame(
            game, game.getDeck(), false, -3, 1));
  }

  @Test
  public void gameAlreadyStarted() {
    KlondikeModel game = factory();

    Readable in = new StringReader("q");
    Appendable out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    game.startGame(game.getDeck(), false, 7, 1);
    assertThrows(IllegalStateException.class, () -> controller.playGame(
            game, game.getDeck(), false, 7, 1));
  }

  @Test
  public void inputMultipleWhiteSpaces() throws IOException {
    KlondikeModel game = factory();

    Readable in = new StringReader("mpf 1 1     mpf 2      3 q");
    Appendable out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    List<Card> cards = game.getDeck();
    List<Card> acesOnly = new ArrayList<>();
    for (Card card : cards) {
      if (getRank(card) == 1) {
        acesOnly.add(card);
      }
    }
    controller.playGame(game, acesOnly, false, 2, 1);
    assertEquals(2, game.getScore());
  }

  @Test
  public void upperCaseQuit() throws IOException {
    KlondikeModel game = factory();

    Readable in = new StringReader("Q");
    Appendable out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    List<Card> cards = game.getDeck();
    controller.playGame(game, cards, false, 7, 1);
    assertTrue(out.toString().contains("Game quit!"));
  }

  @Test
  public void winnerMessage() throws IOException {
    KlondikeModel game = factory();

    Readable in = new StringReader("mpf 1 1 mpf 2 2 mpf 2 3 mdf 4");
    Appendable out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    List<Card> cards = game.getDeck();
    List<Card> acesOnly = new ArrayList<>();
    for (Card card : cards) {
      if (getRank(card) == 1) {
        acesOnly.add(card);
      }
    }
    controller.playGame(game, acesOnly, false, 2, 1);
    assertTrue(out.toString().contains("You win!"));
  }

  @Test
  public void scoreWorks() throws IOException {
    KlondikeModel game = factory();

    Readable in = new StringReader("mdf 1 q");
    Appendable out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    List<Card> cards = game.getDeck();
    List<Card> aces = new ArrayList<>();
    for (Card card : cards) {
      if (getRank(card) == 1) {
        aces.add(card);
      }
    }
    controller.playGame(game, aces, false, 2, 1);
    assertTrue(out.toString().contains("Score: 1"));
  }

  @Test
  public void negativeInputs() throws IOException {
    KlondikeModel game = factory();

    Readable in = new StringReader("mpp 2 1 -1 q");
    Appendable out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    List<Card> cards = game.getDeck();
    List<Card> acesAndTwos = new ArrayList<>();
    for (Card card : cards) {
      if (getRank(card) == 1) {
        acesAndTwos.add(card);
      }
    }
    for (Card card : cards) {
      if (getRank(card) == 2) {
        acesAndTwos.add(card);
      }
    }
    controller.playGame(game, acesAndTwos, false, 3, 1);
    assertTrue(out.toString().contains("Invalid move"));
  }

  @Test
  public void newMoveInMiddleOfMove() throws IOException {
    KlondikeModel game = factory();

    Readable in = new StringReader("mpf 1 mpp 1 q");
    Appendable out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    List<Card> cards = game.getDeck();
    List<Card> acesAndTwos = new ArrayList<>();
    for (Card card : cards) {
      if (getRank(card) == 1) {
        acesAndTwos.add(card);
      }
    }
    for (Card card : cards) {
      if (getRank(card) == 2) {
        acesAndTwos.add(card);
      }
    }
    controller.playGame(game, acesAndTwos, false, 3, 1);
    assertFalse(out.toString().contains("Invalid move"));
  }

  @Test
  public void backToBackMoves() throws IOException {
    KlondikeModel game = factory();

    Readable in = new StringReader("mpp mpf 1 1 q");
    Appendable out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    List<Card> cards = game.getDeck();
    List<Card> acesAndTwos = new ArrayList<>();
    for (Card card : cards) {
      if (getRank(card) == 1) {
        acesAndTwos.add(card);
      }
    }
    for (Card card : cards) {
      if (getRank(card) == 2) {
        acesAndTwos.add(card);
      }
    }
    controller.playGame(game, acesAndTwos, false, 3, 1);
    assertTrue(out.toString().contains("Game quit"));
  }

  @Test
  public void illegalInAndOut() {
    Readable in = null;
    Appendable out = new StringBuilder();
    assertThrows(IllegalArgumentException.class, () -> new KlondikeTextualController(in, out));
    Readable in2 = new StringReader("q");
    Appendable out2 = null;
    assertThrows(IllegalArgumentException.class, () -> new KlondikeTextualController(in2, out2));
    Readable in3 = null;
    Appendable out3 = null;
    assertThrows(IllegalArgumentException.class, () -> new KlondikeTextualController(in3, out3));
  }

  @Test
  public void notZeroIndexed() throws IOException {
    KlondikeModel game = factory();

    Readable in = new StringReader("mpf 1 0 q");
    Appendable out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    List<Card> cards = game.getDeck();
    List<Card> acesOnly = new ArrayList<>();
    for (Card card : cards) {
      if (getRank(card) == 1) {
        acesOnly.add(card);
      }
    }
    controller.playGame(game, acesOnly, false, 2, 1);
    assertTrue(out.toString().contains("Invalid move"));
  }

  @Test
  public void multipleConsecutiveInvalidInput() throws IOException {
    KlondikeModel game = factory();

    Readable in = new StringReader("mpf 1 1 asd fhahfj 712 dsfhfa fhajk mpf 2 2 q");
    Appendable out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    List<Card> cards = game.getDeck();
    List<Card> acesOnly = new ArrayList<>();
    for (Card card : cards) {
      if (getRank(card) == 1) {
        acesOnly.add(card);
      }
    }
    controller.playGame(game, acesOnly, false, 2, 1);
    assertTrue(out.toString().contains("Score: 2"));
  }

  @Test
  public void tellsPlayerToReEnter() {
    KlondikeModel game = factory();

    Readable in = new StringReader("mpf 1 safkjfl 1 q");
    Appendable out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    List<Card> cards = game.getDeck();
    List<Card> acesOnly = new ArrayList<>();
    for (Card card : cards) {
      if (getRank(card) == 1) {
        acesOnly.add(card);
      }
    }
    controller.playGame(game, acesOnly, false, 2, 1);
    assertTrue(out.toString().contains("Re-enter"));
  }

}
