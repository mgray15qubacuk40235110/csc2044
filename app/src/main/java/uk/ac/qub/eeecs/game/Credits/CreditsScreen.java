package uk.ac.qub.eeecs.game.Credits;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.Random;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.GameScreen;

public class CreditsScreen extends GameScreen {

    /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////


     // Define the back button to return to the demo menu
    private PushButton mBackButton;
    private Paint paint = new Paint();

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    public CreditsScreen(Game game) {
        super("CreditsScreen", game);

        // Load the various images used by the cards
        mGame.getAssetManager().loadAssets("txt/assets/CreditsScreenAssets.JSON");
        //AssetManager assetManager = mGame.getAssetManager();

        // Create and position a small back button in the lower-right hand corner
        // of the screen. Also, enable click sounds on press/release interactions.
        mBackButton = new PushButton(
                mDefaultLayerViewport.getWidth() * 0.95f, mDefaultLayerViewport.getHeight() * 0.10f,
                mDefaultLayerViewport.getWidth() * 0.075f, mDefaultLayerViewport.getHeight() * 0.10f,
                "BackArrow", "BackArrowSelected", this);
        mBackButton.setPlaySounds(true, true);
    }

    public void update(ElapsedTime elapsedTime) {

        // Update the back button. If triggered then return to the demo menu.
        mBackButton.update(elapsedTime);
        if (mBackButton.isPushTriggered())
            mGame.getScreenManager().removeScreen(this);

    }

    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

        graphics2D.clear(Color.BLACK);
        mBackButton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        paint.setColor(Color.WHITE);
        paint.setTextSize(getGame().getScreenHeight() / 10);
        paint.setTextAlign(Paint.Align.CENTER);

        graphics2D.drawText("CREDITS", 940.0f, 200.0f, paint);

    }
}
