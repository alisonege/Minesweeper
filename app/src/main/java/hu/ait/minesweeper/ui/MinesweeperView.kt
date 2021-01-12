package hu.ait.minesweeper.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import hu.ait.minesweeper.MainActivity
import hu.ait.minesweeper.R
import hu.ait.minesweeper.model.MinesweeperModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlin.math.log

class MinesweeperView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    var paintBackground = Paint()
    var paintLine = Paint()
    var paintText = Paint()
    var result = String()

    init{
        paintBackground.color = Color.DKGRAY
        paintBackground.style = Paint.Style.FILL

        paintLine.color = Color.WHITE
        paintLine.style = Paint.Style.STROKE
        paintLine.strokeWidth = 5f

        paintText.color = Color.WHITE
        paintText.textSize = 60f

    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawRect(0f, 0f, width.toFloat(),
            height.toFloat(), paintBackground)

        drawBoard(canvas)

        drawField(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        paintText.textSize = height/5f
    }

    private fun drawBoard(canvas: Canvas?) {
        paintLine.style = Paint.Style.STROKE
        paintLine.color = Color.WHITE
        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintLine)

        drawHorizontalLines(canvas)
        drawVerticalLines(canvas)
    }

    private fun drawMinesAround(canvas: Canvas?, x: Int, y: Int){
        if(MinesweeperModel.getFieldClickContent(x, y) &&
            MinesweeperModel.getFieldContent(x,y) != MinesweeperModel.MINE){
            drawText(canvas, x, y)
        }
        else if(MinesweeperModel.getFieldClickContent(x, y) &&
                    MinesweeperModel.getFieldContent(x,y) == MinesweeperModel.MINE){
            drawCircle(canvas, x, y)
        }
    }

    private fun drawText(canvas: Canvas?, x: Int, y: Int){
        val myX = (x * width / 5f + width/20f)
        val myY = (y * height / 5f + height / 6f + y)
        canvas?.drawText(MinesweeperModel.getFieldMineCount(x,y).toString(),
            myX, myY, paintText)
    }

    private fun drawVerticalLines(canvas: Canvas?){
        for(i in 1..4){
            canvas?.drawLine(
                ((i * width) / 5).toFloat(), 0f, ((i *width) / 5).toFloat(), height.toFloat(),
                paintLine)
        }
    }

    private fun drawHorizontalLines(canvas: Canvas?){
        for(i in 1..4){
            canvas?.drawLine(
                0f, ((i *height) / 5).toFloat(), width.toFloat(), ((i *height) / 5).toFloat(),
                paintLine)
        }
    }

    private fun drawCircle(canvas: Canvas?, x: Int, y: Int){
        val centerX = (x * width / 5 + width / 10).toFloat()
        val centerY = (y * height / 5 + height / 10).toFloat()
        val radius = height / 10 - 3
        paintLine.color = Color.BLACK
        paintLine.style = Paint.Style.FILL

        canvas?.drawCircle(centerX, centerY, radius.toFloat(), paintLine)
    }


    private fun drawField(canvas: Canvas?) {
        for (i in 0..4) {
            for (j in 0..4) {
                drawMinesAround(canvas, i, j)
            }
        }
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            val tX = event.x.toInt() / (width / 5)
            val tY = event.y.toInt() / (height / 5)

            if (tX < 5 && tY < 5 && (MinesweeperModel.getFieldClickContent(tX, tY) == false))
            {
                MinesweeperModel.setFieldClickContent(tX, tY)
                checkGuess(tX, tY)
                invalidate()
            } else if(MinesweeperModel.getFieldClickContent(tX, tY)){
                Toast.makeText((context as MainActivity),
                    context.resources.getString(R.string.warning), Toast.LENGTH_LONG).show()
            }
        }
        return true
    }


    fun checkGuess(guessX:Int, guessY: Int) {
        if(MinesweeperModel.getNextGuess() == MinesweeperModel.TRY ) {
            checkTryGuess(guessX, guessY)
        }else if(MinesweeperModel.getNextGuess() == MinesweeperModel.FLAG){
            checkFlagGuess(guessX, guessY)
        }
    }

    private fun checkTryGuess(guessX: Int, guessY: Int){
        if (MinesweeperModel.getFieldContent(guessX, guessY) == MinesweeperModel.MINE) {
            result =  "You just hit a mine. GAME OVER!!! Press restart"
            Toast.makeText((context as MainActivity), result, Toast.LENGTH_LONG).show()
            invalidate()

        }else{
            checkNeighbors(guessX, guessY)
        }
    }

    private fun checkFlagGuess(guessX: Int, guessY: Int){
        if (MinesweeperModel.getFieldContent(guessX, guessY) == MinesweeperModel.MINE) {
            MinesweeperModel.setFieldFlagContent(guessX, guessY)
            flagIsMine()
        } else {
            result = "You guessed wrong :( GAME OVER"
            Toast.makeText((context as MainActivity), result, Toast.LENGTH_LONG).show()
            invalidate()
        }
    }

    private fun flagIsMine(){
        if(MinesweeperModel.allMinesHit()){
            result=  "YOU WON!! Press restart to play again"
            Toast.makeText((context as MainActivity), result, Toast.LENGTH_LONG).show()
            invalidate()
        }else{
            result ="You guessed correct!"
            Toast.makeText((context as MainActivity), result, Toast.LENGTH_LONG).show()
        }
    }

    private fun checkNeighbors(x: Int, y: Int){
        val allPossibleNeighbors = arrayOf(Pair(x-1, y-1), Pair(x-1, y), Pair(x-1, y+1),
            Pair(x, y-1), Pair(x, y+1), Pair(x+1, y-1), Pair(x+1, y), Pair(x+1, y+1))

        for(pair in allPossibleNeighbors){
            if(pair.first in 0..4 && pair.second in 0..4){
                if(MinesweeperModel.getFieldContent(pair.first, pair.second) == MinesweeperModel.MINE) {
                    MinesweeperModel.setFieldMineCount(x, y, 1)
                }
            }
        }
    }

    fun restart(){
        MinesweeperModel.resetModel()
        invalidate()
    }


}