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
    private Card card;
    private Card card2;
    private Card[] cards = new Card[5];
    private List<Card> mCards;
    int cardOffset;

    //Buttons
    private PushButton endTurn;
    private List<PushButton> mControls;

    //Touch Input
    private boolean mTouchIdExists;
    private float[] mTouchLocation = new float[2];
    boolean dragging;

    //Enabling text output
    private Paint textPaint = new Paint();


    /**
     * Width and height of the level
     */

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////


    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels + 96;
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
                .getAssetManager().getBitmap("SimCardBackground"), this);

        // Create cards
        mCards = new ArrayList<>();
        cardOffset = 10;
        for (int i = 0; i < cards.length; i++) {
            cards[i] = new Card((mDefaultScreenViewport.left + 90 + cardOffset), (mDefaultScreenViewport.top + 120), this);
            cardOffset = cardOffset + (int) cards[i].getWidth() + 20;
            mCards.add(cards[i]);
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


        if (mCards.size() > 0) {
            for (Card card : mCards) {
                mTouchIdExists = input.existsTouch(0);
                if (mTouchIdExists) {
                    List<TouchEvent> touchEvents = input.getTouchEvents();
                    if (touchEvents.size() > 0) {
                        TouchEvent lastTouchEvent = touchEvents.get(touchEvents.size() - 1);
                        mTouchLocation[0] = input.getTouchX(0);
                        mTouchLocation[1] = (mDefaultLayerViewport.halfHeight * 2.0f) - input.getTouchY(0);
                        if ((mTouchLocation[0] >= card.getLeft()) & (mTouchLocation[0] <= (card.getLeft() + card.getWidth()))) {
                            if ((mTouchLocation[1] >= card.getBottom()) & (mTouchLocation[1] <= (card.getBottom() + card.getHeight()))) {
                                //dragging = true;
                                //while (dragging) {
                                    //TouchEvent lastTouchEvent = touchEvents.get(touchEvents.size() - 1);
                                    //if (lastTouchEvent.type == 1) {
                                        //dragging = false;
                                    //}
                                    card.position.x = mTouchLocation[0];
                                    card.position.y = mTouchLocation[1];
                                //}
                            }
                        }
                    }
                }

                BoundingBox playerBound = card.getBound();
                if (playerBound.getLeft() < 0)
                    card.position.x -= playerBound.getLeft();
                else if (playerBound.getRight() > (mDefaultLayerViewport.halfWidth * 2.0f))
                    card.position.x -= (playerBound.getRight() - (mDefaultLayerViewport.halfWidth * 2.0f));

                if (playerBound.getBottom() < 0)
                    card.position.y -= playerBound.getBottom();
                else if (playerBound.getTop() > (mDefaultLayerViewport.halfHeight * 2.0f))
                    card.position.y -= (playerBound.getTop() - (mDefaultLayerViewport.halfHeight * 2.0f));
            }
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
        textPaint.setColor(Color.RED);
        textPaint.setTextSize(screenHeight / 16.0f);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTypeface(Typeface.MONOSPACE);

        // Set font values for drawing the touch information
        float lineHeight = screenHeight / 30.0f;
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTextSize(lineHeight);


        // Draw the cards

        if (mCards.size() > 0) {
            for (Card card : mCards) {
                card.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
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

        /*
        if (mCards.size() > 0) {
            graphics2D.drawText("Card Location [" + String.format("%.2f, %.2f]", cards[0].position.x, cards[0].position.y), 10.0f, 60.0f, textPaint);
        }
        */
    }

}


