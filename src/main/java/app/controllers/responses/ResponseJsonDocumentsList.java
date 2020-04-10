package app.controllers.responses;

import app.models.BriefDocument;

import java.util.ArrayList;
import java.util.List;

public class ResponseJsonDocumentsList {

    private List<BriefDocument> documents = new ArrayList<>();

    public ResponseJsonDocumentsList(final List<BriefDocument> documents) {
        this.documents = documents;
    }

    public List<BriefDocument> getDocuments() {
        return this.documents;
    }

    public void setDocuments(final List<BriefDocument> documents) {
        this.documents = documents;
    }
}
