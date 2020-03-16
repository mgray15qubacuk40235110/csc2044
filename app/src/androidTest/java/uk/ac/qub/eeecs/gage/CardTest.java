package uk.ac.qub.eeecs.gage;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.audio.AudioManager;
import uk.ac.qub.eeecs.gage.engine.io.FileIO;
import uk.ac.qub.eeecs.game.DemoGame;
import uk.ac.qub.eeecs.game.SimCards.Card;
import uk.ac.qub.eeecs.game.SimCards.SimCardsScreen;
import uk.ac.qub.eeecs.game.SimCards.SplashScreen;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class  CardTest {

    private Context context;
    private DemoGame game;
    private SimCardsScreen simCards;
    private AssetManager assetManager;
    private SplashScreen splashScreen;
    private Card card;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
        game.mFileIO = new FileIO(context);
        game.mAssetManager = new AssetManager(game);
        game.mAudioManager = new AudioManager(game);
        splashScreen = new SplashScreen(game);
        simCards = new SimCardsScreen(game);
    }

    @Test
    public void cardCreation() {
        Card testCard = new Card(300.0f, 300.0f, splashScreen);
        assertNotNull(testCard);
    }

    @Test
    public void cardSpawnTest() {
        Card testCard = new Card(300.0f, 300.0f, splashScreen);
        assertTrue(testCard.position.x == testCard.getSpawnX());
        assertTrue(testCard.position.y == testCard.getSpawnY());
    }

    @Test
    public void cardWidthTest() {
        Card testCard = new Card(300.0f, 300.0f, splashScreen);
        assertTrue(Card.getDefaultCardWidth() == (testCard.getRight() - testCard.getLeft()));
    }

}
