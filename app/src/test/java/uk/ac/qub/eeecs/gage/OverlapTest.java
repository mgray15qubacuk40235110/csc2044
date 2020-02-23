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
    @Mock
    private Log log;

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
        float platformWidth = 70, platformHeight = 70, platformX = platformHeight, platformY = platformHeight;

        Platform one = new Platform(platformX * 2 + 1, platformY * 2 + 1, platformWidth, platformHeight, "platform", platformDemoScreen);
        mPlatforms.add(one);

        Platform two = new Platform(platformX + 1, platformY + 1, platformWidth, platformHeight, "platform", platformDemoScreen);
        mPlatforms.add(two);

        Platform testPlatform = new Platform(platformX, platformY , platformWidth, platformHeight, "platform", platformDemoScreen);

        assertTrue(overlap(mPlatforms, testPlatform));
    }

    public boolean overlap(ArrayList<Platform> mPlatforms, Platform plat) {

        if (mPlatforms.size() == 0 || mPlatforms == null) {
            return false;
        }
        for (int i = 0; i < mPlatforms.size(); i++) {

            System.out.println("mPlatform " + i + ": " + mPlatforms.get(i).get_x());
            System.out.println("New platform: " + plat.get_x());

            if (plat.get_x() >= mPlatforms.get(i).get_x() && plat.get_x() <= (mPlatforms.get(i).get_x() + mPlatforms.get(i).getWidth())
                    || (plat.get_x() + plat.getWidth()) >= mPlatforms.get(i).get_x() && (plat.get_x() + plat.getWidth()) <= (mPlatforms.get(i).get_x() + mPlatforms.get(i).getWidth()))
            {
                if (plat.get_y() >= mPlatforms.get(i).get_y() && plat.get_y() <= (mPlatforms.get(i).get_y() + mPlatforms.get(i).getHeight())
                        || (plat.get_y() + plat.getHeight()) >= mPlatforms.get(i).get_y() && (plat.get_y() + plat.getHeight()) <= (mPlatforms.get(i).get_y() + mPlatforms.get(i).getHeight())) {
                    System.out.println("New platform overlaps with existing mPlatform " + i);
                    System.out.println();
                    return true;
                }
            }
            System.out.println("New platform DOES NOT overlap with existing mPlatform " + i);
            System.out.println();
        }
        return false;
    }
}
