package net.smileycorp.ldoh.common.difficulty;

public interface DifficultyOptions {

    GameDifficulty getDifficulty();

    void setGameDifficulty(GameDifficulty difficulty);

    static GameDifficulty getDifficulty(Object obj) {
        return ((DifficultyOptions)obj).getDifficulty();
    }

}
