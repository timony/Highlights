package com.atlasgroup.tmika.highlights.renderer

import com.atlasgroup.tmika.highlights.DocumentApiImpl
import com.atlasgroup.tmika.highlights.domain.Highlight
import com.atlasgroup.tmika.highlights.domain.HighlightSegment
import spock.lang.Specification
import spock.lang.Unroll

class HighlightedHtmlRendererTest extends Specification {

    @Unroll
    def 'test highlighted html generation'() {
        given:
        def sourceFile = new File("src/test/resources/com/atlasgroup/tmika/highlights/renderer/${source}")
        def template = DocumentApiImpl.getDocumentByFile(sourceFile);
        def expectedFile = new File("src/test/resources/com/atlasgroup/tmika/highlights/renderer/${expected}")

        def highlight = new Highlight(id: 1L)
        segments.each { segment ->
            highlight.addSegment(new HighlightSegment(segment.key, segment.value))
        }

        def underTest = new HighlightedHtmlRenderer()
                .withHighlightDefinition(highlight)
                .withDocumentTemplate(template)

        when:
        def result = underTest.render();

        then:
        result == expectedFile.text

        where:
        source                           | segments                         || expected
        'simple01.html'                  | [0: 5]                           || 'simple01result01.html'
        'simple01.html'                  | [1: 5]                           || 'simple01result02.html'
        'simple01.html'                  | [1: 1000]                        || 'simple01result03.html'
        'simple02.html'                  | [13: 20]                         || 'simple02result01.html'
        'simple02.html'                  | [13: 48]                         || 'simple02result02.html'
        'assignmentExample.html'         | [7: 9, 10: 60, 61: 62, 191: 221] || 'assignmentExampleResult01.html'
        'assignmentExampleNoBreaks.html' | [7: 9, 10: 60, 61: 62, 191: 221] || 'assignmentExampleNoBreaksResult01.html'
    }
}
