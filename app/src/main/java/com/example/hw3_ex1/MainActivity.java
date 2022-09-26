package com.example.hw3_ex1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button[] [] buttons;
    private TicTacToe tttGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tttGame = new TicTacToe();
        buildGUIbyCode ();
    }

    private void buildGUIbyCode() {

        //get width of the screen
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int w = size.x/TicTacToe.SIDE;


        // create the layout manager as a gridlayout
        GridLayout gridLayout = new GridLayout(this);
        gridLayout.setColumnCount(TicTacToe.SIDE);
        gridLayout.setRowCount(TicTacToe.SIDE);

        // create the buttons and add them to the Gridlayout
        buttons = new Button[TicTacToe.SIDE][TicTacToe.SIDE];
        ButtonHandler bh = new ButtonHandler();

        for (int row = 0; row <TicTacToe.SIDE; row ++){
            for ( int col = 0; col< TicTacToe.SIDE; col ++) {
                buttons[row][col] = new Button(this);
                buttons[row][col].setTextSize((int)(w *2));
                buttons[row][col].setOnClickListener(bh);
                gridLayout.addView(buttons [row] [col], w, w);
            }
        }
        // set layout parameters
        TextView status = new TextView(this);
        GridLayout.Spec rowSpec = GridLayout.spec(TicTacToe.SIDE, 1);
        GridLayout.Spec columnSpec = GridLayout.spec(0, TicTacToe.SIDE);
        GridLayout.LayoutParams lpStatus
                = new GridLayout.LayoutParams(rowSpec, columnSpec);
        status.setLayoutParams(lpStatus);

        // set up status characteristics
        status.setWidth(TicTacToe.SIDE * w);
        status.setHeight(w);
        status.setGravity(Gravity.CENTER);
        status.setBackgroundColor(Color.GREEN);
        status.setTextSize((int) (w * .15));
        status.setText(tttGame.result());

        gridLayout.addView(status);


        // set the gridlayout as the View of this activity
        setContentView(gridLayout);

    }

    private class ButtonHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.w("MainActivity", "Inside onClick, v=" + v);
            for (int row = 0; row < TicTacToe.SIDE; row ++)
                for (int column = 0; column < TicTacToe.SIDE; column ++)
                    if (v == buttons [row][column])
                        update(row,column);
        }
    }

    private void update(int row, int column) {
        Log.w("MainActivity", "Inside update : " + row + ", " + column);
        //buttons[row][column].setText("X");
        int play = tttGame.play(row, column);
        if (play == 1)
            buttons[row][column].setText("X");
        else if (play == 2)
            buttons[row][column].setText("O");
        if (tttGame.isGameOver())
            status.setBackgroundColor(Color.RED);
            enableButtons(false);
            status.setText(tttGame.result());
            showNewGameDialog();
    }

    private void showNewGameDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("This is fun");
        alert.setMessage("Play again ?");
        PlayDialog playAgain = new PlayDialog();
        alert.setPositiveButton("YES", playAgain);
        alert.setNegativeButton("NO", playAgain);
    }

    private void enableButtons(boolean b) {
        boolean enabled = false;
        for (int row = 0; row < TicTacToe.SIDE; row ++)
            for (int column = 0; column < TicTacToe.SIDE; column ++)
                buttons[row][column].setEnabled(enabled);
    }

    private class PlayDialog implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if (i == -1) {
                tttGame.resetGame();
                enableButtons(true);
                resetButton(); }

            else if(i == -2)
                MainActivity.this.finish();
        }

        private void resetButton() {
            for (int row = 0; row < TicTacToe.SIDE; row ++)
                for (int column = 0; column < TicTacToe.SIDE; column ++)
                    buttons[row][column].setText("");
        }
    }
}