package org.bowling

import spock.lang.Specification

class BowlingTest extends Specification {
    def "someLibraryMethod returns true"() {
        setup:
        def lib = new Bowling()

        when:
        def result = lib.someBowlingMethod()

        then:
        result == true
    }
}
