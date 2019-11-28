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
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.game.SimCards.Card;
import uk.ac.qub.eeecs.game.SimCards.SimCardsScreen;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SimCards_TouchInputTest {

    @Mock
    private Game game;
    @Mock
    private Input input;
    @Mock
    private AssetManager assetManager;
    @Mock
    private Bitmap bitmap;
    @Mock
    private SimCardsScreen simCardsScreen;
    @Mock
    private Log log;
    @Mock
    private Card card1;
    private Card card2;

    @Before
    public void setUp() {
        when(game.getAssetManager()).thenReturn(assetManager);
        when(assetManager.getBitmap(any(String.class))).thenReturn(bitmap);
        when(game.getInput()).thenReturn(input);
        when(simCardsScreen.getGame()).thenReturn(game);
        when(simCardsScreen.getName()).thenReturn("CardScreen");
    }

    @Test
    public void touchInputTest() {
        Card[] cards = new Card[5];
        List<Card> mCards;
        int cardOffset = 20;

        mCards = new ArrayList<>();
        card1 = new Card(110, 200, 50, 140, simCardsScreen);
        card2 = new Card (200, 200, 50, 140, simCardsScreen);
        mCards.add(card1);
        mCards.add(card2);

        for (int i = 0; i < mCards.size() - 1; i++) {
            assertTrue((mCards.get(i).getLeft() + mCards.get(i).getWidth()) < mCards.get(i + 1).getLeft());
        }

    }

}
