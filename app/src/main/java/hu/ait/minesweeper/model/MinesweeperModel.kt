package hu.ait.minesweeper.model

import hu.ait.minesweeper.ui.Field
import kotlin.random.Random.Default.nextInt

object MinesweeperModel {

    public val MINE: Int = 1
    public val FLAG: Int = 2
    public val TRY: Int = 3

    public var guess = TRY


    val model = arrayOf(
        arrayOf(
            Field(),
            Field(),
            Field(),
            Field(),
            Field()
        ),
        arrayOf(
            Field(),
            Field(),
            Field(),
            Field(),
            Field()
        ),
        arrayOf(
            Field(),
            Field(),
            Field(),
            Field(),
            Field()
        ),
        arrayOf(
            Field(),
            Field(),
            Field(),
            Field(),
            Field()
        ),
        arrayOf(
            Field(),
            Field(),
            Field(),
            Field(),
            Field()
        )
    )


    fun getFieldMineCount(x: Int, y: Int) = model[x][y].getMinesAround()

    fun setFieldMineCount(x:Int, y:Int, count: Int){
        model[x][y].setMinesAround(count)
    }

    fun getFieldFlagContent(x: Int, y: Int) = model[x][y].isFlagged()

    fun setFieldFlagContent(x: Int, y: Int){
        model[x][y].setIsFlagged()
    }

    fun getFieldClickContent(x: Int, y: Int) = model[x][y].wasClicked()

    fun setFieldClickContent(x: Int, y: Int){
        model[x][y].setClicked()
    }

    fun getFieldContent(x: Int, y: Int) = model[x][y].getType()


    fun placeMines(){
        var numMines = 0
        while(numMines < 3){
            var randX = nextInt(5)
            var randY = nextInt(5)
            if(model[randX][randY].getType() != MINE){
                model[randX][randY].setType(MINE)
                numMines++
            }
        }
    }

    fun allMinesHit(): Boolean{
        var minesCount = 0
        for(i in 0..4){
            for (j in 0..4){
                if(getFieldFlagContent(i,j)){
                    minesCount++
                }
            }
        }
        if(minesCount == 3){
            return true
        }
        return false
    }

    fun changeToggle(){
        guess = if (guess == TRY) FLAG else TRY
    }

    fun getNextGuess() = guess

    fun resetModel(){
        for (i in 0..4){
            for (j in 0..4){
                model[i][j] = Field()
            }
        }
    }


}