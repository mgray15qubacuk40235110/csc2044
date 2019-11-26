package uk.ac.qub.eeecs.game.cardDemo;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.game.cardDemo.Card;
import uk.ac.qub.eeecs.game.spaceDemo.SpaceEntity;

/**
 * Starter class for Card game stories
 *
 * @version 1.0
 */
public class CardDemoScreen extends GameScreen {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Width and height of the level
     */
    private final float LEVEL_WIDTH = 500.0f;
    private final float LEVEL_HEIGHT = 400.0f;

    // Card width and height

//    private static final int DEFAULT_CARD_WIDTH = 20;
//    private static final int DEFAULT_CARD_HEIGHT = 100;

    // Define a card to be displayed
    private Card card;

    // Define the number of cards //

    private final int NUM_CARDS = 5;

    // Storage for cards //

    private List<Card> mCards;

    // Define a card game viewport //

    protected LayerViewport mCardLayerViewport;

    // Define the card game background //

    private GameObject mCardBackground;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create the Card game screen
     *
     * @param game Game to which this screen belongs
     */
    public CardDemoScreen(Game game) {
        super("CardScreen", game);

        setupViewports();

        setupCardGameObjects();
    }

    private void setupViewports() {
        // Setup the screen viewport to use the full screen.
        mDefaultScreenViewport.set(0, 0, mGame.getScreenWidth(), mGame.getScreenHeight());

        // Calculate the layer height that will preserved the screen aspect ratio
        // given an assume 480 layer width.
        float layerHeight = mGame.getScreenHeight() * (480.0f / mGame.getScreenWidth());

        mDefaultLayerViewport.set(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
        mCardLayerViewport = new LayerViewport(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
    }

    private void setupCardGameObjects() {
        mGame.getAssetManager().loadAssets("txt/assets/CardDemoScreenAssets.JSON");

        mCardBackground = new GameObject(LEVEL_WIDTH / 2.0f,
                LEVEL_HEIGHT / 2.0f, LEVEL_WIDTH, LEVEL_HEIGHT, getGame()
                .getAssetManager().getBitmap("CardBackground2"), this);

        // card = new Card(100, 100, this);

        // Initial array storage for cards //

        mCards = new ArrayList<>(NUM_CARDS);

        // Random Cards //
        for (int i = 0; i < NUM_CARDS; i++)
            mCards.add(new Card(100, 100, this));

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

        // Update the card
        card.angularVelocity = 0.0f;

        card.update(elapsedTime);
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

        mCardBackground.draw(elapsedTime, graphics2D, mCardLayerViewport,
                mDefaultScreenViewport);

        for (Card cards : mCards)
            cards.draw(elapsedTime, graphics2D,
                    mDefaultLayerViewport, mDefaultScreenViewport);

        // Draw the card
      //  card.draw(elapsedTime, graphics2D,
              //  mDefaultLayerViewport, mDefaultScreenViewport);

    }
}
