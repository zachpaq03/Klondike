package cs3500.klondike;

import java.io.InputStreamReader;

import cs3500.klondike.controller.KlondikeController;
import cs3500.klondike.controller.KlondikeTextualController;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw04.GameType;
import cs3500.klondike.model.hw04.KlondikeCreator;

/**
 * A class to run the main method to play any version of klondike.
 */
public final class Klondike {
  /**
   * The main method to play a game of klondike, game-type specified by
   * command line arguments.
   *
   * @param args The type of game, and optionally the amount of piles and draw cards.
   */
  public static void main(String[] args) {
    int ntrd = 0;
    int numPiles = 7;
    int numDrawCards = 3;
    if (args.length == 0) {
      throw new IllegalArgumentException("No command line arguments given.\n");
    }
    GameType gameType = getGameType(args);
    if (gameType == GameType.Basic || gameType == GameType.Whitehead) {
      if (args.length > 1) {
        try {
          numPiles = Integer.parseInt(args[1]);
        } catch (NumberFormatException ignored) {
        }
      }
      if (args.length > 2) {
        try {
          numDrawCards = Integer.parseInt(args[2]);
        } catch (NumberFormatException ignored) {
        }
      }
    } else {
      // Case when game type is limited draw
      if (args.length < 2) {
        throw new IllegalArgumentException("Must enter number of times cards can be recycled.");
      } else {
        try {
          ntrd = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
          throw new IllegalArgumentException("Number of redraws must be a number.");
        }
        if (args.length > 2) {
          try {
            numPiles = Integer.parseInt(args[2]);
          } catch (NumberFormatException ignored) {
          }
        }
        if (args.length > 3) {
          try {
            numDrawCards = Integer.parseInt(args[3]);
          } catch (NumberFormatException ignored) {
          }
        }
      }
    }
    KlondikeModel game = KlondikeCreator.create(gameType, ntrd);
    Readable rd = new InputStreamReader(System.in);
    Appendable ap = System.out;
    KlondikeController controller = new KlondikeTextualController(rd, ap);
    try {
      controller.playGame(game, game.getDeck(), true, numPiles, numDrawCards);
    } catch (IllegalStateException ignored) {
    }
  }

  private static GameType getGameType(String[] args) {
    switch (args[0].toLowerCase()) {
      case "basic":
        return GameType.Basic;
      case "limited":
        return GameType.Limited;
      case "whitehead":
        return GameType.Whitehead;
      default:
        throw new IllegalArgumentException("Not a valid game mode.");
    }
  }
}
