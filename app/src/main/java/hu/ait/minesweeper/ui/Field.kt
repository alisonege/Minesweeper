package hu.ait.minesweeper.ui

class Field {

    private var type: Int = 0
    private var minesAround: Int = 0
    private var isFlagged: Boolean = false
    private var wasClicked: Boolean = false

    fun getType() = type

    fun setType(newType: Int){
        type = newType
    }
    fun getMinesAround() = minesAround

    fun setMinesAround(count: Int){
        minesAround += count
    }

    fun isFlagged() = isFlagged

    fun setIsFlagged(){
        isFlagged = true
    }

    fun wasClicked() = wasClicked

    fun setClicked(){
        wasClicked = true
    }

}