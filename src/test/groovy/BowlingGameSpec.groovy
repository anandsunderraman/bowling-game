import org.anand.game.BowlingGame
import spock.lang.Specification

class BowlingGameSpec extends Specification {

    BowlingGame bowlingGame

    def setup() {
        bowlingGame = new BowlingGame(10)
    }

    def "roll method must throw exceptions on invalid inputs"() {

        when: "roll() is invoked with invalid inputs "
        bowlingGame.roll(numberOfPins)

        then: "exceptions are thrown"
        def error = thrown(expectedException)
        assert error.message == expectedMessage

        where: "inputs are"
        numberOfPins | expectedException        | expectedMessage
        -1           | IllegalArgumentException | "numberOfPins rolled must be an integer in the range 0-10"
        11           | IllegalArgumentException | "numberOfPins rolled must be an integer in the range 0-10"
    }

    def "roll method must throw exceptions when the sum of the rolls is greater than 10"() {

        when: "roll() is invoked twice with inputs summing greater than 10 "
        bowlingGame.roll(5)
        bowlingGame.roll(6)

        then: "IllegalStateException is thrown"
        IllegalStateException illegalStateException = thrown()
        assert  illegalStateException.message == "The number of pins in a frame cannot exceed 10"
    }

    def "UnsupportedOperationException is thrown when roll is invoked after game is finished"() {

        given: "a bowling game with a max of 2 frames"
        bowlingGame = new BowlingGame(2)

        when: "roll() is invoked 5 times"
        bowlingGame.roll(1)
        bowlingGame.roll(1)
        bowlingGame.roll(1)
        bowlingGame.roll(1)
        bowlingGame.roll(1)

        then: "UnsupportedOperationException is thrown"
        UnsupportedOperationException unsupportedOperationException = thrown()
        assert  unsupportedOperationException.message == "Cannot roll when game is finished. Please start a new game"
    }

    def "Bowling game is able to return perfect score on 12 strikes"() {

        when: "roll() is invoked 12 times with 10"
        for (int rollCount = 1; rollCount <= 12 ; rollCount++) {
            bowlingGame.roll(10)
        }

        and: "getScore is invoked"
        int gameScore = bowlingGame.getScore()

        then: "gameScore is 300"
        assert gameScore == 300
    }

    def "Bowling game is able to compute score right when the last frame is spare"() {

        when: "roll is invoked such that the last frame is a spare"
        for (int rollCount = 1; rollCount <= 18 ; rollCount++) {
            bowlingGame.roll(0)
        }
        bowlingGame.roll(9)
        bowlingGame.roll(1)
        bowlingGame.roll(1)

        and: "getScore is invoked"
        int gameScore = bowlingGame.getScore()

        then: "game score is 11"
        assert gameScore == 11

    }

    def "Bowling game is able to compute score right when the last frame is a strike"() {

        when: "roll is invoked such that the last frame is a spare"
        for (int rollCount = 1; rollCount <= 18 ; rollCount++) {
            bowlingGame.roll(0)
        }
        bowlingGame.roll(10)
        bowlingGame.roll(1)
        bowlingGame.roll(1)

        and: "getScore is invoked"
        int gameScore = bowlingGame.getScore()

        then: "game score is 12"
        assert gameScore == 12
    }
}
