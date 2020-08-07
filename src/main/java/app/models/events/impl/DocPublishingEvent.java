package app.models.events.impl;

import app.models.basic.BriefDocument;
import app.models.events.Event;
import javax.persistence.CascadeType;
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
    @JoinColumn(name = "report_id")
    private BriefDocument document;

    public BriefDocument getDocument() {
        return document;
    }

    public void setDocument(BriefDocument document) {
        this.document = document;
    }
}
