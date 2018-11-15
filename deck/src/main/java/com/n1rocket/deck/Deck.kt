package com.n1rocket.deck

import java.util.*

class Deck {
    lateinit var cards: MutableList<Card>
    lateinit var using: MutableList<Card>
    lateinit var removed: MutableList<Card>

    init {
        startDeck()
    }

    private fun startDeck() {
        //TODO add 8 decks in one with marked card to shuffle like casinos to avoid counting hack
        cards = ArrayList(Card.values().size)
        using = ArrayList(Card.values().size)
        removed = ArrayList(Card.values().size)
        cards.addAll(Arrays.asList(*Card.values()))
        cards.shuffle()
    }

    fun canTakeCardFromTop(): Boolean {
        return cards.isNotEmpty()
    }

    fun takeCardFromTop(): Card {
        val card: Card = cards[0]

        using.add(card)
        cards.remove(card)

        return card
    }

    fun removeCard(card: Card){
        removed.add(card)
        using.remove(card)
    }

    fun removeCards(cards: List<Card>){
        removed.addAll(cards)
        using.removeAll(cards)
    }

    fun restart() {
        startDeck()
    }

    fun shuffleDeck() {
        cards.shuffle()
    }

    fun shuffleDeckOnlyRemovedCards() {
        cards.addAll(removed)
        removed.clear()
        shuffleDeck()
    }

    fun whichCardIsTheNext(): Card {
        return cards[0]
    }

    fun cardsLeft(): Int {
        return cards.size
    }
}