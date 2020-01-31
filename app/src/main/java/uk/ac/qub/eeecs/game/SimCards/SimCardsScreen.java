package uk.ac.qub.eeecs.game.SimCards;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.view.Display;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.R;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.util.BoundingBox;
import uk.ac.qub.eeecs.gage.util.SteeringBehaviours;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.SplashScreen;


/**
 * Starter class for Card game stories
 *
 * @version 1.0
 */

public class SimCardsScreen extends GameScreen {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    //Define the background of phones
    private GameObject mCardBackground;
    private GameObject mPauseMenu;

    public Card getCurrentCard() {
        return currentCard;
    }

    // Define a card to be displayed
    private Card currentCard;
    private Card[] cards = new Card[5];
    private Card[] AIcards = new Card[5];
    private Card[] deckCards = new Card[4];
    private Card testCard;
    private List<Card> mCards;
    private List<Card> mAICards;
    private List<Card> mDeckCards;
    private int cardOffset;
    private boolean[] flippingBack = new boolean[cards.length];
    private boolean cardsDealt = false;

    //Buttons
    private PushButton endTurn;
    private PushButton pause;
    private List<PushButton> mControls;
    private boolean gamePaused = false;
    private PushButton pausedContinue;
    private PushButton pausedQuit;

    //Touch Input
    private int unpauseCounter = 0;
    private boolean mTouchIdExists;
    private float[] mTouchLocation = new float[2];
    private boolean[] dragging = new boolean[cards.length];

    public boolean[] getRearFacing() {
        return rearFacing;
    }

    private boolean[] rearFacing = new boolean[cards.length];
    private boolean[] flipCard = new boolean[cards.length];
    TouchEvent lastTouchEvent;
    int lastTouchEventType;



    //Enabling text output
    private Paint textPaint = new Paint();
    /**
     * A history of touch events will be maintained - this is held
     * within a list that will be trimmed to ensure it doesn't exceed
     * the history maximum length.
     */
    private static final int TOUCH_EVENT_HISTORY_SIZE = 30;
    private List<String> mTouchEventsInfo = new LinkedList<>();

    /**
     * Width and height of the level
     */

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////


    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels + 128;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    /**
     * Create the Card game screen
     *
     * @param game Game to which this screen belongs
     */

    public SimCardsScreen(Game game) {
        super("CardScreen", game);

        //mDefaultLayerViewport.set(mDefaultLayerViewport.x * 2, mDefaultLayerViewport.y * 2, mDefaultLayerViewport.halfWidth * 2, mDefaultLayerViewport.halfHeight * 2);
        mDefaultLayerViewport.set(getScreenWidth() / 2, getScreenHeight() / 2, getScreenWidth() / 2, getScreenHeight() / 2);
        mDefaultScreenViewport.set(0, 0, (int) mDefaultLayerViewport.halfWidth * 2, (int) mDefaultLayerViewport.halfHeight * 2);

        float layerWidth = mDefaultLayerViewport.halfWidth * 2.0f;
        float layerHeight = mDefaultLayerViewport.halfHeight * 2.0f;

        // Load the various images used by the cards
        mGame.getAssetManager().loadAssets("txt/assets/CardDemoScreenAssets.JSON");

        //Creating buttons
        mControls = new ArrayList<>();
        endTurn = new PushButton(layerWidth - 170.0f, 90.0f, 300.0f, 90.0f,
                "EndTurn", "EndTurnPressed", this);
        pause = new PushButton(70.0f, getScreenHeight() - 60.0f, 70.0f, 70.0f,
                "pauseButton", "pauseButton", this);
        mControls.add(endTurn);
        mControls.add(pause);
        pausedContinue = new PushButton(mDefaultLayerViewport. halfWidth - 300, mDefaultLayerViewport.halfHeight / 2.85f, 350.0f, 105.0f,
                "PausedContinueButton", "PausedContinueButton", this);
        pausedQuit = new PushButton(mDefaultLayerViewport. halfWidth + 300, mDefaultLayerViewport.halfHeight / 2.85f, 350.0f, 105.0f,
                "PausedQuitButton", "PausedQuitButton", this);

        // Create the card background
        mCardBackground = new GameObject(mDefaultLayerViewport.halfWidth,
                mDefaultLayerViewport.halfHeight, mDefaultLayerViewport.halfWidth * 2, mDefaultLayerViewport.halfHeight * 2, getGame()
                .getAssetManager().getBitmap("SimCardBackground3"), this);

        //Create pause menu
        mPauseMenu = new GameObject(mDefaultLayerViewport.halfWidth,
                mDefaultLayerViewport.halfHeight, mDefaultLayerViewport.halfWidth * 1.5f, mDefaultLayerViewport.halfHeight * 1.5f, getGame()
                .getAssetManager().getBitmap("PauseMenu"), this);

        // Create cards
        mCards = new ArrayList<>();
        cardOffset = 20;
        for (int i = 0; i < cards.length; i++) {
            cards[i] = new Card((mDefaultScreenViewport.left + 90 + cardOffset), (mDefaultScreenViewport.top + 140), this);
            cardOffset = cardOffset + (int) cards[i].getWidth() + 20;
            mCards.add(cards[i]);
        }

        // Create AI Cards
        mAICards = new ArrayList<>();
        cardOffset = -20;
        for (int i = 0; i < AIcards.length; i++) {
            AIcards[i] = new Card((mDefaultScreenViewport.right - 90 + cardOffset), (mDefaultScreenViewport.bottom - 140), this);
            cardOffset = cardOffset - (int) AIcards[i].getWidth() - 20;
            mAICards.add(AIcards[i]);
        }

        //Add deck
        mDeckCards = new ArrayList<>();
        cardOffset = 0;
        for (int i = 0; i < 3; i++) {
            deckCards[i] = new Card((mDefaultScreenViewport.right - 110), (mDefaultScreenViewport.bottom / 2 + cardOffset), this);
            mDeckCards.add(deckCards[i]);
            cardOffset = cardOffset - 10;
        }

        //Moving all cards to deck location for dealing animation
        for (int i = 0; i <5; i++) {
            mCards.get(i).setPosition((mDefaultScreenViewport.right - 110), (mDefaultScreenViewport.bottom / 2 - 30));
            mAICards.get(i).setPosition((mDefaultScreenViewport.right - 110), (mDefaultScreenViewport.bottom / 2 - 30));
        }

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

        //Checking all push buttons for an update(click)
        for (PushButton control : mControls) {
            control.update(elapsedTime, mDefaultLayerViewport, mDefaultScreenViewport);
        }

        //Checking if game is paused or unpaused
        managePause(elapsedTime);

        // Get any touch events that have occurred since the last update
        List<TouchEvent> touchEvents = input.getTouchEvents();
        if (touchEvents.size() > 0) {
            lastTouchEvent = touchEvents.get(touchEvents.size() - 1);
            lastTouchEventType = lastTouchEvent.type;
        }

        //Dealing cards if not yet dealt
        if (!cardsDealt && mAICards.size() > 0 && !gamePaused && unpauseCounter == 0) {
            dealCards(elapsedTime);
        }

        //Once cards have been dealt the user can now interact with cards
        if (cardsDealt && mCards.size() > 0 && !gamePaused && unpauseCounter == 0) {
            checkTouchActions(mCards, touchEvents, input);
        }

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
        int screenWidth = graphics2D.getSurfaceWidth();
        int screenHeight = graphics2D.getSurfaceHeight();

        //Draw the background
        mCardBackground.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        // Draw the cards
        if (mCards.size() > 0) {
            for (int i = 0; i < mCards.size(); i++) {
                currentCard = mCards.get(i);
                if (!rearFacing[i] && cardsDealt) {
                    currentCard.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
                } else {
                    currentCard.backDraw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
                }
            }
        }

        //Draw the AI Cards
        if (mAICards.size() > 0) {
            for (int i = 0; i < mAICards.size(); i++) {
                mAICards.get(i).backDraw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
            }
        }

        //Draw the Deck
        if (mDeckCards.size() > 0) {
            for (int i = 0; i < 3; i++) {
                mDeckCards.get(i).backDraw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
            }
        }

        // Draw the controls last of all
        for (PushButton control : mControls)
            control.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        //If the game is paused draw the pause menu
        if (gamePaused) {
            drawPause(elapsedTime, graphics2D, screenHeight, screenWidth);
        }

    }

    public void checkTouchActions(List<Card> mCards, List<TouchEvent> touchEvents, Input input) {

        for (int i = 0; i < mCards.size(); i++) {
            currentCard = mCards.get(i);
            mTouchIdExists = input.existsTouch(0);
            if (mTouchIdExists && !flipCard[i]) {
                mTouchLocation[0] = input.getTouchX(0);
                mTouchLocation[1] = (mDefaultLayerViewport.halfHeight * 2.0f) - input.getTouchY(0);
                if ((mTouchLocation[0] >= currentCard.getLeft()) & (mTouchLocation[0] <= (currentCard.getLeft() + currentCard.getWidth()))) {
                    if ((mTouchLocation[1] >= currentCard.getBottom()) & (mTouchLocation[1] <= (currentCard.getBottom() + currentCard.getHeight()))) {
                        if (touchEvents.size() > 0) {
                            if (lastTouchEventType == 2 || lastTouchEventType == 6) {
                                dragging[i] = true;
                                for (int i2 = 0; i2 < mCards.size(); i2++) {
                                    if (i2 != i && (dragging[i2] || flipCard[i2])) {
                                        dragging[i] = false;
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (!flipCard[i]){
                for (TouchEvent indexTouchEvent : touchEvents) {
                    if (indexTouchEvent.type == 5) {
                        if ((indexTouchEvent.x >= currentCard.getLeft()) & (indexTouchEvent.x <= (currentCard.getLeft() + currentCard.getWidth()))) {
                            if ((((mDefaultLayerViewport.halfHeight * 2.0f) - indexTouchEvent.y) >= currentCard.getBottom()) & (((mDefaultLayerViewport.halfHeight * 2.0f) - indexTouchEvent.y) <= (currentCard.getBottom() + currentCard.getHeight()))) {
                                flipCard[i] = true;
                            }
                        }
                    }
                }
            }

            if (flipCard[i]) {

                if (!flippingBack[i]) {
                    shrinkCard();
                }

                if (currentCard.getWidth() < 90.0f) {
                    rearFacing[i] = !rearFacing[i];
                    flippingBack[i] = !flippingBack[i];
                }

                if (flippingBack[i]) {
                    growCard();
                }

                if (currentCard.getWidth() == currentCard.getDefaultCardWidth()) {
                    flippingBack[i] = false;
                    flipCard[i] = false;
                }
            }

            if (dragging[i]) {
                currentCard.position.x = mTouchLocation[0];
                currentCard.position.y = mTouchLocation[1];
            }

            if (touchEvents.size() > 0) {
                lastTouchEvent = touchEvents.get(touchEvents.size() - 1);
                lastTouchEventType = lastTouchEvent.type;
                if (lastTouchEventType == 1) {
                    dragging[i] = false;
                    currentCard.position.x = currentCard.getSpawnX();
                    currentCard.position.y = currentCard.getSpawnY();
                }
            }


            BoundingBox playerBound = currentCard.getBound();
            if (playerBound.getLeft() < 0)
                currentCard.position.x -= playerBound.getLeft();
            else if (playerBound.getRight() > (mDefaultLayerViewport.halfWidth * 2.0f))
                currentCard.position.x -= (playerBound.getRight() - (mDefaultLayerViewport.halfWidth * 2.0f));

            if (playerBound.getBottom() < 0)
                currentCard.position.y -= playerBound.getBottom();
            else if (playerBound.getTop() > (mDefaultLayerViewport.halfHeight * 2.0f))
                currentCard.position.y -= (playerBound.getTop() - (mDefaultLayerViewport.halfHeight * 2.0f));
        }
    }

    public void shrinkCard(){

        if (currentCard.getWidth() > currentCard.getDefaultCardWidth() * 0.2){
            currentCard.setWidth((currentCard.getWidth() / 1500) * 1000);
        }

    }

    public void growCard(){

        if (currentCard.getWidth() < currentCard.getDefaultCardWidth()) {
            currentCard.setWidth((currentCard.getWidth() / 1000) * 1500);
        }
        if (currentCard.getWidth() > currentCard.getDefaultCardWidth()) {
            currentCard.setWidth(currentCard.getDefaultCardWidth());
        }

    }

    public void dealCards(ElapsedTime elapsedTime) {

            //Deal the users cards
            for (int i = 0; i < 5; i++) {
                mCards.get(i).update(elapsedTime, i);
            }

            //Deal the AI cards once the users cards are dealt
            if (mCards.get(0).position.x == mCards.get(0).getSpawnX() && mCards.get(0).position.y == mCards.get(0).getSpawnY()) {
                for (int i = 5; i < 10; i++) {
                    mAICards.get(i - 5).update(elapsedTime, i);
                }
            }

            //Once both sets of cards are dealt this method is no longer needed
            if (mAICards.get(4).position.x == mAICards.get(4).getSpawnX() && mAICards.get(4).position.y == mAICards.get(4).getSpawnY()) {
                cardsDealt = true;
            }
    }

    public void managePause(ElapsedTime elapsedTime) {
        if (gamePaused) {
            pausedContinue.update(elapsedTime, mDefaultLayerViewport, mDefaultScreenViewport);
            pausedQuit.update(elapsedTime, mDefaultLayerViewport, mDefaultScreenViewport);
        }

        if (pause.isPushTriggered()) {
            gamePaused = true;
        } else if (pausedContinue.isPushTriggered()) {
            gamePaused = false;
            unpauseCounter = 5;
        } else if (pausedQuit.isPushTriggered()) {
            mGame.getScreenManager().removeScreen(this);
            mGame.getScreenManager().removeScreen("SplashScreen");
        }

        if (unpauseCounter > 0) {
            unpauseCounter = unpauseCounter - 1;
        }
    }

    public void drawPause(ElapsedTime elapsedTime, IGraphics2D graphics2D, int screenHeight, int screenWidth) {
        mPauseMenu.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(screenHeight / 12.0f);
        textPaint.setTextAlign(Paint.Align.CENTER);
        graphics2D.drawText("PAUSE MENU", screenWidth / 2, screenHeight / 4.0f, textPaint);
        pausedContinue.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        pausedQuit.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
    }
}


