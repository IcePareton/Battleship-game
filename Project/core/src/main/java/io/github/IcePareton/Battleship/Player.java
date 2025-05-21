/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.github.IcePareton.Battleship;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author marij
 */
public class Player 
{   
    private static final int SHOTS_PER_TURN = 3;
    private List<Ship> shipList;
    private int shots;
    
    
    
    
    public Player()
    {
        shipList = new ArrayList<>();
        shots = SHOTS_PER_TURN;
    }
    
    public void addShip(int[] x, int[] y,int NUMBER_OF_SHIPS, int[] SHIP_SIZES, int currentShipIndex) 
    {
        if (currentShipIndex >= NUMBER_OF_SHIPS) 
        {
            throw new IllegalStateException("All ships have already been added.");
        }

        if (x.length != SHIP_SIZES[currentShipIndex] || y.length != SHIP_SIZES[currentShipIndex]) 
        {
            throw new IllegalArgumentException("Ship coordinates do not match expected ship size.");
        }

        shipList.add(new Ship(SHIP_SIZES[currentShipIndex], x, y));
    }

    
    public void resetShots()
    {
        this.shots = SHOTS_PER_TURN;
    }
    
    public int getshots()
    {
        return this.shots;
    }
    
    public void decreaseShot()
    {
        this.shots--;
    }
    
    public void printShipList()
    {
        
        for(int i = 0; i < shipList.size(); i++)
        {
            System.out.printf("Ship: %d \n", i);
            shipList.get(i).printShip();
        }
    }
    //Shot Position- will need libGDX integration
    
    
}
