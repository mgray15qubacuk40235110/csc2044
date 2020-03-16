package uk.ac.qub.eeecs.game.SimCards;

import android.content.res.Resources;
import android.graphics.Bitmap;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.util.BoundingBox;
import uk.ac.qub.eeecs.gage.util.GraphicsHelper;
import uk.ac.qub.eeecs.gage.util.MathsHelper;
import uk.ac.qub.eeecs.gage.util.SteeringBehaviours;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.gage.world.Sprite;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.game.spaceDemo.SpaceshipDemoScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private static final int DEFAULT_CARD_HEIGHT = (int) (Resources.getSystem().getDisplayMetrics().heightPixels / 4);
    private static final int DEFAULT_CARD_WIDTH = (int) (0.69230769 * DEFAULT_CARD_HEIGHT);

    private int card_height;
    private int card_width;

    // Define the common card base
    private Bitmap mCardBase;

    // Define the card portrait image
    private static ArrayList<Bitmap> mCardPortrait = new ArrayList<Bitmap>();

    // Value used to store random number used to pick a card from the array and remove it.
    private int cardPos;
    //Bound of array
    private static int arrayBound = 20;
    //Manages which cards are in use
    private static boolean[] inUse = new boolean[arrayBound];
    private boolean portraitValid = false;

    //Define the back of card image
    private Bitmap mCardBack;

    //Random portrait storage
    private Bitmap portraitChosen;

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
    private int mDefence;

    //Record where card is first defined
    private float spawnX;
    private float spawnY;

    //Where the card must go
    private Vector2 mDealPosition;

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

        maxAcceleration = 200.0f;
        maxVelocity = 700.0f;
        maxAngularVelocity = 200.0f;
        maxAngularAcceleration = 500.0f;

        setUpImages(gameScreen);

        spawnX = x;
        spawnY = y;
    }

    public Card(float x, float y, int width, int height, GameScreen gameScreen) {
        super(x, y, width, height, null, gameScreen);

        maxAcceleration = 200.0f;
        maxVelocity = 700.0f;
        maxAngularVelocity = 200.0f;
        maxAngularAcceleration = 500.0f;

        setUpImages(gameScreen);

        spawnX = x;
        spawnY = y;
    }


    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

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
        drawBitmap(portraitChosen, mPortraitOffset, mPortraitScale,
                    graphics2D, layerViewport, screenViewport);


        // Draw the card base background
        mBitmap = mCardBase;
        super.draw(elapsedTime, graphics2D, layerViewport, screenViewport);

        // Draw the attack value
        drawBitmap(mCardDigits[mAttack], mAttackOffset, mAttackScale,
                graphics2D, layerViewport, screenViewport);

        // Draw the attack value
        drawBitmap(mCardDigits[mDefence], mHealthOffset, mHealthScale,
                graphics2D, layerViewport, screenViewport);
    }


    //Used to draw the back of the card
    //Created by Michael Gray
    public void backDraw(ElapsedTime elapsedTime, IGraphics2D graphics2D,
                         LayerViewport layerViewport, ScreenViewport screenViewport) {

        mBitmap = mCardBack;
        super.draw(elapsedTime, graphics2D, layerViewport, screenViewport);
    }


    /**
     * This method is used for dealing cards
     *
     * @param elapsedTime    Elapsed time information
     * @param cardPosition   Used for seeking method
     */

    //Created by Jordan McDonald
    public void deal(ElapsedTime elapsedTime, int cardPosition) {

        Vector2 targposition;

        //Determine deal position
        if (cardPosition < 5) {
            targposition = new Vector2(((SimCardsScreen) mGameScreen).getDefaultScreenViewport().left + 90 + 20 + ((20 + DEFAULT_CARD_WIDTH) * (cardPosition)), ((SimCardsScreen) mGameScreen).getDefaultScreenViewport().top + 140 );
        } else if (cardPosition < 10) {
            targposition = new Vector2(((SimCardsScreen) mGameScreen).getDefaultScreenViewport().right - 90 - 20 - ((20 + DEFAULT_CARD_WIDTH) * (cardPosition - 5)), ((SimCardsScreen) mGameScreen).getDefaultScreenViewport().bottom - 140 );
        } else {
            targposition = new Vector2(Vector2.Zero);
        }

        // Seek towards the deal position
        if ((Math.abs(this.position.x - targposition.x) >= 20) || (Math.abs(this.position.y - targposition.y) >= 20)) {
            SteeringBehaviours.seek(this, targposition, acceleration);
        } else {
            this.velocity.set(Vector2.Zero);
            this.acceleration.set(Vector2.Zero);
            this.position.set(targposition);
        }

        // Call the sprite's superclass to apply the determined accelerations
        super.update(elapsedTime);
    }



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

    //Created by Jordan McDonald & Michael Gray
    private void setUpImages(GameScreen gameScreen) {

        Random rand = new Random();
        AssetManager assetManager = gameScreen.getGame().getAssetManager();
        assetManager.loadAndAddBitmap("CardBackground2", "img/CardBackground2.png");

        // Store the common card base image
        mCardBase = assetManager.getBitmap("CardBackground2");

        //Get card portrait
        do {

            cardPos = rand.nextInt(arrayBound);

            if (!inUse[cardPos]) {
                portraitValid = true;
                inUse[cardPos] = true;
            }

            switch(cardPos) {
                case 0 :
                    portraitChosen = assetManager.getBitmap("BlackBerryCurve");
                    mAttack = 4;
                    mDefence = 4;
                    break;
                case 1 :
                    portraitChosen = assetManager.getBitmap("BlackBerryQ10");
                    mAttack = 5;
                    mDefence = 4;
                    break;
                case 2 :
                    portraitChosen = assetManager.getBitmap("GoogleNexus");
                    mAttack = 7;
                    mDefence = 4;
                    break;
                case 3 :
                    portraitChosen = assetManager.getBitmap("HuaweiP20Lite");
                    mAttack = 7;
                    mDefence = 5;
                    break;
                case 4 :
                    portraitChosen = assetManager.getBitmap("HuaweiY9Prime");
                    mAttack = 8;
                    mDefence = 5;
                    break;
                case 5 :
                    portraitChosen = assetManager.getBitmap("iphone4");
                    mAttack = 7;
                    mDefence = 2;
                    break;
                case 6 :
                    portraitChosen = assetManager.getBitmap("iphone5c");
                    mAttack = 7;
                    mDefence = 3;
                    break;
                case 7 :
                    portraitChosen = assetManager.getBitmap("iphone11pro");
                    mAttack = 9;
                    mDefence = 4;
                    break;
                case 8 :
                    portraitChosen = assetManager.getBitmap("iphoneXR");
                    mAttack = 8;
                    mDefence = 6;
                    break;
                case 9 :
                    portraitChosen = assetManager.getBitmap("JCB");
                    mAttack = 1;
                    mDefence = 9;
                    break;
                case 10 :
                    portraitChosen = assetManager.getBitmap("LenovoK6");
                    mAttack = 6;
                    mDefence = 4;
                    break;
                case 11 :
                    portraitChosen = assetManager.getBitmap("LenovoK8Plus");
                    mAttack = 7;
                    mDefence = 4;
                    break;
                case 12 :
                    portraitChosen = assetManager.getBitmap("MotorollaRAZR");
                    mAttack = 4;
                    mDefence = 3;
                    break;
                case 13 :
                    portraitChosen = assetManager.getBitmap("Nokia3310");
                    mAttack = 1;
                    mDefence = 9;
                    break;
                case 14 :
                    portraitChosen = assetManager.getBitmap("NokiaFlip");
                    mAttack = 2;
                    mDefence = 7;
                    break;
                case 15 :
                    portraitChosen = assetManager.getBitmap("SamsungGalaxyNote8");
                    mAttack = 8;
                    mDefence = 4;
                    break;
                case 16 :
                    portraitChosen = assetManager.getBitmap("SamsungGalaxyS7");
                    mAttack = 7;
                    mDefence = 3;
                    break;
                case 17 :
                    portraitChosen = assetManager.getBitmap("SamsungGalaxyS8");
                    mAttack = 8;
                    mDefence = 2;
                    break;
                case 18 :
                    portraitChosen = assetManager.getBitmap("SonyEricsson");
                    mAttack = 3;
                    mDefence = 8;
                    break;
                case 19 :
                    portraitChosen = assetManager.getBitmap("SonyXperiaPlay");
                    mAttack = 8;
                    mDefence = 6;
                    break;
            }

        } while (!portraitValid);

        //Store back of card image
        mCardBack = assetManager.getBitmap("backOfCard");

        // Store each of the damage/health digits
        for(int digit = 0; digit <= 9; digit++)
            mCardDigits[digit] = assetManager.getBitmap(String.valueOf(digit));

    }

    //Created by Jordan McDonald
    public static void resetCards() {
        for (int i = 0; i < arrayBound; i ++) {
            inUse[i] = false;
        }
    }

    public int getmAttack() {return mAttack;}

    public int getmDefence() {return mDefence;}

    public float getSpawnX() {return spawnX;}

    public float getSpawnY() {return spawnY;}

    public float getLeft() {return (position.x - (DEFAULT_CARD_WIDTH / 2));}

    public float getRight() {return (position.x + (DEFAULT_CARD_WIDTH / 2));}

    public float getBottom() {return (position.y - (DEFAULT_CARD_HEIGHT / 2));}

    public static float getDefaultCardWidth() {return DEFAULT_CARD_WIDTH;}


}



