package com.atlasgroup.tmika.highlights.api;

import com.atlasgroup.tmika.highlights.domain.Highlight;
import com.atlasgroup.tmika.highlights.web.HighlightDefinition;

public interface HighlightsApi {

    Highlight getHighlight(String username, long documentId);

    /**
     * Adds highlight into set of highlights
     * @param username the user name
     * @param documentId the id of the document
     * @param highlightDefinition The definition of new highlight
     * @return all highlight definitions for the given user and document
     */
    Highlight addHighlight(String username, long documentId, HighlightDefinition highlightDefinition);

    String getHighlightedDocument(String username,long documentId);
}
