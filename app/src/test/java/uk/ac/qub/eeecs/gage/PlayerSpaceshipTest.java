package uk.ac.qub.eeecs.gage;
import android.content.Entity;
import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.game.MenuScreen;
import uk.ac.qub.eeecs.game.spaceDemo.Asteroid;
import uk.ac.qub.eeecs.game.spaceDemo.PlayerSpaceship;
import uk.ac.qub.eeecs.game.spaceDemo.SpaceEntity;
import uk.ac.qub.eeecs.game.spaceDemo.Turret;
import uk.ac.qub.eeecs.game.spaceDemo.SpaceshipDemoScreen;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlayerSpaceshipTest {

    // We need to firstly mock the set up of the spaceship demo screen before we call the unit test function on it
    @Mock
    private Game game; // Mock version of: Game screen
    @Mock
    private SpaceshipDemoScreen spaceshipDemoScreen; // Mock version of: 'spaceshipDemoScreen'
    @Mock
    private AssetManager assetManager; // Mock version of: 'assetManager'
    @Mock
    private Bitmap bitmap; // Mock version of: 'bitmap'
    @Mock
    private Input input; // Mock version of: 'input'
    @Mock
    private PlayerSpaceship mPlayerSpaceship;


    @Before
    public void setUpMockTest() {
        when(game.getAssetManager()).thenReturn(assetManager);
        when(assetManager.getBitmap(any(String.class))).thenReturn(bitmap);
        when(game.getInput()).thenReturn(input);
        when(spaceshipDemoScreen.getGame()).thenReturn(game);
        when(spaceshipDemoScreen.getName()).thenReturn("SpaceshipDemoScreen");

        // We then construct a mock version of the spaceship demo screen using what has been declared above

    }

    // This is the function which will test something related to the player spaceship
    @Test
    public void expectedMaxAcceleration() {

        // Define what we expect
        float expectedMaxAcceleration = 600.0f;

        mPlayerSpaceship = new PlayerSpaceship(100, 100, spaceshipDemoScreen);

        // Test this condition

        assertTrue(mPlayerSpaceship.maxAcceleration == expectedMaxAcceleration);
        assertEquals(mPlayerSpaceship.getBitmap(), bitmap);
    }

}
