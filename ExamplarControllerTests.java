package cs3500.klondike;

import cs3500.klondike.controller.KlondikeController;
import cs3500.klondike.controller.KlondikeTextualController;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw02.Card;

import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for examplar for the klondike controller.
 */
public abstract class ExamplarControllerTests {

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
  public void quittingWorks() throws IOException {
    KlondikeModel game = factory();

    Readable in = new StringReader("q");
    Appendable out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    List<Card> cards = game.getDeck();
    controller.playGame(game, cards, false, 7, 1);
    assertTrue(out.toString().contains("Game quit!"));
  }

  @Test
  public void extraInputMPF() throws IOException {
    KlondikeModel game = factory();

    Readable in = new StringReader("mpf 1 1 3 mpf 2 2 mpf 2 3 mdf 4");
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
    assertEquals(4, game.getScore());
    assertTrue(game.isGameOver());
  }

  @Test
  public void invalidMove() throws IOException {
    KlondikeModel game = factory();

    Readable in = new StringReader("mpp 1 1 2 q");
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
  public void nonIntInInput() throws IOException {
    KlondikeModel game = factory();

    Readable in = new StringReader("mpf 1 notInt 1 q");
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
  public void invalidInputThenFixed() throws IOException {
    KlondikeModel game = factory();

    Readable in = new StringReader("mpp 2 sdlkfjk 1 4 q");
    Appendable out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    List<Card> cards = game.getDeck();
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
    controller.playGame(game, acesToThree, false, 4, 1);
    assertFalse(out.toString().contains("Invalid move"));
  }


  @Test
  public void invalidMoveDraw() throws IOException {
    KlondikeModel game = factory();

    Readable in = new StringReader("md 1 q");
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
  public void notEnoughInputs() throws IOException {
    KlondikeModel game = factory();

    Readable in = new StringReader("mpp 2 1 mpf 1 1 q");
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
    assertTrue(out.toString().contains("quit"));
  }

  @Test
  public void quitGameState() throws IOException {
    KlondikeModel game = factory();

    Readable in = new StringReader("mpf 1 1 mpf 2 2 q");
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
    String[] lines = out.toString().split("\n");
    assertTrue(lines[lines.length - 2].contains("X"));
  }

  @Test
  public void doNothingGameState() throws IOException {
    KlondikeModel game = factory();

    Readable in = new StringReader("q");
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
    String[] lines = out.toString().split("\n");
    assertTrue(lines[lines.length - 2].contains("A"));
  }

  @Test
  public void properFormatting() throws IOException {
    KlondikeModel game = factory();

    Readable in = new StringReader("q");
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
    String[] lines = out.toString().split("\n");
    assertTrue(lines[lines.length - 1].contains("Score"));
  }

  @Test
  public void changeMindMidMove() throws IOException {
    KlondikeModel game = factory();

    Readable in = new StringReader("mpf 1 asfb mpp 2 1 1 q");
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
    assertTrue(out.toString().contains("Score: 1"));
  }

  @Test
  public void stateOfGameWhenQuitInToString() throws IOException {
    KlondikeModel game = factory();

    Readable in = new StringReader("mpf 1 asfb mpf 2 1 q");
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
    assertTrue(out.toString().contains("State of game when quit:"));
  }

  @Test
  public void canQuitMidMove() throws IOException {
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
  public void validMoveDrawToCascade() throws IOException {
    KlondikeModel game = factory();

    Readable in = new StringReader("md 2 q");
    Appendable out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    List<Card> cards = game.getDeck();
    List<Card> acesToThree = new ArrayList<>();
    for (Card card : cards) {
      if (getRank(card) == 1) {
        acesToThree.add(card);
      }
    }
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
    controller.playGame(game, acesToThree, false, 4, 1);
    assertEquals(game.getPileHeight(1), 3);
  }
}