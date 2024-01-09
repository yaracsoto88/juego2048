package com.example.a2048;

import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Random;

public class Tablero {
    private Casilla[][] board;
    private int score;
    private boolean gameOver = false;
    private TableLayout tableLayout;

    public Tablero(int height, int width, TableLayout tableLayout) {
        this.tableLayout = tableLayout;
        this.initBoard(height, width);
        this.conectarMatrizEnVista();
    }

    public void initBoard(int height, int width) {
        this.board = new Casilla[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                this.board[i][j] = new Casilla();
                this.board[i][j].setValor(0);
            }
        }
        addCasilla();

    }

    public void conectarMatrizEnVista() {
        for (int i = 0; i < 4; i++) {
            TableRow row = (TableRow) tableLayout.getChildAt(i);
            for (int j = 0; j < 4; j++) {
                TextView textView = (TextView) row.getChildAt(j);
                if (board[i][j].getValor() == 0) {
                    textView.setText("");
                } else {
                    textView.setText(String.valueOf(board[i][j].getValor()));
                }

            }
        }

    }

    public void addCasilla() {
        Random ran = new Random();
        int x, y;
        x = ran.nextInt(4);
        y = ran.nextInt(4);
        boolean empty = false;

        while (!empty) {
            if (board[x][y].getValor() == 0) {
                board[x][y].setValor(2);
                empty = true;
            } else {
                x = ran.nextInt(4);
                y = ran.nextInt(4);
            }
        }

    }

    public void up() {
        if (!gameOver) {
            //al empezar por i=1 nos aseguramos de que no se sale del tablero y nos saltamos la primera fila
            for (int i = 1; i < board.length; i++) {
                // Itera sobre las columnas
                for (int j = 0; j < board[0].length; j++) {
                    // Si la casilla en la que nos encontramos no está vacía, realiza el movimiento y la fusión
                    if (board[i][j].getValor() != 0) {
                        moveUp(i, j);
                        fusionUp(i, j);
                    }
                }
            }
            rePintar();
        }
    }

    private void moveUp(int i, int j) {
        //k=i porque empezamos en la posición actual y k >0 volvemos a comprobar que no estamos
        // en la 1a fila y que la casilla de arriba esté vacia
        //k-- porque nos movemos de abajo hacia arriba en la columna
        for (int k = i; k > 0 && board[k - 1][j].getValor() == 0; k--) {
            board[k - 1][j].setValor(board[k][j].getValor());
            board[k][j].setValor(0);
        }
    }

    private void fusionUp(int i, int j) {
        //i>0 no podemos estar en la 1a fila, y compara que el valor d la pos actual y el que está
        //arriba de éste sean iguales para poder fusionarlos
        if (i > 0 && board[i - 1][j].getValor() == board[i][j].getValor()) {
            //multiplicaremos por 2 ya que en este juego siempre se sumarán las casillas
            //siempre y cuando sean iguales y sean 2^n
            board[i - 1][j].setValor(board[i][j].getValor() * 2);
            board[i][j].setValor(0);
            //el valor de la casilla resultante se guarda en score para saber la puntuacion final
            score += board[i - 1][j].getValor();

        }
    }

    public void down() {
        if (!gameOver) {
            //itera desde la segunda fila hasta la primera (i>=0) y comienza desde la penultima fila, no la ultima
            //se mueve de abajo hacia arriba
            for (int i = board.length - 2; i >= 0; i--) {
                //itera por las columnas de izquierda a derecha
                for (int j = 0; j < board[i].length; j++) {
                    //si la casilla en la que nos encontramos no está vacia podremos hacer un mov
                    if (board[i][j].getValor() != 0) {
                        moveDown(i, j);
                        fusionDown(i, j);
                    }
                }
            }
            rePintar();
        }

    }

    private void moveDown(int i, int j) {
        //nos encontramos en la posicion actual y comprobamos que no estamos en la ultima fila
        //y que la casilla de abajo esté vacia para poder hacer el movimento
        for (int k = i; k < board.length - 1 && board[k + 1][j].getValor() == 0; k++) {
            board[k + 1][j].setValor(board[k][j].getValor());
            board[k][j].setValor(0);
        }
    }

    private void fusionDown(int i, int j) {
        //volvemos a comrpobar que no estamos en la ultima fila y que el valor de la casilla actual
        //y la de abajo sean iguales para poder fusionarlas
        if (i < board.length - 1 && board[i + 1][j].getValor() == board[i][j].getValor()) {
            //multiplicaremos por 2 ya que en este juego siempre se sumarán las casillas
            board[i + 1][j].setValor(board[i][j].getValor() * 2);
            //ponemos la casilla actual a 0 ya que se ha fusionado con la de abajo
            board[i][j].setValor(0);
            score += board[i + 1][j].getValor();
        }
    }

    public void left() {
        if (!gameOver) {
            for (int i = 0; i < board.length; i++) {
                //desde la 2a columna de cada fila hasta la última
                for (int j = 1; j < board[i].length; j++) {
                    if (board[i][j].getValor() != 0) {
                        moveLeft(i, j);
                        fusionLeft(i, j);

                    }
                }
            }
            rePintar();
        }
    }

    private void moveLeft(int i, int j) {
        //Lo que cambian en este caso son las j(columnas) por eso k=j
        //k-- porque nos movemos de derecha a izquierda en la fila
        for (int k = j; k >= 1 && board[i][k - 1].getValor() == 0; k--) {
            board[i][k - 1].setValor(board[i][k].getValor());
            board[i][k].setValor(0);
        }

    }

    private void fusionLeft(int i, int j) {
        // j>1 asegura que estamos en una columna que tiene al menos una columna a la izquierda para la fusión
        // y comprobamos que el valor de la casilla actual y la de la izquierda sean iguales para poder fusionarlas
        if (j > 0 && board[i][j - 1].getValor() == board[i][j].getValor()) {
            board[i][j - 1].setValor(board[i][j].getValor() * 2);
            board[i][j].setValor(0);
            score += board[i][j - 1].getValor();
        }

    }

    public void right() {
        if (!gameOver) {
            for (int i = 0; i < board.length; i++) {
                //empezamos en la penultima columna
                //itera desde la penultima columna hasta la primera (j>=0)
                //se mueve de derecha a izquierda en la fila por eso j--
                for (int j = board[i].length - 2; j >= 0; j--) {
                    if (board[i][j].getValor() != 0) {
                        moveRight(i, j);
                        fusionRight(i, j);

                    }
                }
            }
            rePintar();
        }
    }

    private void moveRight(int i, int j) {
        // La diferencia en el incremento/decremento de las variables k refleja la dirección del movimiento en cada caso
        //k < board.length - 1, en vez de -2 porque asi nos aseguramos de que hay una columna válida a la derecha para mover o fusionar.
        for (int k = j; k < board.length - 1 && board[i][k + 1].getValor() == 0; k++) {
            board[i][k + 1].setValor(board[i][k].getValor());
            board[i][k].setValor(0);
        }

    }

    private void fusionRight(int i, int j) {
        if (j < board.length - 1 && board[i][j + 1].getValor() == board[i][j].getValor()) {
            board[i][j + 1].setValor(board[i][j].getValor() * 2);
            board[i][j].setValor(0);
            score += board[i][j + 1].getValor();
        }
    }

    private void rePintar(){
        addCasilla();
        conectarMatrizEnVista();
        Log.d( "2048",this.toString());
    }

    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < board.length; i++) {
            s += "|";
            for (int j = 0; j < board[i].length; j++) {
                s += board[i][j].getValor() + "|";
            }
            s += "\n";
        }
        return s;
    }
}







