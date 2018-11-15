package com.n1rocket.deck

open class Player(var name: String, var money: Double){

    private fun addMoney(money: Double){
        this.money += money
    }

    private fun removeMoney(money: Double){
        this.money -= money
    }

    fun deal(money: Double){
        removeMoney(money)
    }

    fun lose(money: Double){
        removeMoney(money)
    }

    fun winner(money: Double){
        addMoney(money)
    }
}