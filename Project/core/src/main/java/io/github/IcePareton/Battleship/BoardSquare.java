/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.github.IcePareton.Battleship;

/**
 *
 * @author marij
 */
public class BoardSquare 
{
    private boolean isShip;
    private boolean isHit;
    
    public BoardSquare()
    {
        
        this.isHit = false;
        this.isShip = false;
    }
    
    public boolean isHit()
    {
        return this.isHit;
    }
    
    public boolean hasShip()
    {
        return this.isShip;
    }
    
    public void hit()
    {
        this.isHit = true;
    }
    
    public void putShip()
    {
        this.isShip = true;
    }
}
