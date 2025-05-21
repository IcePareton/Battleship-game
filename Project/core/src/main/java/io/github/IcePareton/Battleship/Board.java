/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.github.IcePareton.Battleship;

/**
 *
 * @author marij
 */
public class Board 
{
    private BoardSquare[][] board;

    public Board() 
    {
        board = new BoardSquare[10][10];

        for (int i = 0; i < 10; i++) 
        {
            for (int j = 0; j < 10; j++) 
            {
                board[i][j] = new BoardSquare();
            }
        }
    }

    public BoardSquare getBoardSquare(int x, int y) 
    {
        return board[x][y];
    }

}

