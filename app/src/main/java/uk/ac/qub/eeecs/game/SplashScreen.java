package uk.ac.qub.eeecs.game;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.audio.AudioManager;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.GameScreen;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import java.util.List;
import java.util.Random;
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.util.ViewportHelper;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.Sprite;
import uk.ac.qub.eeecs.game.SimCards.SimCardsScreen;

public class SplashScreen extends GameScreen {
    /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    private final static int GAMEOBJECT_DENSITY = 10;
    private GameObject[] mGameObjects = new GameObject[GAMEOBJECT_DENSITY];
    private Sprite[] mSprites = new Sprite[GAMEOBJECT_DENSITY];
    int[] mColourChoices = new int[]{Color.BLUE, Color.RED, Color.WHITE, Color.YELLOW};

    private final static float GRAVITY = -300.0f;
    private final static float DAMPENING = 0.5f;
    private final static float JUMP_STRENGTH_MIN = 250.0f;
    private final static float JUMP_STRENGTH_MAX = 400.0f;
    private final static float JUMP_TRIGGER_DISTANCE = 5.0f;

    private Paint textPaint = new Paint();
    private GameObject mCardBackground;
    private GameObject mLogo;

    private float loadingCounter = 0;

    private PushButton mSplashScreenButton;
    private int framesRemaining = 115;

    // Define the back button to return to the demo menu
    private PushButton mBackButton;

    //Loading in the image bitmap which will allow the splash screen to be displayed
    AssetManager assetManager = mGame.getAssetManager();
    private GameObject Background;

    // Audio Manager
    AudioManager audioManager;
    boolean soundPlayed = false;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    public SplashScreen(Game game) {
        super("SplashScreen", game);

        // Background
        mGame.getAssetManager().loadAssets("txt/assets/SplashScreenAssets.JSON");
        assetManager.loadAndAddBitmap("splash", "img/splashv5.png");

        float height = game.getScreenHeight();
        float width = game.getScreenWidth();

        mDefaultLayerViewport.set(getScreenWidth() / 2, getScreenHeight() / 2, getScreenWidth() / 2, getScreenHeight() / 2);
        mDefaultScreenViewport.set(0, 0, (int) mDefaultLayerViewport.halfWidth * 2, (int) mDefaultLayerViewport.halfHeight * 2);

        assetManager.loadAndAddBitmap("Background", "img/SimCardsMenuBackground.png");
        assetManager.loadAndAddBitmap("GameLogo", "img/GameLogo.png");

        mSplashScreenButton = new PushButton(60, 60, width, height, "splash", this);

        // Create the card background
        mCardBackground = new GameObject(mDefaultLayerViewport.halfWidth,
                mDefaultLayerViewport.halfHeight, mDefaultLayerViewport.halfWidth * 2, mDefaultLayerViewport.halfHeight * 2, getGame()
                .getAssetManager().getBitmap("Background"), this);

        //Define logo
        mLogo = new GameObject(mDefaultScreenViewport.width / 2, mDefaultScreenViewport.bottom - 270.0f,
                mDefaultScreenViewport.right / 2.5f, mDefaultScreenViewport.bottom / 2.5f,
                getGame().getAssetManager().getBitmap("GameLogo"), this);

        audioManager = getGame().getAudioManager();
    }


    public void update(ElapsedTime elapsedTime) {
        checkIfTimeToMove(elapsedTime);

        if (!soundPlayed) {
            soundPlayed = true;
            playBackgroundMusic();
        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        // Clear the screen and draw the buttons
        graphics2D.clear(Color.BLACK);

        //Draw Background + Logo
        mCardBackground.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        mLogo.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        textPaint.setColor(Color.BLACK);
        graphics2D.drawRect(185.0f, 785, mDefaultScreenViewport.right - 185.0f, 915, textPaint);

        textPaint.setColor(Color.WHITE);
        graphics2D.drawRect(200.0f, 800, mDefaultScreenViewport.right - 200.0f, 900, textPaint);

        textPaint.setColor(Color.MAGENTA);
        graphics2D.drawRect(210.0f, 810, 210 + loadingCounter, 890, textPaint);
        loadingCounter = loadingCounter + 13.15f;

        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(100.0f);
        graphics2D.drawText("Loading...", 235.0f, 765.0f, textPaint);
    }

    private void checkIfTimeToMove(ElapsedTime elapsedTime) {
        //When the frames get to 0 then automatically begin the game

        framesRemaining--;
        if (framesRemaining <= 0) {
            moveToMenuScreen(elapsedTime);
        }
    }

    private void moveToMenuScreen(ElapsedTime elapsedTime) {

        //Move to the card screen after an event
        mSplashScreenButton.update(elapsedTime);
        audioManager.stopMusic();
        mGame.getScreenManager().addScreen(new SimCardsScreen(mGame));
        mGame.getScreenManager().removeScreen(this);

    }

    // Loading Sound
    private void playBackgroundMusic() {
        audioManager.playMusic(assetManager.getMusic("Rise"));
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels + 128;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

}


