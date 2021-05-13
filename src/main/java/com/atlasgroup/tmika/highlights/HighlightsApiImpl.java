package com.atlasgroup.tmika.highlights;

import com.atlasgroup.tmika.highlights.api.DocumentApi;
import com.atlasgroup.tmika.highlights.api.HighlightsApi;
import com.atlasgroup.tmika.highlights.api.exception.NotFoundException;
import com.atlasgroup.tmika.highlights.domain.Highlight;
import com.atlasgroup.tmika.highlights.domain.HighlightRepository;
import com.atlasgroup.tmika.highlights.domain.HighlightSegment;
import com.atlasgroup.tmika.highlights.web.HighlightDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class HighlightsApiImpl implements HighlightsApi {

    @Autowired
    HighlightRepository repository;

    @Autowired
    DocumentApi documentApi;

    @Override
    public Highlight getHighlight(String username, long documentId) {
        return repository.findByUsernameAndDocumentId(username, documentId)
                .orElseThrow(() -> new NotFoundException("Highlights not found"));
    }

    @Override
    public Highlight addHighlight(String username, long documentId, HighlightDefinition highlightDefinition) {
        final var highlight = repository.findByUsernameAndDocumentId(username, documentId)
                .orElseGet(() -> Highlight.builder()
                        .username(username)
                        .documentId(documentId)
                        .build());

        var newSegment = getNewHighlightSegment(documentId, highlightDefinition);
        return repository.save(highlight.addSegment(newSegment));
    }

    private HighlightSegment getNewHighlightSegment(long documentId, HighlightDefinition definition) {
        final long divOffset = documentApi.getElementOffset(documentId, definition.getSourceDivId());
        return HighlightSegment.builder()
                .start(divOffset + definition.getOffset())
                .end(divOffset + definition.getOffset() + definition.getText().length())
                .divOffset(divOffset)
                .divId(definition.getSourceDivId())
                .build();
    }

    @Override
    public String getHighlightedDocument(String username, long documentId) {
        var document = documentApi.getDocumentById(documentId);
        return document.html();
    }
}
