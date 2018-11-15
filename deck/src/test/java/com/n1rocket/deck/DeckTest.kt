package com.n1rocket.deck

import org.junit.Assert
import org.junit.Test

class DeckTest {

    private lateinit var deck: Deck

    @Test
    fun canTakeCardFromTop() {
        deck = Deck()
        Assert.assertEquals(true, deck.canTakeCardFromTop())
    }

    @Test
    fun cannotTakeCardFromTop() {
        deck = Deck()

        while (deck.canTakeCardFromTop()) {
            deck.takeCardFromTop()
        }
        Assert.assertEquals(false, deck.canTakeCardFromTop())
    }

    @Test
    fun takeCardFromTop() {
        deck = Deck()
        val card = deck.whichCardIsTheNext()

        Assert.assertEquals(card, deck.takeCardFromTop())
    }

    @Test
    fun restart() {
        deck = Deck()
        val totalCardsAtStart = deck.cardsLeft()
        deck.takeCardFromTop()
        deck.restart()

        Assert.assertEquals(totalCardsAtStart, deck.cardsLeft())
    }

    @Test
    fun shuffleDeck() {
        deck = Deck()

        val copyCards = deck.cards.toList()
        deck.shuffleDeck()
        //For check all cards
        //println("CopyCards:")
        //copyCards.forEach { card -> println(card.toString()) }
        //println("Deck.cards:")
        //deck.cards.forEach { card -> println(card.toString()) }

        Assert.assertNotEquals(copyCards, deck.cards)
    }

    @Test
    fun removeCard() {
        deck = Deck()
        val cardToRemove = deck.takeCardFromTop()
        deck.removeCard(cardToRemove)

        Assert.assertEquals(listOf(cardToRemove), deck.removed)
    }

    @Test
    fun shuffleDeckWithRemovedCards() {
        deck = Deck()

        var card: Card

        do {
            card = deck.takeCardFromTop()
        } while (deck.canTakeCardFromTop())

        deck.removeCard(card)
        deck.shuffleDeckOnlyRemovedCards()

        Assert.assertEquals(listOf(card), deck.cards)
    }

    @Test
    fun cardsLeft() {
        deck = Deck()

        val expectedCardsLeft = deck.cards.size - 1

        deck.takeCardFromTop()

        Assert.assertEquals(expectedCardsLeft, deck.cardsLeft())
    }
}