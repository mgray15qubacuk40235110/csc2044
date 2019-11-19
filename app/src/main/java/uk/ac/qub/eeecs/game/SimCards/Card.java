package uk.ac.qub.eeecs.game.SimCards;

import android.content.res.Resources;
import android.graphics.Bitmap;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.util.BoundingBox;
import uk.ac.qub.eeecs.gage.util.GraphicsHelper;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.gage.world.Sprite;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import java.util.List;

/**
 * Card class that can be drawn using a number of overlapping images.
 *
 * Note: See the course documentation for extension/refactoring stories
 * for this class.
 *
 * @version 1.0
 */
public class Card extends Sprite {

    // /////////////////////////////////////////////////////////////////////////
    // Properties:
    // /////////////////////////////////////////////////////////////////////////

    // Define the default card width and height
    private static final int DEFAULT_CARD_HEIGHT = Resources.getSystem().getDisplayMetrics().heightPixels / 4;
    private static final int DEFAULT_CARD_WIDTH = (int) (0.69230769 * DEFAULT_CARD_HEIGHT);

    // Define the common card base
    private Bitmap mCardBase;

    // Define the card portrait image
    private Bitmap mCardPortrait;

    //Define the back of card image
    private Bitmap mCardBack;

    // Define the card digit images
    private Bitmap[] mCardDigits = new Bitmap[10];

    // Define the offset locations and scaling for the card portrait
    // card attack and card health values - all measured relative
    // to the centre of the object as a percentage of object size

    private Vector2 mAttackOffset = new Vector2(-0.68f, -0.84f);
    private Vector2 mAttackScale = new Vector2(0.1f, 0.1f);

    private Vector2 mHealthOffset = new Vector2(0.72f, -0.84f);
    private Vector2 mHealthScale = new Vector2(0.1f, 0.1f);

    private Vector2 mPortraitOffset = new Vector2(0.0f, 0.3f);
    private Vector2 mPortraitScale = new Vector2(0.55f, 0.55f);

    // Define the health and attack values
    private int mAttack;
    private int mHealth;

    //Record where card is first defined
    private float spawnX;
    private float spawnY;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create a new platform.
     *
     * @param x          Centre y location of the platform
     * @param y          Centre x location of the platform
     * @param gameScreen Gamescreen to which this platform belongs
     */
    public Card(float x, float y, GameScreen gameScreen) {
        super(x, y, DEFAULT_CARD_WIDTH, DEFAULT_CARD_HEIGHT, null, gameScreen);

        AssetManager assetManager = gameScreen.getGame().getAssetManager();

        // Store the common card base image
        mCardBase = assetManager.getBitmap("CardBackground");

        // Store the card portrait image
        mCardPortrait = assetManager.getBitmap("CardPortrait");

        //Store back of card image
        mCardBack = assetManager.getBitmap("backOfCard");

        // Store each of the damage/health digits
        for(int digit = 0; digit <= 9; digit++)
            mCardDigits[digit] = assetManager.getBitmap(String.valueOf(digit));

        // Set default attack and health values
        mAttack = 1;
        mHealth = 2;

        spawnX = x;
        spawnY = y;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    public float getSpawnX() {return spawnX;}
    public float getSpawnY() {return spawnY;}

    /**
     * Draw the game platform
     *
     * @param elapsedTime    Elapsed time information
     * @param graphics2D     Graphics instance
     * @param layerViewport  Game layer viewport
     * @param screenViewport Screen viewport
     */

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D,
                     LayerViewport layerViewport, ScreenViewport screenViewport) {

        // Draw the portrait
        drawBitmap(mCardPortrait, mPortraitOffset, mPortraitScale,
                graphics2D, layerViewport, screenViewport);

        // Draw the card base background
        mBitmap = mCardBase;
        super.draw(elapsedTime, graphics2D, layerViewport, screenViewport);

        // Draw the attack value
        drawBitmap(mCardDigits[mAttack], mAttackOffset, mAttackScale,
                graphics2D, layerViewport, screenViewport);

        // Draw the attack value
        drawBitmap(mCardDigits[mHealth], mHealthOffset, mHealthScale,
                graphics2D, layerViewport, screenViewport);
    }

    public void backDraw(ElapsedTime elapsedTime, IGraphics2D graphics2D,
                         LayerViewport layerViewport, ScreenViewport screenViewport) {

        mBitmap = mCardBack;
        super.draw(elapsedTime, graphics2D, layerViewport, screenViewport);
    }

    public float getLeft() {return (position.x - (DEFAULT_CARD_WIDTH / 2));}
    public float getBottom() {return (position.y - (DEFAULT_CARD_HEIGHT / 2));}


    private BoundingBox bound = new BoundingBox();

    /**
     * Method to draw out a specified bitmap using a specific offset (relative to the
     * position of this game object) and scaling (relative to the size of this game
     * object).
     *
     * @param bitmap Bitmap to draw
     * @param offset Offset vector
     * @param scale Scaling vector
     * @param graphics2D     Graphics instance
     * @param layerViewport  Game layer viewport
     * @param screenViewport Screen viewport
     */
    private void drawBitmap(Bitmap bitmap, Vector2 offset, Vector2 scale,
        IGraphics2D graphics2D, LayerViewport layerViewport, ScreenViewport screenViewport) {

//        // Calculate a game layer bound for the bitmap to be drawn
//        bound.set(position.x + mBound.halfWidth * offset.x,
//                position.y + mBound.halfHeight * offset.y,
//                mBound.halfWidth * scale.x,
//                mBound.halfHeight * scale.y);

        // Calculate the center position of the rotated offset point.
        double rotation = Math.toRadians(-this.orientation);
        float diffX = mBound.halfWidth * offset.x;
        float diffY = mBound.halfHeight * offset.y;
        float rotatedX = (float)(Math.cos(rotation) * diffX - Math.sin(rotation) * diffY + position.x);
        float rotatedY = (float)(Math.sin(rotation) * diffX + Math.cos(rotation) * diffY + position.y);

        // Calculate a game layer bound for the bitmap to be drawn
        bound.set(rotatedX, rotatedY,
                mBound.halfWidth * scale.x, mBound.halfHeight * scale.y);

        // Draw out the specified bitmap using the calculated bound.
        // The following code is based on the Sprite's draw method.
        if (GraphicsHelper.getSourceAndScreenRect(
                bound, bitmap, layerViewport, screenViewport, drawSourceRect, drawScreenRect)) {

            // Build an appropriate transformation matrix
            drawMatrix.reset();

            float scaleX = (float) drawScreenRect.width() / (float) drawSourceRect.width();
            float scaleY = (float) drawScreenRect.height() / (float) drawSourceRect.height();
            drawMatrix.postScale(scaleX, scaleY);

            drawMatrix.postRotate(orientation, scaleX * bitmap.getWidth()
                    / 2.0f, scaleY * bitmap.getHeight() / 2.0f);

            drawMatrix.postTranslate(drawScreenRect.left, drawScreenRect.top);

            // Draw the bitmap
            graphics2D.drawBitmap(bitmap, drawMatrix, null);
        }
    }

    /**
    private void flipCard(Card card){
        frontFacing = !frontFacing;
        if (frontFacing) {
            card.draw();
        }
        else {
            card.draw();
        }

    }
    **/

    public boolean isTapped(List<TouchEvent> touchEvents) {
        for (TouchEvent t : touchEvents) {
            if (t.type == TouchEvent.TOUCH_SINGLE_TAP && bound.contains(t.x, t.y)) {
                return true;
            }
        }
        return false;
    }

}
