package com.atlasgroup.tmika.highlights.web;

import com.atlasgroup.tmika.highlights.api.HighlightsApi;
import com.atlasgroup.tmika.highlights.domain.Highlight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value ="highlights")
public class HighlightController {

    @Autowired
    HighlightsApi api;

    @GetMapping("/{username}")
    public String getHighlightsByUser(@PathVariable String username) {
        return "ok";
    }

    @PostMapping(value = "/{username}/documents/{documentId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Highlight addHighlight(@PathVariable String username, @PathVariable long documentId, @RequestBody HighlightDefinition definition) {
        return api.addHighlight(username, documentId, definition);
    }

}
