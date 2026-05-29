# Econometrics Analysis Notebooks

This folder contains the executable Python notebooks for the thesis replication workflow.

## Files

| Notebook | Role |
|---|---|
| `Amazon-BuyBox-Econometrics-Analysis-Notebook.ipynb` | Main replication notebook. It reads the raw Xiaomi Mi Smart Band 6 CSV, audits the extraction, constructs the seller-market panel, estimates the econometric specifications, and exports analytical tables. |
| `Amazon_BuyBox_Thesis_Figures.ipynb` | Figure-generation notebook. It rebuilds the thesis panel and exports the figures used in the manuscript. |

## Expected input location

The main input CSV is expected under:

```text
../Datasets/Sport e tempo libero - Smartwatch - Xiaomi Mi Smart Band 6.csv
```

The notebooks also contain fallback logic for local and Colab-style execution, but the repository-local path above is the canonical path for this archive.

## Execution order

Run `Amazon-BuyBox-Econometrics-Analysis-Notebook.ipynb` first. Run `Amazon_BuyBox_Thesis_Figures.ipynb` after the analytical panel and derived outputs have been generated or verified.

## Software stack

The notebooks use `numpy`, `pandas`, `statsmodels`, `scipy`, `patsy`, `scikit-learn`, `matplotlib`, `PyYAML`, `numba`, and `IPython`. The main notebook documents the full methodological inventory in its opening markdown cells.
