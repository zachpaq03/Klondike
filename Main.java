package cs3500.klondike;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cs3500.klondike.controller.KlondikeTextualController;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw04.KlondikeAbstract;
import cs3500.klondike.model.hw04.WhiteheadKlondike;

/**
 * A main class so that the program can be run.
 */
public class Main {

  private static int getRank(Card card) {
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

  /**
   * A main method so that the program can be run.
   *
   * @param args an input array.
   * @throws IOException if it is unable to append.
   */
  public static void main(String[] args) throws IOException {
    KlondikeAbstract game = new WhiteheadKlondike();
    Readable rd = new InputStreamReader(System.in);
    Appendable ap = System.out;
    KlondikeTextualController controller = new KlondikeTextualController(rd, ap);
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
    controller.playGame(game, cards, true, 7, 1);
  }
}
