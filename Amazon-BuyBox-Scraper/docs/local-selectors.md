# Local Selector Configuration

The extractor is self-contained. Page selectors are stored in:

```text
src/main/resources/selectors.properties
```

Selectors use CSS syntax. Each property may contain multiple fallback selectors separated by `||`. The extractor evaluates them from left to right and uses the first matching non-empty value.

Example:

```properties
buybox.price=#corePrice_feature_div .a-offscreen||#apex_desktop .a-price .a-offscreen||.a-price .a-offscreen
```

Selectors can be replaced with project-specific extraction rules without changing the Java code. The included selectors are conservative defaults and require empirical validation on saved HTML pages before extracted variables are used in analysis.
