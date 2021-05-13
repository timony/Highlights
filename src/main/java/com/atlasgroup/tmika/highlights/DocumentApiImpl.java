package com.atlasgroup.tmika.highlights;

import com.atlasgroup.tmika.highlights.api.DocumentApi;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

@Service
public class DocumentApiImpl implements DocumentApi {

    private static final String DOC_LOCATION_TEMPLATE = "classpath:documents/%d.html";

    @Autowired
    ApplicationContext context;

    /**
     * Reading document simplified to keep the task in reasonable boundaries
     *
     * @param documentId the id of the document
     * @return document resource
     */
    @Override
    public Document getDocumentById(long documentId) {
        try {
            var document = context.getResource(String.format(DOC_LOCATION_TEMPLATE, documentId));
            return Jsoup.parse(document.getFile(), StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public long getElementOffset(long documentId, String elementId) {
        var doc = getDocumentById(documentId);
        var body = doc.body();
        var sourceDiv = doc.getElementById(elementId);
        return body.text().indexOf(sourceDiv.text());
    }


}
