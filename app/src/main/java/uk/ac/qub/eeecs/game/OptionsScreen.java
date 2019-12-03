package uk.ac.qub.eeecs.game;

import android.graphics.Color;

import java.util.List;
import java.util.Random;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.audio.AudioManager;
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

    private PushButton returnToMenu;
    private PushButton musicToggle;
    private PushButton pauseMusic;
    private PushButton colourToggle;

    private boolean colourToggleOn;

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

        assetManager.loadAndAddBitmap("BackArrow", "img/BackArrow.png");
        assetManager.loadAndAddBitmap("BackArrowSelected", "img/BackArrowSelected.png");

        assetManager.loadAndAddBitmap("PlayButton", "img/PlayButton.png");
        assetManager.loadAndAddBitmap("PlayButton", "img/PlayButton.png");

        assetManager.loadAndAddBitmap("PauseButton", "img/PauseButton.png");
        assetManager.loadAndAddBitmap("PauseButton", "img/PauseButton.png");

        assetManager.loadAndAddBitmap("Circle", "img/Circle.png");
        assetManager.loadAndAddBitmap("Circle", "img/Circle.png");


        // Define the spacing that will be used to position the buttons
        int spacingX = (int)mDefaultLayerViewport.getWidth() / 6;
        int spacingY = (int)mDefaultLayerViewport.getHeight() / 3;

        // Create the trigger buttons

        musicToggle = new PushButton(
                spacingX * 2.70f, spacingY * 2.80f, 25, 25,
                "PlayButton", "PlayButton",this );
        musicToggle.setPlaySounds(true, true);

        pauseMusic = new PushButton(
                spacingX * 3.20f, spacingY * 2.80f, 25, 25,
                "PauseButton", "PauseButton",this );
        pauseMusic.setPlaySounds(true, true);

        returnToMenu = new PushButton(
                mDefaultLayerViewport.getWidth() * 0.95f, mDefaultLayerViewport.getHeight() * 0.10f,
                mDefaultLayerViewport.getWidth() * 0.075f, mDefaultLayerViewport.getHeight() * 0.10f,
                "BackArrow", "BackArrowSelected", this);
        returnToMenu.setPlaySounds(true, true);

        colourToggle = new PushButton(
                spacingX * 0.50f, spacingY * 1.5f, 80, 80,
                "Circle", "Circle",this);
        colourToggle.setPlaySounds(true, true);

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
            returnToMenu.update(elapsedTime);
            musicToggle.update(elapsedTime);
            pauseMusic.update(elapsedTime);
            colourToggle.update(elapsedTime);

            AudioManager audioManager = getGame().getAudioManager();

            mGame.getAssetManager().loadAssets("txt/assets/OptionsScreenAssets.JSON");

            if (returnToMenu.isPushTriggered())
                mGame.getScreenManager().addScreen(new MenuScreen(mGame));
            else if (colourToggle.isPushTriggered())
                    colourToggleOn = !colourToggleOn;
            else if (musicToggle.isPushTriggered())
                    audioManager.playMusic(getGame().getAssetManager().getMusic("Wow"));
            else if (pauseMusic.isPushTriggered())
                if (audioManager.isMusicPlaying()) {
                    audioManager.pauseMusic();
            }
        }
    }

    private void setupCardGameObjects() {

        mGame.getAssetManager().loadAssets("txt/assets/OptionsScreenAssets.JSON");

        // Background

//        mOptionsBackground = new GameObject(230,
//                160, mDefaultLayerViewport.getWidth(), mDefaultLayerViewport.getHeight(), getGame()
//                .getAssetManager().getBitmap(""), this);
    }
    /**
     * Draw the menu screen
     *
     * @param elapsedTime Elapsed time information
     * @param graphics2D  Graphics instance
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

       if (colourToggleOn) {
           String colourArray[] = {"#FF3333", "#FCFA00", "#70FC00", "#00FCD6", "#0078FC", "#A100FC", "#FC00D2", "#FC9300"};

           Random i = new Random();
           int c = i.nextInt(8 - 1) + 1;

           graphics2D.clear(Color.parseColor(colourArray[c]));
       }

//       mOptionsBackground.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        returnToMenu.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        musicToggle.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        pauseMusic.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        colourToggle.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

    }
}