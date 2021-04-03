package com.vahagn.android_mid_homework_10

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat

class Ex3 : AppCompatActivity() {
    private var field = arrayOf(
        arrayOf(Pair(' ', R.id.imageView2), Pair(' ', R.id.imageView3), Pair(' ', R.id.imageView4)),
        arrayOf(Pair(' ', R.id.imageView5), Pair(' ', R.id.imageView6), Pair(' ', R.id.imageView7)),
        arrayOf(Pair(' ', R.id.imageView8), Pair(' ', R.id.imageView9), Pair(' ', R.id.imageView10))
    )

    private val players = arrayOf(Pair('x', Pair(R.drawable.ic_x_24, R.id.xAvatar)), Pair('o', Pair(R.drawable.ic_o_24, R.id.oAvatar)))
    private var whichTurn = 0
    private var endGame = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ex3)

        findViewById<TextView>(R.id.p1Name).text = MainActivity.players[0].name
        findViewById<TextView>(R.id.p2Name).text = MainActivity.players[1].name
    }

    //*****************************************************
    //Does not return anything if the field is already busy
    //*****************************************************
    private fun findIndexes(id: Int): Pair<Int, Int>? {
        field.forEachIndexed { x, arrayOfPairs ->
            arrayOfPairs.forEachIndexed { y, cell ->
                if(id == cell.second && cell.first == ' ') return Pair(x, y)
            }
        }

        return null
    }

    fun move(view: View) {
        if(endGame) return
        findIndexes(view.id)?.apply {
            (view as ImageView).setImageDrawable(ContextCompat.getDrawable(this@Ex3, players[whichTurn].second.first))
            field[this.first][this.second] = Pair(players[whichTurn].first, field[this.first][this.second].second)
            if(hasWon(this.first, this.second)) {
                endGame = true
                Toast.makeText(this@Ex3,"${MainActivity.players[whichTurn].name} win!", Toast.LENGTH_SHORT).show()
                MainActivity.players[whichTurn].score++
            }

            changeTurn()
        }
    }

    private fun changeTurn() {
        whichTurn = (whichTurn + 1) % 2
        setTurnVisually(whichTurn)
    }

    private fun setTurnVisually(playerIndex: Int) {
        ConstraintSet().apply {
            val baseLayout = findViewById<ConstraintLayout>(R.id.baseLayout)
            clone(baseLayout)
            connect(R.id.turnArrow, ConstraintSet.START, players[playerIndex].second.second, ConstraintSet.START)
            connect(R.id.turnArrow, ConstraintSet.END, players[playerIndex].second.second, ConstraintSet.END)
            applyTo(baseLayout)
        }
    }

    private fun hasWon(x: Int, y: Int): Boolean {
        val directions = arrayOf(Pair(1,1), Pair(0, 1), Pair(1, 0), Pair(1, -1))
        val coefficients = arrayOf(-2, -1, 1, 2)
        val curSign = field[x][y].first

        var neighborsCount: Int
        directions.forEach { dir ->
            neighborsCount = 0

            coefficients.forEach { coef ->
                val curX = x + coef * dir.first
                val curY = y + coef * dir.second
                if(curX in 0..2 && curY in 0..2 && field[curX][curY].first == curSign)
                    neighborsCount++
            }

            if(neighborsCount == 2) return true
        }

        return false
    }

    fun reset(view: View) {
        field = arrayOf(
            arrayOf(Pair(' ', R.id.imageView2), Pair(' ', R.id.imageView3), Pair(' ', R.id.imageView4)),
            arrayOf(Pair(' ', R.id.imageView5), Pair(' ', R.id.imageView6), Pair(' ', R.id.imageView7)),
            arrayOf(Pair(' ', R.id.imageView8), Pair(' ', R.id.imageView9), Pair(' ', R.id.imageView10))
        )

        field.forEach {
            it.forEach {
                findViewById<ImageView>(it.second).setImageDrawable(null)
            }
        }

        endGame = false
        whichTurn = 0
        setTurnVisually(0)
    }
}