package uk.ac.qub.eeecs.game;

import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.spaceDemo.SpaceshipDemoScreen;

/**
 * An exceedingly basic menu screen with a couple of touch buttons
 *
 * @version 1.0
 */
public class OptionsScreen extends GameScreen {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Background
     */

    private GameObject mOptionsBackground;


    /**
     * Define the buttons for playing the 'games'
     */

    private PushButton mSpaceshipDemoButton;
    private PushButton optionButton;
    private PushButton returnToMenu;

    // Background //


    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create a simple menu screen
     *
     * @param game Game to which this screen belongs
     */
    public OptionsScreen(Game game) {
        super("MenuScreen", game);

        // Load in the bitmaps used on the main menu screen

        // Change these icons

        AssetManager assetManager = mGame.getAssetManager();

        assetManager.loadAndAddBitmap("SpaceDemoIcon", "img/SpaceDemoIcon.png");
        assetManager.loadAndAddBitmap("SpaceDemoIconSelected", "img/SpaceDemoIconSelected.png");

        assetManager.loadAndAddBitmap("return", "img/return.png");
        assetManager.loadAndAddBitmap("returnSelected", "img/returnSelected.png");


        // Define the spacing that will be used to position the buttons
        int spacingX = (int)mDefaultLayerViewport.getWidth() / 6;
        int spacingY = (int)mDefaultLayerViewport.getHeight() / 3;

        // Create the trigger buttons

        mSpaceshipDemoButton = new PushButton(
                spacingX * 0.56f, spacingY * 1.5f, spacingX, spacingY,
                "SpaceDemoIcon", "SpaceDemoIconSelected",this);
        mSpaceshipDemoButton.setPlaySounds(true, true);
        mSpaceshipDemoButton.setHeight(100);
        mSpaceshipDemoButton.setWidth(100);

        returnToMenu = new PushButton(
                spacingX * 5.50f, spacingY * 0.75f, spacingX, spacingY,
                "return", "returnSelected",this );
        returnToMenu.setPlaySounds(true, true);

        setupCardGameObjects();
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Update the menu screen
     *
     * @param elapsedTime Elapsed time information
     */
    @Override
    public void update(ElapsedTime elapsedTime) {

        // Process any touch events occurring since the update
        Input input = mGame.getInput();

        List<TouchEvent> touchEvents = input.getTouchEvents();
        if (touchEvents.size() > 0) {

            // Update each button and transition if needed
            mSpaceshipDemoButton.update(elapsedTime);
            returnToMenu.update(elapsedTime);

            if (mSpaceshipDemoButton.isPushTriggered())
                mGame.getScreenManager().addScreen(new SpaceshipDemoScreen(mGame));

            else if (returnToMenu.isPushTriggered())
                mGame.getScreenManager().addScreen(new MenuScreen(mGame));

        }
    }

    private void setupCardGameObjects() {

        mGame.getAssetManager().loadAssets("txt/assets/OptionsScreenAssets.JSON");

        // Background
        mOptionsBackground = new GameObject(230,
                160, mDefaultLayerViewport.getWidth(), mDefaultLayerViewport.getHeight(), getGame()
                .getAssetManager().getBitmap("RetroBG"), this);

    }

    /**
     * Draw the menu screen
     *
     * @param elapsedTime Elapsed time information
     * @param graphics2D  Graphics instance
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

        // Clear the screen and draw the buttons
//        graphics2D.clear((Color.WHITE));

        mOptionsBackground.draw(elapsedTime, graphics2D, mDefaultLayerViewport,
                mDefaultScreenViewport);

        mSpaceshipDemoButton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        returnToMenu.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

    }
}