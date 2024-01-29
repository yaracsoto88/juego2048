package com.example.a2048;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class game2048 extends AppCompatActivity {
    Tablero board;
    private TableLayout tableLayout;
    private GestureDetector mGestureDetector;
    private Button btNewGame;
    private int height = 4;
    private int width = 4;
    TextView score;
    String puntos;
    private Button btUndo;
    private TextView tvTimer;
    private Handler handler;
    private int segundos=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2048);
        score = findViewById(R.id.score);
        tableLayout = findViewById(R.id.tableLayout);
        board = new Tablero(height, width, tableLayout,this);
        mGestureDetector = new GestureDetector(this, new EscucharGestos());
        btNewGame = findViewById(R.id.btNewGame);
        btNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                board = new Tablero(height, width, tableLayout, game2048.this);

            }
        });
        btUndo=findViewById(R.id.btUndo);
        btUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                board.undo();
            }
        });
        tvTimer=findViewById(R.id.tvTimer);
        handler = new Handler();
        actualizarTiempo();
    }

    private void actualizarTiempo() {
        int minutosActuales = segundos / 60;
        int segundosActuales = segundos % 60;

        // Formatear los minutos y segundos con dos dígitos
        String tiempoFormateado = String.format("%02d:%02d", minutosActuales, segundosActuales);

        // Establecer el texto formateado en tu TextView
        tvTimer.setText(tiempoFormateado);

        segundos++;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                actualizarTiempo();
            }
        }, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Detener el temporizador al salir de la actividad
        handler.removeCallbacksAndMessages(null);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class EscucharGestos extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
            float ancho = Math.abs(e2.getX() - e1.getX());
            float alto = Math.abs(e2.getY() - e1.getY());

            if (ancho > alto) {
                if (e2.getX() > e1.getX()) {
                    board.right();
                    puntos = ("" + board.getScore());
                    score.setText(puntos);
                    results();

                } else {
                    board.left();
                    puntos = ("" + board.getScore());
                    score.setText(puntos);
                    results();
                }
            } else {
                if (e1.getY() > e2.getY()) {
                    board.up();
                    puntos = ("" + board.getScore());
                    score.setText(puntos);
                    results();
                } else {
                    board.down();
                    puntos = ("" + board.getScore());
                    score.setText(puntos);
                    results();
                }
            }
            return true;
        }
    }


    private void results() {
        if (board.gameWon()) {
            Toast.makeText(this, "Has ganado", Toast.LENGTH_SHORT).show();
        }
        if (board.gameLost()) {
            Toast.makeText(this, "Has perdido", Toast.LENGTH_SHORT).show();
        }

    }
}
