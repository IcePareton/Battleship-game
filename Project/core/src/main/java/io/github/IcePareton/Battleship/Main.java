package io.github.IcePareton.Battleship;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import java.util.ArrayList;
import java.util.List;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main implements ApplicationListener {
    Texture oneByOneTex;
    Texture twoByOneTex;
    Texture threeByOneTex;
    Texture fourByOneTex;
    Texture fiveByOneTex;
    Texture board;
    
    Sprite fiveByOneSpr;
    Sprite oneByOneSpr;
    Sprite twoByOneSpr;
    Sprite threeByOneSpr;
    Sprite fourByOneSpr;
            
    SpriteBatch spriteBatch;
    FitViewport viewport;
    
    Game game;
    GameState gameState;
    private int startX; 
    private int startY;
    private Sprite currentShipPreview = null;
    private final List<Sprite> placedShips = new ArrayList<>();
    private boolean showCurrentShip = false;
    enum GameState 
    {
        PLACING_SHIPS,
        PLACING_SHIPS_SECOND_PHASE,
        PLAYER_TURN,
        AI_TURN,
        GAME_OVER
    }
    
    
    @Override
    public void create() {
        
        game = new Game();
        gameState = GameState.PLACING_SHIPS;
        oneByOneTex = new Texture ("1x1.png");
        twoByOneTex = new Texture("2x1.png");
        threeByOneTex = new Texture("3x1.png");
        fourByOneTex = new Texture("4x1.png");
        fiveByOneTex = new Texture("5x1.png");
        board = new Texture("dualGrid.png");
        
        
        oneByOneSpr = new Sprite(oneByOneTex);
        twoByOneSpr = new Sprite(twoByOneTex);
        threeByOneSpr = new Sprite(threeByOneTex);
        fourByOneSpr = new Sprite(fourByOneTex);
        fiveByOneSpr = new Sprite(fiveByOneTex);

        oneByOneSpr.setSize(1, 1);
        twoByOneSpr.setSize(1, 2);
        threeByOneSpr.setSize(1, 3);
        fourByOneSpr.setSize(1, 4);
        fiveByOneSpr.setSize(1, 5);
        
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(21, 10);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render() {
        input();
        draw();
    }
    
    private void draw() {
        ScreenUtils.clear(Color.WHITE);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();

        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        spriteBatch.draw(board, 0, 0, worldWidth, worldHeight);

        for (Sprite ship : placedShips) 
        {
            ship.draw(spriteBatch);
        }

        if (showCurrentShip && currentShipPreview != null) 
        {
            currentShipPreview.draw(spriteBatch);
        }

        spriteBatch.end();
    }

    
    private void input()
    { 
        if (!Gdx.input.justTouched()) return;
        {
            int screenX = Gdx.input.getX();
            int screenY = Gdx.input.getY();

            Vector3 worldCoords = new Vector3(screenX, screenY, 0);
            viewport.getCamera().unproject(worldCoords);

            int gridX = (int) worldCoords.x;
            int gridY = (int) worldCoords.y;
            
            if (!isValidGridPosition(gridX, gridY)) 
            {
                return;
            }
            
            if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) 
            {
                if (gameState == GameState.PLACING_SHIPS_SECOND_PHASE) 
                { 
                    currentShipPreview = null;
                    showCurrentShip = false;
                    gameState = GameState.PLACING_SHIPS;
                }
                return;
            }

            switch(gameState) 
            {
                case PLACING_SHIPS:
                    game.resetFinalDirectionNESW();
                    handleShipPlacementPhase(gridX, gridY);
                    break;
                
                case PLACING_SHIPS_SECOND_PHASE:
                    handleShipPlacementSecondPhase(gridX, gridY);
                    break;

                case PLAYER_TURN:
                    System.out.print("Someone won lmao");
                    //handlePlayerTurn(gridX, gridY);
                    break;

                case AI_TURN:
                    System.out.print("Someone won lmao");
                    //handleAiTurn();
                    break;

                case GAME_OVER:
                    System.out.print("Someone won lmao");
                    System.exit(0);
                    break;
            }
        }
    }
    
    
    
    private boolean isValidGridPosition(int x, int y)
    {
        if ((x >= 0) && (x <= 21) && (x != 11) && (y >= 0) && (y <= 10)) // within either board, not in between or elsewhere
        {
            return true;
        }
        return false;
    }
    
    private void handleShipPlacementPhase(int x, int y) 
    {
    
        Boolean success = game.startingShipPlacement(x, y, "Player");
        if (success == false) 
        {
            return;
        } 
        else             
        {
            startX = x;
            startY = y;
            currentShipPreview = new Sprite(oneByOneSpr);
            currentShipPreview.setPosition(x, y);
            currentShipPreview.setRotation(0);
            showCurrentShip = true;
            
            gameState = GameState.PLACING_SHIPS_SECOND_PHASE;
            draw();
        }
        
    }
    
    private void handleShipPlacementSecondPhase(int x, int y)
    {
        boolean success = game.endingShipPlacement(x, y, startX, startY);
            if (success == true) 
            {
                placeShip();
                game.incramentCurrentShipIndex();
                if (game.getCurrentShipIndex() >= game.getNUMBER_OF_SHIPS()) 
                {
                    game.aiShipPlacement();
                    gameState = GameState.PLAYER_TURN;
                } 
                else 
                {
                    gameState = GameState.PLACING_SHIPS;
                }
            } 
    }
    
    
    private void placeShip()
    {
        int dir = game.getFinalDirectionNESW();
        if (dir == -1)
        {
            return;
        }
        int shipSize = game.getShipSize(game.getCurrentShipIndex());
        Sprite shipSprite;
        
        switch (shipSize) 
        {
            case 5: 
                shipSprite = new Sprite(fiveByOneSpr); 
                break;
            case 4: 
                shipSprite = new Sprite(fourByOneSpr); 
                break;
            case 3: 
                shipSprite = new Sprite(threeByOneSpr); 
                break;
            case 2: 
                shipSprite = new Sprite(twoByOneSpr); 
                break;
            default: 
                shipSprite = new Sprite(oneByOneSpr); 
                break;
        }

    // Apply rotation based on direction
    float rotation;
    switch (dir) 
    {
        case 0:
            rotation = 180;     // North
            break;
        case 1:
            rotation = 270;   // East
            break;
        case 2:
            rotation = 0;   // South
            break;
        case 3:
            rotation = 90;    // West
            break;
        default:
            rotation = 0;
            break;
    }

    shipSprite.setOrigin(0.5f, 0.5f);
    shipSprite.setRotation(rotation);
    shipSprite.setPosition(startX, startY);

    placedShips.add(shipSprite);
    currentShipPreview = null;
    showCurrentShip = false;
    
    }
    
        

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void dispose() {
        // Destroy application's resources here.
    }
}