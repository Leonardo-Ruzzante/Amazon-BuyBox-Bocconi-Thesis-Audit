package org.research.amazonbuybox.messaging;

import org.research.amazonbuybox.model.Experiment;

public class ExtractionRequest {

    private Experiment experiment;
    private String extractionTimestamp;

    public ExtractionRequest() {
    }

    public ExtractionRequest(Experiment experiment, String extractionTimestamp) {
        this.experiment = experiment;
        this.extractionTimestamp = extractionTimestamp;
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }

    public String getExtractionTimestamp() {
        return extractionTimestamp;
    }

    public void setExtractionTimestamp(String extractionTimestamp) {
        this.extractionTimestamp = extractionTimestamp;
    }
}
