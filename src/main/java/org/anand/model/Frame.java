package org.anand.model;

/**
 * Represents the state of a Frame
 */
public class Frame {

    private final int firstRoll;
    private final int secondRoll;
    private boolean isStrike = false;
    private boolean isSpare = false;
    private int score = 0;

    public Frame(int firstRoll, int secondRoll) {
        this.firstRoll = firstRoll;
        this.secondRoll = secondRoll;
        this.score = firstRoll + secondRoll;
        if (this.firstRoll == 10) {
            isStrike = true;
        } else if (this.firstRoll + this.secondRoll == 10) {
            isSpare = true;
        }
    }

    public int getFirstRoll() {
        return firstRoll;
    }

    public int getSecondRoll() {
        return secondRoll;
    }

    public boolean isStrike() {
        return isStrike;
    }

    public boolean isSpare() {
        return isSpare;
    }

    public int getScore() {
        return score;
    }

    /**
     * Computes the score of a frame based on frames ahead
     * @param oneFrameAhead
     * @param twoFramesAhead
     */
    public void computeScore(Frame oneFrameAhead, Frame twoFramesAhead) {

        int currentFrameScore = this.firstRoll + this.secondRoll;

        if (isStrike && oneFrameAhead != null) {

            //handles the case where there are consecutive strikes
            if ( oneFrameAhead.isStrike) {
                
                currentFrameScore = currentFrameScore + oneFrameAhead.firstRoll;
                if (twoFramesAhead != null) {
                    currentFrameScore = currentFrameScore + twoFramesAhead.firstRoll;
                } else {
                    currentFrameScore = currentFrameScore + oneFrameAhead.secondRoll;
                }
            } else {
                currentFrameScore = currentFrameScore + oneFrameAhead.firstRoll + oneFrameAhead.secondRoll;
            }
        } else if(isSpare && oneFrameAhead != null) {
            currentFrameScore = currentFrameScore + oneFrameAhead.firstRoll;
        }

        score = currentFrameScore;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Frame{");
        sb.append("firstRoll=").append(firstRoll);
        sb.append(", secondRoll=").append(secondRoll);
        sb.append(", isStrike=").append(isStrike);
        sb.append(", isSpare=").append(isSpare);
        sb.append(", score=").append(score);
        sb.append('}');
        return sb.toString();
    }
}
