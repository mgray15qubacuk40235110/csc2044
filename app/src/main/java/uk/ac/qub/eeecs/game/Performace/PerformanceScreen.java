package uk.ac.qub.eeecs.game.Performace;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.ui.PushButton;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.Log;
import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.ui.PushButton;

import java.io.IOException;
import java.util.Random;

public class PerformanceScreen extends GameScreen {

    /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////


     // Define the back button to return to the demo menu
    private PushButton mBackButton;
    private int rectSide = 60;
    private int rectTopBottom = 20;
    private Random random;
    private Bitmap image;
    private Rect rectangle;
    private PushButton mupFrames;
    private PushButton mdownFrames;
    private int numberOfRectangles = 100;
    private int numCalls;
    private Paint paint = new Paint();

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    public PerformanceScreen(Game game) {
        super("PerformanceScreen", game);

        // Load the various images used by the cards
        mGame.getAssetManager().loadAssets("txt/assets/PerformanceScreenAssets.JSON");
        AssetManager assetManager = mGame.getAssetManager();
        assetManager.loadAndAddBitmap("plusSignIcon", "img/plusSignIcon.PNG");
        assetManager.loadAndAddBitmap("minusSignIcon", "img/minusSignIcon.PNG");

        // Create and position a small back button in the lower-right hand corner
        // of the screen. Also, enable click sounds on press/release interactions.
        mBackButton = new PushButton(
                mDefaultLayerViewport.getWidth() * 0.95f, mDefaultLayerViewport.getHeight() * 0.10f,
                mDefaultLayerViewport.getWidth() * 0.075f, mDefaultLayerViewport.getHeight() * 0.10f,
                "BackArrow", "BackArrowSelected", this);
        mBackButton.setPlaySounds(true, true);
        mupFrames = new PushButton(
                mDefaultLayerViewport.getWidth() * 0.15f, mDefaultLayerViewport.getHeight() * 0.10f,
                mDefaultLayerViewport.getWidth() * 0.075f, mDefaultLayerViewport.getHeight() * 0.10f,
                "plusSignIcon", "plusSignIcon", this);
        mdownFrames = new PushButton(
                mDefaultLayerViewport.getWidth() * 0.25f, mDefaultLayerViewport.getHeight() * 0.10f,
                mDefaultLayerViewport.getWidth() * 0.075f, mDefaultLayerViewport.getHeight() * 0.10f,
                "minusSignIcon", "minusSignIcon", this);

    }

    public void update(ElapsedTime elapsedTime) {

        // Update the back button. If triggered then return to the demo menu.
        mBackButton.update(elapsedTime);
        if (mBackButton.isPushTriggered())
            mGame.getScreenManager().removeScreen(this);


        mupFrames.update(elapsedTime);
        mdownFrames.update(elapsedTime);

        if (mupFrames.isPushTriggered())
        {
            numberOfRectangles+=500;
        }
        else if (mdownFrames.isPushTriggered())
        {
            if(numberOfRectangles > 500)
            {
                numberOfRectangles -= 500;
            }
        }
    }

    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

        mBackButton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        paint.setColor(Color.BLACK);
        paint.setTextSize(getGame().getScreenHeight() / 10);
        paint.setTextAlign(Paint.Align.LEFT);
        numCalls++;

        graphics2D.drawText("Num = " + numCalls, 50.0f, 50.0f, paint);
        graphics2D.drawText("Num of rectangles = " + numberOfRectangles, 50.0f, 150.0f, paint);

        mupFrames.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        mdownFrames.draw(elapsedTime, graphics2D , mDefaultLayerViewport, mDefaultScreenViewport);

        for (int i=0; i < numberOfRectangles; i++)
        {

        }
    }
}
