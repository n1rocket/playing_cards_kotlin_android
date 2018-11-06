package com.n1rocket.deck.cards

open class Hand {
    var cards:MutableList<Card> = mutableListOf()

    fun addCard(card: Card){
        this.cards.add(card)
    }

    fun addCards(cards: List<Card>){
        this.cards.addAll(cards)
    }
}
