package org.bowling

import spock.lang.Specification
import spock.lang.Unroll

class  BowlingTest extends Specification {

    /**
     * Tests for creating frames form bowling rolls and
     * scoring those frames.
     */
    @Unroll
    def "Bowling testing"() {
        expect:
        BowlingGame bowlingGame = BowlingGame.createGameFromRolls(rolls)
        expected == bowlingGame.frames*.score

        where:
        expected                                 || rolls
        // Example from homework problem
        [9, null, null]                          || [4, 5, 'X', 8]
        // Not enough rolls to score the spare (frame 2)
        [9, null]                                || [4, 5, 3, '/']
        // Invalid type (doesn't accept boolean) and '|' is not an acceptable string
        [1, 3]                                   || [1, false, 3, '|']
        // Invalid string value (numbers shouldn't be strings)
        [1, 3]                                   || [1, '2', 3, '4']
        // Invalid integer values (only 0-9 is acceptable)
        [1, 3]                                   || [1, -10, 3, 15]
        // Not enough rolls to score strike (frame 1)
        [null]                                   || ['X']
        // Not enough rolls to score spare (frame 1)
        [null]                                   || ['/']
        // Not enough rolls to score strike (frame 1) or 3 (frame 2)
        [null, null]                             || ['X', 3]
        // Not enough rolls to score spare (frame 1)
        [null]                                   || [3, '/']
        // Fixed homework problem example
        [9, 19, 9]                               || [4, 5, 'X', 8, 1]
        [9, 12, 6]                               || [4, 5, 3, '/', 2, 4]
        [17, 7]                                  || ['X', 3, 4]
        [3, 7]                                   || [1, 2, 3, 4]
        [14, 9]                                  || [1, '/', 4, 5]
        [3, 15, 9]                               || [1, 2, 4, '/', 5, 4]
        [20, 10, 5, 20, 29, 19, 9, 7, 18, 12]    || ['X', 7, '/', 0, 5, 8, '/', 'X', 'X', 9, 0, 5, 2, 7, '/', 8, '/', 2]
        [30, 30, 30, 30, 30, 30, 30, 30, 23, 17] || ['X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 3, 4]
        [30, 30, 30, 30, 30, 30, 30, 30, 30, 30] || ['X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X']
    }
}
