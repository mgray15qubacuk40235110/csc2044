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
import uk.ac.qub.eeecs.game.StatsMenu;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)

public class StatsTest {

    String statsmenu = "Stats";
    String gameScreenName = "Game Screen";

    @Mock
    Game game;
    @Mock
    GameScreen gameScreen = Mockito.mock(GameScreen.class);
    @Mock
    StatsMenu statsMenu = Mockito.mock(StatsMenu.class);

    @Before
    public void SetUpSplashScreen() {
        when(statsMenu.getName()).thenReturn(statsmenu);
        when(gameScreen.getName()).thenReturn(gameScreenName);
    }

    @Test
    public void addSplashScreenTest() throws Exception {
        ScreenManager manager = new ScreenManager(game);
        manager.addScreen(statsMenu);
        assertEquals(statsMenu, manager.getCurrentScreen());
        System.out.println("2. Success!");
    }

    @Test
    public void getSplashScreenTest() throws Exception {
        ScreenManager manager = new ScreenManager(game);
        manager.addScreen(gameScreen);
        manager.addScreen(statsMenu);
        assertEquals(statsMenu, manager.getCurrentScreen());
        System.out.println("4. Success!");
    }

    @Test
    public void removeSplashScreenIfValid () throws Exception {
        ScreenManager manager = new ScreenManager(game);
        manager.addScreen(statsMenu );
        manager.addScreen(gameScreen);
        assertTrue(manager.removeScreen(statsmenu));
        System.out.println("1. Success!");
    }

    @Test
    public void removeSplashScreenIfNotValid() throws Exception {
        ScreenManager manager = new ScreenManager(game);
        manager.addScreen(statsMenu );
        manager.addScreen(gameScreen);
        manager.removeScreen(statsMenu);
        assertFalse(manager.removeScreen(statsmenu));
        System.out.println("3. Success!");
    }
}
