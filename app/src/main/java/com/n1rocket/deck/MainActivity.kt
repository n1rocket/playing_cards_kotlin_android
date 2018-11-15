package com.n1rocket.deck

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.n1rocket.deck.blackjack.BankerBlackJack
import com.n1rocket.deck.blackjack.BlackJackGame
import com.n1rocket.deck.blackjack.HandBlackJack
import com.n1rocket.deck.blackjack.PlayerBlackJack
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var blackJackGame : BlackJackGame

    private lateinit var actualPlayer: PlayerBlackJack
    private val player1: PlayerBlackJack = PlayerBlackJack("Jon Snow", 5000.0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fabHit.setOnClickListener { blackJackGame.doPlayerAction(actualPlayer, BlackJackGame.PlayerAction.Hit()) }
        fabDouble.setOnClickListener { blackJackGame.doPlayerAction(actualPlayer, BlackJackGame.PlayerAction.X2Bet()) }
        fabStand.setOnClickListener { blackJackGame.doPlayerAction(actualPlayer, BlackJackGame.PlayerAction.Stand()) }
        fabReset.setOnClickListener { startBlackJack() }

        startBlackJack()
    }

    fun startBlackJack(){
        blackJackGame = BlackJackGame(object : BlackJackGame.BlackJackGameDelegate {
            override fun onBankerWinner() {
                hideActions()
                showMessage("Banker Winner!")
            }

            override fun onPlayerLose(player: PlayerBlackJack, bid: Double) {
                hideActions()
                showMessage("${player.name} Lose!")
            }

            override fun onPlayerDraw(player: PlayerBlackJack, prize: Double) {
                Log.d("BlackJack", "onPlayerDraw: ${player.name}, $prize€")
                hideActions()
                showMessage("${player.name} Draw!")
            }

            override fun onBankerBust() {
                hideActions()
                Log.d("BlackJack", "onBankerBust")
            }

            override fun onPlayerUpdated(player: PlayerBlackJack, handBlackJack: HandBlackJack) {
                Log.d("BlackJack", "onPlayerUpdated: ${player.name}, $handBlackJack")

                showPlayer(player, handBlackJack)
            }

            override fun onPlayerBust(player: PlayerBlackJack) {
                Log.d("BlackJack", "onPlayerBust: ${player.name}")
                hideActions()
                showMessage("${player.name} Bust!")
            }

            override fun onBankerUpdated(banker: BankerBlackJack) {
                Log.d("BlackJack", "onBankerUpdated: ${banker.name}, ${banker.hand}")
                showBanker(banker)
            }

            override fun onTurn(
                player: PlayerBlackJack,
                handBlackJack: HandBlackJack,
                playerActions: List<BlackJackGame.PlayerAction>
            ){
                Log.d("BlackJack", "onTurn: ${player.name}, $handBlackJack")

                actualPlayer = player
                showPlayer(player, handBlackJack)
                showActions(playerActions)
            }

            override fun onPlayerWinner(player: PlayerBlackJack, prize: Double) {
                Log.d("BlackJack", "onPlayerWinner: ${player.name}, $prize€")
                hideActions()
                showMessage("${player.name} Win!")
            }
        }, object : LoggerInterface {
            override fun d(LOGTAG: String, message: String) {
                Log.d(LOGTAG, message)
            }
        })

        val bids: HashMap<PlayerBlackJack, Double> = hashMapOf(
            Pair(player1, 100.0)
        )

        blackJackGame.startRound(bids)
    }

    private fun showMessage(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
    }

    private fun showBanker(banker: BankerBlackJack) {
        bankerName.text = banker.name
        bankerHand.text = banker.hand.toString()
    }

    private fun showActions(playerActions: List<BlackJackGame.PlayerAction>) {
        playerActions.forEach {
            when(it){
                is BlackJackGame.PlayerAction.Hit -> fabHit.show()
                is BlackJackGame.PlayerAction.X2Bet -> fabDouble.show()
                is BlackJackGame.PlayerAction.Stand -> fabStand.show()
            }
        }
    }

    private fun hideActions() {
        fabHit.hide()
        fabDouble.hide()
        fabStand.hide()
    }

    private fun showPlayer(player: PlayerBlackJack, hand: HandBlackJack) {
        playerName.text = player.name
        playerHand.text = hand.toString()
    }
}
