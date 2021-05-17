package com.atlasgroup.tmika.highlights.domain

import spock.lang.Specification
import spock.lang.Unroll

import static com.atlasgroup.tmika.highlights.domain.HighlightSegment.Interaction.*

class HighlightSegmentTest extends Specification {

    @Unroll
    def 'should compare two segments'() {
        given:

        def segment1 = new HighlightSegment(start: offset1, end: length1, divOffset: 0)
        def segment2 = new HighlightSegment(start: offset2, end: length2, divOffset: 0)

        when:
        def result = segment1 <=> segment2

        then:
        result == expectedResult

        where:
        offset1 | length1 | offset2 | length2 || expectedResult
        0       | 0       | 0       | 0       || 0
        0       | 0       | 1       | 0       || -1
        1       | 0       | 0       | 0       || 1
        1       | 10      | 1       | 20      || -1
        1       | 20      | 1       | 10      || 1
        1       | 20      | 1       | 20      || 0
    }

    @Unroll
    def '#firstStart:#firstEnd is interacting #secondStart:#secondEnd -> #expectedResult'() {
        given:
        def first = new HighlightSegment(start: firstStart, end: firstEnd, divOffset: 0)
        def second = new HighlightSegment(start: secondStart, end: secondEnd, divOffset: 0)

        when:
        def result = first.getInteraction(second)

        then:
        result == expectedResult

        where:
        firstStart | firstEnd | secondStart | secondEnd || expectedResult
        1          | 6        | 7           | 10        || NONE
        8          | 10       | 1           | 6         || NONE
        1          | 6        | 1           | 6         || EQUAL
        3          | 4        | 1           | 6         || EATEN
        3          | 6        | 1           | 6         || EATEN
        3          | 6        | 3           | 10        || EATEN
        2          | 10       | 4           | 6         || ATE
        2          | 10       | 4           | 10        || ATE
        2          | 10       | 2           | 8         || ATE
        3          | 6        | 1           | 3         || TOUCHING
        3          | 6        | 6           | 10        || TOUCHING
        3          | 6        | 2           | 4         || OVERLAPPING_LEFT
        3          | 6        | 5           | 10        || OVERLAPPING_RIGHT
    }

    @Unroll
    def '#firstStart:#firstEnd merged with #secondStart:#secondEnd: #expectedStart:#expectedEnd'() {
        given:
        def first = new HighlightSegment(start: firstStart, end: firstEnd, divOffset: 0)
        def second = new HighlightSegment(start: secondStart, end: secondEnd, divOffset: 0)

        when:
        first.merge(second)

        then:
        first.start == expectedStart
        first.end == expectedEnd

        where:
        firstStart | firstEnd | secondStart | secondEnd || expectedStart | expectedEnd
        1          | 5        | 6           | 10        || 1             | 5
        6          | 10       | 1           | 5         || 6             | 10
        3          | 6        | 1           | 3         || 1             | 6
        3          | 6        | 6           | 10        || 3             | 10

    }
}