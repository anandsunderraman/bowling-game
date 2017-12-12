package org.anand;


import org.anand.game.BowlingGame;

public class App
{
    public static void main( String[] args ) {
        BowlingGame bowlingGame = new BowlingGame(10);
        System.out.println(bowlingGame.isFinished());

        for (int frameCount = 1; frameCount <= 20; frameCount++) {
            bowlingGame.roll(4);
        }

        System.out.println(bowlingGame.getScore());
        System.out.println(bowlingGame.isFinished());

    }
}
