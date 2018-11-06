package com.n1rocket.deck.cards.blackjack

class BlackJackPrizes(val bid: Double) {
    fun blackJack(): Double {
        return bid * 3 / 2
    }
}
