package uk.ac.qub.eeecs.game.spaceDemo;

import java.util.Random;

import uk.ac.qub.eeecs.gage.util.SteeringBehaviours;
import uk.ac.qub.eeecs.gage.world.GameScreen;

/**
 * Simple asteroid
 *
 * Note: See the course documentation for extension/refactoring stories
 * for this class.
 *
 * @version 1.0
 */
public class Blackhole extends SpaceEntity {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Default size for the asteroid
     */

    private static float DEFAULT_RADIUS;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create an asteroid
     *
     * @param startX     x location of the asteroid
     * @param startY     y location of the asteroid
     * @param gameScreen Gamescreen to which asteroid belongs
     */
    public Blackhole(float startX, float startY, GameScreen gameScreen) {
        super(startX, startY, DEFAULT_RADIUS * 2.0f, DEFAULT_RADIUS * 2.0f , null, gameScreen);

        Random random = new Random();

        // Randomise blackhole radius

        Random random2 = new Random();

        DEFAULT_RADIUS = random2.nextFloat() * 100.0f;

        mBitmap = gameScreen.getGame().getAssetManager()
                .getBitmap(random.nextBoolean() ? "Blackhole" : "Blackhole2");

        mRadius = 0;
        mMass = 10000.0f;

        angularVelocity = random.nextFloat() * 500.0f - 20.0f;

    }

}
