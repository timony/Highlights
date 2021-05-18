package com.atlasgroup.tmika.highlights.web;

import com.atlasgroup.tmika.highlights.api.HighlightsApi;
import com.atlasgroup.tmika.highlights.domain.Highlight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HighlightController {

    @Autowired
    HighlightsApi api;

    @GetMapping("/{username}/documents/{documentId}/highlights")
    public Highlight getHighlightsByUser(@PathVariable String username, @PathVariable long documentId) {
        return api.getHighlight(username, documentId);
    }

    @PostMapping(value = "/{username}/documents/{documentId}/highlights", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Highlight addHighlight(@PathVariable String username, @PathVariable long documentId, @RequestBody HighlightDefinition definition) {
        return api.addHighlight(username, documentId, definition);
    }

    @GetMapping(value = "/{username}/documents/{documentId}", produces = MediaType.TEXT_HTML_VALUE)
    public String getDocument(@PathVariable String username, @PathVariable long documentId, @RequestParam(required = false) boolean renderStyle) {
        return api.getHighlightedDocument(username, documentId, renderStyle);
    }

}
