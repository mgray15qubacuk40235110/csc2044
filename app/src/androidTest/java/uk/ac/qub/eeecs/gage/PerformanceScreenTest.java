package uk.ac.qub.eeecs.gage;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.runner.RunWith;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.Performace.PerformanceScreen;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class PerformanceScreenTest {
    private Game game;
    private GameScreen gameScreen;
    ElapsedTime elaspedTime = new ElapsedTime();
    AssetManager assetManager;

    @Before

    public void setUp()
    {
        game = new GameTest(1200, 700);
        gameScreen = new PerformanceScreen(game);
        assetManager = game.getAssetManager();
    }


    public void checkNumRectangles()
{
    gameScreen = new PerformanceScreen(game);
    game.getScreenManager().addScreen(new PerformanceScreen(game));
    PerformanceScreen perScreen = new PerformanceScreen((game));
    perScreen.getMupFrames().isPushTriggered();
    if(perScreen.getNumberOfRectangles() == 2)
    {
        assertEquals(perScreen.getNumberOfRectangles(),2);
    }
}
}
class GameTest extends Game{
    public GameTest(int i, int j) {
        super();

        this.mAssetManager = new AssetManager(this);
    }
}
