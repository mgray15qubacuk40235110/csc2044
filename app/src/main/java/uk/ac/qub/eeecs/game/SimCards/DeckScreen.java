package uk.ac.qub.eeecs.game.SimCards;

import android.content.res.Resources;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;

public class DeckScreen extends GameScreen {

    private GameObject mBackground;

    //Define the back button to return to the main menu
    private PushButton mBackButton;

    //Define deck of cards to be displayed
    private List<Card> mCards;
    private Card[] cards = new Card[20];

    //Defaults
    private static final int DEFAULT_CARD_HEIGHT = (int) (Resources.getSystem().getDisplayMetrics().heightPixels / 2.5);
    private static final int DEFAULT_CARD_WIDTH = (int) (0.69230769 * DEFAULT_CARD_HEIGHT);

    public DeckScreen(Game game) {
        super("DeckScreen", game);

        setupViewports();

        // Load in the bitmaps used on the main menu screen
        AssetManager assetManager = mGame.getAssetManager();
        assetManager.loadAndAddBitmap("Background", "img/SimCardsMenuBackground.png");
        assetManager.loadAndAddBitmap("BackArrow", "img/BackArrow.png");
        assetManager.loadAndAddBitmap("BackArrowSelected", "img/BackArrowSelected.png");
        mGame.getAssetManager().loadAssets("txt/assets/CardDemoScreenAssets.JSON");

        //Define Background
        mBackground = new GameObject(mDefaultLayerViewport.halfWidth,
                mDefaultLayerViewport.halfHeight, mDefaultLayerViewport.halfWidth * 2, mDefaultLayerViewport.halfHeight * 2, getGame()
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

        // Setup the screen viewport to use the full screen.
        // mDefaultScreenViewport.set( 0, 0, mGame.getScreenWidth(), mGame.getScreenHeight());

        // Calculate the layer height that will preserved the screen aspect ratio
        // given an assume 480 layer width.
        //float layerHeight = mGame.getScreenHeight() * (480.0f / mGame.getScreenWidth());

        //mDefaultLayerViewport.set(240.0f, layerHeight/2.0f, 240.0f, layerHeight/2.0f);
        //mSpaceLayerViewport = new LayerViewport(240.0f, layerHeight/2.0f, 240.0f, layerHeight/2.0f);
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

        mBackButton.update(elapsedTime);

        if (mBackButton.isPushTriggered()) {
            mGame.getScreenManager().removeScreen(this);
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
}
