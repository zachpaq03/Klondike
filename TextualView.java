package cs3500.klondike.view;

import java.io.IOException;

/**
 * An interface for a textual view for a game of klondike.
 */
public interface TextualView {
  /**
   * Renders a model in some manner (e.g. as text, or as graphics, etc.).
   * @throws IOException if the rendering fails for some reason
   */
  void render() throws IOException;
}
