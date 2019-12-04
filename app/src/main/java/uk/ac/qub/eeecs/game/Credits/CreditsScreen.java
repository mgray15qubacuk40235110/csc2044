package uk.ac.qub.eeecs.game.Credits;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.Random;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.engine.audio.AudioManager;

public class CreditsScreen extends GameScreen {

    /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////


     // Define the back button to return to the demo menu
    private PushButton mBackButton;
    private Paint paint = new Paint();

    //music
    AudioManager audioManager = getGame().getAudioManager();

    //Scrolling feature
    private float yScroll = 0.0f;

    //For photo
    private GameObject mSquad;
    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    public CreditsScreen(Game game) {
        super("CreditsScreen", game);

        // Load the various images used by the screen
        mGame.getAssetManager().loadAssets("txt/assets/CreditsScreenAssets.JSON");

        //Load final image
        mSquad = new GameObject(mDefaultLayerViewport.halfWidth,
                mDefaultLayerViewport.halfHeight, mDefaultLayerViewport.halfWidth * 2, mDefaultLayerViewport.halfHeight * 2, getGame()
                .getAssetManager().getBitmap("Squad"), this);

        // Create and position a small back button in the lower-right hand corner
        // of the screen. Also, enable click sounds on press/release interactions.
        mBackButton = new PushButton(
                mDefaultLayerViewport.getWidth() * 0.95f, mDefaultLayerViewport.getHeight() * 0.10f,
                mDefaultLayerViewport.getWidth() * 0.075f, mDefaultLayerViewport.getHeight() * 0.10f,
                "BackArrow", "BackArrowSelected", this);
        mBackButton.setPlaySounds(true, true);
    }

    public void update(ElapsedTime elapsedTime) {

        playBackgroundMusic();

        // Update the back button. If triggered then return to the demo menu.
        mBackButton.update(elapsedTime);
        if (mBackButton.isPushTriggered()) {
            audioManager.stopMusic();
            mGame.getScreenManager().removeScreen(this);
        }

        //Scrolling text
        yScroll += 4.0f;
    }

    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

        graphics2D.clear(Color.BLACK);
        mBackButton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        paint.setColor(Color.WHITE);
        paint.setTextSize(getGame().getScreenHeight() / 10);
        paint.setTextAlign(Paint.Align.CENTER);

        graphics2D.drawText("SIM CARDS", 930.0f, 900.0f - yScroll, paint);

        graphics2D.drawText("STARRING:", 930.0f, 1200.0f - yScroll, paint);
        paint.setTextSize(getGame().getScreenHeight() / 15);
        graphics2D.drawText("Some cards & phones", 930.0f, 1300.0f - yScroll, paint);

        paint.setTextSize(getGame().getScreenHeight() / 10);
        graphics2D.drawText("CREATED BY:", 930.0f, 1600.0f - yScroll, paint);
        paint.setTextSize(getGame().getScreenHeight() / 15);
        graphics2D.drawText("Jordan 'Jort' McDonald", 930.0f, 1700.0f - yScroll, paint);
        graphics2D.drawText("Micklaus 'Mickolas' 'Meek' 'Mick' Gray", 930.0f, 1800.0f - yScroll, paint);
        graphics2D.drawText("Rian 'Hey that's my user story' Murphy", 930.0f, 1900.0f - yScroll, paint);
        graphics2D.drawText("Jamie 'Workhorse' 'I gel my hair back' Finnegan", 930.0f, 2000.0f - yScroll, paint);
        graphics2D.drawText("Klara 'Would have a nickname if you thought of one' Daly", 930.0f, 2100.0f - yScroll, paint);

        paint.setTextSize(getGame().getScreenHeight() / 10);
        graphics2D.drawText("SPECIAL THANKS:", 930.0f, 2400.0f - yScroll, paint);
        paint.setTextSize(getGame().getScreenHeight() / 15);
        graphics2D.drawText("Chris 'A little bit of the bubbly' Jericho", 930.0f, 2500.0f - yScroll, paint);
        graphics2D.drawText("Dara Conlon", 930.0f, 2600.0f - yScroll, paint);
        graphics2D.drawText("Phil Hanna", 930.0f, 2700.0f - yScroll, paint);
        graphics2D.drawText("Baby Yoda", 930.0f, 2800.0f - yScroll, paint);
        graphics2D.drawText("My mom", 930.0f, 2900.0f - yScroll, paint);

        paint.setTextSize(getGame().getScreenHeight() / 10);
        graphics2D.drawText("NO ANIMALS WERE HARMED IN THE", 930.0f, 3200.0f - yScroll, paint);
        graphics2D.drawText("MAKING OF THIS GAME", 930.0f, 3290.0f - yScroll, paint);

        if (3290.0f - yScroll < -30.0f) {
            mSquad.draw(elapsedTime, graphics2D, mDefaultLayerViewport,
                    mDefaultScreenViewport);
            mBackButton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        }
    }

    private void playBackgroundMusic() {

        if(!audioManager.isMusicPlaying())
            audioManager.playMusic(
                    getGame().getAssetManager().getMusic("CreditsTheme"));
    }
}
