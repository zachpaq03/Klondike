package cs3500.klondike.model.hw04;

import cs3500.klondike.model.hw02.Card;

import java.util.HashMap;
import java.util.Map;

/**
 * A class for limited draw klondike, which is a version klondike where the
 * amount of times a draw card can be recycled is limited.
 */
public class LimitedDrawKlondike extends KlondikeAbstract {
  private int numTimesRedrawAllowed;
  private Map<Card, Integer> numTimesRedrawed;

  /**
   * A constructor for limited draw klondike.
   *
   * @param numTimesRedrawAllowed The number of times a card is allowed to be
   *                              discarded.
   * @throws IllegalArgumentException if the number of times given is negative.
   */
  public LimitedDrawKlondike(int numTimesRedrawAllowed) {
    super();
    if (numTimesRedrawAllowed < 0) {
      throw new IllegalArgumentException("Cannot have a negative value for the amount of redraws.");
    }
    this.numTimesRedrawAllowed = numTimesRedrawAllowed;
    this.numTimesRedrawed = new HashMap<>();
  }

  @Override
  public void discardDraw() throws IllegalStateException {
    super.gameNotStarted();
    if (super.getDrawCards().isEmpty()) {
      throw new IllegalStateException("There are no cards in the draw pile");
    }
    Card topDraw = super.getDrawCards().get(0);
    numTimesRedrawed.put(topDraw, numTimesRedrawed.getOrDefault(topDraw, 0) + 1);
    if (numTimesRedrawed.get(topDraw) > numTimesRedrawAllowed) {
      super.removeFromDraw(0);
    } else {
      super.discardDraw();
    }
  }


}

