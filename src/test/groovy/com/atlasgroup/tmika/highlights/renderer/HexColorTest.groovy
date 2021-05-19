package com.atlasgroup.tmika.highlights.renderer

import spock.lang.Specification
import spock.lang.Unroll

import java.util.regex.Pattern

class HexColorTest extends Specification {

    private static final String HEX_PATTERN = '^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$'

    @Unroll
    def 'should render hex no. #i'() {
        given:
        Pattern pattern = Pattern.compile(HEX_PATTERN)

        when:
        def random = HexColor.random()

        then:
        noExceptionThrown()
        pattern.matcher(random).matches()

        where:
        i << (1..10)

    }
}
