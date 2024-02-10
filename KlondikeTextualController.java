package cs3500.klondike.controller;

import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.view.KlondikeTextualView;
import cs3500.klondike.view.TextualView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * A class got a controller specific for the textual view
 * of a klondike game.
 */
public class KlondikeTextualController implements cs3500.klondike.controller.KlondikeController {
  private final Scanner scan;
  private final Appendable out;

  /**
   * A constructor for this controller.
   *
   * @param r A readable input.
   * @param a An appendable output.
   * @throws IllegalArgumentException if either parameter is null.
   */
  public KlondikeTextualController(Readable r, Appendable a) {
    if (r == null || a == null) {
      throw new IllegalArgumentException("Input or output cannot be null.");
    }
    this.scan = new Scanner(r);
    this.out = a;
  }

  @Override
  public void playGame(KlondikeModel model, List<Card> deck,
                       boolean shuffle, int numPiles, int numDraw) {
    // Making sure model is not null.
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null\n");
    }
    // Checking if game can be started with the given parameters.
    try {
      model.startGame(deck, shuffle, numPiles, numDraw);
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Cannot start game with these inputs.");
    }
    KlondikeTextualView view = new KlondikeTextualView(model, out);
    // While game is on going, print board, print score, and read given move.
    while (!model.isGameOver()) {
      try {
        printBoard(view, out);
        getScore(model, out);
        out.append(", ");
        printMessage("Enter your move:", out);
        String token;
        token = scan.next();
        // Allow user to quit.
        if (token.equalsIgnoreCase("q")) {
          quitGame(view, model);
          return;
        }
        switch (token) {
          case "mpp":
            // Check if the given inputs are the right format for mpp.
            List<Integer> paramsMPP = checkMPP(scan);
            // In case the user enters q mid-move, giving them a way to quit.
            if (paramsMPP == quitList) {
              quitGame(view, model);
              return;
            }
            // Check if the move provided by the user is valid.
            try {
              model.movePile(paramsMPP.get(0), paramsMPP.get(1), paramsMPP.get(2));
            } catch (IllegalStateException | IllegalArgumentException ex) {
              out.append(badMoveMessage() + ex.getMessage() + "\n");
            }
            break;
          case "mpf":
            // Check if the given inputs are the right format for mpf.
            List<Integer> paramsMPF = checkMPF(scan);
            // In case the user enters q mid-move, giving them a way to quit.
            if (paramsMPF == quitList) {
              quitGame(view, model);
              return;
            }
            // Check if the move provided by the user is valid.
            try {
              model.moveToFoundation(paramsMPF.get(0), paramsMPF.get(1));
            } catch (IllegalStateException | IllegalArgumentException ex) {
              out.append(badMoveMessage() + ex.getMessage() + "\n");
            }
            break;
          case "md":
            // Check if the given inputs are the right format for md.
            int dp = checkMD(scan);
            // In case the user enters q mid-move, giving them a way to quit.
            if (dp == quitNum) {
              quitGame(view, model);
              return;
            }
            // Check if the move provided by the user is valid.
            try {
              model.moveDraw(dp);
            } catch (IllegalStateException | IllegalArgumentException ex) {
              out.append(badMoveMessage() + ex.getMessage() + "\n");
            }
            break;
          case "mdf":
            // Check if the given inputs are the right format for mdf.
            int fp = checkMDF(scan);
            // In case the user enters q mid-move, giving them a way to quit.
            if (fp == quitNum) {
              quitGame(view, model);
              return;
            }
            // Check if the move provided by the user is valid.
            try {
              model.moveDrawToFoundation(fp);
            } catch (IllegalStateException | IllegalArgumentException ex) {
              out.append(badMoveMessage() + ex.getMessage() + "\n");
            }
            break;
          case "dd":
            // Discard draw does not need any additional inputs, so go ahead with the move.
            // Check if the move provided by the user is valid.
            try {
              model.discardDraw();
            } catch (IllegalStateException e) {
              out.append(badMoveMessage() + e.getMessage() + "\n");
            }
            break;
          default:
            // If none of the expected inputs is given, then tell user that their move is invalid.
            out.append(badMoveMessage() + "\n");
            break;
        }
      } catch (IOException | NoSuchElementException e) {
        throw new IllegalStateException("Append failed");
      }
    }
    // When the game is over, print the board one last time.
    try {
      printBoard(view, out);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    // If they won, print winner message.
    if (didTheyWin(model, deck)) {
      try {
        out.append("You win! " + gameOverMessage());
        getScore(model, out);
        out.append("\n");
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      // If they lost, print the loser message.
    } else {
      try {
        out.append(gameOverMessage());
        getScore(model, out);
        out.append("\n");
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  // The message printed when the game is over.
  private String gameOverMessage() {
    return "Game over. ";
  }

  // Prints textual view of game.
  private void printBoard(TextualView view, Appendable out) throws IOException {
    view.render();
    out.append("\n");
  }

  // Prints the score of the game.
  private void getScore(KlondikeModel model, Appendable out) throws IOException {
    out.append(String.format("Score: %s", model.getScore()));
  }

  // Prints a given message.
  private void printMessage(String message, Appendable out) {
    try {
      out.append(message);
      out.append("\n");
    } catch (IOException e) {
      throw new IllegalStateException(e.getMessage());
    }
  }

  // List used when a user inputs q, causing program to quit.
  private final List<Integer> quitList = new ArrayList<>();

  // Checks the next 3 inputs to see if they are integers,
  // does not check that they are positive because model will
  // check that.
  private List<Integer> checkMPP(Scanner scan) throws IOException {
    int srcPile = 0;
    int cardsMoved = 0;
    int destPile = 0;
    boolean srcPileFound = false;
    boolean cardsMovedFound = false;
    boolean destPileFound = false;
    while (!srcPileFound) {
      try {
        String input = scan.next();
        if (input.equalsIgnoreCase("q")) {
          return quitList;
        }
        srcPile = Integer.parseInt(input) - 1;
        srcPileFound = true;
      } catch (NumberFormatException | InputMismatchException e) {
        out.append("Source pile must be an integer, try again. ");
        out.append(reEnterMessage() + "\n");
      }
    }
    while (!cardsMovedFound) {
      try {
        String input = scan.next();
        if (input.equalsIgnoreCase("q")) {
          return quitList;
        }
        cardsMoved = Integer.parseInt(input);
        cardsMovedFound = true;
      } catch (NumberFormatException | InputMismatchException e) {
        out.append("The amount of cards you're moving must be an integer, try again. ");
        out.append(reEnterMessage() + "\n");
      }
    }
    while (!destPileFound) {
      try {
        String input = scan.next();
        if (input.equalsIgnoreCase("q")) {
          return quitList;
        }
        destPile = Integer.parseInt(input) - 1;
        destPileFound = true;
      } catch (NumberFormatException | InputMismatchException e) {
        out.append("Destination pile must be an integer, try again. ");
        out.append(reEnterMessage() + "\n");
      }
    }
    return Arrays.asList(srcPile, cardsMoved, destPile);
  }

  // Checks the next 2 inputs to see if they are integers,
  // does not check that they are positive because model will
  // check that.
  private List<Integer> checkMPF(Scanner scan) throws IOException {
    int srcPile = 0;
    int foundationPile = 0;
    boolean srcPileFound = false;
    boolean foundationPileFound = false;
    while (!srcPileFound) {
      try {
        String input = scan.next();
        if (input.equalsIgnoreCase("q")) {
          return quitList;
        }
        srcPile = Integer.parseInt(input) - 1;
        srcPileFound = true;
      } catch (NumberFormatException | InputMismatchException e) {
        out.append("Source pile must be an integer, try again. ");
        out.append(reEnterMessage() + "\n");
      }
    }
    while (!foundationPileFound) {
      try {
        String input = scan.next();
        if (input.equalsIgnoreCase("q")) {
          return quitList;
        }
        foundationPile = Integer.parseInt(input) - 1;
        foundationPileFound = true;
      } catch (NumberFormatException | InputMismatchException e) {
        out.append("Foundation pile must be an integer, try again. ");
        out.append(reEnterMessage() + "\n");
      }
    }
    return Arrays.asList(srcPile, foundationPile);
  }

  // Number returned when a user enters q, causing the program to quit.
  int quitNum = -1;

  // Checks the next input to see if it is an integer,
  // does not check that it is positive because model will
  // check that.
  private Integer checkMD(Scanner scan) throws IOException {
    int destPile = 0;
    boolean destPileFound = false;
    while (!destPileFound) {
      try {
        String input = scan.next();
        if (input.equalsIgnoreCase("q")) {
          return quitNum;
        }
        destPile = Integer.parseInt(input) - 1;
        destPileFound = true;
      } catch (NumberFormatException | InputMismatchException e) {
        out.append("Destination pile must be an integer, try again. ");
        out.append(reEnterMessage() + "\n");
      }
    }
    return destPile;
  }

  // Checks the next input to see if it is an integer,
  // does not check that it is positive because model will
  // check that.
  private Integer checkMDF(Scanner scan) throws IOException {
    int foundationPile = 0;
    boolean exceptionCaught = true;
    while (exceptionCaught) {
      try {
        String input = scan.next();
        if (input.equalsIgnoreCase("q")) {
          return quitNum;
        }
        foundationPile = Integer.parseInt(input) - 1;
        exceptionCaught = false;
      } catch (NumberFormatException | InputMismatchException e) {
        out.append("Foundation pile must be an integer, try again. ");
        out.append(reEnterMessage() + "\n");
      }
    }
    return foundationPile;
  }

  // Sends the messages that are sent when a game is quit.
  private void quitGame(KlondikeTextualView view, KlondikeModel model) throws IOException {
    out.append("Game quit!\n");
    out.append("State of game when quit:\n");
    out.append(view.toString() + "\n");
    getScore(model, out);
  }

  // The message sent after an invalid move.
  private String badMoveMessage() {
    return "Invalid move. Play again. ";
  }

  // A method to get the rank of a given card.
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

  // A method to check if a game is won by getting the highest rank in a deck,
  // and checking if the score is equal to that rank times the amount of
  // foundation piles.
  private boolean didTheyWin(KlondikeModel model, List<Card> deck) {
    if (!model.isGameOver()) {
      return false;
    }
    int highestRank = 0;
    for (Card card : deck) {
      if (getRank(card) > highestRank) {
        highestRank = getRank(card);
      }
    }
    return model.getScore() == highestRank * model.getNumFoundations();
  }

  // The message sent when it is necessary to re-enter a move.
  private String reEnterMessage() {
    return "Re-enter a value:";
  }

}
