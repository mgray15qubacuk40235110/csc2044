package uk.ac.qub.eeecs.game.SimCards;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AI {

    private static int AIHealth;

    public static Card takeTurn(List<Card> AICards) {

        Random rand = new Random();
        boolean attack = false;

        //Decide between attack and defence
        switch(rand.nextInt(2)) {
            case 0: attack = true;
        }

        //Placeholder
        if (attack) {
            return AICards.get(0);
        } else {
            return AICards.get(1);
        }
    }

    public static Card playDefence(List<Card> AICards, int userAttack, int userHealth) {

        List<Card> betterCards = new ArrayList<>();
        int winMultiplayer = 0;
        int winChance = 0;
        Random rand = new Random();

        //Add all cards that will win the round to an array
        for (int i = 0; i < AICards.size(); i++) {
            if (AICards.get(i).getmDefence() >= userAttack) {
                betterCards.add(AICards.get(i));
            }
        }

        //Formula to work out whether AI will play a better card (based on AI & user health)
        winMultiplayer = ((userHealth / AIHealth) / SimCardsScreen.MAX_HEALTH) * 200;
        if (winMultiplayer > 100) {winMultiplayer = 100;}
        winChance = rand.nextInt(101);
        if (winMultiplayer >= winChance) {
            //Roll successful so play a better card if any exist in hand, if not play any
            switch (betterCards.size()) {
                case 0:  return pickRandomCard(AICards);
                case 1:  return betterCards.get(0);
                default: return pickRandomCard(betterCards);
            }
        } else {
            //Play any card as roll was not successful
            return pickRandomCard(AICards);
        }

    }

    public static Card playAttack(List<Card> AICards, int userDefence, int userHealth) {

        List<Card> betterCards = new ArrayList<>();
        int winMultiplayer = 0;
        int winChance = 0;
        Random rand = new Random();

        //Add all cards that will win the round to an array
        for (int i = 0; i < AICards.size(); i++) {
            if (AICards.get(i).getmDefence() >= userDefence) {
                betterCards.add(AICards.get(i));
            }
        }

        //Formula to work out whether AI will play a better card (based on AI & user health)
        winMultiplayer = ((userHealth / AIHealth) / SimCardsScreen.MAX_HEALTH) * 200;
        if (winMultiplayer > 100) {winMultiplayer = 101;}
        winChance = rand.nextInt(100);
        if (winMultiplayer >= winChance) {
            //Roll successful so play a better card if any exist in hand, if not play any
            switch (betterCards.size()) {
                case 0:  return pickRandomCard(AICards);
                case 1:  return betterCards.get(0);
                default: return pickRandomCard(betterCards);
            }
        } else {
            //Play any card as roll was not successful
            return pickRandomCard(AICards);
        }
    }

    private static Card pickRandomCard(List<Card> cards) {

        Random rand = new Random();

        return cards.get(rand.nextInt(cards.size()));

    }

    //AI Health Management
    public static void manageAIHealth(int damage) {

        if (damage > 0) {
            if (AIHealth <= damage) {
                AIHealth = 0;
            } else {
                AIHealth -= damage;
            }
        }

    }

    public static void setAIHealth(int aiHealth) {
        AIHealth = aiHealth;
    }

    public static int getAIHealth() {
        return AIHealth;
    }
}
