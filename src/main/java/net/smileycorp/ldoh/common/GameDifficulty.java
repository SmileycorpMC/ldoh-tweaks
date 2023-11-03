package net.smileycorp.ldoh.common;

import net.minecraft.util.text.TextComponentTranslation;

import java.util.Locale;

public interface GameDifficulty {

    Level[] values = { Level.SANDBOX, Level.TUTORIAL, Level.ADVENTURER, Level.NOVICE, Level.HARDCORE, Level.SURVIVOR};

    Level getGameDifficulty();

    void setGameDifficulty(Level difficulty);

    static Level getGameDifficulty(Object obj) {
        return ((GameDifficulty)obj).getGameDifficulty();
    }

    static void setGameDifficulty(Object obj, Level difficulty) {
        ((GameDifficulty)obj).setGameDifficulty(difficulty);
    }

    static Level getGameDifficulty(byte b) {
        return values[b % values.length];
    }

    enum Level {



        SANDBOX,
        TUTORIAL,
        ADVENTURER,
        NOVICE,
        HARDCORE,
        SURVIVOR;

        public TextComponentTranslation getTitle() {
            return new TextComponentTranslation("gamedifficulty." + toString().toLowerCase(Locale.US) + ".name");
        }

        public TextComponentTranslation getDescription() {
            return new TextComponentTranslation("gamedifficulty." + toString().toLowerCase(Locale.US) + ".description");
        }

        public boolean hardcore() {
            return ordinal() > 3;
        }

    }
}
