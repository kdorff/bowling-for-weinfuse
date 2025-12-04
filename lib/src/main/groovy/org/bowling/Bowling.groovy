package org.bowling

/**
 * An individual bowling roll.
 */
class Roll {
    /**
     * The roll index in the input data.
     */
    final int rollNumber

    /**
     * Roll value, if the roll is 0 - 9. Otherwise null.
     */
    final Integer value

    /**
     * If roll a strike.
     */
    final boolean isStrike

    /**
     * If roll is a spare.
     */
    final boolean isSpare

    /**
     * Construct a Roll object.
     * @param rollNumber (index for this roll in the rolls input)
     */
    Roll(final int rollNumber,
         final Integer value, final boolean isStrike, final boolean isSpare) {
        this.rollNumber = rollNumber
        this.value = value
        this.isStrike = isStrike
        this.isSpare = isSpare
    }
}

/**
 * An individual bowling frame.
 */
class Frame {
    /**
     * Frame number (0-based).
     */
    final int frameNumber

    /**
     * Up to two rolls per frame, except for
     * frame 10 (frameNumber == 9) which can have up tp 3.
     */
    protected final List<Roll> rolls = []

    /**
     * The score for a frame. Or null if it hasn't been calculated yet
     * or cannot be calculated.
     */
    protected Integer score = null

    /**
     * Construct the object
     * @param frameNumber 0-based game frame number
     */
    Frame(final int frameNumber) {
        this.frameNumber = frameNumber
    }

    /**
     * Get the score for the Frame (or null if not
     * yet calculated or if it cannot be calculated
     * based on the number of provided rolls).
     * @return score for Frame (possibly null)
     */
    Integer getScore() {
        return score
    }
}

/**
 * Class for scoring a bowling game.
 * Takes a list of rolls and can provide a list of scored frames.
 * The game does not need to be complete.
 * If there aren't enough rolls to score some frames, those
 * frames will have a null score.
 */
class BowlingGame {
    /**
     * All known frames for the game (singe player).
     */
    private List<Frame> frames = []

    /**
     * All known rolls for the game (singe player).
     */
    private List<Roll> allRolls = []

    /**
     * Create a BowlingGame with a list of rolls.
     * @param inputRolls list of integers (0-9) or '/' for spare
     * or 'X' for strike.
     */
    BowlingGame(final List<Object> inputRolls) {
        // Load the rolls into the frames
        createFramesFromRolls(inputRolls)
        // Score all of the frames we found
        frames.each { Frame frame ->
            scoreFrame(frame)
        }
    }

    /**
     * Get the list of Frames for the game.
     * @return the list of frames.
     */
    List<Frame> getFrames() {
        return frames
    }

    /**
     * Create game Frames from the provided rolls.
     * This does NOT score the frames, yet.
     * @param inputRolls the list of rolls to create Frames from.
     */
    private void createFramesFromRolls(final List<Object> inputRolls) {
        int frameNumber = 0
        int frameRollNumber = 0
        inputRolls.eachWithIndex { inputRollValue, inputRollIndex ->
            // Determine details for the current roll
            Integer rollValue = null
            boolean rollIsStrike = false
            boolean rollIsSpare = false
            if (frameRollNumber == 0) {
                // Add a new frame
                frames.add(new Frame(frameNumber))
            }
            switch (inputRollValue) {
                case Integer:
                    rollValue = (Integer) inputRollValue
                    if (rollValue < 0 || rollValue > 9) {
                        System.err.println("Invalid roll (invalid integer) at index $inputRollIndex with value $inputRollValue")
                        rollValue = 0
                    }
                    break
                case String:
                    if (inputRollValue == 'X') {
                        rollIsStrike = true
                    } else if (inputRollValue == '/') {
                        // We are on frameRollNumber == 1
                        rollIsSpare = true
                    } else {
                        // Unknown string value.
                        System.err.println("Invalid roll (invalid string) at index $inputRollIndex with value $inputRollValue")
                        rollValue = 0
                    }
                    break
                default:
                    // Unknown type
                    System.err.println("Invalid roll (unknown type) at index $inputRollIndex with value $inputRollValue")
                    rollValue = 0
            }

            // Save the current roll
            Roll roll = new Roll(inputRollIndex, rollValue, rollIsStrike, rollIsSpare)
            frames[frameNumber].rolls << roll
            allRolls << roll

            // Next roll will go into...
            if ((frameRollNumber == 1 || roll.isSpare || roll.isStrike) && frameNumber != 9) {
                // A new frame
                frameNumber++
                frameRollNumber = 0
            } else {
                // Another roll for the same frame
                frameRollNumber++
            }
        }
    }

    /**
     * Starting with Frame nextFrame, try to get numRolls roll values.
     * This will be null if numRolls rolls cannot be obtained.
     * @param nextFrame the frame to start getting the rolls from
     * @param allRolls list of all of the rolls (parsed from input)
     * @param numRolls the number of rolls to return the sum of
     * @return sum of numRolls rolls starting at nextFrame. Return null if that
     * many rolls cannot be found.
     */
    private Integer sumNextNRolls(
            final Frame nextFrame,
            final int numRolls) {
        if (nextFrame == null) {
            // No frame
            return null
        }
        if (nextFrame.rolls.size() == 0) {
            // No rolls found in the frame
            return null
        }

        // In the list of all rolls, get the input roll number
        // for the first roll in this next frame. And attempt
        // to grab numRolls.
        int firstRollNumber = nextFrame.rolls[0].rollNumber
        int lastRollNumber = firstRollNumber + numRolls - 1
        if (allRolls.size() <= lastRollNumber) {
            // We don't have enough follow up rolls
            return null
        }
        List<Roll> rollsToSum = allRolls[firstRollNumber..lastRollNumber]

        int sum = 0
        rollsToSum.each { Roll roll ->
            if (roll.value != null) {
                sum += roll.value
            } else if (roll.isStrike) {
                sum += 10
            } else if (roll.isSpare) {
                sum = 10
            }
        }
        return sum
    }

    /**
     * Score an individual frame or set the score to null if we
     * don't have enough rolls to score this frame.
     * @param allFrames List of all of the known frames for the game
     * @param allRolls List of all of the known rolls for the game
     */
    protected void scoreFrame(final Frame frame) {
        Integer scoreForFrame = null
        switch (frame.rolls.size()) {
            case 0:
                // Frame has no rolls. null score.
                break
            case 1:
                if (frame.rolls[0].isStrike) {
                    // Next two rolls (score null if rolls not found)
                    Integer nextRollsSum = sumNextNRolls(frames[frame.frameNumber + 1], 2)
                    scoreForFrame = nextRollsSum == null ? null : 10 + nextRollsSum
                }
                break
            case 2:
                if (frame.rolls[1].isSpare) {
                    // Next one roll (score null if roll not found)
                    Integer nextRollsSum = sumNextNRolls(frames[frame.frameNumber + 1], 1)
                    scoreForFrame = nextRollsSum == null ? null : 10 + nextRollsSum
                } else {
                    scoreForFrame = frame.rolls[0].value + frame.rolls[1].value
                }
                break
            default:
                // Frame 10 may be a special case where
                // there can be three values.
                scoreForFrame = 0
                frame.rolls.eachWithIndex { Roll roll, int rollIndex ->
                    if (roll.value != null) {
                        scoreForFrame += roll.value
                    } else if (roll.isStrike) {
                        scoreForFrame += 10i
                    } else if (roll.isSpare) {
                        // Spare, previous roll must be a number
                        Roll previousRoll = frame.rolls[rollIndex - 1]
                        scoreForFrame += 10i - previousRoll.value
                    }
                }
                break
        }
        frame.score = scoreForFrame
    }

    /**
     * Create a BowlingGame, a scored list of Frames, given a list of rolls.
     * @param inputRolls input rolls. List of Integer 0-9, 'X', or '/'.
     * @return a BowlingGame with scored frames (as best as possible,
     * if the list of rolls does not represent a full game).
     */
    static BowlingGame createGameFromRolls(final List<Object> inputRolls) {
        BowlingGame bowlingGame = new BowlingGame(inputRolls)
        return bowlingGame
    }
}