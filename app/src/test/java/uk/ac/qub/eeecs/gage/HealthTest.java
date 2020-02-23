package uk.ac.qub.eeecs.gage;

import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.SimCards.SimCardsScreen;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class HealthTest {

    @Mock
    private Game game;
    @Mock
    private Input input;
    @Mock
    private AssetManager assetManager = mock(AssetManager.class);
    @Mock
    private Bitmap bitmap = mock(Bitmap.class);
    @Mock
    private GameScreen simCardsScreen = mock(SimCardsScreen.class);

    int userHealth = 50;
    int AIHealth = 50;

    @Before
    public void setUp() {
        when(game.getAssetManager()).thenReturn(assetManager);
        when(assetManager.getBitmap(any(String.class))).thenReturn(bitmap);
        when(game.getInput()).thenReturn(input);
    }

    @Test
    public void user_health_test_invalid() {
        manageUserHealth(-2);
        assertEquals(50, userHealth);
    }

    @Test
    public void user_health_test_valid() {
        manageUserHealth(3);
        assertEquals(47, userHealth);
    }

    @Test
    public void user_health_test_0_health() {
        manageUserHealth(60);
        assertEquals(0, userHealth);
    }


    @Test
    public void ai_health_test_invalid() {
        manageAIHealth(-5);
        assertEquals(50, AIHealth);
    }

    @Test
    public void ai_health_test_valid() {
        manageAIHealth(5);
        assertEquals(45, AIHealth);
    }

    @Test
    public void ai_health_test_0_health() {
        manageAIHealth(80);
        assertEquals(0, AIHealth);
    }

    private void manageUserHealth(int damage) {

        if (damage > 0) {
            if (userHealth <= damage) {
                userHealth = 0;
            } else {
                userHealth -= damage;
            }
        }

    }

    private void manageAIHealth(int damage) {

        if (damage > 0) {
            if (AIHealth <= damage) {
                AIHealth = 0;
            } else {
                AIHealth -= damage;
            }
        }

    }
}
