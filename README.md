# Amazon Buy Box Thesis Replication Repository

This repository is the replication archive for a master's thesis on Amazon.it seller-list visibility, Fulfillment by Amazon (FBA) status, and ranking outcomes. It contains the raw scraped datasets, the self-contained scraper code, the econometric notebooks, the exported empirical outputs, and supporting reference material.

The archive is organized for auditability and reproducibility. Raw data, scraping code, econometric code, generated results, figures, and references are kept in separate folders so that each stage of the empirical workflow can be inspected independently.

## Repository structure

```text
Amazon-BuyBox-Bocconi-Thesis/
├── Amazon-BuyBox-Scraped-Data/
├── Amazon-BuyBox-Scraper/
├── Amazon-BuyBox-Econometrics-Analysis/
├── References/
├── README.md
├── .gitignore
└── .gitattributes
```

| Folder | Role |
|---|---|
| `Amazon-BuyBox-Scraped-Data/` | Raw scraped Amazon.it CSV files collected from four product-listing settings. |
| `Amazon-BuyBox-Scraper/` | Java/Spring scraping pipeline configured to run without external spreadsheet services. |
| `Amazon-BuyBox-Econometrics-Analysis/` | Python notebooks, raw Xiaomi input file, audit outputs, econometric results, figures, and machine-readable exports. |
| `References/` | Supporting PDF references used to document the upstream data and scraping lineage. |

## Empirical scope

The thesis analysis focuses on the Amazon.it seller list for the Xiaomi Mi Smart Band 6. The raw source file contains 9,424 observations and 32 columns across 62 timestamped markets. The analysis notebook constructs a final third-party seller-market panel with 5,107 observations, 119 third-party seller identities, and 62 retained markets.

The empirical object is seller-list rank within a product page. Lower values of `rank_pct` denote better within-market seller-list position. The econometric design estimates observational associations between FBA status and rank outcomes after conditioning on visible offer characteristics, market fixed effects, and turnover-related measures. The repository does not identify a causal FBA adoption effect, does not reconstruct Amazon's internal ranking algorithm, and does not measure Featured Offer allocation or consumer purchases.

## Main replication path

The primary replication workflow is contained in:

```text
Amazon-BuyBox-Econometrics-Analysis/Econometrics-Analysis/Amazon-BuyBox-Econometrics-Analysis-Notebook.ipynb
```

The notebook reads the Xiaomi Mi Smart Band 6 raw CSV from:

```text
Amazon-BuyBox-Econometrics-Analysis/Datasets/Sport e tempo libero - Smartwatch - Xiaomi Mi Smart Band 6.csv
```

It then performs the raw-data audit, constructs the seller-market panel, estimates the static and dynamic specifications, runs robustness and validity diagnostics, and exports the results under:

```text
Amazon-BuyBox-Econometrics-Analysis/Datasets/
```

The figure-generation workflow is contained in:

```text
Amazon-BuyBox-Econometrics-Analysis/Econometrics-Analysis/Amazon_BuyBox_Thesis_Figures.ipynb
```

## Data and output policy

Raw CSV files are retained as immutable research inputs. Derived CSV, YAML, JSONL, PNG, PDF, and SVG outputs are retained as an inspection record of the thesis results. Re-running the notebooks may regenerate these outputs, but the archived versions allow the empirical record to be checked without rerunning the full workflow.

The thesis manuscript itself is not included in this repository. The repository contains the materials needed to audit and reproduce the empirical pipeline.
