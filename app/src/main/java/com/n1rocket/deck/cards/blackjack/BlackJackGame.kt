package com.n1rocket.deck.cards.blackjack

import com.n1rocket.deck.cards.Card
import com.n1rocket.deck.cards.Deck
import com.n1rocket.deck.cards.Player

class BlackJackGame(val delegate: BlackJackGameDelegate) {
    var deck: Deck = Deck()
    lateinit var players: List<Player>
    lateinit var hands: HashMap<Player, HandBlackJack>
    lateinit var bids: HashMap<Player, Double>

    interface BlackJackGameDelegate {
        fun onTurn(player: Player, playerActions: List<PlayerAction>): PlayerAction
        fun onPlayerWinner(player: Player, prize : Double)
    }

    sealed class PlayerAction {
        class Hit() : PlayerAction()
        class Stand() : PlayerAction()
        class x2() : PlayerAction()
    }

    fun startRound(bids : HashMap<Player, Double>) {
        this.bids = bids
        this.players = bids.keys.toList()

        deck.shuffleDeck()

        dealCards()

        checkDirectWinners()
    }


    private fun dealCards() {
        players.forEach { hands[it] = HandBlackJack() }

        hands.forEach {
            it.value.addCard(takeCard())
        }

        hands.forEach {

            val card = takeCard()

            if (it.key.isBank){ card.hidden = true }

            it.value.addCard(card)
        }

    }

    private fun checkDirectWinners() {

        hands.filter { playerHand -> !playerHand.key.isBank }.forEach {
            if(it.value.hasDirectWinner()){
                bids[it.key]?.let { bid ->
                    it.key.winner(bid)
                    deck.removeCards(it.value.cards)
                    delegate.onPlayerWinner(it.key, BlackJackPrizes(bid).blackJack())
                }
            }
        }

    }

    private fun takeCard(): Card {
        if (!deck.canTakeCardFromTop()) {
            deck.shuffleDeckOnlyRemovedCards()
        }
        return deck.takeCardFromTop()
    }

}