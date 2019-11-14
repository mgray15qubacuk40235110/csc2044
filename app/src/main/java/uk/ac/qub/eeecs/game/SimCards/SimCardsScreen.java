package uk.ac.qub.eeecs.game.SimCards;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;

/**
 * Starter class for Card game stories
 *
 * @version 1.0
 */
public class SimCardsScreen extends GameScreen {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Define the background of phones
     */
    private GameObject mCardBackground;

    // Define a card to be displayed
    private Card card;

    //Buttons
    private PushButton endTurn;
    private List<PushButton> mControls;

    /**
     * Width and height of the level
     */

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create the Card game screen
     *
     * @param game Game to which this screen belongs
     */
    public SimCardsScreen(Game game) {
        super("CardScreen", game);

        mDefaultScreenViewport.set( 0, 0, mGame.getScreenWidth(), mGame.getScreenHeight());

        // Load the various images used by the cards
        mGame.getAssetManager().loadAssets("txt/assets/CardDemoScreenAssets.JSON");

        // Determine the layer size to correctly position the touch buttons
        float layerWidth = mDefaultLayerViewport.halfWidth * 2.0f;

        //Creating buttons
        mControls = new ArrayList<>();
        endTurn = new PushButton(layerWidth - 35.0f, 25.0f, 50.0f, 30.0f,
                "EndTurn", "EndTurnPressed", this);
        mControls.add(endTurn);

        // Create the card background
        mCardBackground = new GameObject(480 / 2.0f,
                320 / 2.0f, 480, 320, getGame()
                .getAssetManager().getBitmap("SimCardBackground"), this);

        // Create a new, centered card
        card = new Card(mDefaultLayerViewport.x, mDefaultLayerViewport.y, this);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////


    /**
     * Update the card demo screen
     *
     * @param elapsedTime Elapsed time information
     */
    @Override
    public void update(ElapsedTime elapsedTime) {
        // Process any touch events occurring since the last update
        Input input = mGame.getInput();

        // Update the card
        card.angularVelocity = 5f;

        card.update(elapsedTime);
    }

    /**
     * Draw the card demo screen
     *
     * @param elapsedTime Elapsed time information
     * @param graphics2D  Graphics instance
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.clear(Color.WHITE);

        mCardBackground.draw(elapsedTime, graphics2D, mDefaultLayerViewport,
                mDefaultScreenViewport);


        // Draw the card
        card.draw(elapsedTime, graphics2D,
                mDefaultLayerViewport, mDefaultScreenViewport);

        for (PushButton control : mControls)
            control.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
    }
}


