package com.n1rocket.deck

open class Hand {
    var cards: MutableList<Card> = mutableListOf()

    fun addCard(card: Card) {
        this.cards.add(card)
    }

    fun addCards(cards: List<Card>) {
        this.cards.addAll(cards)
    }

    fun cleanHand() {
        this.cards = mutableListOf()
    }

    fun hasCards(): Boolean {
        return this.cards.isNotEmpty()
    }
}
