package com.n1rocket.deck.cards.blackjack

sealed class BlackJackPrizes {
    class BlackJack : BlackJackPrizes()
    class Simple : BlackJackPrizes()
    class Draw : BlackJackPrizes()

    fun getPrize(bid: Double) :Double {
        return when(this) {
            is BlackJack -> bid * 3 / 2
            is Simple -> bid * 2
            is Draw -> bid
        }
    }
}
