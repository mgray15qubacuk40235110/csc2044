package uk.ac.qub.eeecs.gage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.SimCards.SplashScreen;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)

public class SplashscreenTest {

    String splashscreen = "Splashscreen";
    String gameScreenName = "Game Screen";

    @Mock
    Game game;
    @Mock
    GameScreen gameScreen = Mockito.mock(GameScreen.class);
    @Mock
    SplashScreen splashScreen = Mockito.mock(SplashScreen.class);

    @Before
    public void SetUpSplashScreen() {
        when(splashScreen.getName()).thenReturn(splashscreen);
        when(gameScreen.getName()).thenReturn(gameScreenName);
    }

    @Test
    public void addSplashScreenTest() throws Exception {
        ScreenManager manager = new ScreenManager(game);
        manager.addScreen(splashScreen);
        assertEquals(splashScreen, manager.getCurrentScreen());
        System.out.println("2. Success!");
    }

    @Test
    public void getSplashScreenTest() throws Exception {
        ScreenManager manager = new ScreenManager(game);
        manager.addScreen(gameScreen);
        manager.addScreen(splashScreen);
        assertEquals(splashScreen, manager.getCurrentScreen());
        System.out.println("4. Success!");
    }

    @Test
    public void removeSplashScreenIfValid () throws Exception {
        ScreenManager manager = new ScreenManager(game);
        manager.addScreen(splashScreen );
        manager.addScreen(gameScreen);
        assertTrue(manager.removeScreen(splashscreen));
        System.out.println("1. Success!");
    }

    @Test
    public void removeSplashScreenIfNotValid() throws Exception {
        ScreenManager manager = new ScreenManager(game);
        manager.addScreen(splashScreen );
        manager.addScreen(gameScreen);
        manager.removeScreen(splashScreen);
        assertFalse(manager.removeScreen(splashscreen));
        System.out.println("3. Success!");
    }
}
