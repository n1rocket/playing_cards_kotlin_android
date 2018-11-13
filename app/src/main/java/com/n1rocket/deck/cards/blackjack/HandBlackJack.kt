package com.n1rocket.deck.cards.blackjack

import com.n1rocket.deck.cards.Card
import com.n1rocket.deck.cards.Hand

class HandBlackJack(var isHiddenCard : Boolean = false) : Hand() {

    private fun getTotalMaxValue(withHiddenCards :Boolean = true ): Int {
        var totalValue: Int = 0
        var areThereACE: Boolean = false

        cards.forEach {
            if (withHiddenCards){
                areThereACE = it.value == Card.CardValue.ACE
                totalValue += getValue(it.value)
            }else{
                if (cards[0] == it) {
                    areThereACE = it.value == Card.CardValue.ACE
                    totalValue += getValue(it.value)
                }else if (!isHiddenCard){
                    areThereACE = it.value == Card.CardValue.ACE
                    totalValue += getValue(it.value)
                }
            }

        }

        if (totalValue > WIN_HAND_VALUE && areThereACE) {
            totalValue -= DIFFERENCE_ACE_VALUE
        }

        return totalValue
    }

    fun showCardHidden(){
        isHiddenCard = false
    }

    private fun getValue(value: Card.CardValue): Int {
        return when (value) {
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
        return getTotalMaxValue() == WIN_HAND_VALUE
    }

    fun canSplit(): Boolean {
        var canSplit = false
        if (cards.size == 2) {
            canSplit = cards[0].value == cards[1].value
        }
        return canSplit
    }

    override fun toString(): String {
        var string = "Hand: ["

        cards.forEach {

            string += if (cards[0] == it || !isHiddenCard) {
                "<$it>"
            }else{
                "<HIDDEN>"
            }
        }
        string += "] Value: ${this.getTotalMaxValue(false)}"
        return string
    }

    fun isBust() : Boolean {
        return getTotalMaxValue() > WIN_HAND_VALUE
    }

    fun arriveToBankerLimit(): Boolean {
        return getTotalMaxValue() in 17..21
    }

    fun compareTo(vsHand: HandBlackJack): Int {
        return this.getTotalMaxValue().compareTo(vsHand.getTotalMaxValue())
    }

    companion object {
        const val WIN_HAND_VALUE: Int = 21
        const val DIFFERENCE_ACE_VALUE: Int = 10
    }

}
