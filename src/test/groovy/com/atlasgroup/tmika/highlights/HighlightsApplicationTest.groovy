package com.atlasgroup.tmika.highlights

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class HighlightsApplicationTest extends Specification {

    @Autowired
    ObjectMapper objectMapper

    def 'Application test'() {
        expect:
        objectMapper
    }
}
