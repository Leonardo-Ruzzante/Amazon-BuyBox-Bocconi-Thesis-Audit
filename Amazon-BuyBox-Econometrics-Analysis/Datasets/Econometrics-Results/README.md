# Econometrics Results

This folder contains the exported empirical results from the econometric notebook.

## Contents

The root of this folder contains CSV tables and YAML manifests for static specifications, dynamic specifications, robustness checks, inference diagnostics, common-support diagnostics, bounded-outcome checks, binary top-k prominence outputs, and synthesis tables.

Two subfolders contain thesis-aligned outputs:

```text
thesis_synthesis_aligned/
thesis_figures_aligned/
```

`thesis_synthesis_aligned/` contains aligned CSV tables used for table reconstruction.

`thesis_figures_aligned/` contains aligned figure exports in PNG and SVG formats.

## Manifests

`econometrics_manifest.yaml` and `model_specifications.yaml` document the exported result files and the headline model specifications. These files connect individual CSV outputs to the model families and diagnostics that generated them.

## Interpretation

The files in this folder are downstream analytical outputs. They should be interpreted together with the notebook code and the thesis text. The results record conditional associations in an observational seller-list panel; they do not establish a causal FBA adoption effect.
