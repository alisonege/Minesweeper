package hu.ait.minesweeper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.ait.minesweeper.model.MinesweeperModel
import hu.ait.minesweeper.ui.MinesweeperView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MinesweeperModel.placeMines()

        tbFlag.setOnClickListener() {
            MinesweeperModel.changeToggle()

        }

        btnRestart.setOnClickListener(){
            mineView.restart()
            MinesweeperModel.placeMines()
        }


    }



}