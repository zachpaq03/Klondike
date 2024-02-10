package cs3500.klondike.model.hw04;

import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.KlondikeModel;

/**
 * Class that creates one of the three types of klondike games.
 */
public class KlondikeCreator {

  /**
   * A method that returns a specific type of klondike game.
   *
   * @param type One of the three types of klondike games.
   * @return The spcified type of klondike game.
   */
  public static KlondikeModel create(GameType type, int ntrd) {
    if (type == GameType.Basic) {
      return new BasicKlondike();
    } else if (type == GameType.Whitehead) {
      return new WhiteheadKlondike();
    } else {
      return new LimitedDrawKlondike(ntrd);
    }
  }
}
