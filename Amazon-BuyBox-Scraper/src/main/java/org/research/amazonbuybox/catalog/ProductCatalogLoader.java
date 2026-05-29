package org.research.amazonbuybox.catalog;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.research.amazonbuybox.model.Experiment;

public class ProductCatalogLoader {

    public LinkedList<Experiment> loadExperiments(Path catalogFile) throws IOException {
        if (!Files.exists(catalogFile)) {
            throw new IOException("Product catalog file not found: " + catalogFile.toAbsolutePath());
        }

        LinkedList<Experiment> experiments = new LinkedList<>();
        List<String> lines = Files.readAllLines(catalogFile, StandardCharsets.UTF_8);

        for (int rowIndex = 0; rowIndex < lines.size(); rowIndex++) {
            String line = lines.get(rowIndex).trim();
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }

            List<String> fields = parseCsvLine(line);
            if (isHeaderRow(fields)) {
                continue;
            }

            if (fields.size() < 3) {
                throw new IOException("Invalid product catalog row " + (rowIndex + 1)
                        + ": expected experiment_id,name,product_url");
            }

            Experiment experiment = new Experiment();
            experiment.setExperimentId(Integer.parseInt(fields.get(0).trim()));
            experiment.setName(fields.get(1).trim());
            experiment.setUrl(fields.get(2).trim());
            experiments.add(experiment);
        }

        return experiments;
    }

    private boolean isHeaderRow(List<String> fields) {
        if (fields.isEmpty()) {
            return false;
        }
        return "experiment_id".equalsIgnoreCase(fields.get(0).trim())
                || "id".equalsIgnoreCase(fields.get(0).trim());
    }

    private List<String> parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean quoted = false;

        for (int i = 0; i < line.length(); i++) {
            char character = line.charAt(i);
            if (character == '"') {
                if (quoted && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    quoted = !quoted;
                }
            } else if (character == ',' && !quoted) {
                fields.add(current.toString());
                current.setLength(0);
            } else {
                current.append(character);
            }
        }

        fields.add(current.toString());
        return fields;
    }
}
