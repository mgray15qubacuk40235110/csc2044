package uk.ac.qub.eeecs.game;

import android.content.res.Resources;
import android.graphics.Color;

import java.util.List;
import java.util.Random;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.R;
import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.audio.AudioManager;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import java.util.concurrent.TimeUnit;

/**
 * An exceedingly basic menu screen with a couple of touch buttons
 *
 * @version 1.0
 */
public class ControlsMenu extends GameScreen {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    private Paint paint = new Paint();

   // int[] mColourChoices = new int[]{Color.BLUE, Color.RED, Color.WHITE, Color.YELLOW};


    /**
     * Background
     */

    private GameObject mOptionsBackground;

    /**
     * Define the buttons for playing the 'games'
     */

    private PushButton returnToMenu;

    private float xScroll = 0.0f;
    private float yScroll = 0.0f;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create a simple menu screen
     *
     * @param game Game to which this screen belongs
     */
    public ControlsMenu(Game game) {
        super("ControlsMenu", game);

        mDefaultLayerViewport.set(getScreenWidth() / 2, getScreenHeight() / 2, getScreenWidth() / 2, getScreenHeight() / 2);
        mDefaultScreenViewport.set(0, 0, (int) mDefaultLayerViewport.halfWidth * 2, (int) mDefaultLayerViewport.halfHeight * 2);

        // Load in the bitmaps used on the main menu screen

        AssetManager assetManager = mGame.getAssetManager();

        assetManager.loadAndAddBitmap("BackArrow", "img/BackArrow.png");
        assetManager.loadAndAddBitmap("BackArrowSelected", "img/BackArrowSelected.png");

        // Define the spacing that will be used to position the buttons
        int spacingX = (int)mDefaultLayerViewport.getWidth() / 6;
        int spacingY = (int)mDefaultLayerViewport.getHeight() / 3;

        // Create the trigger buttons

        returnToMenu = new PushButton(
                mDefaultLayerViewport.getWidth() * 0.95f, mDefaultLayerViewport.getHeight() * 0.10f,
                mDefaultLayerViewport.getWidth() * 0.075f, mDefaultLayerViewport.getHeight() * 0.10f,
                "BackArrow", "BackArrowSelected", this);
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
            returnToMenu.update(elapsedTime);

            mGame.getAssetManager().loadAssets("txt/assets/OptionsScreenAssets.JSON");

            if (returnToMenu.isPushTriggered())
                mGame.getScreenManager().addScreen(new OptionsScreen(mGame));
        }

    }

    private void setupCardGameObjects() {

        mGame.getAssetManager().loadAssets("txt/assets/OptionsScreenAssets.JSON");

        // Background
        mOptionsBackground = new GameObject(960,
                540, mDefaultLayerViewport.getWidth(), mDefaultLayerViewport.getHeight(), getGame()
                .getAssetManager().getBitmap("SimCardsMenuBackground"), this);
    }
    /**
     * Draw the menu screen
     *
     * @param elapsedTime Elapsed time information
     * @param graphics2D  Graphics instance
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

        mOptionsBackground.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        returnToMenu.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        // Textual controls
        String colourArray[] = {"#EE00FF", "#EC01FC", "#E501F5", "#E201F2", "#E001F0", "#DE01EE", "#DB00EA",
                "#D801E7", "#D501E4", "#D101E0", "#CB02D9", "#C701D5", "#C401D2", "#C401D2", "#C701D5",
                "#CB02D9", "#D101E0", "#D501E4", "#D801E7", "#DB00EA", "#DE01EE", "#DD01EC", "#E001F0",
                "#E201F2", "#E501F5", "#EC01FC", "#EE00FF"};

        Random i = new Random();
        int c = i.nextInt(24 - 1) + 1;

       //  graphics2D.clear(Color.parseColor(colourArray[c]));

        paint.setColor(Color.parseColor(colourArray[c]));
        paint.setTextSize(getGame().getScreenHeight() / 10);
        paint.setTextAlign(Paint.Align.CENTER);

        graphics2D.drawText("Controls Menu - Sim Cards", 938.0f, 100.0f, paint);

        paint.setTextSize(getGame().getScreenHeight() / 10);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.BLACK);
        graphics2D.drawText("Controls Menu - Sim Cards", 934.0f, 100.0f, paint);

        paint.setColor(Color.parseColor(colourArray[c]));
        paint.setTextSize(getGame().getScreenHeight() / 10);
        paint.setTextAlign(Paint.Align.CENTER);

        graphics2D.drawText("Controls Menu - Sim Cards", 930.0f, 100.0f, paint);

        paint.setColor(Color.WHITE);
        paint.setTextSize(getGame().getScreenHeight() / 25);
        graphics2D.drawText("The Aim: ", 300.0f, 200.0f, paint);
        graphics2D.drawText("• Users are given a deck of randomly generated 'Sim Cards'", 765.0f, 300.0f, paint);
        graphics2D.drawText("and must fight against an AI player, which also has a deck", 765.0f, 350.0f, paint);
        graphics2D.drawText("of randomly generated cards.", 495.0f, 400.0f, paint);
        graphics2D.drawText("• This line will be used to explain instructions further", 705.0f, 500.0f, paint);
        graphics2D.drawText("• This line will also be used to explain instructions further", 750.0f, 600.0f, paint);
        graphics2D.drawText("• This line will ALSO be used to explain instructions further", 765.0f, 700.0f, paint);
        graphics2D.drawText("            • This line will probably not be used to explain instructions further", 765.0f, 800.0f, paint);

    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels + 128;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}