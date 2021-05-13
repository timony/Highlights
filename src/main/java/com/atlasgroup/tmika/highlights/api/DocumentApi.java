package com.atlasgroup.tmika.highlights.api;

import org.jsoup.nodes.Document;

import java.io.IOException;

public interface DocumentApi {

    /**
     * Gets the document resource by its id
     * @param documentId the id of the document to get
     * @return the document resource
     */
    Document getDocumentById(long documentId) throws IOException;
}
