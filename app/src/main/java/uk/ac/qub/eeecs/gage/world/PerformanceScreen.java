package uk.ac.qub.eeecs.gage.world;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetManager;

public abstract class PerformanceScreen extends GameScreen implements Runnable {

    //constructor
    public PerformanceScreen(Game game) {
        super("Performance Screen", game);
        AssetManager assetManager = mGame.getAssetManager();
    }

    @Override
    public void run() {

    }
}
