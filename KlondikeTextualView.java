package cs3500.klondike.view;

import java.io.IOException;

import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;

/**
 * A simple text-based rendering of the Klondike game.
 */
public class KlondikeTextualView implements TextualView {
  private final KlondikeModel model;
  private final Appendable appendable;
  // ... any other fields you need

  public KlondikeTextualView(KlondikeModel model) {
    this.model = model;
    this.appendable = null;
  }

  public KlondikeTextualView(KlondikeModel model, Appendable appendable) {
    this.model = model;
    this.appendable = appendable;
  }

  // Returns what should be printed for cards in the draw or foundation.
  private String whatToPrintFound(Card card) {
    if (card == null) {
      return "<none>";
    } else {
      return card.toString();
    }
  }

  // Returns what should be printed for cards in the cascade piles.
  private String whatToPrintCasc(int pileNum, int card) {
    if (!this.model.isCardVisible(pileNum, card)) {
      return "?";
    } else {
      return this.model.getCardAt(pileNum, card).toString();
    }
  }

  @Override
  public String toString() {
    StringBuilder board = new StringBuilder();
    board.append("Draw: ");
    for (int i = 0; i < this.model.getDrawCards().size(); i++) {
      board.append(whatToPrintFound(this.model.getDrawCards().get(i)));
      if (i != this.model.getDrawCards().size() - 1) {
        board.append(", ");
      }
    }
    board.append("\n");
    board.append("Foundation: ");
    for (int j = 0; j < this.model.getNumFoundations(); j++) {
      board.append(whatToPrintFound(this.model.getCardAt(j)));
      if (j != this.model.getNumFoundations() - 1) {
        board.append(", ");
      }
    }
    board.append("\n");
    boolean[] xPile = new boolean[this.model.getNumPiles()];
    for (int k = 0; k < this.model.getNumPiles(); k++) {
      if (this.model.getPileHeight(k) == 0) {
        board.append("  X");
        xPile[k] = true;
      } else {
        int firstWhiteSpaces = 3 - whatToPrintCasc(k, 0).length();
        for (int w = 0; w < firstWhiteSpaces; w++) {
          board.append(" ");
        }
        board.append(whatToPrintCasc(k, 0));
      }
    }
    if (this.model.getNumRows() != 1 && this.model.getNumRows() != 0) {
      board.append("\n");
    }
    for (int p = 1; p < this.model.getNumRows(); p++) {
      for (int k = 0; k < this.model.getNumPiles(); k++) {
        if (this.model.getPileHeight(k) > p) {
          int whiteSpaces = 3 - whatToPrintCasc(k, p).length();
          for (int w = 0; w < whiteSpaces; w++) {
            board.append(" ");
          }
          board.append(whatToPrintCasc(k, p));
        } else {
          board.append("   ");
        }
      }
      if (p != this.model.getNumRows() - 1) {
        board.append("\n");
      }
    }
    return board.toString();
  }

  @Override
  public void render() throws IOException {
    this.appendable.append(this.toString());
  }
}
