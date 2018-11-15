package com.n1rocket.deck.blackjack

import com.n1rocket.deck.Player

class PlayerBlackJack(name: String, money: Double, var stand: Boolean = false) : Player(name, money) {
    fun doStand(stand: Boolean = true) {
        this.stand = stand
    }

    interface PlayerActionDelegate {
        fun doPlayerAction(player: PlayerBlackJack, action: BlackJackGame.PlayerAction)
    }

}
