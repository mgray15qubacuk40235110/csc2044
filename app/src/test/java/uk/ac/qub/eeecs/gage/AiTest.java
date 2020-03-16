package uk.ac.qub.eeecs.gage;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.SimCards.AI;
import uk.ac.qub.eeecs.game.SimCards.Card;
import uk.ac.qub.eeecs.game.SimCards.SimCardsScreen;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class AiTest {

        @Mock
        private Game game;
        @Mock
        private Input input;
        @Mock
        private AssetManager assetManager;
        @Mock
        private Bitmap bitmap;
        @Mock
        private SimCardsScreen gameScreen;
        @Mock
        private Context context = mock(Context.class);
        @Mock
        private android.content.res.Resources resources;

        List<Card> mCards = new ArrayList<>();

        @Before
        public void setUp() {

            when(game.getAssetManager()).thenReturn(assetManager);
            when(assetManager.getBitmap(any(String.class))).thenReturn(bitmap);
            when(game.getInput()).thenReturn(input);
            when(gameScreen.getGame()).thenReturn(game);
            when(gameScreen.getName()).thenReturn("CardScreen");

            AI.setAIHealth(50);

        }

        @Test
        public void attack_test() {

            Card card = new Card(150.0f, 150.0f, gameScreen);
            Card card2 = new Card(150.0f, 150.0f, gameScreen);
            Card card3 = new Card(150.0f, 150.0f, gameScreen);
            Card card4 = new Card(150.0f, 150.0f, gameScreen);
            Card card5 = new Card(150.0f, 150.0f, gameScreen);
            mCards.add(card);
            mCards.add(card2);
            mCards.add(card3);
            mCards.add(card4);
            mCards.add(card5);

            //Get the card used to attack
            boolean aCardPlayed = false;
            Card cardPlayed = AI.playAttack(mCards, 2, 50);

            //Looking to see if one of the cards were played
            for (int i = 0; i < 5; i++) {
                if (mCards.get(i).equals(cardPlayed)) {
                    aCardPlayed = true;
                }
            }

            //Proof one of the cards used as a parameter were used
            assertTrue(aCardPlayed);


        }

    @Test
    public void defence_test() {

        Card card = new Card(150.0f, 150.0f, gameScreen);
        Card card2 = new Card(150.0f, 150.0f, gameScreen);
        Card card3 = new Card(150.0f, 150.0f, gameScreen);
        Card card4 = new Card(150.0f, 150.0f, gameScreen);
        Card card5 = new Card(150.0f, 150.0f, gameScreen);
        mCards.add(card);
        mCards.add(card2);
        mCards.add(card3);
        mCards.add(card4);
        mCards.add(card5);

        //Get the card used to attack
        boolean aCardPlayed = false;
        Card cardPlayed = AI.playDefence(mCards, 2, 50);

        //Looking to see if one of the cards were played
        for (int i = 0; i < 5; i++) {
            if (mCards.get(i).equals(cardPlayed)) {
                aCardPlayed = true;
            }
        }

        //Proof one of the cards used as a parameter were used
        assertTrue(aCardPlayed);


    }

    @Test
    public void set_health_test() {

            AI.setAIHealth(5);
            assertEquals(5, AI.getAIHealth());
    }

    @Test
    public void manage_AI_health_test() {

            AI.setAIHealth(50);
            AI.manageAIHealth(3);
            assertEquals(47, AI.getAIHealth());
    }

}
