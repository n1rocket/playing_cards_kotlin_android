package com.n1rocket.deck.blackjack

import com.n1rocket.deck.Card
import com.n1rocket.deck.Deck
import com.n1rocket.deck.LoggerInterface

class BlackJackGame(private val delegate: BlackJackGameDelegate, private val logger: LoggerInterface) {
    lateinit var deck: Deck
    private lateinit var players: List<PlayerBlackJack>
    private lateinit var banker: BankerBlackJack
    private lateinit var hands: HashMap<PlayerBlackJack, HandBlackJack>
    private lateinit var bids: HashMap<PlayerBlackJack, Double>

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
        fun onBankerBust()
        fun onPlayerDraw(player: PlayerBlackJack, prize: Double)
        fun onPlayerLose(player: PlayerBlackJack, bid: Double)
        fun onBankerWinner()
    }

    sealed class PlayerAction {
        class Hit : PlayerAction()
        class Stand : PlayerAction()
        class X2Bet : PlayerAction()
        class Split : PlayerAction()
    }

    fun startRound(bids: HashMap<PlayerBlackJack, Double>) {
        this.deck = Deck()
        this.hands = HashMap()
        this.players = ArrayList()
        logger.d("BJ", "startRound")
        this.bids = bids
        resetStandPlayers()
        this.banker = BankerBlackJack("Rodrigo Rato")
        this.players = bids.keys.toList()
        dealBids()
        this.deck.shuffleDeck()
        dealCards()
        checkDirectWinners()

        if (hasPlayerAlive()) {
            nextPlayer()
        }else{
            cleanBanker()
        }
    }

    private fun resetStandPlayers() {
        this.bids.forEach {
            it.key.doStand(false)
        }
    }


    fun doPlayerAction(player: PlayerBlackJack, action: PlayerAction) {
        logger.d("BJ", "doPlayerAction")

        when (action) {
            is PlayerAction.Hit -> {
                hit(player)
            }
            is PlayerAction.X2Bet -> {
                x2Bet(player)
            }
            is PlayerAction.Stand -> {
                stand(player)
            }
            is PlayerAction.Split -> {
                split(player)
            }
        }
    }

    private fun hit(player: PlayerBlackJack) {
        logger.d("BJ", "hit")
        hands[player]?.let {
            it.addCard(takeCard())
            delegate.onPlayerUpdated(player, it)
            checkIsBust(player, it)
        }
    }

    private fun stand(player: PlayerBlackJack) {
        logger.d("BJ", "stand")
        player.doStand()
        nextPlayer()
    }

    private fun x2Bet(player: PlayerBlackJack) {
        logger.d("BJ", "x2Bet")
        bids[player]?.run {
            this * 2
        }
        addCardToPlayer(player)
    }

    private fun addCardToPlayer(player: PlayerBlackJack) {
        hands[player]?.let {
            it.addCard(takeCard())
            delegate.onPlayerUpdated(player, it)
            if (!checkIsBust(player, it)) {
                stand(player)
            }
        }
    }

    private fun split(player: PlayerBlackJack) {
        logger.d("BJ", "split")

    }

    private fun checkIsBust(player: PlayerBlackJack, hand: HandBlackJack): Boolean {
        if (hand.isBust()) {
            delegate.onPlayerBust(player)
            playerLose(player, hand)
        }
        return hand.isBust()
    }

    private fun nextPlayer() {
        logger.d("BJ", "nextPlayer")

        getNextPlayerAndHand()?.let {
            delegate.onTurn(it.key, it.value, getPlayerActions(it))
        } ?: this.goBanker()
    }

    private fun goBanker() {
        logger.d("BJ", "goBanker")

        if (hasPlayerAlive()) {

            banker.hand.showCardHidden()
            delegate.onBankerUpdated(banker)

            while (!banker.hand.arriveToBankerLimit() && !banker.hand.isBust()) {
                logger.d("BJ", "!arriveToBankerLimit && !isBust")

                banker.hand.addCard(takeCard())
                delegate.onBankerUpdated(banker)
            }

            if (banker.hand.isBust()) {
                cleanBanker()
                delegate.onBankerBust()
                playerAliveWinners()
            } else {
                checkBankerHandVsPlayersAliveHand()
            }
        } else {
            bankerWinner()
        }

    }


    private fun checkBankerHandVsPlayersAliveHand() {
        hands.filter { it.key.stand }.forEach {

            logger.d("BJ", "Comparation: ${banker.hand.compareTo(it.value)}")

            when (banker.hand.compareTo(it.value)) {
                -1 -> playerWinner(it.key, it.value, BlackJackPrizes.Simple())
                0 -> playerDraw(it.key, it.value, BlackJackPrizes.Draw())
                1 -> bankerWinnerToPlayer(it.key, it.value)
            }
        }
        cleanBanker()
    }

    private fun playerDraw(
        player: PlayerBlackJack,
        hand: HandBlackJack,
        prize: BlackJackPrizes
    ) {
        bids[player]?.let { bid ->
            player.winner(bid)
            deck.removeCards(hand.cards)
            hand.cleanHand()
            delegate.onPlayerDraw(player, prize.getPrize(bid))
        }
    }

    private fun playerWinner(
        player: PlayerBlackJack,
        hand: HandBlackJack,
        prize: BlackJackPrizes
    ) {
        bids[player]?.let { bid ->
            player.winner(bid)
            deck.removeCards(hand.cards)
            hand.cleanHand()
            delegate.onPlayerWinner(player, prize.getPrize(bid))
        }
    }


    private fun playerLose(
        player: PlayerBlackJack,
        hand: HandBlackJack
    ) {
        bids[player]?.let { bid ->
            player.lose(bid)
            deck.removeCards(hand.cards)
            hand.cleanHand()
            delegate.onPlayerLose(player, bid)
        }
    }

    private fun bankerWinner() {
        cleanBanker()
        delegate.onBankerWinner()
    }

    private fun bankerWinnerToPlayer(
        player: PlayerBlackJack,
        hand: HandBlackJack
    ) {
        playerLose(player, hand)
    }

    private fun cleanBanker() {
        banker.hand.let {
            deck.removeCards(it.cards)
            it.cleanHand()
        }
    }

    private fun playerAliveWinners() {
        hands.filter { it.key.stand }.forEach {
            delegate.onPlayerUpdated(it.key, it.value)
            playerWinner(it.key, it.value, BlackJackPrizes.Simple())
        }
    }

    private fun getPlayerActions(playerAndHand: Map.Entry<PlayerBlackJack, HandBlackJack>): List<PlayerAction> {
        logger.d("BJ", "getPlayerActions")
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
        logger.d("BJ", "hasPlayerAlive")
        var hasPlayerAlive = false

        hands.forEach {
            if (it.value.hasCards()) {
                hasPlayerAlive = true
            }
        }

        return hasPlayerAlive
    }

    private fun getNextPlayerAndHand(): Map.Entry<PlayerBlackJack, HandBlackJack>? {
        logger.d("BJ", "getNextPlayerAndHand")
        hands.forEach {
            if (it.value.hasCards() && !it.key.stand) {
                return it
            }
        }
        return null
    }

    private fun dealBids() {
        logger.d("BJ", "dealBids")
        bids.forEach {
            it.key.deal(it.value)
        }
    }

    private fun dealCards() {
        logger.d("BJ", "dealCards")
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
        logger.d("BJ", "checkDirectWinners")
        hands.forEach {
            if (it.value.hasDirectWinner()) {
                delegate.onPlayerUpdated(it.key, it.value)
                playerWinner(it.key, it.value, BlackJackPrizes.BlackJack())
            }
        }

    }

    private fun takeCard(): Card {
        logger.d("BJ", "takeCard")
        if (!deck.canTakeCardFromTop()) {
            deck.shuffleDeckOnlyRemovedCards()
        }
        return deck.takeCardFromTop()
    }

}