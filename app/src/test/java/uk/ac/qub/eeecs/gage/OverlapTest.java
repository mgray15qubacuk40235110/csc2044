package uk.ac.qub.eeecs.gage;

import android.graphics.Bitmap;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.util.BoundingBox;
import uk.ac.qub.eeecs.gage.util.CollisionDetector;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.game.MenuScreen;
import uk.ac.qub.eeecs.game.platformDemo.Platform;
import uk.ac.qub.eeecs.game.platformDemo.PlatformDemoScreen;
import uk.ac.qub.eeecs.game.spaceDemo.SpaceshipDemoScreen;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OverlapTest {

    @Mock
    private Game game;
    @Mock
    private Input input;
    @Mock
    private AssetManager assetManager;
    @Mock
    private Bitmap bitmap;
    @Mock
    private PlatformDemoScreen platformDemoScreen;
    @Mock
    private Platform platform;

    @Before
    public void setUp() {
        when(game.getAssetManager()).thenReturn(assetManager);
        when(assetManager.getBitmap(any(String.class))).thenReturn(bitmap);
        when(game.getInput()).thenReturn(input);
        when(platformDemoScreen.getGame()).thenReturn(game);
        when(platformDemoScreen.getName()).thenReturn("PlatformDemoScreen");
    }

    @Test
    public void overlap_Platform_Method_Test() {
        ArrayList<Platform> mPlatforms = new ArrayList<>();
        int numPlatforms = 2, platformOffset = 200;
        float platformWidth = 70, platformHeight = 70, platformX = platformHeight, platformY = platformHeight;

        Platform one = new Platform(platformX, platformY, platformWidth, platformHeight, "platform", platformDemoScreen);
        mPlatforms.add(one);

        Platform two = new Platform(platformX + 1, platformY + 1, platformWidth, platformHeight, "platform", platformDemoScreen);
        mPlatforms.add(two);

        assertFalse(platformDemoScreen.overlap(mPlatforms, new Platform(platformX, platformY , platformWidth, platformHeight, "platform", platformDemoScreen)));
    }

    @Test
    public void Test_Test() {
    assertTrue(1 == 1);
    }
}
