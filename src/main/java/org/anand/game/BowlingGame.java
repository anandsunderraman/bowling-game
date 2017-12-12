package org.anand.game;

import org.anand.model.Frame;

import java.util.LinkedList;
import java.util.ListIterator;

public class BowlingGame {

    private LinkedList<Frame> frameSequence = new LinkedList<>();

    //denotes if the game is over or not
    private boolean isFinished = false;

    //denotes the previousFrame
    //used to recompute the previousFrame score if previousFrame was a spare or a strike
    private Frame previousFrame = null;

    //denotes the first frame
    //used to iterate over the list to compute the current score
    private Frame headFrame = null;

    //holds the state of the currentFirst and currentSecond rolls
    //this is required for validation before Frame can be created
    //with the right values
    private int currentFirstRoll = -1;
    private int currentSecondRoll = -1;

    //holds the value of the number of frames completed
    private int framesCompleted = 0;

    //maximum permissible frames
    private int maxFrameCount = 10;

    //specifies number of allowed throws if last frame is spare or strike
    private boolean allowExtraThrows = false;

    public BowlingGame(int maxFrameCount) {
        this.maxFrameCount = maxFrameCount;
    }

    /**
     * Number of pins knocked down in a roll
     * @param numberOfPins
     */
    public void roll(int numberOfPins) {
        //if isFinished == true then throw UnsupportedOperationException
        if ( isFinished() ) {
            throw new UnsupportedOperationException("Cannot roll when game is finished. Please start a new game");
        }

        //if numberOfPins is less than 0 or greater than 10 throw illegal argument exception
        if (numberOfPins < 0 || numberOfPins > 10) {
            throw new IllegalArgumentException("numberOfPins rolled must be an integer in the range 0-10");
        }

        if (currentFirstRoll == -1) {
            currentFirstRoll = numberOfPins;

            if ( allowExtraThrows ) {
                //if extra throw is allowed and the previousFrame isSpare then allow only one extra throw
                if ( frameSequence.peekLast().isSpare() ) {
                    currentSecondRoll = 0;
                    addFrame();
                }
            } else if (currentFirstRoll == 10) {
                currentSecondRoll = 0;
                addFrame();
            }
        } else if (currentSecondRoll == -1) {

            currentSecondRoll = numberOfPins;
            if (currentFirstRoll + currentSecondRoll > 10 && !allowExtraThrows) {
                throw new IllegalStateException("The number of pins in a frame cannot exceed 10");
            }
            addFrame();
        }
    }

    //iterates through the frames starting from the head and computes the current score
    public int getScore() {

        ListIterator<Frame> frameListIterator = frameSequence.listIterator();
        Frame currentFrame = frameListIterator.hasNext() ? frameListIterator.next() : null;
        Frame oneFrameAhead = frameListIterator.hasNext() ? frameListIterator.next() : null;
        Frame twoFramesAhead = frameListIterator.hasNext() ? frameListIterator.next() : null;

        while (currentFrame != null) {
            currentFrame.computeScore(oneFrameAhead, twoFramesAhead);
            currentFrame = oneFrameAhead;
            oneFrameAhead = twoFramesAhead;
            twoFramesAhead = frameListIterator.hasNext() ? frameListIterator.next() : null;
        }

        currentFrame = oneFrameAhead;
        oneFrameAhead = twoFramesAhead;

        if ( currentFrame != null) {
            currentFrame.computeScore(oneFrameAhead, null);
        }

        int totalScore = frameSequence.stream().mapToInt(Frame::getScore).sum();

        //if size of frame sequence is equal to maxFrameCount then
        if ( frameSequence.size() == maxFrameCount) {
            return totalScore;
        } else {
            Frame penultimateFrame = frameSequence.get(frameSequence.size() - 2);
            Frame lastFrame = frameSequence.get(frameSequence.size() - 1);
            if (penultimateFrame.isStrike()) {
                return totalScore - lastFrame.getFirstRoll() - lastFrame.getSecondRoll();
            } else {
                return totalScore - lastFrame.getFirstRoll();
            }
        }
    }

    public boolean isFinished() {
        return isFinished;
    }

    /**
     * encapsulates the logic of adding a frame
     * deciding if the game should end
     * also trigger score computation
     */
    private void addFrame() {

        Frame currentFrame = new Frame(currentFirstRoll, currentSecondRoll);

        frameSequence.add(currentFrame);

        //if we had allowed for extra throws then it is time to terminate the game here
        if (allowExtraThrows) {
            isFinished = true;
            return;
        }

        if (frameSequence.size() == maxFrameCount) {
            if ( currentFrame.isStrike() || currentFrame.isSpare() ) {
                allowExtraThrows = true;
            } else {
                isFinished = true;
            }
        }
        currentFirstRoll = -1;
        currentSecondRoll = -1;
    }


}
