package uk.ac.qub.eeecs.gage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.audio.AudioManager;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.OptionsScreen;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MusicTest {

    String optionsScreenMenu = "Menu Screen";
    String gameScreenName = "Game Screen";

    @Mock
    Game game;
    @Mock
    GameScreen gameScreen = Mockito.mock(GameScreen.class);
    @Mock
    OptionsScreen optionsScreen = Mockito.mock(OptionsScreen.class);

    @Before
    public void SetUpScreen() {
        when(optionsScreen.getName()).thenReturn(optionsScreenMenu);
        when(gameScreen.getName()).thenReturn(gameScreenName);
        game.mAudioManager = new AudioManager(game);

    }

}
