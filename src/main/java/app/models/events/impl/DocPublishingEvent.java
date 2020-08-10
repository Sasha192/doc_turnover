package app.models.events.impl;

import app.models.basic.BriefDocument;
import app.models.events.Event;
import app.models.serialization.ExcludeForJsonEvent;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue(value = "doc_event")
public class DocPublishingEvent extends Event {

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "doc_id", insertable = false, updatable = false)
    @ExcludeForJsonEvent
    private BriefDocument document;

    @Column(name = "doc_id")
    private Long documentId;

    public BriefDocument getDocument() {
        return document;
    }

    public void setDocument(BriefDocument document) {
        this.document = document;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }
}
