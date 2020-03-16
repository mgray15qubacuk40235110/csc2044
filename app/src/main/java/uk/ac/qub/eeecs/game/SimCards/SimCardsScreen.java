package uk.ac.qub.eeecs.game.SimCards;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.audio.AudioManager;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.util.BoundingBox;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.SimCards.AI;

public class SimCardsScreen extends GameScreen {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    //Define the gamescreen of phones
    private GameObject mCardBackground;
    private GameObject mPauseMenu;
    private GameObject attackCardSlot;
    private GameObject attackBanner;
    private GameObject defendCardSlot;
    private GameObject defendBanner;
    private GameObject versusSymbol;
    private GameObject yourTurn;
    private GameObject aiTurn;

    //Define health & health bars
    private GameObject userHealthBar;
    private GameObject aiHealthBar;
    private int userHealth = 50;
    public static final int MAX_HEALTH = 50;

    // Define cards & their properties
    private Card currentCard;
    private Card currentAICard;
    private List<Card> mCards;
    private List<Card> mAICards;
    private List<Card> mDeckCards;
    private int cardOffset;
    private boolean[] flippingBack = new boolean[5];
    private boolean[] rearFacing = new boolean[5];
    private boolean[] flipCard = new boolean[5];

    //Manages game state
    private boolean cardInPlay = false;
    private boolean attackCardInPlay = false;
    private boolean defenceCardInPLay = false;
    private int intCardInPlay = -1;

    private boolean aiCardInPlay = false;
    private int AIintCardInPlay = -1;

    boolean endTurnSelected;
    private boolean cardsDealt = false;
    private boolean userTurn = true;
    private int cardCounter = 0;

    //Buttons
    private PushButton endTurnButton;
    private PushButton pause;
    private List<PushButton> mControls;
    private boolean gamePaused = false;
    private PushButton pausedContinue;
    private PushButton pausedQuit;

    //Touch Input
    private int unpauseCounter = 0;
    private boolean mTouchIdExists;
    private float[] mTouchLocation = new float[2];
    private boolean[] dragging = new boolean[5];
    private TouchEvent lastTouchEvent;
    private int lastTouchEventType;

    //Useful data
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    private AssetManager assetManager = mGame.getAssetManager();

    // Audio Manager
    private AudioManager audioManager = getGame().getAudioManager();
    private boolean soundPlayed = false;

    //Enabling text output
    private Paint textPaint = new Paint();

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create the Card game screen
     *
     * @param game Game to which this screen belongs
     */

    public SimCardsScreen(Game game) {
        super("CardScreen", game);

        //Set viewports
        mDefaultLayerViewport.set(getScreenWidth() / 2, screenHeight / 2, getScreenWidth() / 2, screenHeight / 2);
        mDefaultScreenViewport.set(0, 0, (int) mDefaultLayerViewport.halfWidth * 2, (int) mDefaultLayerViewport.halfHeight * 2);

        // Load the various images used by the cards
        mGame.getAssetManager().loadAssets("txt/assets/CardDemoScreenAssets.JSON");
        mGame.getAssetManager().loadAndAddBitmap("Background", "img/SimCardsMenuBackground.png");

        //Set defaults
        Card.resetCards();
        AI.setAIHealth(50);

        //Set up game screen
        setUpObjects();
        setUpCards();
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////


    /**
     * Update the card demo screen
     *
     * @param elapsedTime Elapsed time information
     */
    @Override
    public void update(ElapsedTime elapsedTime) {

        // Process any touch events occurring since the last update
        Input input = mGame.getInput();

        //Checking all push buttons for an update(click)
        for (PushButton control : mControls) {
            control.update(elapsedTime, mDefaultLayerViewport, mDefaultScreenViewport);
        }

        if (endTurnButton.isPushTriggered()) {
            endTurnSelected = true;
            endTurn(elapsedTime);
        }

        //Checking if game is paused or unpaused
        managePause(elapsedTime);

        // Get any touch events that have occurred since the last update
        List<TouchEvent> touchEvents = input.getTouchEvents();
        if (touchEvents.size() > 0) {
            lastTouchEvent = touchEvents.get(touchEvents.size() - 1);
            lastTouchEventType = lastTouchEvent.type;
        }

        //Dealing cards if not yet dealt
        if (!cardsDealt && mAICards.size() > 0 && !gamePaused && unpauseCounter == 0) {
            dealInitialCards(elapsedTime);
        }

        //Once cards have been dealt the user can now interact with cards
        //Once the user has placed his card the AI card is decided
        if (cardsDealt && mCards.size() > 0 && !gamePaused && unpauseCounter == 0) {
            checkTouchActions(mCards, touchEvents, input);
            if (cardInPlay && !aiCardInPlay) {
                takeAITurn();
            }
        }

        playBackgroundMusic();

    }

    /**
     * Draw the card demo screen
     *
     * @param elapsedTime Elapsed time information
     * @param graphics2D  Graphics instance
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

        graphics2D.clear(Color.WHITE);
        int screenWidth = graphics2D.getSurfaceWidth();
        int screenHeight = graphics2D.getSurfaceHeight();

        //Draw the background
        mCardBackground.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        //Draw Health Bars
        userHealthBar.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        aiHealthBar.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        drawHealth(graphics2D, userHealth, AI.getAIHealth());

        // Draw the cards
        if (mCards.size() > 0) {
            for (int i = 0; i < mCards.size(); i++) {
                currentCard = mCards.get(i);
                if (!rearFacing[i] && cardsDealt) {
                    currentCard.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
                } else {
                    currentCard.backDraw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
                }
            }
        }

        //Draw the AI Cards
        if (mAICards.size() > 0) {
            for (int i = 0; i < mAICards.size(); i++) {
                if (i == AIintCardInPlay && endTurnSelected) {
                    mAICards.get(i).draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
                } else {
                    mAICards.get(i).backDraw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
                }
            }
        }

        //Draw the Deck
        if (mDeckCards.size() > 0) {
            for (int i = 0; i < 3; i++) {
                mDeckCards.get(i).backDraw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
            }
        }

        // Draw the controls last of all
        if (cardInPlay) {
            endTurnButton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        }
        pause.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);


        //Draw attack/defend
        if (cardsDealt) {
            textPaint.setColor(Color.BLACK);

            attackCardSlot.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
            attackBanner.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
            graphics2D.drawText("A", mDefaultScreenViewport.right / 3.4f, mDefaultScreenViewport.bottom / 1.565f, textPaint);
            graphics2D.drawText("T", mDefaultScreenViewport.right / 3.27f, mDefaultScreenViewport.bottom / 1.573f, textPaint);
            graphics2D.drawText("T", mDefaultScreenViewport.right / 3.14f, mDefaultScreenViewport.bottom / 1.581f, textPaint);
            graphics2D.drawText("A", mDefaultScreenViewport.right / 3.01f, mDefaultScreenViewport.bottom / 1.590f, textPaint);
            graphics2D.drawText("C", mDefaultScreenViewport.right / 2.88f, mDefaultScreenViewport.bottom / 1.595f, textPaint);
            graphics2D.drawText("K", mDefaultScreenViewport.right / 2.75f, mDefaultScreenViewport.bottom / 1.592f, textPaint);

            defendCardSlot.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
            defendBanner.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
            graphics2D.drawText("D", mDefaultScreenViewport.right / 1.60f, mDefaultScreenViewport.bottom / 1.565f, textPaint);
            graphics2D.drawText("E", mDefaultScreenViewport.right / 1.566f, mDefaultScreenViewport.bottom / 1.573f, textPaint);
            graphics2D.drawText("F", mDefaultScreenViewport.right / 1.532f, mDefaultScreenViewport.bottom / 1.581f, textPaint);
            graphics2D.drawText("E", mDefaultScreenViewport.right / 1.498f, mDefaultScreenViewport.bottom / 1.590f, textPaint);
            graphics2D.drawText("N", mDefaultScreenViewport.right / 1.464f, mDefaultScreenViewport.bottom / 1.595f, textPaint);
            graphics2D.drawText("D", mDefaultScreenViewport.right / 1.43f, mDefaultScreenViewport.bottom / 1.592f, textPaint);

            versusSymbol.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

            if (userTurn) {
                yourTurn.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
            } else {
                aiTurn.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
            }
        }

        //If the game is paused draw the pause menu
        if (gamePaused) {
            drawPause(elapsedTime, graphics2D, screenHeight, screenWidth);
        }

    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels + 128;
    }

    //Created by Jordan McDonald
    private void setUpObjects() {

        float layerWidth = mDefaultLayerViewport.halfWidth * 2.0f;

        //Creating buttons
        mControls = new ArrayList<>();
        endTurnButton = new PushButton(layerWidth - 110.0f, mDefaultScreenViewport.bottom /2 - 190, 200.0f, 60.0f,
                "EndTurn", "EndTurnPressed", this);
        endTurnButton.setPlaySounds(true, true);

        pause = new PushButton(70.0f, screenHeight - 60.0f, 90.0f, 90.0f,
                "pauseButton", "pauseButton", this);
        pause.setPlaySounds(true, true);

        mControls.add(endTurnButton);
        mControls.add(pause);

        pausedContinue = new PushButton(mDefaultLayerViewport. halfWidth - 300, mDefaultLayerViewport.halfHeight / 2.85f, 350.0f, 105.0f,
                "PausedContinueButton", "PausedContinueButton", this);
        pausedContinue.setPlaySounds(true, true);

        pausedQuit = new PushButton(mDefaultLayerViewport. halfWidth + 300, mDefaultLayerViewport.halfHeight / 2.85f, 350.0f, 105.0f,
                "PausedQuitButton", "PausedQuitButton", this);
        pausedQuit.setPlaySounds(true, true);

        // Create the card background
        mCardBackground = new GameObject(mDefaultLayerViewport.halfWidth,
                mDefaultLayerViewport.halfHeight, mDefaultLayerViewport.halfWidth * 2, mDefaultLayerViewport.halfHeight * 2, getGame()
                .getAssetManager().getBitmap("Background"), this);

        //Create pause menu
        mPauseMenu = new GameObject(mDefaultLayerViewport.halfWidth,
                mDefaultLayerViewport.halfHeight, mDefaultLayerViewport.halfWidth * 1.5f, mDefaultLayerViewport.halfHeight * 1.5f, getGame()
                .getAssetManager().getBitmap("PauseMenu"), this);

        //Create User Health Bar
        userHealthBar = new GameObject(mDefaultScreenViewport.right - 350, mDefaultScreenViewport.top + 70, 650, 400,
                getGame().getAssetManager().getBitmap("HealthBar2"), this);

        //Create AI Health Bar
        aiHealthBar = new GameObject(mDefaultScreenViewport.left + 420, mDefaultScreenViewport.bottom - 110, 650, 400,
                getGame().getAssetManager().getBitmap("HealthBar2"), this);

        //Attack/Defend Object
        attackCardSlot = new GameObject(mDefaultScreenViewport.width / 3, mDefaultScreenViewport.bottom / 2,
                (float) 0.69230769 * (screenHeight / 4f), screenHeight / 4,
                getGame().getAssetManager().getBitmap("CardOutline"), this);
        attackBanner = new GameObject(mDefaultScreenViewport.width / 2.985f, mDefaultScreenViewport.bottom / 2.45f,
                (float) 0.93230769 * (screenHeight / 4f), screenHeight / 2.7f,
                getGame().getAssetManager().getBitmap("Banner"), this);

        defendCardSlot = new GameObject(mDefaultScreenViewport.width / 1.5f, mDefaultScreenViewport.bottom / 2,
                (float) 0.69230769 * (screenHeight / 4f), screenHeight / 4,
                getGame().getAssetManager().getBitmap("CardOutline"), this);
        defendBanner = new GameObject(mDefaultScreenViewport.width / 1.495f, mDefaultScreenViewport.bottom / 2.45f,
                (float) 0.93230769 * (screenHeight / 4f), screenHeight / 2.7f,
                getGame().getAssetManager().getBitmap("Banner"), this);

        //Versus Symbol
        versusSymbol = new GameObject(mDefaultScreenViewport.width / 2, mDefaultScreenViewport.bottom / 2, screenHeight / 5, screenHeight / 5,
                getGame().getAssetManager().getBitmap("VersusSymbol"), this);

        //Turn Symbols
        yourTurn = new GameObject(mDefaultScreenViewport.width / 2, mDefaultScreenViewport.bottom / 1.5f, screenHeight / 3.7f, screenHeight / 7,
                getGame().getAssetManager().getBitmap("YourTurn"), this);
        aiTurn = new GameObject(mDefaultScreenViewport.width / 2, mDefaultScreenViewport.bottom / 1.5f, screenHeight / 3.7f, screenHeight / 7,
                getGame().getAssetManager().getBitmap("AITurn"), this);

    }

    //Created by Jordan McDonald
    private void setUpCards() {

        //Add deck
        mDeckCards = new ArrayList<>();
        cardOffset = 0;
        for (int i = 0; i < 3; i++) {
            Card card = new Card((mDefaultScreenViewport.right - 110), (mDefaultScreenViewport.bottom / 2.0f + cardOffset), this);
            mDeckCards.add(card);
            cardOffset = cardOffset - 10;
        }

        Card.resetCards();

        // Create cards
        mCards = new ArrayList<>();
        cardOffset = 20;
        for (int i = 0; i < 5; i++) {
            Card card = new Card((mDefaultScreenViewport.left + 90 + cardOffset), (mDefaultScreenViewport.top + 140), this);
            cardOffset = cardOffset + (int) card.getWidth() + 20;
            mCards.add(card);
        }

        // Create AI Cards
        mAICards = new ArrayList<>();
        cardOffset = -20;
        for (int i = 0; i < 5; i++) {
            Card card = new Card((mDefaultScreenViewport.right - 90 + cardOffset), (mDefaultScreenViewport.bottom - 140), this);
            cardOffset = cardOffset - (int) card.getWidth() - 20;
            mAICards.add(card);
        }

        //Moving all cards to deck location for dealing animation
        for (int i = 0; i <5; i++) {
            mCards.get(i).setPosition((mDefaultScreenViewport.right - 110), (mDefaultScreenViewport.bottom / 2.0f - 30));
            mAICards.get(i).setPosition((mDefaultScreenViewport.right - 110), (mDefaultScreenViewport.bottom / 2.0f - 30));
        }

    }

    //Created by Jordan McDonald
    private void checkTouchActions(List<Card> mCards, List<TouchEvent> touchEvents, Input input) {

        //Check each card
        for (int i = 0; i < mCards.size(); i++) {
            currentCard = mCards.get(i);
            mTouchIdExists = input.existsTouch(0);
            //Check if card being dragged
            if (mTouchIdExists && !flipCard[i]) {
                mTouchLocation[0] = input.getTouchX(0);
                mTouchLocation[1] = (mDefaultLayerViewport.halfHeight * 2.0f) - input.getTouchY(0);
                if ((mTouchLocation[0] >= currentCard.getLeft()) & (mTouchLocation[0] <= (currentCard.getLeft() + currentCard.getWidth()))) {
                    if ((mTouchLocation[1] >= currentCard.getBottom()) & (mTouchLocation[1] <= (currentCard.getBottom() + currentCard.getHeight()))) {
                        if (touchEvents.size() > 0) {
                            if (lastTouchEventType == 2 || lastTouchEventType == 6) {
                                dragging[i] = true;
                                for (int i2 = 0; i2 < mCards.size(); i2++) {
                                    if (i2 != i && (dragging[i2] || flipCard[i2])) {
                                        dragging[i] = false;

                                    }
                                }
                            }
                        }
                    }
                }
            //Check if card being tapped
            } else if (!flipCard[i]){
                for (TouchEvent indexTouchEvent : touchEvents) {
                    if (indexTouchEvent.type == 5) {
                        if ((indexTouchEvent.x >= currentCard.getLeft()) & (indexTouchEvent.x <= (currentCard.getLeft() + currentCard.getWidth()))) {
                            if ((((mDefaultLayerViewport.halfHeight * 2.0f) - indexTouchEvent.y) >= currentCard.getBottom()) & (((mDefaultLayerViewport.halfHeight * 2.0f) - indexTouchEvent.y) <= (currentCard.getBottom() + currentCard.getHeight()))) {
                                //If card is in play then reset it
                                if (i == intCardInPlay) {
                                    intCardInPlay = -1;
                                    cardInPlay = false;
                                    attackCardInPlay = false;
                                    defenceCardInPLay = false;
                                    currentCard.position.x = currentCard.getSpawnX();
                                    currentCard.position.y = currentCard.getSpawnY();

                                    //Reset ai card also
                                    aiCardInPlay = false;
                                    mAICards.get(AIintCardInPlay).position.x = mAICards.get(AIintCardInPlay).getSpawnX();
                                    mAICards.get(AIintCardInPlay).position.y = mAICards.get(AIintCardInPlay).getSpawnY();
                                    AIintCardInPlay = -1;

                                //Simply flip any card not in play
                                } else {
                                    flipCard[i] = true;
                                }
                            }
                        }
                    }
                }
            }

            //If card being tapped
            if (flipCard[i]) {
                flipCurrentCard(currentCard, i);
            }

            //If the current card is being dragged and not in a slot follow user's finger
            if (dragging[i] && i != intCardInPlay) {
                currentCard.position.x = mTouchLocation[0];
                currentCard.position.y = mTouchLocation[1];
            }

            //Figuring out what to do when a card being dragged is released
            if (touchEvents.size() > 0 && dragging[i]) {
                lastTouchEvent = touchEvents.get(touchEvents.size() - 1);
                lastTouchEventType = lastTouchEvent.type;
                //If a card is released and no card is in a slot
                if (lastTouchEventType == 1 && !cardInPlay) {
                    //Dragged to attack slot
                    if ((currentCard.position.x >= (attackCardSlot.position.x - attackCardSlot.getWidth() / 2))
                            && (currentCard.position.x <= (attackCardSlot.position.x + attackCardSlot.getWidth() / 2))
                            && (currentCard.position.y >= (attackCardSlot.position.y - attackCardSlot.getHeight() / 2))
                            && (currentCard.position.y <= (attackCardSlot.position.y + attackCardSlot.getHeight() / 2))) {

                        dragging[i] = false;
                        cardInPlay = true;
                        attackCardInPlay = true;
                        intCardInPlay = i;
                        currentCard.position.x = attackCardSlot.position.x;
                        currentCard.position.y = attackCardSlot.position.y;

                    }
                    //Dragged to defend slot
                    else if ((currentCard.position.x >= (defendCardSlot.position.x - defendCardSlot.getWidth() / 2))
                            && (currentCard.position.x <= (defendCardSlot.position.x + defendCardSlot.getWidth() / 2))
                            && (currentCard.position.y >= (defendCardSlot.position.y - defendCardSlot.getHeight() / 2))
                            && (currentCard.position.y <= (defendCardSlot.position.y + defendCardSlot.getHeight() / 2))) {

                        dragging[i] = false;
                        cardInPlay = true;
                        defenceCardInPLay = true;
intCardInPlay = i;
                        currentCard.position.x = defendCardSlot.position.x;
                        currentCard.position.y = defendCardSlot.position.y;
                    }
                    //Not dragged to either slot
                    else {
                        dragging[i] = false;
                        currentCard.position.x = currentCard.getSpawnX();
                        currentCard.position.y = currentCard.getSpawnY();
                    }
                }
                //Card released while a card is already in a slot
                else if (lastTouchEventType == 1 && cardInPlay)  {
                    if (i != intCardInPlay) {
                        dragging[i] = false;
                        currentCard.position.x = currentCard.getSpawnX();
                        currentCard.position.y = currentCard.getSpawnY();
                    }
                }
            }

            //Making sure a card cannot leave the confines of the visible screen
            BoundingBox playerBound = currentCard.getBound();
            if (playerBound.getLeft() < 0)
                currentCard.position.x -= playerBound.getLeft();
            else if (playerBound.getRight() > (mDefaultLayerViewport.halfWidth * 2.0f))
                currentCard.position.x -= (playerBound.getRight() - (mDefaultLayerViewport.halfWidth * 2.0f));

            if (playerBound.getBottom() < 0)
                currentCard.position.y -= playerBound.getBottom();
            else if (playerBound.getTop() > (mDefaultLayerViewport.halfHeight * 2.0f))
                currentCard.position.y -= (playerBound.getTop() - (mDefaultLayerViewport.halfHeight * 2.0f));

        }
    }

    //Created by Jordan McDonald
    private void takeAITurn() {

        if (attackCardInPlay) {
            currentAICard = AI.playDefence(mAICards, mCards.get(intCardInPlay).getmAttack(), userHealth);
            currentAICard.position.x = defendCardSlot.position.x;
            currentAICard.position.y = defendCardSlot.position.y;
            aiCardInPlay = true;
            for (int i = 0; i < mAICards.size(); i++) {
                if (mAICards.get(i).equals(currentAICard)) {
                    AIintCardInPlay = i;
                }
            }
        } else if (defenceCardInPLay) {
            currentAICard = AI.playAttack(mAICards, mCards.get(intCardInPlay).getmDefence(), userHealth);
            currentAICard.position.x = attackCardSlot.position.x;
            currentAICard.position.y = attackCardSlot.position.y;
            aiCardInPlay = true;
            for (int i = 0; i < mAICards.size(); i++) {
                if (mAICards.get(i).equals(currentAICard)) {
                    AIintCardInPlay = i;
                }
            }
        } else {
            currentAICard = AI.takeTurn(mAICards);
        }

    }

    //Created by Jordan McDonald
    private void endTurn(ElapsedTime elapsedTime) {

        int damage;

        if (attackCardInPlay) {

            damage = Math.abs(mCards.get(intCardInPlay).getmAttack() - mAICards.get(AIintCardInPlay).getmDefence());

            if (mCards.get(intCardInPlay).getmAttack() > mAICards.get(AIintCardInPlay).getmDefence()) {
                AI.manageAIHealth(damage);
            } else { manageUserHealth(damage); }

        } else {

            damage = Math.abs(mCards.get(intCardInPlay).getmDefence() - mAICards.get(AIintCardInPlay).getmAttack());

            if (mCards.get(intCardInPlay).getmDefence() > mAICards.get(AIintCardInPlay).getmAttack()) {
                AI.manageAIHealth(damage);
            } else { manageUserHealth(damage); }

        }

        mCards.remove(intCardInPlay);
        mAICards.remove(AIintCardInPlay);

        //Making new Cards
        int cardOffset = 20 + (intCardInPlay * (int) (Card.getDefaultCardWidth() + 20));
        Card userReplacementCard = new Card((mDefaultScreenViewport.left + 90 + cardOffset), (mDefaultScreenViewport.top + 140), this);

        cardOffset = -20 + (AIintCardInPlay * (int) (-Card.getDefaultCardWidth() - 20));
        Card aiReplacementCard = new Card((mDefaultScreenViewport.right - 90 + cardOffset), (mDefaultScreenViewport.bottom - 140), this);

        mCards.add(intCardInPlay, userReplacementCard);
        mAICards.add(AIintCardInPlay, aiReplacementCard);

        //userReplacementCard.setPosition((mDefaultScreenViewport.right - 110), (mDefaultScreenViewport.bottom / 2.0f - 30));
        //aiReplacementCard.setPosition((mDefaultScreenViewport.right - 110), (mDefaultScreenViewport.bottom / 2.0f - 30));
        //userReplacementCard.deal(elapsedTime, intCardInPlay);
        //aiReplacementCard.deal(elapsedTime, AIintCardInPlay + 5);

        //Moving on to next turn
        userTurn = !userTurn;
        endTurnSelected = false;
        intCardInPlay = -1;
        AIintCardInPlay = -1;
        cardInPlay = false;
        attackCardInPlay = false;
        defenceCardInPLay = false;
        aiCardInPlay = false;

        cardCounter++;

        if (cardCounter == 4) {
            Card.resetCards();
            cardCounter = 0;
        }

    }

    //Created by Michael Gray
    public void flipCurrentCard(Card currentCard, int i) {

        soundPlayed = false;
        if (!soundPlayed) {
            soundPlayed = true;
            audioManager.play(assetManager.getSound("CardFlipSound"));
        }

        if (!flippingBack[i]) {
            shrinkCard();
        }

        if (currentCard.getWidth() < 90.0f) {
            rearFacing[i] = !rearFacing[i];
            flippingBack[i] = !flippingBack[i];
        }

        if (flippingBack[i]) {
            growCard();
        }

        if (currentCard.getWidth() == Card.getDefaultCardWidth()) {
            flippingBack[i] = false;
            flipCard[i] = false;
        }

    }

    //Created by Michael Gray
    public void shrinkCard(){

        if (currentCard.getWidth() > Card.getDefaultCardWidth() * 0.2){
            currentCard.setWidth((currentCard.getWidth() / 1500) * 1000);
        }

    }

    //Created by Michael Gray
    public void growCard(){

        if (currentCard.getWidth() < Card.getDefaultCardWidth()) {
            currentCard.setWidth((currentCard.getWidth() / 1000) * 1500);
        }
        if (currentCard.getWidth() > Card.getDefaultCardWidth()) {
            currentCard.setWidth(Card.getDefaultCardWidth());
        }

    }

    //Created by Jordan McDonald
    private void dealInitialCards(ElapsedTime elapsedTime) {

            //Deal the users cards
            for (int i = 0; i < 5; i++) {
                mCards.get(i).deal(elapsedTime, i);
            }

            //Deal the AI cards once the users cards are dealt
            if (mCards.get(0).position.x == mCards.get(0).getSpawnX() && mCards.get(0).position.y == mCards.get(0).getSpawnY()) {
                for (int i = 5; i < 10; i++) {
                    mAICards.get(i - 5).deal(elapsedTime, i);
                }
            }

            //Once both sets of cards are dealt this method is no longer needed
            if (mAICards.get(4).position.x == mAICards.get(4).getSpawnX() && mAICards.get(4).position.y == mAICards.get(4).getSpawnY()) {
                cardsDealt = true;
            }
    }

    //Created by Jordan McDonald
    private void managePause(ElapsedTime elapsedTime) {
        if (gamePaused) {
            pausedContinue.update(elapsedTime, mDefaultLayerViewport, mDefaultScreenViewport);
            pausedQuit.update(elapsedTime, mDefaultLayerViewport, mDefaultScreenViewport);
        }

        if (pause.isPushTriggered()) {
            gamePaused = true;
        } else if (pausedContinue.isPushTriggered()) {
            gamePaused = false;
            unpauseCounter = 5;
        } else if (pausedQuit.isPushTriggered()) {
            mGame.getScreenManager().removeScreen(this);
            mGame.getScreenManager().removeScreen("SplashScreen");
        }

        if (unpauseCounter > 0) {
            unpauseCounter = unpauseCounter - 1;
        }
    }

    //Created by Jordan McDonald
    private void drawPause(ElapsedTime elapsedTime, IGraphics2D graphics2D, int screenHeight, int screenWidth) {
        mPauseMenu.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(screenHeight / 12.0f);
        graphics2D.drawText("PAUSE MENU", screenWidth / 2.65f, screenHeight / 4.0f, textPaint);
        pausedContinue.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        pausedQuit.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
    }

    //Created by Jordan McDonald
    private void manageUserHealth(int damage) {

        if (damage > 0) {
            if (userHealth <= damage) {
                userHealth = 0;
            } else {
                userHealth -= damage;
            }
        }

    }

    //Created by Jordan McDonald
    private void drawHealth(IGraphics2D graphics2D, int userHealth, int AIHealth) {

        //Drawing the user's health
        textPaint.setColor(Color.GREEN);
        if (userHealth == 0 || userHealth == 1) {
            graphics2D.drawRect(mDefaultScreenViewport.right - 553.5f, mDefaultScreenViewport.bottom - 73.4f,
                    (mDefaultScreenViewport.right - 553.5f) + (9.17f * userHealth), mDefaultScreenViewport.bottom - 63, textPaint);
        } else if (userHealth >= 2) {
            graphics2D.drawRect(mDefaultScreenViewport.right - 540, mDefaultScreenViewport.bottom - 83.8f,
                    (mDefaultScreenViewport.right - 540) + (8.9f * userHealth), mDefaultScreenViewport.bottom - 63, textPaint);
            graphics2D.drawRect(mDefaultScreenViewport.right - 553.5f, mDefaultScreenViewport.bottom - 73.4f,
                    (mDefaultScreenViewport.right - 553.5f) + (9.17f * userHealth), mDefaultScreenViewport.bottom - 63, textPaint);
            graphics2D.drawRect(mDefaultScreenViewport.right - 530, mDefaultScreenViewport.bottom - 115,
                    (mDefaultScreenViewport.right - 530) + (8.7f * userHealth), mDefaultScreenViewport.bottom - 63, textPaint);
        }

        //Drawing the AI's health
        textPaint.setColor(Color.GRAY);
        if (AI.getAIHealth() == 0 || AI.getAIHealth() == 1) {
            graphics2D.drawRect(mDefaultScreenViewport.left + 216, mDefaultScreenViewport.top + 108,
                    (mDefaultScreenViewport.left + 216) + (9.17f * AIHealth), mDefaultScreenViewport.top + 118.4f, textPaint);
        } else if (AI.getAIHealth() >= 2) {
            graphics2D.drawRect(mDefaultScreenViewport.left + 216, mDefaultScreenViewport.top + 108,
                    (mDefaultScreenViewport.left + 216) + (9.17f * AIHealth), mDefaultScreenViewport.top + 118.4f, textPaint);
            graphics2D.drawRect(mDefaultScreenViewport.left + 229.5f, mDefaultScreenViewport.top + 97.6f,
                    (mDefaultScreenViewport.left + 229.5f) + (8.9f * AIHealth), mDefaultScreenViewport.top + 118.4f, textPaint);
            graphics2D.drawRect(mDefaultScreenViewport.left + 239.5f, mDefaultScreenViewport.top + 66.4f,
                    (mDefaultScreenViewport.left + 239.5f) + (8.7f * AIHealth), mDefaultScreenViewport.top + 118.4f, textPaint);
        }

        //Displaying health values via text
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(35.0f);

        graphics2D.drawText("Health: " + userHealth + "/" + MAX_HEALTH, mDefaultScreenViewport.right - 530,
                mDefaultScreenViewport.bottom - 135, textPaint);

        graphics2D.drawText("AI Health: " + AI.getAIHealth() + "/" + MAX_HEALTH, mDefaultScreenViewport.left + 245,
                mDefaultScreenViewport.top + 165, textPaint);

    }

    //Created by Jamie Finnegan
    private void playBackgroundMusic() {
        AudioManager audioManager = getGame().getAudioManager();
        if(!audioManager.isMusicPlaying())
            audioManager.playMusic(
                    getGame().getAssetManager().getMusic("Resonance"));
    }

    //Created by Michael Gray
    public boolean[] getRearFacing() { return rearFacing;}

    //Created by Michael Gray
    public Card getCurrentCard() { return currentCard; }

}


