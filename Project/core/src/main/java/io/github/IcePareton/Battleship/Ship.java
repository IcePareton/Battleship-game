/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.github.IcePareton.Battleship;


/**
 *
 * @author marij
 */
public class Ship 
{
    private int size;
    private int[] x;
    private int[] y;

    public Ship(int size, int[] x, int[] y) 
    {
        if (x.length != size || y.length != size) 
        {
            throw new IllegalArgumentException("Coordinate arrays must match the given size.");
        }

        this.size = size;
        this.x = x.clone();
        this.y = y.clone();
    }

    public boolean isSunk() 
    {
        for (int i = 0; i < size; i++) 
        {
            int currentX = x[i];
            int currentY = y[i];
            
            // TODO: Check if this coordinate is tagged
        }

        return false; // placeholder return
    }
    
    public void printShip()
    {
        for (int i = 0; i < size; i++) 
        {
            System.out.printf("X: %d, Y: %d \n", x[i], y[i]);
        }
    }
}

