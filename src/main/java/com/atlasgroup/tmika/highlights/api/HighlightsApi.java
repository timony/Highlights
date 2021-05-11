package com.atlasgroup.tmika.highlights.api;

import com.atlasgroup.tmika.highlights.domain.Highlight;
import com.atlasgroup.tmika.highlights.web.HighlightDefinition;

public interface HighlightsApi {

    /**
     * Adds highlight into set of highlights
     * @param highlightDefinition
     * @return
     */
    Highlight addHighlight(String username, long documentID, HighlightDefinition highlightDefinition);
}
