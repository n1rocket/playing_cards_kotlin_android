package com.n1rocket.deck.cards.blackjack

import android.util.Log
import com.n1rocket.deck.cards.Card
import com.n1rocket.deck.cards.Deck

class BlackJackGame(private val delegate: BlackJackGameDelegate) {
    lateinit var deck: Deck
    lateinit var players: List<PlayerBlackJack>
    lateinit var banker: BankerBlackJack
    lateinit var hands: HashMap<PlayerBlackJack, HandBlackJack>
    lateinit var bids: HashMap<PlayerBlackJack, Double>

    interface BlackJackGameDelegate {
        fun onTurn(
            player: PlayerBlackJack,
            handBlackJack: HandBlackJack,
            playerActions: List<PlayerAction>
        )

        fun onPlayerWinner(player: PlayerBlackJack, prize: Double)
        fun onBankerUpdated(banker: BankerBlackJack)
        fun onPlayerUpdated(player: PlayerBlackJack, handBlackJack: HandBlackJack)
        fun onPlayerBust(player: PlayerBlackJack)
    }

    sealed class PlayerAction {
        class Hit : PlayerAction()
        class Stand : PlayerAction()
        class X2Bet : PlayerAction()
        class Split : PlayerAction()
    }

    fun startRound(bids: HashMap<PlayerBlackJack, Double>) {
        this.deck = Deck()
        Log.d("BJ", "startRound")
        this.bids = bids
        this.banker = BankerBlackJack("Rodrigo Rato")
        this.players = bids.keys.toList()
        dealBids()
        deck.shuffleDeck()
        dealCards()
        checkDirectWinners()

        if (hasPlayerAlive()) {
            nextPlayer()
        }
    }


    fun doPlayerAction(player: PlayerBlackJack, action: PlayerAction) {
        Log.d("BJ", "doPlayerAction")

        when (action) {
            is PlayerAction.Hit -> { hit(player) }
            is PlayerAction.X2Bet -> { x2Bet(player) }
            is PlayerAction.Stand -> { stand(player) }
            is PlayerAction.Split -> { split(player) }
        }
    }

    private fun stand(player: PlayerBlackJack) {
        Log.d("BJ", "stand")
        player.doStand()
        nextPlayer()
    }

    private fun x2Bet(player: PlayerBlackJack) {
        Log.d("BJ", "x2Bet")

    }

    private fun split(player: PlayerBlackJack) {
        Log.d("BJ", "split")

    }

    private fun hit(player: PlayerBlackJack) {
        Log.d("BJ", "hit")
        hands[player]?.let {
            it.addCard(takeCard())
            delegate.onPlayerUpdated(player, it)
            checkBust(player, it)
        }
    }

    private fun checkBust(player: PlayerBlackJack, hand: HandBlackJack) {
        if (hand.isBust()){
            delegate.onPlayerBust(player)
        }
    }

    private fun nextPlayer() {
        Log.d("BJ", "nextPlayer")

        getNextPlayerAndHand()?.let {
            delegate.onTurn(it.key, it.value, getPlayerActions(it))
        }?: this.goBanker()
    }

    private fun goBanker() {
        banker.hand.showCardHidden()
        delegate.onBankerUpdated(banker)

        //TODO If banker hand >= 17 stand banker and check winners
    }

    private fun getPlayerActions(playerAndHand: Map.Entry<PlayerBlackJack, HandBlackJack>): List<PlayerAction> {
        Log.d("BJ", "getPlayerActions")
        val playerActions = mutableListOf<PlayerAction>()

        playerActions.add(PlayerAction.Hit())
        playerActions.add(PlayerAction.Stand())
        playerActions.add(PlayerAction.X2Bet())

        if (playerAndHand.value.canSplit()) {
            playerActions.add(PlayerAction.Split())
        }

        return playerActions
    }

    private fun hasPlayerAlive(): Boolean {
        Log.d("BJ", "hasPlayerAlive")
        var hasPlayerAlive = false

        hands.forEach {
            if (it.value.hasCards()) {
                hasPlayerAlive = true
            }
        }

        return hasPlayerAlive
    }

    private fun getNextPlayerAndHand(): Map.Entry<PlayerBlackJack, HandBlackJack>? {
        Log.d("BJ", "getNextPlayerAndHand")
        hands.forEach {
            if (it.value.hasCards() && !it.key.stand) {
                return it
            }
        }
        return null
    }

    private fun dealBids() {
        Log.d("BJ", "dealBids")
        bids.forEach {
            it.key.deal(it.value)
        }
    }

    private fun dealCards() {
        Log.d("BJ", "dealCards")
        hands = HashMap()
        players.forEach { hands[it] = HandBlackJack() }

        hands.forEach {
            it.value.addCard(takeCard())
        }

        banker.hand = HandBlackJack(true)
        banker.hand.addCard(takeCard())

        hands.forEach {
            it.value.addCard(takeCard())
        }
        banker.hand.addCard(takeCard())

        delegate.onBankerUpdated(banker)

    }

    private fun checkDirectWinners() {
        Log.d("BJ", "checkDirectWinners")
        hands.forEach {
            if (it.value.hasDirectWinner()) {
                delegate.onPlayerUpdated(it.key, it.value)
                bids[it.key]?.let { bid ->
                    it.key.winner(bid)
                    it.value.cleanHand()
                    deck.removeCards(it.value.cards)
                    delegate.onPlayerWinner(it.key, BlackJackPrizes(bid).blackJack())
                }
            }
        }

    }

    private fun takeCard(): Card {
        Log.d("BJ", "takeCard")
        if (!deck.canTakeCardFromTop()) {
            deck.shuffleDeckOnlyRemovedCards()
        }
        return deck.takeCardFromTop()
    }

}