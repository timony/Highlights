package com.atlasgroup.tmika.highlights.domain

import spock.lang.Specification
import spock.lang.Unroll

import static com.atlasgroup.tmika.highlights.domain.HighlightSegment.Interaction.*

class HighlightSegmentTest extends Specification {

    @Unroll
    def 'should compare two segments'() {
        given:

        def segment1 = new HighlightSegment(elementId1, offset1, length1)
        def segment2 = new HighlightSegment(elementId2, offset2, length2)

        when:
        def result = segment1.compareTo(segment2)

        then:
        result == expectedResult

        where:
        elementId1 | offset1 | length1 | elementId2 | offset2 | length2 || expectedResult
        'a'        | 0       | 0       | 'b'        | 0       | 0       || -1
        'b'        | 0       | 0       | 'a'        | 0       | 0       || 1
        'a'        | 0       | 0       | 'a'        | 1       | 0       || -1
        'a'        | 1       | 0       | 'a'        | 0       | 0       || 1
        'a'        | 1       | 10      | 'a'        | 1       | 20      || -1
        'a'        | 1       | 20      | 'a'        | 1       | 10      || 1
        'a'        | 1       | 20      | 'a'        | 1       | 20      || 0
    }

    @Unroll
    def '[#firstStart:#firstEnd] is interacting [#secondStart:#secondEnd]: #expectedResult'() {
        given:
        def first = new HighlightSegment('a', firstStart, firstEnd)
        def second = new HighlightSegment('a', secondStart, secondEnd)

        when:
        def result = first.getInteraction(second)

        then:
        result == expectedResult

        where:
        firstStart | firstEnd | secondStart | secondEnd || expectedResult
        1          | 6        | 8           | 10        || NONE
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
        def first = new HighlightSegment('a', firstStart, firstEnd)
        def second = new HighlightSegment('a', secondStart, secondEnd)

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