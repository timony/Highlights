package com.atlasgroup.tmika.highlights;

import com.atlasgroup.tmika.highlights.api.DocumentApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class DocumentApiImpl implements DocumentApi {

    private static final String DOC_LOCATION_TEMPLATE = "classpath:documents/%d.html";

    @Autowired
    ApplicationContext context;

    /**
     * Reading document simplified to keep the task in reasonable boundaries
     * @param documentId the id of the document
     * @return document resource
     */
    @Override
    public Resource getDocumentById(long documentId) {
        return context.getResource(String.format(DOC_LOCATION_TEMPLATE, documentId));
    }
}
