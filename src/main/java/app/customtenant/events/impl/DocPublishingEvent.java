
package app.customtenant.events.impl;

import app.customtenant.events.Event;
import app.customtenant.models.basic.BriefDocument;
import app.customtenant.models.serialization.ExcludeForJsonEvent;
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
            cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "doc_id", insertable = false, updatable = false)
    @ExcludeForJsonEvent
    private BriefDocument document;

    @Column(name = "doc_id")
    private Long documentId;

    public DocPublishingEvent() {
        setEventTypeEnum(EventType.DOC_PUB);
    }

    public BriefDocument getDocument() {
        return document;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }
}
