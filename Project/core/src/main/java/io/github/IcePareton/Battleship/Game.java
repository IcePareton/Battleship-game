/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.github.IcePareton.Battleship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 *
 * @author marij
 */
public class Game 
{
    private static final int NUMBER_OF_SHIPS = 5;
    private static final int[] SHIP_SIZES = {5, 4, 3, 3, 2};
    
    private Player player;
    private Player ai;
    private Board playerBoard;
    private Board aiBoard;
    
    private int currentShipIndex = 0;
    private Boolean[] currentDirectionsNESW;
    private int finalDirectionNESW = -1;

    
    public Game()
    {
       player = new Player();
       ai = new Player();
       playerBoard = new Board();
       aiBoard = new Board();
    }
    
    public Boolean startingShipPlacement(int x, int y, String playOrAi)
    {
        
        if (x >= 11) //aka, return if it was clicked outside player board.
        {
            return false;
        }
        Board board;
        
        if (playOrAi.equals("Player"))
        {
            board = playerBoard;
        }
        else if(playOrAi.equals("Ai"))
        {
            board = aiBoard;
        }
        else
        {
            return false;
        }
        
        if(board.getBoardSquare(x, y).hasShip())
        {
            return false;
        }
        
        currentDirectionsNESW = new Boolean[4];
        Arrays.fill(currentDirectionsNESW, Boolean.TRUE);
        
        int size = SHIP_SIZES[currentShipIndex];

        // Check North (index 0)
        for (int step = 0; step < size; step++) 
        {
            int currentY = y - step;
            if (currentY < 0) 
            {
                currentDirectionsNESW[0] = false;
                break;
            }
            if (board.getBoardSquare(x, currentY).hasShip()) 
            {
                currentDirectionsNESW[0] = false;
                break;
            }
        }

        // Check East (index 1)
        for (int step = 0; step < size; step++) 
        {
            int currentX = x + step;
            if (currentX >= 10) 
            {
                currentDirectionsNESW[1] = false;
                break;
            }
            if (board.getBoardSquare(currentX, y).hasShip()) 
            {
                currentDirectionsNESW[1] = false;
                break;
            }
        }

        // Check South (index 2)
        for (int step = 0; step < size; step++) 
        {
            int currentY = y + step;
            if (currentY >= 10) 
            {
                currentDirectionsNESW[2] = false;
                break;
            }
            if (board.getBoardSquare(x, currentY).hasShip()) 
            {
                currentDirectionsNESW[2] = false;
                break;
            }
        }

        // Check West (index 3)
        for (int step = 0; step < size; step++) 
        {
            int currentX = x - step;
            if (currentX < 0) 
            {
                currentDirectionsNESW[3] = false;
                break;
            }
            if (board.getBoardSquare(currentX, y).hasShip()) 
            {
                currentDirectionsNESW[3] = false;
                break;
            }
        }
        
        for (boolean direction : currentDirectionsNESW) 
        {
            if (direction == true) 
            {
                return true; // At least one direction is possible
            }
        }
        currentDirectionsNESW = null;
        return false;
    }
        
        
    
    public boolean endingShipPlacement(int x, int y, int startX, int startY) 
    {
        if (x >= 11) return false;

        int deltaX = x - startX;
        int deltaY = y - startY;

        if ((deltaX == 0 && deltaY == 0) || (deltaX != 0 && deltaY != 0))
        {
            return false;
        }

        finalDirectionNESW = getDirectionIndex(deltaX, deltaY);
        if (finalDirectionNESW == -1) 
        {
            return false;
        }

        int size = SHIP_SIZES[currentShipIndex];
        int[] xCoords = new int[size];
        int[] yCoords = new int[size];

        populateShipCoordinates(finalDirectionNESW, size, xCoords, yCoords, startX, startY);

        for (int i = 0; i < size; i++) 
        {
            playerBoard.getBoardSquare(xCoords[i], yCoords[i]).putShip();
        }

        player.addShip(xCoords, yCoords, NUMBER_OF_SHIPS, SHIP_SIZES, currentShipIndex);
        return true;
    }
    
        public void aiShipPlacement() 
        {
        Random rand = new Random();

        for (int i = 0; i < NUMBER_OF_SHIPS; i++) 
        {
            currentShipIndex = i;
            boolean placed = false;

            while (!placed) 
            {
                int startX = rand.nextInt(10);
                int startY = rand.nextInt(10);

                if (!startingShipPlacement(startX, startY, "Ai")) 
                {
                    continue; 
                }

                List<Integer> validDirections = new ArrayList<>();
                for (int dir = 0; dir < 4; dir++) {
                    if (currentDirectionsNESW[dir] != null && currentDirectionsNESW[dir]) 
                    {
                        validDirections.add(dir);
                    }
                }

                int chosenDir = validDirections.get(rand.nextInt(validDirections.size()));
                int size = SHIP_SIZES[i];
                int[] xCoords = new int[size];
                int[] yCoords = new int[size];

                populateShipCoordinates(chosenDir, size, xCoords, yCoords, startX, startY);

                for (int j = 0; j < size; j++) 
                {
                    aiBoard.getBoardSquare(xCoords[j], yCoords[j]).putShip();
                }

                ai.addShip(xCoords, yCoords, NUMBER_OF_SHIPS, SHIP_SIZES, i);
                placed = true;
            }
        }
        
        ai.printShipList();
    }

    
    private int getDirectionIndex(int deltaX, int deltaY)   
    {
        if (deltaY < 0 && currentDirectionsNESW[0] == true) return 0; // North
        if (deltaX > 0 && currentDirectionsNESW[1] == true) return 1; // East
        if (deltaY > 0 && currentDirectionsNESW[2] == true) return 2; // South
        if (deltaX < 0 && currentDirectionsNESW[3] == true) return 3; // West
        return -1;
    }
    
    private void populateShipCoordinates(int directionIndex, int size, int[] xCoords, int[] yCoords, int startX, int startY) 
    {
        for (int i = 0; i < size; i++)
        {
            switch (directionIndex) 
            {
                case 0: 
                    xCoords[i] = startX;      
                    yCoords[i] = startY - i; 
                    break; // North
                case 1: 
                    xCoords[i] = startX + i;  
                    yCoords[i] = startY;     
                    break; // East
                case 2: 
                    xCoords[i] = startX;      
                    yCoords[i] = startY + i; 
                    break; // South
                case 3: 
                    xCoords[i] = startX - i;  
                    yCoords[i] = startY;     
                    break; // West
            }
        }
    }

    public int getFinalDirectionNESW()
    {
        return finalDirectionNESW;
    }
    
    public void resetFinalDirectionNESW()
    {
        finalDirectionNESW = -1;
    }

    public int getCurrentShipIndex()
    {
        return currentShipIndex;
    }
     
    public int getNUMBER_OF_SHIPS()
    {
        return NUMBER_OF_SHIPS;
    }
    
    public int getShipSize(int index)
    {
        return SHIP_SIZES[index];
    }
    
    public void incramentCurrentShipIndex()
    {
        currentShipIndex++;
    }

      
        
}
