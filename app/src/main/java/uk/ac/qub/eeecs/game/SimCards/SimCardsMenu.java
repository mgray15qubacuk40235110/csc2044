package uk.ac.qub.eeecs.game.SimCards;

import android.content.res.Resources;
import android.graphics.Color;

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
import uk.ac.qub.eeecs.game.SplashScreen;


public class SimCardsMenu extends GameScreen {


    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Define the buttons for playing the 'games'
     */
    private PushButton mPlayButton;
    private PushButton mDeckButton;
    private PushButton mQuitButton;

    //Logo
    private GameObject mLogo;


    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create a simple menu screen
     *
     * @param game Game to which this screen belongs
     */
    public SimCardsMenu(Game game) {
        super("SimCardsMenu", game);

        mDefaultLayerViewport.set(getScreenWidth() / 2, getScreenHeight() / 2, getScreenWidth() / 2, getScreenHeight() / 2);
        mDefaultScreenViewport.set(0, 0, (int) mDefaultLayerViewport.halfWidth * 2, (int) mDefaultLayerViewport.halfHeight * 2);

        // Load in the bitmaps used on the main menu screen
        AssetManager assetManager = mGame.getAssetManager();
        assetManager.loadAndAddBitmap("PlayGameButton", "img/PlayGame.png");
        assetManager.loadAndAddBitmap("ViewDeckButton", "img/ViewDeck.png");
        assetManager.loadAndAddBitmap("QuitGameButton", "img/QuitGame.png");
        assetManager.loadAndAddBitmap("GameLogo", "img/GameLogo.png");
        assetManager.loadAndAddBitmap("Background", "img/SimCardsMenuBackground.png");

        // Define the spacing that will be used to position the buttons
        float offS = (mDefaultScreenViewport.bottom / 8) + 20.0f;

        //Define buttons
        mPlayButton = new PushButton(mDefaultScreenViewport.right / 2, mDefaultScreenViewport.bottom / 2 - 100.0f,
                mDefaultScreenViewport.right / 3.5F, mDefaultScreenViewport.bottom / 8, "PlayGameButton", this);

        mDeckButton = new PushButton(mDefaultScreenViewport.right / 2, mDefaultScreenViewport.bottom / 2 - 100.0f - offS,
                mDefaultScreenViewport.right / 3.5F, mDefaultScreenViewport.bottom / 8, "ViewDeckButton", this);

        mQuitButton = new PushButton(mDefaultScreenViewport.right / 2, mDefaultScreenViewport.bottom / 2 - 100.0f - (2 * offS),
                mDefaultScreenViewport.right / 3.5F, mDefaultScreenViewport.bottom / 8, "QuitGameButton", this);

        //Define logo
        mLogo = new GameObject(mDefaultScreenViewport.width / 2, mDefaultScreenViewport.bottom - 270.0f, mDefaultScreenViewport.right / 2.5f, mDefaultScreenViewport.bottom / 2.5f, getGame().getAssetManager().getBitmap("GameLogo"), this);
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
            mPlayButton.update(elapsedTime);
            mDeckButton.update(elapsedTime);
            mQuitButton.update(elapsedTime);


            if (mPlayButton.isPushTriggered()) {
                mGame.getScreenManager().addScreen(new SplashScreen(mGame));
            }
            else if (mDeckButton.isPushTriggered()) {
                mGame.getScreenManager().removeScreen(this);
            }
            else if (mQuitButton.isPushTriggered()) {
                mGame.getScreenManager().removeScreen(this);
            }
        }
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
        graphics2D.clear(Color.BLACK);

        //Draw Logo
        mLogo.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        //Draw Buttons
        mPlayButton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        mDeckButton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        mQuitButton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels + 128;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

}
