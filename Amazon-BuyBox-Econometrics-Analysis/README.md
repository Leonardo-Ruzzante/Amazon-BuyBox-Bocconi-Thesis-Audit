# Amazon BuyBox Econometrics Analysis

Replication record for the master thesis on FBA fulfillment status and seller-list ranking outcomes for the Xiaomi Mi Smart Band 6 on Amazon Italy.

## 1. Overview
This folder contains the empirical workflow underlying the thesis. The notebook Amazon-BuyBox-Econometrics-Analysis-Notebook.ipynb proceeds from the raw scraped CSV, audits the data-generating structure, constructs a seller-market panel of 5,107 third-party seller-market observations across 62 markets and 119 sellers, estimates the static and dynamic econometric specifications described in the thesis, evaluates robustness and dependence diagnostics, and writes the audited tables to a structured output tree.

The repository functions as the replication record for the numerical values underlying the thesis. The mapping tables in Section 5 and Section 6 of this document link every CSV produced by the notebook either to a thesis table label (when the file is the direct source of a table in the manuscript) or to the supplementary-diagnostic marker (when the file is retained for replication but is not directly cited in the body or appendices).

Numerical record of the headline coefficients reported by the notebook. Static specification S1 yields an FBA coefficient on rank_pct of -0.380436 with two-way clustered p-value below 1e-9 (main_results_market_fe_rankpct.csv). Static specification S4 yields a residual coefficient of -0.051038 with p-value 0.002207. Dynamic specification D1 yields an FBA-by-total-turnover coefficient of 0.002467 with p-value 0.002034 (dynamic_fba_premium_inference_rankpct.csv and seven cross-validating exports). Dynamic specifications D2, D3, and D4 yield 0.002710 (p = 0.002782), 0.002278 (p = 0.001278), and -0.000075 (p = 0.915343) respectively (dynamic_starting_rank_adjustment_diagnostics_rankpct.csv). These quantities match the values stated in Chapter 5 of the manuscript at the rounding precision reported there.

## 2. Repository structure
The repository structure mirrors the replication archive used for the empirical workflow.

> Amazon-BuyBox-Econometrics-Analysis/
>
> \|-- Econometrics-Analysis/
>
> \| \`-- Amazon-BuyBox-Econometrics-Analysis-Notebook.ipynb
>
> \|-- Datasets/
>
> \| \|-- Sport e tempo libero - Smartwatch - Xiaomi Mi Smart Band 6.csv
>
> \| \|-- EDA-Results/
>
> \| \|-- Econometrics-Results/
>
> \| \| \|-- thesis_synthesis_aligned/
>
> \| \| \`-- thesis_figures_aligned/
>
> \| \`-- Machine-Readable-Results-Aggregated/
>
> \`-- README.md

## 3. Execution environment
### 3.1. Google Colab (primary execution path)
The notebook can run end-to-end in Google Colab when the repository is available under the following Drive path.

> /content/drive/MyDrive/Amazon-BuyBox-Econometrics-Analysis/
>
> Datasets/
>
> Sport e tempo libero - Smartwatch - Xiaomi Mi Smart Band 6.csv

The first code cell mounts Drive and verifies the core scientific stack. The input-file resolver searches the Drive Datasets directory, the Colab working directory, the standard /content path, and the local filesystem in that priority order. The three output directories EDA-Results, Econometrics-Results, and Machine-Readable-Results-Aggregated are created on first execution if absent.

### 3.2. Local Jupyter (secondary execution path)
The notebook also runs in a local Jupyter environment. For local execution, the raw CSV can be placed in the same directory as the notebook or in one of the standard candidate paths. The first cell will skip the Drive mount with an informational message and continue with local file resolution.

### 3.3. Software environment
The notebook depends on the following Python packages, all of which are pre-installed in standard Colab runtimes:

- numpy, pandas, statsmodels, scipy, patsy, scikit-learn, matplotlib, PyYAML, numba, IPython.

The first markdown cell of the notebook records the full package and methodological inventory together with links to the official documentation of every model, diagnostic test, and inference procedure used downstream.

## 4. Output directories
### 4.1. Datasets/EDA-Results/
CSV tables produced by Part I of the notebook (Sections 1 to 17). Records the raw-data audit, the candidate sample definitions, the variable-provenance classification, the rank-summary and balance tables, the seller-presence and FBA-switching diagnostics, and the audit-check registry. Approximately 50 files.

### 4.2. Datasets/Econometrics-Results/
CSV tables produced by Part II of the notebook (Sections 1 to 45). Records the static and dynamic regression outputs, the inference and robustness diagnostics, the propensity-overlap and common-support diagnostics, the dynamic placebo and hardening checks, the binary top-10 prominence outputs, the synthesis tables, and the audit and export manifests. Approximately 160 files at the root of the folder.

Two YAML payloads are written alongside the CSV files. model_specifications.yaml lists the headline static and dynamic specifications with their control sets, inference choice, and validation status. export_manifest.yaml indexes every CSV with row counts, column names, and a short description of the analytical role of the table.

Two subdirectories carry the thesis-facing aligned exports:

- Datasets/Econometrics-Results/thesis_synthesis_aligned/ contains the 13 thesis\_...\_aligned.csv tables and the output_alignment_audit.csv file-presence check. These are the versions inserted into the thesis tables in the manuscript; the upstream non-aligned files at the root of Econometrics-Results are retained for traceability.

- Datasets/Econometrics-Results/thesis_figures_aligned/ contains six aligned figures in PNG and SVG (twelve files in total).

### 4.3. Datasets/Machine-Readable-Results-Aggregated/
Compact structured export produced by the final cell of the notebook. Aggregates the raw CSV, the complete EDA-Results set, and the complete Econometrics-Results set into a single JSONL file machine_readable_results.jsonl and a companion index machine_readable_results_index.json. Each table is stored as a sequence of schema, profile, and row-chunk records, so the original CSV contents can be reconstructed exactly while avoiding repeated CSV, preview, and profile files. The folder also contains its own README.md documenting the reconstruction rule.

## 5. EDA-Results mapping
Files in Datasets/EDA-Results/ mapped to their notebook section of origin and to their reference in the thesis. The notebook-section column refers to the section numbering inside the Amazon-BuyBox-Econometrics-Analysis-Notebook.ipynb, Part I. The thesis-reference column gives the LaTeX label of the table when the file is a direct source, or the supplementary-diagnostic marker when the file is not explicitly cited.

| **CSV file**                                   | **Notebook section** | **Thesis reference**                                                                                                        |
|------------------------------------------------|----------------------|-----------------------------------------------------------------------------------------------------------------------------|
| df_raw_typed_audit.csv                         | Part I, Section 4    | Underlying audited raw dataframe for the data-construction narrative of Chapter 3.                                          |
| df_new_offer_raw.csv                           | Part I, Section 10   | Candidate offer-level sample referenced by Appendix Table tab:appendix-candidate-samples.                                   |
| df_new_offer_strict_dedup.csv                  | Part I, Section 10   | Strict-dedup candidate referenced by Appendix Table tab:appendix-candidate-samples.                                         |
| df_new_offer_relaxed_dedup.csv                 | Part I, Section 10   | Relaxed-dedup candidate referenced by Appendix Table tab:appendix-candidate-samples.                                        |
| df_new_seller_best.csv                         | Part I, Section 10   | Seller-best candidate referenced by Appendix Table tab:appendix-candidate-samples.                                          |
| df_new_seller_best_thirdparty.csv              | Part I, Section 10   | Final seller-best third-party panel (5,107 rows). Used in Chapter 3 and Appendix Table tab:final-panel-facts.               |
| table_market_delivery_audit.csv                | Part I, Section 8    | Delivery-audit detail underlying tab:audit-headline.                                                                        |
| table_failed_delivery_markets.csv              | Part I, Section 8    | Inputs to the delivery-failure count in tab:audit-headline.                                                                 |
| table_robust_vs_stored_delivery.csv            | Part I, Section 8    | Supplementary diagnostic retained in the replication archive.      |
| table_failed_market_examples.csv               | Part I, Section 8    | Supplementary diagnostic retained in the replication archive.      |
| table_shipping_audit_summary.csv               | Part I, Section 9    | Shipping-audit summary feeding tab:audit-headline.                                                                          |
| table_paid_zero_anomaly_breakdown.csv          | Part I, Section 9    | Paid-zero breakdown referenced by Chapter 3 narrative and tab:audit-headline.                                               |
| table_paid_zero_explicit_examples.csv          | Part I, Section 9    | Supplementary diagnostic retained in the replication archive.      |
| table_paid_zero_contact_examples.csv           | Part I, Section 9    | Supplementary diagnostic retained in the replication archive.      |
| table_paid_zero_explicit_by_timestamp.csv      | Part I, Section 9    | Supplementary diagnostic retained in the replication archive.      |
| table_paid_zero_explicit_by_seller.csv         | Part I, Section 9    | Supplementary diagnostic retained in the replication archive.      |
| table_unit_of_analysis_summary.csv             | Part I, Section 10   | Underlies the unit-of-analysis discussion in Chapter 3.                                                                     |
| table_candidate_sample_summary.csv             | Part I, Section 10   | Source for Appendix Table tab:appendix-candidate-samples.                                                                   |
| table_repeated_seller_examples.csv             | Part I, Section 10   | Supplementary diagnostic retained in the replication archive.      |
| table_convenience_audit.csv                    | Part I, Section 11   | Supplementary diagnostic retained in the replication archive.      |
| table_convenience_takeaway.csv                 | Part I, Section 11   | Supplementary diagnostic retained in the replication archive.      |
| table_outcome_support.csv                      | Part I, Section 12   | Source for Appendix Table tab:appendix-topk-support.                                                                        |
| table_group_size.csv                           | Part I, Section 13   | Source for the panel-composition row of tab:final-panel-facts.                                                              |
| table_covariate_summary_by_fba.csv             | Part I, Section 13   | Source for Table tab:balance-fba.                                                                                           |
| table_covariate_smd.csv                        | Part I, Section 13   | Source for Table tab:balance-fba (standardized mean differences).                                                           |
| table_rank_summary_by_fba.csv                  | Part I, Section 13   | Source for Table tab:rank-summary-fba.                                                                                      |
| table_offer_repeat_headline.csv                | Part I, Section 14   | Supplementary diagnostic retained in the replication archive.      |
| table_market_fingerprint_summary.csv           | Part I, Section 14   | Supplementary diagnostic retained in the replication archive.      |
| table_seller_presence_summary.csv              | Part I, Section 14   | Underlies the no-within-seller-FBA-switching statement in Chapter 3 and the panel-composition row of tab:final-panel-facts. |
| table_seller_switch_headline.csv               | Part I, Section 14   | Underlies the within-seller-FBA-switching count cited in Chapter 3.                                                         |
| table_observed_both_status_sellers.csv         | Part I, Section 14   | Supplementary diagnostic retained in the replication archive.      |
| table_mixed_seller_summary.csv                 | Part I, Section 14   | Supplementary diagnostic retained in the replication archive.      |
| table_across_market_fba_switchers.csv          | Part I, Section 14   | Supplementary diagnostic retained in the replication archive.      |
| table_ml_split_results.csv                     | Part I, Section 14   | Supplementary diagnostic retained in the replication archive.      |
| table_ml_split_summary.csv                     | Part I, Section 14   | Supplementary diagnostic retained in the replication archive.      |
| table_variable_provenance.csv                  | Part I, Section 15   | Source for Appendix Table tab:appendix-variable-dictionary.                                                                 |
| table_review_usability_headline.csv            | Part I, Section 16   | Supplementary diagnostic retained in the replication archive.      |
| table_review_field_status.csv                  | Part I, Section 16   | Supplementary diagnostic retained in the replication archive.      |
| table_review_problem_examples.csv              | Part I, Section 16   | Supplementary diagnostic retained in the replication archive.      |
| table_contact_courier_summary.csv              | Part I, Section 16   | Underlies the 50 contact-courier rows referenced in Chapter 3 and tab:audit-headline.                                       |
| table_contact_courier_policy.csv               | Part I, Section 16   | Supplementary diagnostic retained in the replication archive.      |
| table_contact_courier_examples.csv             | Part I, Section 16   | Supplementary diagnostic retained in the replication archive.      |
| table_rank_variation_headline.csv              | Part I, Section 16   | Source for Table tab:market-rank-variation.                                                                                 |
| table_top_turnover_headline.csv                | Part I, Section 16   | Supplementary diagnostic retained in the replication archive.      |
| table_same_day_headline.csv                    | Part I, Section 16   | Supplementary diagnostic retained in the replication archive.      |
| table_same_day_pair_table.csv                  | Part I, Section 16   | Supplementary diagnostic retained in the replication archive.      |
| table_same_day_change_examples.csv             | Part I, Section 16   | Supplementary diagnostic retained in the replication archive.      |
| table_third_party_market_size_summary.csv      | Part I, Section 16   | Source for the market-size dispersion row of tab:final-panel-facts.                                                         |
| table_offer_threshold_support.csv              | Part I, Section 16   | Source for Appendix Table tab:appendix-topk-support.                                                                        |
| table_audit_checks.csv                         | Part I, Section 17   | Source for Appendix Table tab:audit-headline.                                                                               |
| manual_validation_delivery_shipping_sample.csv | Part I, Section 17   | Supplementary diagnostic retained in the replication archive.      |

## 6. Econometrics-Results mapping
Files in Datasets/Econometrics-Results/ mapped by analytical block.

### 6.1. Sample construction and audit
| **CSV file**                        | **Notebook section** | **Thesis reference**                                                                                                   |
|-------------------------------------|----------------------|------------------------------------------------------------------------------------------------------------------------|
| attrition_table.csv                 | Part II, Section 3   | Source for Table tab:sample-construction.                                                                              |
| price_order_audit_rankpct.csv       | Part II, Section 3a  | Source for Table tab:price-order-audit.                                                                                |
| price_order_audit_trace_rankpct.csv | Part II, Section 3a  | Source for Appendix Table tab:app-price-order-trace.                                                                   |
| turnover_construction_audit.csv     | Part II, Section 3b  | Source for Appendix Table tab:turnover-construction-audit.                                                             |
| identification_diagnostics.csv      | Part II, Section 4   | Supplementary diagnostic retained in the replication archive. |

### 6.2. Descriptive balance, price formation, and support
| **CSV file**                          | **Notebook section** | **Thesis reference**                                                                                                   |
|---------------------------------------|----------------------|------------------------------------------------------------------------------------------------------------------------|
| balance_table_by_fba.csv              | Part II, Section 6   | Source for Table tab:balance-fba.                                                                                      |
| balance_hypothesis_tests_by_fba.csv   | Part II, Section 6   | Hypothesis-test row of tab:balance-fba.                                                                                |
| rank_summary_by_fba.csv               | Part II, Section 6   | Source for Table tab:rank-summary-fba (econometric-panel version).                                                     |
| market_overlap_summary.csv            | Part II, Section 6   | Supplementary diagnostic retained in the replication archive. |
| delivery_support_rankpct.csv          | Part II, Section 7   | Supplementary diagnostic retained in the replication archive. |
| delivery_tail_sensitivity_rankpct.csv | Part II, Section 7   | Supplementary diagnostic retained in the replication archive. |
| shipping_support_by_fba.csv           | Part II, Section 7   | Supplementary diagnostic retained in the replication archive. |
| fast_delivery_imputation_audit.csv    | Part II, Section 7   | Source for Appendix Table tab:app-fast-delivery-imputation-audit.                                                      |
| fast_delivery_premium_benchmark.csv   | Part II, Section 7   | Supplementary diagnostic retained in the replication archive. |
| price_threshold_support.csv           | Part II, Section 7   | Supplementary diagnostic retained in the replication archive. |

### 6.3. Static headline results
| **CSV file**                                 | **Notebook section** | **Thesis reference**                                                                                                   |
|----------------------------------------------|----------------------|------------------------------------------------------------------------------------------------------------------------|
| main_results_market_fe_rankpct.csv           | Part II, Section 9   | Source for Table tab:ch5-static-models.                                                                                |
| attenuation_decomposition_rankpct.csv        | Part II, Section 9   | Source for Table tab:ch5-attenuation.                                                                                  |
| standardized_feature_association_rankpct.csv | Part II, Section 9   | Source for Table tab:ch5-standardized-features.                                                                        |
| price_measure_ols_comparison_rankpct.csv     | Part II, Section 9   | Supplementary diagnostic retained in the replication archive. |
| effect_translation_rankpct.csv               | Part II, Section 9   | Supplementary diagnostic retained in the replication archive. |

### 6.4. Static functional-form and sensitivity diagnostics
| **CSV file**                                                 | **Notebook section** | **Thesis reference**                                                                                                   |
|--------------------------------------------------------------|----------------------|------------------------------------------------------------------------------------------------------------------------|
| bounded_outcome_diagnostics_rankpct.csv                      | Part II, Section 11  | Source for Appendix Table tab:app-ch6-bounded-outcome.                                                                 |
| bounded_outcome_method_note_rankpct.csv                      | Part II, Section 11  | Supplementary diagnostic retained in the replication archive. |
| fractional_logit_rankpct.csv                                 | Part II, Section 11  | Source for the fractional-logit row of Table tab:ch5-static-validity.                                                  |
| fractional_logit_ape_rankpct.csv                             | Part II, Section 11  | Source for the fractional-logit APE column of Table tab:ch5-static-validity.                                           |
| functional_form_diagnostics_rankpct.csv                      | Part II, Section 11  | Source for Appendix Table tab:app-ch6-functional-form.                                                                 |
| functional_form_fragility_summary_rankpct.csv                | Part II, Section 11  | Underlies the functional-form-sensitivity rows of tab:ch6-validity-map.                                                |
| spline_functional_form_sensitivity_rankpct.csv               | Part II, Section 11  | Supplementary diagnostic retained in the replication archive. |
| logistics_value_adjusted_price_results_rankpct.csv           | Part II, Section 11  | Source for Appendix Table tab:app-logistics-value-adjusted-price-results.                                              |
| logistics_value_adjusted_price_summary_rankpct.csv           | Part II, Section 11  | Supplementary diagnostic retained in the replication archive. |
| logistics_value_adjusted_price_audit_rankpct.csv             | Part II, Section 11  | Supplementary diagnostic retained in the replication archive. |
| logistics_value_adjusted_price_design_rank_check_rankpct.csv | Part II, Section 11  | Supplementary diagnostic retained in the replication archive. |
| fast_delivery_premium_sensitivity_rankpct.csv                | Part II, Section 11  | Supplementary diagnostic retained in the replication archive. |
| market_equal_weighted_rankpct.csv                            | Part II, Section 11  | Equal-market-weighted row of Appendix Table tab:app-ch6-static-additional.                                             |
| mundlak_rankpct.csv                                          | Part II, Section 11  | Mundlak row of Appendix Table tab:app-ch6-static-additional.                                                           |
| extended_control_sensitivity_rankpct.csv                     | Part II, Section 11  | Source for Appendix Table tab:app-ch6-extended-controls.                                                               |

### 6.5. Static inference and robustness
| **CSV file**                                   | **Notebook section** | **Thesis reference**                                                                                                   |
|------------------------------------------------|----------------------|------------------------------------------------------------------------------------------------------------------------|
| cluster_sensitivity_rankpct.csv                | Part II, Section 12  | Clustering rows of Appendix Table tab:app-ch6-finite-cluster-inference.                                                |
| cr2_cluster_correction_rankpct.csv             | Part II, Section 12  | CR2 row of Appendix Table tab:app-ch6-finite-cluster-inference.                                                        |
| wild_cluster_bootstrap_rankpct.csv             | Part II, Section 12  | Wild cluster bootstrap row of Appendix Table tab:app-ch6-finite-cluster-inference.                                     |
| wild_cluster_bootstrap_sensitivity_rankpct.csv | Part II, Section 12  | Supplementary diagnostic retained in the replication archive. |
| sample_sensitivity_rankpct.csv                 | Part II, Section 12  | Source for Appendix Table tab:app-ch6-sample-sensitivity.                                                              |
| temporal_stability_rankpct.csv                 | Part II, Section 12  | Supplementary diagnostic retained in the replication archive. |
| influence_trimmed_rankpct.csv                  | Part II, Section 12  | Source for Appendix Table tab:app-ch6-influence.                                                                       |
| leave_one_cluster_influence_rankpct.csv        | Part II, Section 12  | Cluster-influence row of Appendix Table tab:app-ch6-influence.                                                         |
| leave_one_cluster_influence_detail_rankpct.csv | Part II, Section 12  | Supplementary diagnostic retained in the replication archive. |
| observation_influence_summary_rankpct.csv      | Part II, Section 12  | Observation-influence row of Appendix Table tab:app-ch6-influence.                                                     |
| top_observation_influence_rankpct.csv          | Part II, Section 12  | Supplementary diagnostic retained in the replication archive. |
| omitted_variable_sensitivity_rankpct.csv       | Part II, Section 12  | Source for Appendix Table tab:app-ch6-omitted-variable-sensitivity.                                                    |
| static_residual_lag_diagnostic_rankpct.csv     | Part II, Section 12  | Supplementary diagnostic retained in the replication archive. |

### 6.6. Propensity overlap and common-support diagnostics
| **CSV file**                                       | **Notebook section** | **Thesis reference**                                                                                                   |
|----------------------------------------------------|----------------------|------------------------------------------------------------------------------------------------------------------------|
| propensity_overlap_summary_rankpct.csv             | Part II, Section 13  | Source for Appendix Table tab:app-ch6-propensity-overlap.                                                              |
| propensity_overlap_quantiles_rankpct.csv           | Part II, Section 13  | Quantile detail of tab:app-ch6-propensity-overlap.                                                                     |
| propensity_overlap_interpretation_rankpct.csv      | Part II, Section 13  | Supplementary diagnostic retained in the replication archive. |
| overlap_balance_summary_rankpct.csv                | Part II, Section 13  | Supplementary diagnostic retained in the replication archive. |
| overlap_balance_tests_rankpct.csv                  | Part II, Section 13  | Supplementary diagnostic retained in the replication archive. |
| overlap_design_decision_rankpct.csv                | Part II, Section 13  | Supplementary diagnostic retained in the replication archive. |
| overlap_trimmed_estimates_rankpct.csv              | Part II, Section 13  | Common-support trim row of Table tab:ch5-static-validity.                                                              |
| common_support_balance_total_price_rankpct.csv     | Part II, Section 14  | Source for Appendix Table tab:app-ch6-common-support-balance.                                                          |
| overlap_design_status_summary_rankpct.csv          | Part II, Section 15  | Underlies the credible-overlap row of tab:ch6-validity-map.                                                            |
| common_support_nonfba_survivor_profile_rankpct.csv | Part II, Section 16  | Non-FBA survivor profile cited in Chapter 6 narrative on common support.                                               |
| common_support_residual_gap_rankpct.csv            | Part II, Section 17  | Source for Appendix Table tab:app-ch6-common-support-residual-gap.                                                     |
| feature_correlation_market_demeaned_rankpct.csv    | Part II, Section 34  | Supplementary diagnostic retained in the replication archive. |
| top_feature_correlation_pairs_rankpct.csv          | Part II, Section 34  | Supplementary diagnostic retained in the replication archive. |
| feature_vif_market_demeaned_rankpct.csv            | Part II, Section 34  | Source for Appendix Table tab:app-ch6-vif.                                                                             |
| feature_block_wald_tests_rankpct.csv               | Part II, Section 34  | Supplementary diagnostic retained in the replication archive. |

### 6.7. Literature mapping
| **CSV file**                    | **Notebook section** | **Thesis reference**                                                      |
|---------------------------------|----------------------|---------------------------------------------------------------------------|
| chen_tsai_benchmark_mapping.csv | Part II, Section 18  | Source for Appendix Table tab:literature-positioning (Chen and Tsai row). |

### 6.8. Dynamic headline results
| **CSV file**                                                     | **Notebook section** | **Thesis reference**                                                                                                   |
|------------------------------------------------------------------|----------------------|------------------------------------------------------------------------------------------------------------------------|
| dynamic_vacancy_design_summary_rankpct.csv                       | Part II, Section 19  | Source for Appendix Table tab:app-ch5-dynamic-support.                                                                 |
| dynamic_vacancy_descriptive_rankpct.csv                          | Part II, Section 19  | Supplementary diagnostic retained in the replication archive. |
| dynamic_vacancy_identification_logic_rankpct.csv                 | Part II, Section 19  | Supplementary diagnostic retained in the replication archive. |
| dynamic_vacancy_transition_summary_rankpct.csv                   | Part II, Section 19  | Supplementary diagnostic retained in the replication archive. |
| dynamic_vacancy_transition_diagnostics_rankpct.csv               | Part II, Section 19  | Supplementary diagnostic retained in the replication archive. |
| dynamic_vacancy_interpretation_rankpct.csv                       | Part II, Section 19  | Supplementary diagnostic retained in the replication archive. |
| dynamic_consecutive_transition_variation_rankpct.csv             | Part II, Section 19  | Supplementary diagnostic retained in the replication archive. |
| dynamic_within_seller_sample_summary_rankpct.csv                 | Part II, Section 19  | Supplementary diagnostic retained in the replication archive. |
| dynamic_within_seller_variation_rankpct.csv                      | Part II, Section 19  | Supplementary diagnostic retained in the replication archive. |
| dynamic_vacancy_exposure_distribution_rankpct.csv                | Part II, Section 19  | Source for Appendix Table tab:app-ch5-turnover-distribution.                                                           |
| dynamic_above_below_decomposition_rankpct.csv                    | Part II, Section 22  | Source for Table tab:ch5-dynamic-secondary (decomposition rows).                                                       |
| dynamic_above_below_decomposition_main_rankpct.csv               | Part II, Section 22  | Supplementary diagnostic retained in the replication archive. |
| dynamic_above_below_joint_diagnostic_rankpct.csv                 | Part II, Section 22  | Supplementary diagnostic retained in the replication archive. |
| dynamic_directional_decomposition_decision_rankpct.csv           | Part II, Section 22  | Supplementary diagnostic retained in the replication archive. |
| dynamic_directional_decomposition_decision_reference_rankpct.csv | Part II, Section 22  | Supplementary diagnostic retained in the replication archive. |
| dynamic_directional_reference_cr2_reference_rankpct.csv          | Part II, Section 21  | Supplementary diagnostic retained in the replication archive. |
| dynamic_directional_reference_wcb_reference_rankpct.csv          | Part II, Section 21  | Supplementary diagnostic retained in the replication archive. |
| dynamic_dropouts_below_decomposition_reference_rankpct.csv       | Part II, Section 22  | Supplementary diagnostic retained in the replication archive. |
| dynamic_vacancy_models_rankpct.csv                               | Part II, Section 23  | Source for Table tab:ch5-dynamic-models (D1, D2, D3, D4 rows).                                                         |
| dynamic_starting_rank_adjustment_diagnostics_rankpct.csv         | Part II, Section 23  | Source for the D2 and D3 starting-rank rows of Table tab:ch5-dynamic-models.                                           |
| dynamic_starting_rank_quartile_support_rankpct.csv               | Part II, Section 23  | Supplementary diagnostic retained in the replication archive. |
| dynamic_vacancy_rank_tier_models_rankpct.csv                     | Part II, Section 23  | Source for Appendix Table tab:app-ch6-ranktier-stress (D4 stress test).                                                |
| dynamic_vacancy_rank_tier_summary_rankpct.csv                    | Part II, Section 23  | Supplementary diagnostic retained in the replication archive. |
| dynamic_decision_quantities_rankpct.csv                          | Part II, Section 23  | Supplementary diagnostic retained in the replication archive. |
| dynamic_decision_scorecard_rankpct.csv                           | Part II, Section 23  | Underlies the dynamic rows of Table tab:ch6-validity-map.                                                              |
| dynamic_fba_premium_summary_rankpct.csv                          | Part II, Section 23  | Supplementary diagnostic retained in the replication archive. |
| dynamic_fba_premium_inference_rankpct.csv                        | Part II, Section 23  | Supplementary diagnostic retained in the replication archive. |
| dynamic_vacancy_effect_translation_rankpct.csv                   | Part II, Section 23  | Supplementary diagnostic retained in the replication archive. |

### 6.9. Dynamic finite-cluster inference
| **CSV file**                                | **Notebook section** | **Thesis reference**                                                                                                   |
|---------------------------------------------|----------------------|------------------------------------------------------------------------------------------------------------------------|
| dynamic_cr2_inference_rankpct.csv           | Part II, Section 21  | CR2 row of Appendix Table tab:app-ch6-finite-cluster-inference (dynamic block).                                        |
| dynamic_wild_cluster_bootstrap_rankpct.csv  | Part II, Section 21  | Wild cluster bootstrap row of Appendix Table tab:app-ch6-finite-cluster-inference (dynamic block).                     |
| dynamic_conservative_inference_rankpct.csv  | Part II, Section 21  | Supplementary diagnostic retained in the replication archive. |
| dynamic_residual_lag_diagnostic_rankpct.csv | Part II, Section 21  | Supplementary diagnostic retained in the replication archive. |

### 6.10. Dynamic placebo checks
| **CSV file**                                     | **Notebook section** | **Thesis reference**                                                                                                   |
|--------------------------------------------------|----------------------|------------------------------------------------------------------------------------------------------------------------|
| dynamic_turnover_placebos_rankpct.csv            | Part II, Section 24  | Source for Appendix Table tab:app-ch6-dynamic-placebos.                                                                |
| dynamic_turnover_placebo_decision_rankpct.csv    | Part II, Section 24  | Supplementary diagnostic retained in the replication archive. |
| dynamic_permutation_placebo_turnover_rankpct.csv | Part II, Section 24  | Permutation row of Appendix Table tab:app-ch6-dynamic-placebos.                                                        |

### 6.11. Dynamic hardening and mechanism diagnostics
| **CSV file**                                      | **Notebook section** | **Thesis reference**                                                                                                   |
|---------------------------------------------------|----------------------|------------------------------------------------------------------------------------------------------------------------|
| dynamic_distance_banded_turnover_rankpct.csv      | Part II, Section 25  | Source for Appendix Table tab:app-ch6-dynamic-hardening (distance-banded rows).                                        |
| dynamic_clean_turnover_restrictions_rankpct.csv   | Part II, Section 25  | Clean-transition rows of Appendix Table tab:app-ch6-dynamic-hardening.                                                 |
| dynamic_partial_identification_bounds_rankpct.csv | Part II, Section 25  | Supplementary diagnostic retained in the replication archive. |
| dynamic_hardening_summary_rankpct.csv             | Part II, Section 25  | Supplementary diagnostic retained in the replication archive. |
| dynamic_mechanism_validation_summary_rankpct.csv  | Part II, Section 25  | Supplementary diagnostic retained in the replication archive. |
| dynamic_churn_decomposition_rankpct.csv           | Part II, Section 25  | Supplementary diagnostic retained in the replication archive. |
| dynamic_churn_decomposition_summary_rankpct.csv   | Part II, Section 25  | Supplementary diagnostic retained in the replication archive. |
| dynamic_churn_direction_correlation_rankpct.csv   | Part II, Section 25  | Supplementary diagnostic retained in the replication archive. |
| dynamic_rank_tier_stress_diagnostics_rankpct.csv  | Part II, Section 25  | Source for Appendix Table tab:app-ch6-ranktier-stress (stress diagnostics rows).                                       |

### 6.12. Dropout process, return patterns, and stockout consistency
| **CSV file**                                              | **Notebook section** | **Thesis reference**                                                                                                   |
|-----------------------------------------------------------|----------------------|------------------------------------------------------------------------------------------------------------------------|
| dynamic_dropout_process_rankpct.csv                       | Part II, Section 26  | Supplementary diagnostic retained in the replication archive. |
| dynamic_dropout_process_summary_rankpct.csv               | Part II, Section 26  | Supplementary diagnostic retained in the replication archive. |
| dynamic_pre_disappearance_smoothness_rankpct.csv          | Part II, Section 26  | Pre-disappearance smoothness row of Appendix Table tab:app-ch6-stockout-consistency.                                   |
| dynamic_pre_disappearance_smoothness_decision_rankpct.csv | Part II, Section 26  | Supplementary diagnostic retained in the replication archive. |
| dynamic_return_pattern_rankpct.csv                        | Part II, Section 27  | Supplementary diagnostic retained in the replication archive. |
| dynamic_return_pattern_summary_rankpct.csv                | Part II, Section 27  | Supplementary diagnostic retained in the replication archive. |
| dynamic_dropout_event_table_rankpct.csv                   | Part II, Section 28  | Supplementary diagnostic retained in the replication archive. |
| dynamic_dropout_rank_quartile_summary_rankpct.csv         | Part II, Section 28  | Supplementary diagnostic retained in the replication archive. |
| dynamic_stockout_consistency_definitions_rankpct.csv      | Part II, Section 28  | Supplementary diagnostic retained in the replication archive. |
| dynamic_stockout_consistency_decision_rankpct.csv         | Part II, Section 28  | Supplementary diagnostic retained in the replication archive. |
| dynamic_stockout_consistency_summary_rankpct.csv          | Part II, Section 28  | Supplementary diagnostic retained in the replication archive. |
| dynamic_stockout_consistent_turnover_rankpct.csv          | Part II, Section 28  | Stockout-consistent rerun row of Appendix Table tab:app-ch6-stockout-consistency.                                      |
| dynamic_dropout_composition_rankpct.csv                   | Part II, Section 29  | Supplementary diagnostic retained in the replication archive. |
| dynamic_continuous_heterogeneity_rankpct.csv              | Part II, Section 30  | Supplementary diagnostic retained in the replication archive. |
| dynamic_continuous_marginal_effects_rankpct.csv           | Part II, Section 30  | Supplementary diagnostic retained in the replication archive. |

### 6.13. Top-10 entry and economic-salience screen
| **CSV file**                                             | **Notebook section** | **Thesis reference**                                                                                                   |
|----------------------------------------------------------|----------------------|------------------------------------------------------------------------------------------------------------------------|
| dynamic_economic_salience_rankpct.csv                    | Part II, Section 31  | Supplementary diagnostic retained in the replication archive. |
| binary_top10_prominence_models_rankpct.csv               | Part II, Section 32  | Source for Appendix Table tab:app-ch5-top10 (LPM specification rows).                                                  |
| binary_top10_prominence_decision_rankpct.csv             | Part II, Section 32  | Supplementary diagnostic retained in the replication archive. |
| binary_top10_prominence_diagnostics_rankpct.csv          | Part II, Section 32  | Supplementary diagnostic retained in the replication archive. |
| binary_top10_prominence_inference_rankpct.csv            | Part II, Section 32  | Supplementary diagnostic retained in the replication archive. |
| binary_top10_prominence_permutation_rankpct.csv          | Part II, Section 32  | Supplementary diagnostic retained in the replication archive. |
| binary_top10_prominence_placebos_rankpct.csv             | Part II, Section 32  | Supplementary diagnostic retained in the replication archive. |
| binary_top10_prominence_stockout_consistency_rankpct.csv | Part II, Section 32  | Supplementary diagnostic retained in the replication archive. |
| binary_top10_prominence_threshold_family_rankpct.csv     | Part II, Section 32  | Supplementary diagnostic retained in the replication archive. |
| binary_top10_prominence_influence_summary_rankpct.csv    | Part II, Section 32  | Supplementary diagnostic retained in the replication archive. |
| binary_top10_prominence_leave_one_rankpct.csv            | Part II, Section 32  | Supplementary diagnostic retained in the replication archive. |
| binary_top10_fitted_probability_audit_rankpct.csv        | Part II, Section 32  | Supplementary diagnostic retained in the replication archive. |

### 6.14. Power and minimum-detectable-effect audit
| **CSV file**                           | **Notebook section** | **Thesis reference**                                                                                                   |
|----------------------------------------|----------------------|------------------------------------------------------------------------------------------------------------------------|
| dynamic_mde_summary_rankpct.csv        | Part II, Section 33  | Source for Appendix Table tab:app-ch6-dynamic-mde.                                                                     |
| dynamic_mde_subgroup_audit_rankpct.csv | Part II, Section 33  | Subgroup MDE rows of Appendix Table tab:app-ch6-dynamic-mde.                                                           |
| dynamic_power_audit_rankpct.csv        | Part II, Section 33  | Supplementary diagnostic retained in the replication archive. |
| dynamic_support_mde_audit_rankpct.csv  | Part II, Section 33  | Supplementary diagnostic retained in the replication archive. |

### 6.15. Interaction analysis
| **CSV file**                           | **Notebook section** | **Thesis reference**                                                                                                   |
|----------------------------------------|----------------------|------------------------------------------------------------------------------------------------------------------------|
| interaction_margins_rankpct.csv        | Part II, Section 35  | Source for Table tab:ch5-interaction-margins.                                                                          |
| interaction_terms_rankpct.csv          | Part II, Section 35  | Source for Appendix Table tab:app-ch5-interaction-terms.                                                               |
| interaction_interpretation_rankpct.csv | Part II, Section 35  | Supplementary diagnostic retained in the replication archive. |
| interaction_support_rankpct.csv        | Part II, Section 35  | Supplementary diagnostic retained in the replication archive. |
| excluded_interactions_rankpct.csv      | Part II, Section 35  | Supplementary diagnostic retained in the replication archive. |

### 6.16. Synthesis, audit registry, and export manifests
| **CSV file**                                       | **Notebook section** | **Thesis reference**                                                                                                   |
|----------------------------------------------------|----------------------|------------------------------------------------------------------------------------------------------------------------|
| evidence_to_claim_summary.csv                      | Part II, Section 36  | Underlies the validity-boundary discussion of Chapter 6 and the synthesis paragraph closing Chapter 5.                 |
| interpretation_table.csv                           | Part II, Section 37  | Supplementary diagnostic retained in the replication archive. |
| spine_table_manifest.csv                           | Part II, Section 39  | Supplementary diagnostic retained in the replication archive. |
| econometrics_audit_checks.csv                      | Part II, Section 40  | Internal audit registry. Underlies tab:audit-headline (econometric audit rows).                                        |
| econometrics_audit_reconciliation.csv              | Part II, Section 40  | Supplementary diagnostic retained in the replication archive. |
| audit_consistency_checks.csv                       | Part II, Section 40  | Supplementary diagnostic retained in the replication archive. |
| identification_diagnostics.csv                     | Part II, Section 40  | Supplementary diagnostic retained in the replication archive. |
| final_sample_preview.csv                           | Part II, Section 40  | Supplementary diagnostic retained in the replication archive. |
| missingness_summary.csv                            | Part II, Section 40  | Supplementary diagnostic retained in the replication archive. |
| numerical_pathology_summary.csv                    | Part II, Section 40  | Supplementary diagnostic retained in the replication archive. |
| static_wooldridge_style_residual_test_rankpct.csv  | Part II, Section 41  | Static Wooldridge row of Appendix Table tab:app-ch6-finite-cluster-inference.                                          |
| dynamic_wooldridge_style_residual_test_rankpct.csv | Part II, Section 41  | Dynamic Wooldridge row of Appendix Table tab:app-ch6-finite-cluster-inference.                                         |
| temporal_persistence_by_seller_rankpct.csv         | Part II, Section 41  | Supplementary diagnostic retained in the replication archive. |
| serial_dependence_claim_implications_rankpct.csv   | Part II, Section 41  | Supplementary diagnostic retained in the replication archive. |
| serial_dependence_method_note_rankpct.csv          | Part II, Section 41  | Supplementary diagnostic retained in the replication archive. |
| econometrics_table_manifest.csv                    | Part II, Section 43  | Supplementary diagnostic retained in the replication archive. |
| output_alignment_audit.csv                         | Part II, Section 44  | Supplementary diagnostic retained in the replication archive. |
| synthesis_manifest.csv                             | Part II, Section 44  | Supplementary diagnostic retained in the replication archive. |
| figure_manifest.csv                                | Part II, Section 44  | Supplementary diagnostic retained in the replication archive. |

### 6.17. Thesis-aligned synthesis tables (subfolder)
Files produced by Section 44 with the thesis\_...\_aligned.csv prefix. They reside in the subdirectory Datasets/Econometrics-Results/thesis_synthesis_aligned/. These are the versions actually inserted into the thesis tables; the upstream non-aligned files in the parent Econometrics-Results/ folder are retained for traceability. The subdirectory also contains output_alignment_audit.csv, the file-presence check that verifies every required upstream input is reachable when the aligned exports are regenerated.

| **CSV file**                                                    | **Notebook section** | **Thesis reference**                                                                                                   |
|-----------------------------------------------------------------|----------------------|------------------------------------------------------------------------------------------------------------------------|
| thesis_master_evidence_table_aligned.csv                        | Part II, Section 44  | Thesis-facing aligned master table. Underlies Table tab:ch6-validity-map (synthesis row).                              |
| thesis_temporal_stability_aligned.csv                           | Part II, Section 44  | Supplementary diagnostic retained in the replication archive. |
| thesis_consistency_warning_summary_aligned.csv                  | Part II, Section 44  | Supplementary diagnostic retained in the replication archive. |
| thesis_dropout_return_pattern_aligned.csv                       | Part II, Section 44  | Supplementary diagnostic retained in the replication archive. |
| thesis_functional_form_fragility_aligned.csv                    | Part II, Section 44  | Supplementary diagnostic retained in the replication archive. |
| thesis_logistics_value_adjusted_price_specification_aligned.csv | Part II, Section 44  | Thesis-aligned version of Appendix Table tab:app-logistics-value-adjusted-price-results.                               |
| thesis_oster_sensitivity_aligned.csv                            | Part II, Section 44  | Oster (2019) sensitivity row of Appendix Table tab:app-ch6-omitted-variable-sensitivity.                               |
| thesis_oster_sensitivity_grid.csv                               | Part II, Section 44  | Supplementary diagnostic retained in the replication archive. |
| thesis_oster_sensitivity_grid_aligned.csv                       | Part II, Section 44  | Supplementary diagnostic retained in the replication archive. |
| thesis_pre_disappearance_abnormal_smoothness_aligned.csv        | Part II, Section 44  | Supplementary diagnostic retained in the replication archive. |
| thesis_price_interaction_key_result_aligned.csv                 | Part II, Section 44  | Thesis-aligned price-interaction row of Table tab:ch5-interaction-margins.                                             |
| thesis_price_interaction_margins_aligned.csv                    | Part II, Section 44  | Supplementary diagnostic retained in the replication archive. |
| thesis_top10_stockout_zero_se_audit_aligned.csv                 | Part II, Section 44  | Supplementary diagnostic retained in the replication archive. |

### 6.18. Thesis-aligned figures (subfolder)
Section 44 also writes six figures in PNG and SVG to the subdirectory Datasets/Econometrics-Results/thesis_figures_aligned/. The figures visualize the headline analytical claims and are referenced in the thesis prose as supporting visual evidence.

| **CSV file**                                         | **Notebook section** | **Thesis reference**                                                                                                                             |
|------------------------------------------------------|----------------------|--------------------------------------------------------------------------------------------------------------------------------------------------|
| figure_01_static_fba_attenuation_aligned.png/.svg    | Part II, Section 44  | Visualization of the attenuation pattern across S1-S4 (Table tab:ch5-attenuation).                                                               |
| figure_02_dynamic_premium_ranktier_aligned.png/.svg  | Part II, Section 44  | Visualization of the dynamic FBA premium across D1-D4 (Table tab:ch5-dynamic-models and Appendix Table tab:app-ch6-ranktier-stress).             |
| figure_03_topk_threshold_family_aligned.png/.svg     | Part II, Section 44  | Visualization of the top-k threshold family for the binary-prominence estimand (Appendix Table tab:app-ch5-top10).                               |
| figure_04_distance_banded_turnover_aligned.png/.svg  | Part II, Section 44  | Visualization of the distance-banded turnover restrictions (Appendix Table tab:app-ch6-dynamic-hardening).                                       |
| figure_05_overlap_smd_summary_aligned.png/.svg       | Part II, Section 44  | Visualization of the standardized mean differences before and after common-support trimming (Appendix Table tab:app-ch6-common-support-balance). |
| figure_06_price_interaction_margins_aligned.png/.svg | Part II, Section 44  | Visualization of the static FBA association at selected product-price values (Table tab:ch5-interaction-margins).                                |

## 7. Machine-Readable-Results-Aggregated
The folder is built by the final cell of the notebook (Section 45). It aggregates the raw source, the EDA-Results contents, and the Econometrics-Results contents into a single compact JSONL file with companion index. The folder is intended for downstream programmatic consumption (for example, by a checker that needs every analytical artefact in a single stream). It does not replace the per-table CSV files; the upstream CSV layout in EDA-Results and Econometrics-Results remains the canonical reference for the thesis.

Reconstruction rule for one table: locate its table_schema record by source path, locate every table_chunk record sharing the same source path, sort the chunks by their record_index, and concatenate the row arrays in chunk order. The result is bit-identical to the original CSV.

## 8. Wording convention for outputs not explicitly cited
Throughout this document, the marker used in the thesis-reference column for files not directly cited in the manuscript is the following phrase, verbatim:

> *Supplementary diagnostic retained in the replication archive.*

These files are produced for two reasons. First, they are intermediate artefacts that downstream sections consume (for example, the support diagnostics consumed by the validity-boundary map). Second, they document analytical decisions that the thesis discusses in prose without showing a numerical table (for example, the dropout-return composition and the stockout-consistency restriction). Their availability supports independent replication of every quantity cited in the manuscript.

## 9. Citation
The thesis is the canonical citation for the empirical analysis. The repository is the replication record. A possible bibliographic entry for the replication record is:

> *Amazon-BuyBox-Econometrics-Analysis: replication notebook for the master thesis on FBA fulfillment status and seller-list ranking outcomes for the Xiaomi Mi Smart Band 6 on Amazon Italy. Universita Commerciale Luigi Bocconi, master thesis replication archive, 2026.*
