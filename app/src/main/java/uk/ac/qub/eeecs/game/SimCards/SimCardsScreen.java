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
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;



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

    // Define a card to be displayed
    private Card currentCard;
    private Card[] cards = new Card[5];
    private Card[] AIcards = new Card[5];
    private List<Card> mCards;
    private List<Card> mAICards;
    private int cardOffset;
    private boolean[] flippingBack = new boolean[cards.length];

    //Buttons
    private PushButton endTurn;
    private List<PushButton> mControls;

    //Touch Input
    private boolean mTouchIdExists;
    private float[] mTouchLocation = new float[2];
    private boolean[] dragging = new boolean[cards.length];
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
        mControls.add(endTurn);

        // Create the card background
        mCardBackground = new GameObject(mDefaultLayerViewport.halfWidth,
                mDefaultLayerViewport.halfHeight, mDefaultLayerViewport.halfWidth * 2, mDefaultLayerViewport.halfHeight * 2, getGame()
                .getAssetManager().getBitmap("SimCardBackground3"), this);

        // Create cards
        mCards = new ArrayList<>();
        cardOffset = 20;
        for (int i = 0; i < cards.length; i++) {
            cards[i] = new Card((mDefaultScreenViewport.left + 90 + cardOffset), (mDefaultScreenViewport.top + 120), this);
            cardOffset = cardOffset + (int) cards[i].getWidth() + 20;
            mCards.add(cards[i]);
        }

        // Create AI Cards
        mAICards = new ArrayList<>();
        cardOffset = -20;
        for (int i = 0; i < AIcards.length; i++) {
            AIcards[i] = new Card((mDefaultScreenViewport.right - 90 + cardOffset), (mDefaultScreenViewport.bottom - 160), this);
            cardOffset = cardOffset - (int) AIcards[i].getWidth() - 20;
            mAICards.add(AIcards[i]);
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

        for (PushButton control : mControls)
            control.update(elapsedTime, mDefaultLayerViewport, mDefaultScreenViewport);

        // Get any touch events that have occurred since the last update
        List<TouchEvent> touchEvents = input.getTouchEvents();
        if (touchEvents.size() > 0) {
            lastTouchEvent = touchEvents.get(touchEvents.size() - 1);
            lastTouchEventType = lastTouchEvent.type;
        }

        if (mCards.size() > 0) {
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

        mCardBackground.draw(elapsedTime, graphics2D, mDefaultLayerViewport,
                mDefaultScreenViewport);

        int screenWidth = graphics2D.getSurfaceWidth();
        int screenHeight = graphics2D.getSurfaceHeight();

        // Display a message to the user
        textPaint.setColor(Color.YELLOW);
        textPaint.setTextSize(screenHeight / 16.0f);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTypeface(Typeface.MONOSPACE);

        // Set font values for drawing the touch information
        float lineHeight = screenHeight / 30.0f;
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTextSize(lineHeight);


        // Draw the cards

        if (mCards.size() > 0) {
            for (int i = 0; i < mCards.size(); i++) {
                currentCard = mCards.get(i);
                if (!rearFacing[i]) {
                    currentCard.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
                } else {
                    currentCard.backDraw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
                }
            }
        }

        if (mAICards.size() > 0) {
            for (int i = 0; i < mAICards.size(); i++) {
                mAICards.get(i).backDraw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
            }
        }

        // Draw the controls last of all
        for (PushButton control : mControls)
            control.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);


        if (mTouchIdExists) {
            graphics2D.drawText("Pointer Id Detected [" +
                            String.format("%.2f, %.2f]", mTouchLocation[0], mTouchLocation[1]),
                    10.0f, 30.0f, textPaint);
        } else {
            graphics2D.drawText("Pointer Id Not detected.",
                    10.0f, 30.0f, textPaint);
        }

        // Draw the touch event history
        int lineNumber = 1;
        textPaint.setTextAlign(Paint.Align.RIGHT);
        for (int eventIdx = 0; eventIdx < mTouchEventsInfo.size(); eventIdx++) {
            graphics2D.drawText(mTouchEventsInfo.get(eventIdx),
                    screenWidth, lineHeight * lineNumber++, textPaint);
        }

    }

    public void checkTouchActions(List<Card> mCards, List<TouchEvent> touchEvents, Input input) {

        for (int i = 0; i < mCards.size(); i++) {
            currentCard = mCards.get(i);
            mTouchIdExists = input.existsTouch(0);
            if (mTouchIdExists && flipCard[i] == false) {
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
            } else if (flipCard[i] == false){
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

                if (flippingBack[i] == false) {
                    shrinkCard();
                }

                if (currentCard.getWidth() < 90.0f) {
                    rearFacing[i] = !rearFacing[i];
                    flippingBack[i] = !flippingBack[i];
                }

                if (flippingBack[i] == true) {
                    growCard();
                }

                if (currentCard.getWidth() == currentCard.getDefaultCardWidth()) {
                    flippingBack[i] = false;
                    flipCard[i] = false;
                }
            }

            if (dragging[i] == true) {
                //flipCard[i] = false;
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

}


