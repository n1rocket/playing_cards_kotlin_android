package com.n1rocket.deck.cards.blackjack

import com.n1rocket.deck.cards.Card
import org.junit.Assert
import org.junit.Test

class HandBlackJackTest {

    @Test
    fun getTotalMaxValue() {
        val valueExpected = 20
        val handBlackJack = HandBlackJack()
        handBlackJack.addCards(listOf(Card.THREE_S, Card.KING_C, Card.ACE_S, Card.SIX_C))

        Assert.assertEquals(valueExpected, handBlackJack.getTotalMaxValue())
    }
}