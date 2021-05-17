package com.atlasgroup.tmika.highlights.renderer

import com.atlasgroup.tmika.highlights.domain.Highlight
import com.atlasgroup.tmika.highlights.domain.HighlightSegment
import org.jsoup.Jsoup
import spock.lang.Specification
import spock.lang.Unroll

import java.nio.charset.StandardCharsets

class HighlightedHtmlRendererTest extends Specification {

    @Unroll
    def 'test highlighted html generation'() {
        given:
        def sourceFile = new File("src/test/resources/${source}")
        def expectedFile = new File("src/test/resources/${expected}")

        def highlight = new Highlight(id: 1L)
        segments.each { segment ->
            highlight.addSegment(new HighlightSegment(
                    start: segment.key, end: segment.value
            ))
        }

        def underTest = new HighlightedHtmlRenderer()
                .withHighlightDefinition(highlight)
                .withDocumentTemplate(Jsoup.parse(sourceFile, StandardCharsets.UTF_8.name()))

        when:
        def result = underTest.render();

        then:
        result == expectedFile.text

        where:
        source          | segments || expected
        'simple01.html' | [0: 5]   || 'simple01result.html'
    }
}
