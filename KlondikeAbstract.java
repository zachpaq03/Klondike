package cs3500.klondike.model.hw04;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.CardClass;
import cs3500.klondike.model.hw02.Color;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw02.Suit;

/**
 * A class for a basic game of klondike.
 */


public abstract class KlondikeAbstract implements KlondikeModel {

  private List<Card>[] cascadePiles;
  private int numCascadePiles;
  private List<Card>[] foundationPiles;
  private int numFoundationPiles;
  private List<Card> drawCards;
  private int numVisDrawCards;
  private boolean gameStarted;
  private boolean gameEnded;
  private List<Card> flippedCards;
  private int highestRank;

  /**
   * Constructs a model for Klondike that is completely empty.
   */
  public KlondikeAbstract() {
    this.cascadePiles = new List[0];
    this.numCascadePiles = 0;
    this.foundationPiles = new List[0];
    this.numFoundationPiles = 0;
    this.drawCards = new ArrayList<>();
    this.gameStarted = false;
    this.gameEnded = false;
    this.numVisDrawCards = 0;
    this.flippedCards = new ArrayList<>();
    this.highestRank = 0;
  }

  @Override
  public List<Card> getDeck() {
    List<Card> deck = new ArrayList<>();
    for (int i = 1; i <= 13; i++) {
      for (Suit suit : Suit.values()) {
        deck.add(new CardClass(i, suit));
      }
    }
    return deck;
  }

  // Shuffles the given list of cards.
  protected void shuffleCards(List<Card> cards) {
    Collections.shuffle(cards);
  }

  // Deals out the given list of cards into the number of cascade
  // piles specified by numPiles.
  void dealCards(List<Card> deck, int numPiles) {
    int deckIndex = 0;
    for (int i = 0; i < numPiles; i++) {
      for (int j = i; j < numPiles; j++) {
        if (deckIndex < deck.size()) {
          this.cascadePiles[j].add(deck.get(deckIndex));
          deckIndex++;
        } else {
          break;
        }
      }
    }
    while (deckIndex < deck.size()) {
      this.drawCards.add(deck.get(deckIndex));
      deckIndex++;
    }
    for (int i = 0; i < numPiles; i++) {
      if (!this.cascadePiles[i].isEmpty()) {
        this.flippedCards.add(this.cascadePiles[i].get(this.cascadePiles[i].size() - 1));
      }
    }
    this.flippedCards.addAll(this.drawCards);
  }


  // Gets the rank of a card by checking its toString().
  protected int getRank(Card card) {
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

  // Checks a cards suit by checking its toString().
  protected Suit getSuit(Card card) {
    char suitChar = card.toString().charAt(card.toString().length() - 1);

    if (suitChar == '♣') {
      return Suit.club;
    } else if (suitChar == '♢') {
      return Suit.diamond;
    } else if (suitChar == '♡') {
      return Suit.heart;
    } else if (suitChar == '♠') {
      return Suit.spade;
    } else {
      throw new IllegalArgumentException("Not a suit.");
    }
  }

  // Checks if a given list of cards is a valid deck for a game
  // of Klondike based on our rules. Makes sure the list has an
  // even amount of each suit and rank (unless the amount is 0)
  // and that there are consecutive runs of cards.
  protected boolean validDeck(List<Card> deck) {
    Map<Suit, Integer> countOfSuit = new HashMap<>();
    for (Card card : deck) {
      countOfSuit.put(getSuit(card), countOfSuit.getOrDefault(getSuit(card), 0) + 1);
    }
    List<Integer> checking = new ArrayList<>(countOfSuit.values());
    int checker = checking.get(0);
    for (Integer num : checking) {
      if (num != checker) {
        return false;
      }
    }
    Map<Integer, Integer> countOfRank = new HashMap<>();
    for (Card card : deck) {
      int count = countOfRank.getOrDefault(getRank(card), 0);
      countOfRank.put(getRank(card), count + 1);
    }
    List<Integer> valuesAsList = new ArrayList<>(countOfRank.values());
    int checkerNums = valuesAsList.get(0);
    for (Integer nums : valuesAsList) {
      if (nums != checkerNums) {
        return false;
      }
    }
    int max = 0;
    List<Integer> keysAsList = new ArrayList<>(countOfRank.keySet());
    for (Integer nums : keysAsList) {
      max = Math.max(max, nums);
    }
    for (int i = max; i > 0; i--) {
      if (!keysAsList.contains(i)) {
        return false;
      }
    }
    return true;
  }

  // Checks to see if there are enough cards in the deck to
  // form the number of cascade piles specified by numPiles.
  boolean canCascadeBeDealt(List<Card> deck, int numPiles) {
    int numCascadeCards = 0;
    for (int i = numPiles; i > 0; i--) {
      numCascadeCards += i;
    }
    return deck.size() >= numCascadeCards;
  }

  // Checks how many aces are in the deck, which helps to set
  // up how many foundation piles there should be.
  protected int numAces(List<Card> deck) {
    int count = 0;
    for (Card card : deck) {
      if (getRank(card) == 1) {
        count++;
      }
    }
    return count;
  }

  // Finds the highest rank in the deck so the game can set
  // what cards can go to an empty cascade pile.
  protected int getHighestRank(List<Card> deck) {
    int max = 0;
    for (Card card : deck) {
      max = Math.max(getRank(card), max);
    }
    return max;
  }

  // Initializes each index in the cascade piles array with an
  // empty list so cards can be added.
  void setUpCascadePiles(int amount) {
    for (int i = 0; i < amount; i++) {
      this.cascadePiles[i] = new ArrayList<>();
    }
  }

  // Initializes each index in the foundation piles array with an
  // empty list so cards can be added.
  void setUpFoundationPiles(int amount) {
    for (int i = 0; i < amount; i++) {
      this.foundationPiles[i] = new ArrayList<>();
    }
  }

  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numPiles, int numDraw)
          throws IllegalArgumentException {
    if (gameStarted) {
      throw new IllegalStateException("A game has already been started.");
    }
    if (deck == null) {
      throw new IllegalArgumentException("Null deck.");
    }
    if (deck.isEmpty()) {
      throw new IllegalArgumentException("Empty deck.");
    }
    for (Card card : deck) {
      if (card == null) {
        throw new IllegalArgumentException("No null cards in deck.");
      }
    }
    List<Card> gameCards = new ArrayList<>(deck);
    if (!validDeck(gameCards)) {
      throw new IllegalArgumentException("Invalid deck.");
    }
    if (numPiles <= 0) {
      throw new IllegalArgumentException("Cannot have 0 or negative cascade piles.");
    }
    if (!canCascadeBeDealt(gameCards, numPiles)) {
      throw new IllegalArgumentException(String.format(
              "Not enough cards in the deck for %d cascade piles.", numPiles));
    }
    if (numDraw <= 0) {
      throw new IllegalArgumentException("Cannot have 0 or negative draw cards.");
    }
    if (shuffle) {
      shuffleCards(gameCards);
    }
    this.numFoundationPiles = numAces(deck);
    this.foundationPiles = new List[this.numFoundationPiles];
    this.numCascadePiles = numPiles;
    this.cascadePiles = new List[this.numCascadePiles];
    setUpCascadePiles(this.numCascadePiles);
    this.highestRank = getHighestRank(gameCards);
    dealCards(gameCards, numPiles);
    setUpFoundationPiles(this.numFoundationPiles);
    this.numVisDrawCards = numDraw;
    this.gameStarted = true;
  }

  // Gets the color of a card by checking suit.
  protected Color getColor(Card card) {
    if (getSuit(card) == Suit.club || getSuit(card) == Suit.spade) {
      return Color.black;
    } else {
      return Color.red;
    }
  }

  // Checks if it is legal based on the rules of Klondike for a card
  // to move to a specific pile. If the pile is empty, it checks if
  // the card is the highest rank in the game.
  boolean legalMoveCascadePile(Card mover, List<Card> destPile, int moverIndex, int srcPile) {
    int lastIndex = destPile.size() - 1;
    if (lastIndex == -1) {
      return getRank(mover) == this.highestRank;
    }
    Card base = destPile.get(lastIndex);
    return getRank(mover) + 1 == getRank(base)
            && getColor(mover) != getColor(base);
  }

  @Override
  public void movePile(int srcPile, int numCards, int destPile)
          throws IllegalStateException {
    gameNotStarted();
    if (srcPile + 1 > this.numCascadePiles || destPile + 1 > this.numCascadePiles
            || srcPile < 0 || destPile < 0) {
      throw new IllegalArgumentException("At least one of the given piles does not exist.");
    }
    if (srcPile == destPile) {
      throw new IllegalArgumentException("Attempting to move to the same pile.");
    }
    if (numCards > this.cascadePiles[srcPile].size()) {
      throw new IllegalArgumentException("Attempting to move more cards than exist in the pile.");
    }
    int moverIndex = this.cascadePiles[srcPile].size() - numCards;
    if (!isCardVisible(srcPile, moverIndex)) {
      throw new IllegalStateException("Attempting to move a card or cards that are not flipped.");
    }
    if (!legalMoveCascadePile(this.cascadePiles[srcPile].get(moverIndex),
            this.cascadePiles[destPile], moverIndex, srcPile)) {
      throw new IllegalStateException("Not a legal move.");
    }
    int size = this.cascadePiles[srcPile].size();
    for (int i = moverIndex; i < size; i++) {
      this.cascadePiles[destPile].add(this.cascadePiles[srcPile].get(moverIndex));
      this.cascadePiles[srcPile].remove(moverIndex);
    }
    if (moverIndex != 0) {
      if (!this.flippedCards.contains(this.cascadePiles[srcPile].get(moverIndex - 1))) {
        this.flippedCards.add(this.cascadePiles[srcPile].get(moverIndex - 1));
      }
    }
  }

  @Override
  public void moveDraw(int destPile) throws IllegalStateException {
    gameNotStarted();
    if (destPile + 1 > this.numCascadePiles || destPile < 0) {
      throw new IllegalArgumentException("This destination pile does not exist.");
    }
    if (this.drawCards.isEmpty()) {
      throw new IllegalStateException("There are no draw cards.");
    }
    if (!legalMoveCascadePile(this.getDrawCards().get(0), this.cascadePiles[destPile],
            Integer.MAX_VALUE, 0)) {
      throw new IllegalStateException("Not a legal move.");
    }

    this.cascadePiles[destPile].add(this.drawCards.get(0));
    this.drawCards.remove(0);
  }

  // Checks if it is legal based on the rules of Klondike for a card
  // to move to a specific foundation pile. If the pile is empty, it
  // checks if the card is an ace.
  boolean legalMoveFoundationPile(Card mover, List<Card> foundationPile) {
    int lastIndex = foundationPile.size() - 1;
    if (lastIndex == -1) {
      return getRank(mover) == 1;
    }
    Card base = foundationPile.get(lastIndex);
    return getRank(mover) - 1 == getRank(base)
            && getSuit(mover) == getSuit(base);
  }

  @Override
  public void moveToFoundation(int srcPile, int foundationPile)
          throws IllegalStateException {
    gameNotStarted();
    if (srcPile + 1 > this.numCascadePiles || srcPile < 0) {
      throw new IllegalArgumentException("Not a valid cascade pile.");
    }
    if (foundationPile + 1 > this.numFoundationPiles || foundationPile < 0) {
      throw new IllegalArgumentException("Not a valid foundation pile.");
    }
    if (this.cascadePiles[srcPile].size() == 0) {
      throw new IllegalStateException("This cascade pile is empty.");
    }
    int lastIndex = this.cascadePiles[srcPile].size() - 1;
    if (!legalMoveFoundationPile(this.cascadePiles[srcPile].get(lastIndex),
            this.foundationPiles[foundationPile])) {
      throw new IllegalStateException("Not a valid move.");
    }
    this.foundationPiles[foundationPile].add(this.cascadePiles[srcPile].get(lastIndex));
    this.cascadePiles[srcPile].remove(lastIndex);

    if (lastIndex != 0) {
      if (!this.flippedCards.contains(this.cascadePiles[srcPile].get(lastIndex - 1))) {
        this.flippedCards.add(this.cascadePiles[srcPile].get(lastIndex - 1));
      }
    }
  }

  @Override
  public void moveDrawToFoundation(int foundationPile)
          throws IllegalStateException {
    gameNotStarted();
    if (foundationPile + 1 > this.numFoundationPiles || foundationPile < 0) {
      throw new IllegalArgumentException("Not a valid foundation pile.");
    }
    if (this.drawCards.isEmpty()) {
      throw new IllegalStateException("There are no draw cards.");
    }
    if (!legalMoveFoundationPile(this.getDrawCards().get(0),
            this.foundationPiles[foundationPile])) {
      throw new IllegalStateException("Not a valid move.");
    }

    this.foundationPiles[foundationPile].add(this.drawCards.get(0));
    this.drawCards.remove(0);
  }

  @Override
  public void discardDraw() throws IllegalStateException {
    gameNotStarted();
    if (this.drawCards.isEmpty()) {
      throw new IllegalStateException("There are no cards in the draw pile.");
    }
    addToDraw(this.drawCards.get(0));
    removeFromDraw(0);
  }

  protected void addToDraw(Card card) {
    this.drawCards.add(card);
  }

  protected void removeFromDraw(int index) {
    this.drawCards.remove(index);
  }

  @Override
  public int getNumRows() {
    gameNotStarted();
    int max = 0;
    for (List<Card> pile : this.cascadePiles) {
      max = Math.max(pile.size(), max);
    }
    return max;
  }

  @Override
  public int getNumPiles() {
    gameNotStarted();
    return this.numCascadePiles;
  }

  @Override
  public int getNumDraw() {
    gameNotStarted();
    return this.numVisDrawCards;
  }

  @Override
  public boolean isGameOver() throws IllegalStateException {
    gameNotStarted();
    if (this.gameEnded) {
      return true;
    }
    List<Card> visibleNonFoundationCards = new ArrayList<>();
    for (int cascadeIndex = 0; cascadeIndex < this.numCascadePiles; cascadeIndex++) {
      for (int cardIndex = 0; cardIndex < this.getPileHeight(cascadeIndex); cardIndex++) {
        if (isCardVisible(cascadeIndex, cardIndex)) {
          visibleNonFoundationCards.add(getCardAt(cascadeIndex, cardIndex));
        }
      }
    }
    for (Card visCard : visibleNonFoundationCards) {
      for (List<Card> cascPile : this.cascadePiles) {
        if (legalMoveCascadePile(visCard, cascPile, 0, 0)) {
          return false;
        }

      }
    }
    for (Card visCard : visibleNonFoundationCards) {
      for (List<Card> foundPile : this.foundationPiles) {
        if (legalMoveFoundationPile(visCard, foundPile)) {
          return false;
        }
      }
    }
    return this.drawCards.isEmpty();
  }

  @Override
  public int getScore() throws IllegalStateException {
    gameNotStarted();
    int score = 0;
    for (List<Card> foundPile : this.foundationPiles) {
      int lastIndex = foundPile.size() - 1;
      if (lastIndex >= 0) {
        score += getRank(foundPile.get(lastIndex));
      }
    }
    return score;
  }

  @Override
  public int getPileHeight(int pileNum) throws IllegalStateException {
    gameNotStarted();
    if (pileNum + 1 > this.numCascadePiles || pileNum < 0) {
      throw new IllegalArgumentException("Not a valid cascade pile.");
    }
    return this.cascadePiles[pileNum].size();
  }

  @Override
  public boolean isCardVisible(int pileNum, int card) throws IllegalStateException {
    gameNotStarted();
    if (pileNum + 1 > this.numCascadePiles || pileNum < 0) {
      throw new IllegalArgumentException("Not a valid cascade pile.");
    }
    if (card >= this.cascadePiles[pileNum].size() || card < 0) {
      throw new IllegalArgumentException("This pile does not have a card in that index.");
    }
    return this.flippedCards.contains(this.cascadePiles[pileNum].get(card));
  }

  @Override
  public Card getCardAt(int pileNum, int card) throws IllegalStateException {
    gameNotStarted();
    if (pileNum + 1 > this.numCascadePiles || pileNum < 0) {
      throw new IllegalArgumentException("Not a valid cascade pile.");
    }
    if (card < 0) {
      throw new IllegalArgumentException("A card's index cannot be negative.");
    }
    if (card >= this.cascadePiles[pileNum].size()) {
      throw new IllegalArgumentException("Invalid coordinates.");
    }
    if (!isCardVisible(pileNum, card)) {
      throw new IllegalArgumentException("This card is not flipped.");
    }
    return this.cascadePiles[pileNum].get(card);
  }

  @Override
  public Card getCardAt(int foundationPile) throws IllegalStateException {
    gameNotStarted();
    if (foundationPile + 1 > this.numFoundationPiles || foundationPile < 0) {
      throw new IllegalArgumentException("Not a valid foundation pile.");
    }
    if (this.foundationPiles[foundationPile].isEmpty()) {
      return null;
    }
    int lastIndex = this.foundationPiles[foundationPile].size() - 1;
    return this.foundationPiles[foundationPile].get(lastIndex);
  }

  @Override
  public List<Card> getDrawCards() throws IllegalStateException {
    gameNotStarted();
    int amount = this.numVisDrawCards;
    amount = Math.min(amount, this.drawCards.size());
    List<Card> result = new ArrayList<>();
    for (int i = 0; i < amount; i++) {
      result.add(this.drawCards.get(i));
    }
    return result;
  }

  @Override
  public int getNumFoundations() throws IllegalStateException {
    gameNotStarted();
    return this.numFoundationPiles;
  }

  // Checks if the game has started yet.
  void gameNotStarted() throws IllegalStateException {
    if (!this.gameStarted) {
      throw new IllegalStateException("Game has not started yet.");
    }
  }
}


