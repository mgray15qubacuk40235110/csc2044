package uk.ac.qub.eeecs.game.SimCards;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.engine.input.Input;

public class DeckScreen extends GameScreen {

    //Defines image for background
    private GameObject mBackground;

    //Define the back button to return to the main menu
    private PushButton mBackButton;

    //Define deck of cards to be displayed
    private List<Card> mCards;
    private Card[] cards = new Card[20];

    //Measures scroll speed
    float scroll = 0;

    //Manages touch events
    private TouchEvent lastTouchEvent;
    int lastTouchEventType;
    private boolean mTouchIdExists;
    private boolean scrolling;
    private float[] originalTouchLocation = new float[2];
    private float[] releaseTouchLocation = new float[2];

    //Defaults
    private static final int DEFAULT_CARD_HEIGHT = (int) (Resources.getSystem().getDisplayMetrics().heightPixels / 2.5);
    private static final int DEFAULT_CARD_WIDTH = (int) (0.69230769 * DEFAULT_CARD_HEIGHT);

    public DeckScreen(Game game) {
        super("DeckScreen", game);

        setupViewports();

        Card.resetCards();

        // Load in the bitmaps used on the main menu screen
        AssetManager assetManager = mGame.getAssetManager();
        assetManager.loadAndAddBitmap("Background", "img/SimCardsMenuBackground.png");
        assetManager.loadAndAddBitmap("BackArrow", "img/BackArrow.png");
        assetManager.loadAndAddBitmap("BackArrowSelected", "img/BackArrowSelected.png");
        mGame.getAssetManager().loadAssets("txt/assets/CardDemoScreenAssets.JSON");

        //Define Background
        mBackground = new GameObject(mDefaultLayerViewport.halfWidth,
                mDefaultLayerViewport.halfHeight, mDefaultLayerViewport.halfWidth * 4.4f, mDefaultLayerViewport.halfHeight * 5.4f, getGame()
                .getAssetManager().getBitmap("Background"), this);

        //Back Button
        mBackButton = new PushButton(
                mDefaultLayerViewport.getWidth() * 0.95f, mDefaultLayerViewport.getHeight() * 0.10f,
                mDefaultLayerViewport.getWidth() * 0.075f, mDefaultLayerViewport.getHeight() * 0.10f,
                "BackArrow", "BackArrowSelected", this);
        mBackButton.setPlaySounds(true, true);


        //Define Deck
        mCards = new ArrayList<>();
        int xOffset = 120;
        int xOffset2 = 120;
        int xOffset3 = 120;
        int xOffset4 = 120;

        //Layout cards in rows
        for (int i = 0; i < 20; i++) {
            if (i < 5) {
                cards[i] = new Card(mDefaultScreenViewport.left + 100 + xOffset,mDefaultScreenViewport.bottom - 270, DEFAULT_CARD_WIDTH, DEFAULT_CARD_HEIGHT, this);
                xOffset = xOffset + 65 + DEFAULT_CARD_WIDTH;
                mCards.add(cards[i]);
            } else if (i < 10) {
                cards[i] = new Card(mDefaultScreenViewport.left + 100 + xOffset2, mDefaultScreenViewport.bottom - 730, DEFAULT_CARD_WIDTH, DEFAULT_CARD_HEIGHT, this);
                xOffset2 = xOffset2 + 65 + DEFAULT_CARD_WIDTH;
                mCards.add(cards[i]);
            } else if (i < 15) {
                cards[i] = new Card(mDefaultScreenViewport.left + 100 + xOffset3, mDefaultScreenViewport.bottom - 1190, DEFAULT_CARD_WIDTH, DEFAULT_CARD_HEIGHT, this);
                xOffset3 = xOffset3 + 65 + DEFAULT_CARD_WIDTH;
                mCards.add(cards[i]);
            } else {
                cards[i] = new Card(mDefaultScreenViewport.left + 100 + xOffset4, mDefaultScreenViewport.bottom - 1650, DEFAULT_CARD_WIDTH, DEFAULT_CARD_HEIGHT, this);
                xOffset4 = xOffset4 + 65 + DEFAULT_CARD_WIDTH;
                mCards.add(cards[i]);
            }
        }
    }

    private void setupViewports() {

        mDefaultLayerViewport.set(getScreenWidth() / 2, getScreenHeight() / 2, getScreenWidth() / 2, getScreenHeight() / 2);
        mDefaultScreenViewport.set(0, 0, (int) mDefaultLayerViewport.halfWidth * 2, (int) mDefaultLayerViewport.halfHeight * 2);

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

        // Process any touch events occurring since the last update
        Input input = mGame.getInput();

        //Updating back button and checking if tapped
        mBackButton.update(elapsedTime);
        if (mBackButton.isPushTriggered()) {
            mGame.getScreenManager().removeScreen(this);
        }

        //Updating screen with any scrolling actions
        checkScroll(input);

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

        //Draw Background
        mBackground.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        //Draw Cards
        if (mCards.size() > 0) {
            for (int i = 0; i < mCards.size(); i++) {
                mCards.get(i).draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
            }
        }

        //Draw Button
        mBackButton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels + 128;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public void checkScroll(Input input) {

        //Getting touch events since last update
        List<TouchEvent> touchEvents = input.getTouchEvents();
        if (touchEvents.size() > 0) {
            lastTouchEvent = touchEvents.get(touchEvents.size() - 1);
            lastTouchEventType = lastTouchEvent.type;
        }

        //Checking if either the user is scrolling or, if they're not, have they released their finger
        mTouchIdExists = input.existsTouch(0);
        if (mTouchIdExists && !scrolling) {
            originalTouchLocation[0] = input.getTouchX(0);
            originalTouchLocation[1] = (mDefaultLayerViewport.halfHeight * 2.0f) - input.getTouchY(0);
            if (touchEvents.size() > 0) {
                if (lastTouchEventType == 2 || lastTouchEventType == 6) {
                    scroll = 0;
                    scrolling = true;
                }
            }
        } else {
            for (TouchEvent indexTouchEvent : touchEvents) {
                if (indexTouchEvent.type == 1) {
                    scrolling = false;
                }
            }
        }

        //This runs when the user is scrolling with their finger
        if(scrolling) {
            //Get current finger location
            releaseTouchLocation[1] = (mDefaultLayerViewport.halfHeight * 2.0f) - input.getTouchY(0);
            //Different circumstances, is user scrolling down or up, once they've stopped scrolling slow down the scroll
            if (releaseTouchLocation[1] > originalTouchLocation[1]) {
                scroll -= 3.0f;
            }
            else if (releaseTouchLocation[1] < originalTouchLocation[1]) {
                scroll += 3.0f;
            }
            else if (scroll > 0) {
                scroll = scroll - 1.5f;
            }
            else if (scroll < 0) {
                scroll = scroll + 1.5f;
            }
        }

        //Keeps screen in upper and lower bounds
        if (mDefaultLayerViewport.getBottom() < -830) {
            mDefaultLayerViewport.y -= (mDefaultLayerViewport.getBottom() + 830);
            scroll = 0;
            scrolling = false;
        }
        else if (mDefaultLayerViewport.getTop() > 1080) {
            mDefaultLayerViewport.y -= (mDefaultLayerViewport.getTop() - 1080);
            scroll = 0;
            scrolling = false;
        }

        //Applying the calculated scroll to the screen
        mDefaultLayerViewport.y += scroll;

        //Keep back button in same position at all times
        mBackButton.setPosition(mDefaultLayerViewport.getRight() - 100, mDefaultLayerViewport.y - 430);

        }

    }
