package uk.ac.qub.eeecs.gage;

import android.graphics.Bitmap;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.util.ArrayList;
import java.util.List;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.game.SimCards.Card;
import uk.ac.qub.eeecs.game.SimCards.SimCardsScreen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.content.Context;

import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.world.GameScreen;


@RunWith(AndroidJUnit4.class)
public class cardFlipUnitTest {

    private Context context;
    private Game game;
    private GameScreen gameScreen;


    @Test

    @Before
    public void setUp() {
        game = new GameTest(1280, 720);
        gameScreen = new SimCardsScreen(game);
        game.getAssetManager();



        //  context = InstrumentationRegistry.getTargetContext;
    }

    @Test
    public void cardFlip_validData_isSuccessful() {

    }

    public void checkFlip() {
        gameScreen = new SimCardsScreen(game);
        game.getScreenManager().addScreen(new SimCardsScreen(game));
        SimCardsScreen simCards = new SimCardsScreen(game);
        if (simCards.getCurrentCard().getWidth() < 90.0f) {
            assertEquals(simCards.getRearFacing(), null);
        }
    }

    public void checkGrowMethod(){
        gameScreen = new SimCardsScreen(game);
        game.getScreenManager().addScreen(new SimCardsScreen(game));
        SimCardsScreen simCards = new SimCardsScreen(game);
        simCards.growCard();

        if (simCards.getCurrentCard().getWidth() < simCards.getCurrentCard().getDefaultCardWidth()){
            assertEquals(simCards.getCurrentCard().getWidth(), (simCards.getCurrentCard().getWidth() / 1000) * 1500);
        }
    }

    public void checkShrinkMethod(){
        gameScreen = new SimCardsScreen(game);
        game.getScreenManager().addScreen(new SimCardsScreen(game));
        SimCardsScreen simCards = new SimCardsScreen(game);
        simCards.shrinkCard();

        if (simCards.getCurrentCard().getWidth() > simCards.getCurrentCard().getDefaultCardWidth() * 0.2){
            assertEquals(simCards.getCurrentCard().getWidth(), (simCards.getCurrentCard().getWidth() / 1500) * 1000);
        }
    }
}
