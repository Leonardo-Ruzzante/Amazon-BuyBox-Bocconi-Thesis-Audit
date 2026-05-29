package org.research.amazonbuybox.messaging;

import org.research.amazonbuybox.model.Experiment;

public class CrawlingRequest {

    private Experiment experiment;

    public CrawlingRequest() {
    }

    public CrawlingRequest(Experiment experiment) {
        this.experiment = experiment;
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }
}
