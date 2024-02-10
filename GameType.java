package cs3500.klondike.model.hw04;

/**
 * Enum class for the three types of klondike games.
 * Basic is a standard game.
 * Limited limits the amount of times draw cards can be recycled.
 * Whitehead is when all cascade pile cards are face up, builds are
 * with same color cards, and only same suit cards can be moved
 * together.
 */
public enum GameType {
  Basic, Limited, Whitehead
}
