package com.n1rocket.deck.cards.blackjack

import com.n1rocket.deck.cards.Card
import com.n1rocket.deck.cards.Hand

class HandBlackJack : Hand() {

    fun getTotalMaxValue(): Int {
        var totalValue:Int = 0
        var areThereACE:Boolean = false

        cards.forEach {
            areThereACE = it.value == Card.CardValue.ACE
            totalValue += getValue(it.value)
        }

        if (totalValue > WIN_HAND_VALUE && areThereACE){
            totalValue -= DIFFERENCE_ACE_VALUE
        }

        return totalValue
    }

    private fun getValue(value: Card.CardValue) : Int{
        return when(value){
            Card.CardValue.ACE -> 11
            Card.CardValue.KING -> 10
            Card.CardValue.QUEEN -> 10
            Card.CardValue.JACK -> 10
            Card.CardValue.TEN -> 10
            Card.CardValue.NINE -> 9
            Card.CardValue.EIGHT -> 8
            Card.CardValue.SEVEN -> 7
            Card.CardValue.SIX -> 6
            Card.CardValue.FIVE -> 5
            Card.CardValue.FOUR -> 4
            Card.CardValue.THREE -> 3
            Card.CardValue.TWO -> 2
        }
    }

    fun hasDirectWinner(): Boolean {
        val totalValue = getTotalMaxValue()
        return totalValue == WIN_HAND_VALUE
    }

    companion object {
        const val WIN_HAND_VALUE: Int = 21
        const val DIFFERENCE_ACE_VALUE: Int = 10
    }

}
