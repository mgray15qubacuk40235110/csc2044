package uk.ac.qub.eeecs.game.Performace;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.view.Display;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.R;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.util.BoundingBox;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.SimCards.Card;

public class PerformanceScreen extends GameScreen {

    /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////


     // Define the back button to return to the demo menu
    private PushButton mBackButton;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    public PerformanceScreen(Game game) {
        super("PerformanceScreen", game);

        // Load the various images used by the cards
        mGame.getAssetManager().loadAssets("txt/assets/PerformanceScreenAssets.JSON");

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

        mBackButton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

    }

}
