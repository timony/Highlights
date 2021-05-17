package com.atlasgroup.tmika.highlights.domain

import spock.lang.Specification
import spock.lang.Unroll

class HighlightTest extends Specification {

    @Unroll
    def 'should handle adding segment where #desc'() {
        given: 'Current state'
        def currentState = currentSegments.collect { new HighlightSegment(start: it.key, end: it.value, divOffset: 0) }
        def highlight = new Highlight(documentId: 100, username: 'user1', segments: currentState)

        and: 'Segment to add'
        def newSegment = new HighlightSegment(start: start, end: end, divOffset: 0)

        when:
        def result = highlight.addSegment(newSegment)

        then:
        result.segments.size() == expectedSegments.size()
        result.getSegments().sort() == expectedSegments.collect { new HighlightSegment(start: it.key, end: it.value, divOffset: 0) }

        where:
        currentSegments      | start | end || expectedSegments      | desc
        [0: 5, 10: 11]       | 7     | 9   || [0: 5, 7: 9, 10: 11]  | 'new segment is not interacting with other'
        [0: 5, 10: 11]       | 1     | 2   || [0: 5, 10: 11]        | 'new segment is eaten by first existing'
        [0: 5, 10: 11]       | 4     | 8   || [0: 3, 4: 8, 10: 11]  | 'new segment overlaps first existing'
        [0: 5, 10: 12]       | 4     | 11  || [0: 3, 4: 11, 12: 12] | 'first segment overlaps both first and second existing'
        [0: 5, 10: 11]       | 10    | 14  || [0: 5, 10: 14]        | 'second existing is eaten by new segment'
        [0: 5, 7: 8, 10: 14] | 4     | 11  || [0: 3, 4: 11, 12: 14] | 'new segment eats second and shrinks first and third'
        [0: 5, 10: 15]       | 5     | 7   || [0: 7, 10: 15]        | 'new segment merges with first existing'
        [0: 5, 10: 15]       | 7     | 10  || [0: 5, 7: 15]         | 'new segment merges with second existing'
        [0: 5, 10: 15]       | 5     | 10  || [0: 15]               | 'new segment merges two unrelated segments'

    }
}
