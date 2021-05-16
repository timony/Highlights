package com.atlasgroup.tmika.highlights.renderer;

import com.atlasgroup.tmika.highlights.domain.Highlight;
import org.dom4j.DocumentHelper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

public class HighlightedHtmlRenderer implements HtmlRenderer {

    private Highlight highlight;
    private Document template;

    public HighlightedHtmlRenderer() {
    }

    @Override
    public String render() {
        org.dom4j.Document doc = DocumentHelper.createDocument();
        org.dom4j.Element html = doc.addElement("html");
        org.dom4j.Element body = html.addElement("body");
        renderNode(body, template.body());
        return doc.asXML();
    }

    private void renderNode(org.dom4j.Element parentNode, Element fromNode) {
        fromNode.attributes().asList().forEach(attribute -> parentNode.addAttribute(attribute.getKey(), attribute.getValue()));
        for (Node childNode : fromNode.childNodes()) {
            if (childNode == null) {
                continue;
            } else if (childNode instanceof TextNode) {
                renderTextNode(parentNode, (TextNode) childNode);
            } else if (childNode instanceof Element) {
                final org.dom4j.Element subParent = parentNode.addElement(childNode.nodeName());
                renderNode(subParent, (Element) childNode);
            }
        }
    }

    private void renderTextNode(org.dom4j.Element parentNode, TextNode fromNode) {
        parentNode.addText(fromNode.text().trim());
    }

    public HighlightedHtmlRenderer withHighlightDefinition(Highlight highlight) {
        this.highlight = highlight;
        return this;
    }

    public HighlightedHtmlRenderer withDocumentTemplate(Document template) {
        this.template = template;
        return this;
    }
}
