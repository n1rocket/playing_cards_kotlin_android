package com.n1rocket.deck

enum class Card private constructor(var value: CardValue, var suit: CardSuit) {

    ACE_S(CardValue.ACE, CardSuit.SPADES),

    ACE_H(CardValue.ACE, CardSuit.HEARTS),

    ACE_C(CardValue.ACE, CardSuit.CLUBS),

    ACE_D(CardValue.ACE, CardSuit.DIAMONDS),

    KING_S(CardValue.KING, CardSuit.SPADES),

    KING_H(CardValue.KING, CardSuit.HEARTS),

    KING_C(CardValue.KING, CardSuit.CLUBS),

    KING_D(CardValue.KING, CardSuit.DIAMONDS),

    QUEEN_S(CardValue.QUEEN, CardSuit.SPADES),

    QUEEN_H(CardValue.QUEEN, CardSuit.HEARTS),

    QUEEN_C(CardValue.QUEEN, CardSuit.CLUBS),

    QUEEN_D(CardValue.QUEEN, CardSuit.DIAMONDS),

    JACK_S(CardValue.JACK, CardSuit.SPADES),

    JACK_H(CardValue.JACK, CardSuit.HEARTS),

    JACK_C(CardValue.JACK, CardSuit.CLUBS),

    JACK_D(CardValue.JACK, CardSuit.DIAMONDS),

    TEN_S(CardValue.TEN, CardSuit.SPADES),

    TEN_H(CardValue.TEN, CardSuit.HEARTS),

    TEN_C(CardValue.TEN, CardSuit.CLUBS),

    TEN_D(CardValue.TEN, CardSuit.DIAMONDS),

    NINE_S(CardValue.NINE, CardSuit.SPADES),

    NINE_H(CardValue.NINE, CardSuit.HEARTS),

    NINE_C(CardValue.NINE, CardSuit.CLUBS),

    NINE_D(CardValue.NINE, CardSuit.DIAMONDS),

    EIGHT_S(CardValue.EIGHT, CardSuit.SPADES),

    EIGHT_H(CardValue.EIGHT, CardSuit.HEARTS),

    EIGHT_C(CardValue.EIGHT, CardSuit.CLUBS),

    EIGHT_D(CardValue.EIGHT, CardSuit.DIAMONDS),

    SEVEN_S(CardValue.SEVEN, CardSuit.SPADES),

    SEVEN_H(CardValue.SEVEN, CardSuit.HEARTS),

    SEVEN_C(CardValue.SEVEN, CardSuit.CLUBS),

    SEVEN_D(CardValue.SEVEN, CardSuit.DIAMONDS),

    SIX_S(CardValue.SIX, CardSuit.SPADES),

    SIX_H(CardValue.SIX, CardSuit.HEARTS),

    SIX_C(CardValue.SIX, CardSuit.CLUBS),

    SIX_D(CardValue.SIX, CardSuit.DIAMONDS),

    FIVE_S(CardValue.FIVE, CardSuit.SPADES),

    FIVE_H(CardValue.FIVE, CardSuit.HEARTS),

    FIVE_C(CardValue.FIVE, CardSuit.CLUBS),

    FIVE_D(CardValue.FIVE, CardSuit.DIAMONDS),

    FOUR_S(CardValue.FOUR, CardSuit.SPADES),

    FOUR_H(CardValue.FOUR, CardSuit.HEARTS),

    FOUR_C(CardValue.FOUR, CardSuit.CLUBS),

    FOUR_D(CardValue.FOUR, CardSuit.DIAMONDS),

    THREE_S(CardValue.THREE, CardSuit.SPADES),

    THREE_H(CardValue.THREE, CardSuit.HEARTS),

    THREE_C(CardValue.THREE, CardSuit.CLUBS),

    THREE_D(CardValue.THREE, CardSuit.DIAMONDS),

    TWO_S(CardValue.TWO, CardSuit.SPADES),

    TWO_H(CardValue.TWO, CardSuit.HEARTS),

    TWO_C(CardValue.TWO, CardSuit.CLUBS),

    TWO_D(CardValue.TWO, CardSuit.DIAMONDS);

    override fun toString(): String {

        var s = ""

        when (value) {
            CardValue.ACE -> s = "A"

            CardValue.KING -> s = "K"

            CardValue.QUEEN -> s = "Q"

            CardValue.JACK -> s = "J"

            CardValue.TEN -> s = "T"

            CardValue.NINE -> s = "9"

            CardValue.EIGHT -> s = "8"

            CardValue.SEVEN -> s = "7"

            CardValue.SIX -> s = "6"

            CardValue.FIVE -> s = "5"

            CardValue.FOUR -> s = "4"

            CardValue.THREE -> s = "3"

            CardValue.TWO -> s = "2"
        }

        s += when (suit) {
            CardSuit.SPADES -> "s"

            CardSuit.HEARTS -> "h"

            CardSuit.CLUBS -> "c"

            CardSuit.DIAMONDS -> "d"
        }

        return s
    }

    enum class CardValue {
        ACE, KING, QUEEN, JACK, TEN, NINE, EIGHT, SEVEN, SIX, FIVE, FOUR, THREE, TWO
    }

    enum class CardSuit {
        SPADES, HEARTS, CLUBS, DIAMONDS
    }

    companion object {

        /**
         * get the ordinal of the card by the ordinal of the value and the ordinal
         * of the suit.

         * @param valueOrdinal
         * * 		The ordinal of the card value
         * *
         * @param suitOrdinal
         * * 		The ordinal of the card suit
         * *
         * @return value ordinal
         */
        fun ordinalByOrdinals(valueOrdinal: Int, suitOrdinal: Int): Int {
            return valueOrdinal * 4 + suitOrdinal
        }

        operator fun get(value: CardValue, suit: CardSuit): Card {
            return values()[ordinalByOrdinals(value.ordinal, suit.ordinal)]
        }

        /**
         * parses the string to find out what card it is. can handle codes like As,
         * Qh, Jc, Td, 9c and so on.

         * @param s
         * * 		String to be parsed
         * *
         * @return the card represented by the string
         */
        fun getFromString(s: String): Card {

            val c1 = s[0]
            val c2 = s[1]

            var value: CardValue? = null
            var suit: CardSuit? = null

            when (c1) {
                'A' -> value = CardValue.ACE
                'K' -> value = CardValue.KING
                'Q' -> value = CardValue.QUEEN
                'J' -> value = CardValue.JACK
                'T' -> value = CardValue.TEN
                '9' -> value = CardValue.NINE
                '8' -> value = CardValue.EIGHT
                '7' -> value = CardValue.SEVEN
                '6' -> value = CardValue.SIX
                '5' -> value = CardValue.FIVE
                '4' -> value = CardValue.FOUR
                '3' -> value = CardValue.THREE
                '2' -> value = CardValue.TWO
            }

            when (c2) {
                's' -> suit = CardSuit.SPADES
                'h' -> suit = CardSuit.HEARTS
                'c' -> suit = CardSuit.CLUBS
                'd' -> suit = CardSuit.DIAMONDS
            }

            if (value == null || suit == null) {
                throw IllegalArgumentException()
            }

            return Card[value, suit]
        }
    }
}