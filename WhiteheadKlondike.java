package cs3500.klondike.model.hw04;

import java.util.List;

import cs3500.klondike.model.hw02.Card;

/**
 * This is a stub implementation of the {@link cs3500.klondike.model.hw02.KlondikeModel} interface.
 * You may assume that the actual implementation of WhiteheadKlondike will have a zero-argument
 * (i.e. default) constructor, and that all the methods below will be implemented.  You may not make
 * any other assumptions about the implementation of this class (e.g. what fields it might have,
 * or helper methods, etc.).
 */
public class WhiteheadKlondike extends KlondikeAbstract {

  @Override
  boolean legalMoveCascadePile(Card mover, List<Card> destPile, int moverIndex, int srcPile) {
    if (moverIndex < this.getPileHeight(srcPile) - 1) {
      for (int i = moverIndex; i < this.getPileHeight(srcPile); i++) {
        if (getSuit(getCardAt(srcPile, moverIndex)) != getSuit(getCardAt(srcPile, i))) {
          return false;
        }
        if (i > moverIndex) {
          if (getRank(getCardAt(srcPile, i)) + 1 != getRank(getCardAt(srcPile, i - 1))) {
            return false;
          }
        }
      }
    }
    int lastIndex = destPile.size() - 1;
    if (lastIndex == -1) {
      return true;
    }
    Card base = destPile.get(lastIndex);
    return getRank(mover) + 1 == getRank(base)
            && getColor(mover) == getColor(base);
  }

  @Override
  public boolean isCardVisible(int pileNum, int card) throws IllegalStateException {
    super.gameNotStarted();
    return true;
  }
}
