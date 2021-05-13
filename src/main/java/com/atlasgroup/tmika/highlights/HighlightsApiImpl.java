package com.atlasgroup.tmika.highlights;

import com.atlasgroup.tmika.highlights.api.DocumentApi;
import com.atlasgroup.tmika.highlights.api.HighlightsApi;
import com.atlasgroup.tmika.highlights.api.exception.NotFoundException;
import com.atlasgroup.tmika.highlights.domain.Highlight;
import com.atlasgroup.tmika.highlights.domain.HighlightRepository;
import com.atlasgroup.tmika.highlights.domain.HighlightSegment;
import com.atlasgroup.tmika.highlights.web.HighlightDefinition;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.UncheckedIOException;

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

        final HighlightSegment newSegment = getNewHighlightSegment(documentId, highlightDefinition);
        return repository.save(highlight.addSegment(newSegment));
    }

    private HighlightSegment getNewHighlightSegment(long documentId, HighlightDefinition definition) {
        try {
            Document doc = documentApi.getDocumentById(documentId);
            final Element body = doc.body();
            final Element sourceDiv = doc.getElementById(definition.getSourceDivId());
            final int divOffset = body.text().indexOf(sourceDiv.text());
            return HighlightSegment.builder()
                    .start(definition.getOffset())
                    .end(definition.getOffset() + definition.getText().length())
                    .divOffset(divOffset)
                    .divId(sourceDiv.id())
                    .build();
        } catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

    @Override
    public String getHighlightedDocument(String username, long documentId) {
        try {
            final Document document = documentApi.getDocumentById(documentId);

            document.body().getElementById("doc").textNodes().get(0).splitText(4);


            return document.html();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
