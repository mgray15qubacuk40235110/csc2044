package uk.ac.qub.eeecs.game;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
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

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    public SplashScreen(Game game) {
        super("SplashScreen", game);

        // Background
        assetManager.loadAndAddBitmap("splash", "img/splashv5.png");

        float height = game.getScreenHeight();
        float width = game.getScreenWidth();

        mDefaultLayerViewport.set(getScreenWidth() / 2, getScreenHeight() / 2, getScreenWidth() / 2, getScreenHeight() / 2);
        mDefaultScreenViewport.set(0, 0, (int) mDefaultLayerViewport.halfWidth * 2, (int) mDefaultLayerViewport.halfHeight * 2);

        assetManager.loadAndAddBitmap("arrow", "img/UpArrow.png");
        assetManager.loadAndAddBitmap("card1", "img/CardBackground.png");
        assetManager.loadAndAddBitmap("card2", "img/CardBackground1.png");
        assetManager.loadAndAddBitmap("Background", "img/SimCardsMenuBackground.png");
        assetManager.loadAndAddBitmap("GameLogo", "img/GameLogo.png");

        mSplashScreenButton = new PushButton(60, 60, width, height, "splash", this);
        createGameObjectAndSprites();

        // Create the card background
        mCardBackground = new GameObject(mDefaultLayerViewport.halfWidth,
                mDefaultLayerViewport.halfHeight, mDefaultLayerViewport.halfWidth * 2, mDefaultLayerViewport.halfHeight * 2, getGame()
                .getAssetManager().getBitmap("Background"), this);

        //Define logo
        mLogo = new GameObject(mDefaultScreenViewport.width / 2, mDefaultScreenViewport.bottom - 270.0f,
                mDefaultScreenViewport.right / 2.5f, mDefaultScreenViewport.bottom / 2.5f,
                getGame().getAssetManager().getBitmap("GameLogo"), this);
    }

    public void update(ElapsedTime elapsedTime) {
        checkIfTimeToMove(elapsedTime);
        checkIfButtonPushedToMove(elapsedTime);
        checkCard(elapsedTime);
    }

    public int getFramesRemaining() {
        return framesRemaining;
    }

    public void setFramesRemaining(int framesRemaining) {
        this.framesRemaining = framesRemaining;
    }

    private void checkIfTimeToMove(ElapsedTime elapsedTime) {
        //When the frames get to 0 then automatically begin the game

        framesRemaining--;
        if (framesRemaining <= 0) {
            moveToMenuScreen(elapsedTime);
        }
    }

    private void checkIfButtonPushedToMove(ElapsedTime elapsedTime) {
        Input input = mGame.getInput();

        List<TouchEvent> touchEvents = input.getTouchEvents();
        if (touchEvents.size() > 0) {

            //Updating button to go to the card demo screen if clicked
            moveToMenuScreen(elapsedTime);
        }
    }

    private void moveToMenuScreen(ElapsedTime elapsedTime) {
        //Move to the card screen after an event

        mSplashScreenButton.update(elapsedTime);
        mGame.getScreenManager().addScreen(new SimCardsScreen(mGame));

        //If the user clicks the screen then the user has chosen to ready up and will be moved to play area
        if (mSplashScreenButton.isPushTriggered())
            mGame.getScreenManager().addScreen(new SimCardsScreen(mGame));

    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
    // Clear the screen and draw the buttons
        graphics2D.clear(Color.BLACK);

        //Draw Background + Logo
        mCardBackground.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        mLogo.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        textPaint.setColor(Color.WHITE);
        graphics2D.drawText("Loading...", 375.0f, 725.0f, textPaint);

        textPaint.setColor(Color.BLACK);
        graphics2D.drawRect(185.0f,785,mDefaultScreenViewport.right - 185.0f,915,textPaint);

        textPaint.setColor(Color.WHITE);
        graphics2D.drawRect(200.0f,800,mDefaultScreenViewport.right - 200.0f,900,textPaint);

        textPaint.setColor(Color.BLACK);
        graphics2D.drawRect(210.0f,810,210 + loadingCounter,890,textPaint);
        loadingCounter = loadingCounter + 13.15f;

        counter(elapsedTime, graphics2D);
        //drawSprites(elapsedTime,graphics2D);

    }
    //Reduce number drawn to screen
    public void counter(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

        int mColour = mColourChoices[(int) (elapsedTime.totalTime % mColourChoices.length)];

        float textSize =
                ViewportHelper.convertXDistanceFromLayerToScreen(
                        mDefaultLayerViewport.getHeight() * 0.05f,
                        mDefaultLayerViewport, mDefaultScreenViewport);
        textPaint.setTextSize(textSize * 1.5f);
        textPaint.setTextAlign(Paint.Align.CENTER);

        textPaint.setColor(mColour);
    }

    public void checkCard(ElapsedTime elapsedTime) {

        Random random = new Random();

        Input input = mGame.getInput();
        Bitmap arrow = mGame.getAssetManager().getBitmap("arrow");

        for (int idx = 0; idx < mGameObjects.length; idx++) {
            GameObject trigger = mGameObjects[idx];
            trigger.setBitmap(arrow);

            Sprite card = mSprites[idx];
            if ((card.position.y - card.getHeight() / 2.0f)
                    - (trigger.position.y + trigger.getHeight() / 2.0f) < JUMP_TRIGGER_DISTANCE)
                card.velocity.y =
                        random.nextInt((int) (JUMP_STRENGTH_MAX - JUMP_STRENGTH_MIN)) +
                                JUMP_STRENGTH_MIN;

        }
        float groundHeight = (mDefaultLayerViewport.getWidth()
                - mDefaultLayerViewport.getHeight() * 0.10f) / GAMEOBJECT_DENSITY;

        // Update each card sprite
        for (Sprite card : mSprites) {

            // Apply gravity and update the cards position
            card.acceleration.y = GRAVITY;
            card.update(elapsedTime);

            //Velocity

            if (card.position.y - card.getHeight() / 2.0f < groundHeight) {
                card.position.y = groundHeight + card.getHeight() / 2.0f;
                card.velocity.y = -card.velocity.y * DAMPENING;
            }
        }

    }
    public void drawSprites(ElapsedTime elapsedTime, IGraphics2D graphics2D){

        // Draw each of the card sprites
        for (Sprite card : mSprites)
            card.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        // Draw each of the card1 sprites
        for (Sprite card1 : mSprites)
            card1.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
    }
    public void createGameObjectAndSprites() {
        Random random = new Random();

        // Create the game object and sprite entities
        for (int idx = 0; idx < GAMEOBJECT_DENSITY; idx++) {

            // Determine the size of the card entities based on the entity density
            float objectWidth = (mDefaultLayerViewport.getWidth()
                    - mDefaultLayerViewport.getHeight() * 0.10f) / GAMEOBJECT_DENSITY;
            float objectHeight = objectWidth;

            // Create the card game object trigger
            GameObject trigger = new GameObject(objectWidth * (idx + 0.5f), objectHeight / 2.0f, objectWidth, objectHeight, assetManager.getBitmap("card1"), this);
            mGameObjects[idx] = trigger;

            //Create a random card

            for (int i = 1; i <= 10; i++) {
                int randomNumber = (int) (Math.random() * 100);

                if (randomNumber <= 49) {

                    // Create the card1 sprite
                    Sprite card = new Sprite(
                            objectWidth * (idx + 0.5f),
                            random.nextInt((int) (mDefaultLayerViewport.getHeight() - objectHeight * 2.0f)) + objectHeight * 1.5f,
                            objectWidth, objectHeight, assetManager.getBitmap("card1"), this);
                    mSprites[idx] = card;
                } else {

                    // Create the card2 sprite
                    Sprite card2 = new Sprite(
                            objectWidth * (idx + 0.5f),
                            random.nextInt((int) (mDefaultLayerViewport.getHeight() - objectHeight * 2.0f)) + objectHeight * 1.5f,
                            objectWidth, objectHeight, assetManager.getBitmap("card2"), this);
                    mSprites[idx] = card2;
                }

            }
        }
    }
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels + 128;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}



