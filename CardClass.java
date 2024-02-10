package cs3500.klondike.model.hw02;

/**
 * A class for cards in a game of solitare.
 * Cards have a suit that ranges from 1 to 13
 * and are a part of 1 of 4 suits.
 */
public class CardClass implements Card {

  private int rank;
  private Suit suit;

  /**
   * A constructor for a card.
   *
   * @param rank the rank of the card.
   * @param suit the suit of the card.
   */
  public CardClass(int rank, Suit suit) {
    if (rank <= 13 && rank >= 1) {
      this.rank = rank;
    }
    this.suit = suit;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    switch (this.rank) {
      case 1:
        sb.append('A');
        break;
      case 11:
        sb.append('J');
        break;
      case 12:
        sb.append('Q');
        break;
      case 13:
        sb.append('K');
        break;
      default:
        if (this.rank >= 2 && this.rank <= 10) {
          sb.append(this.rank);
        }
        break;
    }

    switch (this.suit) {
      case club:
        sb.append('♣');
        break;
      case spade:
        sb.append('♠');
        break;
      case heart:
        sb.append('♡');
        break;
      case diamond:
        sb.append('♢');
        break;
      default:
        break;
    }
    return sb.toString();
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    CardClass card = (CardClass) other;
    return this.rank == card.rank && this.suit == card.suit;
  }


  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + rank;
    result = 31 * result + suit.hashCode();
    return result;
  }

}
