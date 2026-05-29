# Machine-Readable Results Aggregated

This folder contains a compact machine-readable export of the empirical record.

## Files

`machine_readable_results.jsonl` contains the raw input table, EDA tables, econometric result tables, and text-based result files as structured JSONL records.

`machine_readable_results_index.json` contains the table and document index, including row counts, column names, source paths, checksums, and notes on skipped files.

## Table reconstruction rule

1. Locate the `table_schema` record for the required `table_id`.
2. Read every `table_chunk` record with the same `table_id`.
3. Reconstruct the table by applying `column_names` to each row in the `data` list.

## Scope

Figures are not embedded in this folder. The corresponding tabular inputs, figure manifests, and structured text records are represented when available. Original figure files remain in `../Figures/` and aligned figure exports remain in `../Econometrics-Results/thesis_figures_aligned/`.
