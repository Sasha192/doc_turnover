
package app.customtenant.events.impl;

import app.customtenant.events.Event;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "doc_event")
public class DocPublishingEvent extends Event {

    @Column(name = "doc_id")
    private Long documentId;

    public DocPublishingEvent() {
        setEventTypeEnum(EventType.DOC_PUB);
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }
}
