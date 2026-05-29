# Amazon Buy Box Scraper

This folder contains a self-contained Java/Spring scraper for collecting Amazon.it product-page and offer-list information. The scraper reads product URLs from a local CSV catalog, stores HTML and screenshots locally, parses saved pages with local CSS selectors, and writes offer-level CSV outputs to the local filesystem.

The pipeline does not depend on Google Sheets, Google Drive, or an external selector registry. Runtime paths and selector rules are configured through local files.

## Folder structure

```text
Amazon-BuyBox-Scraper/
├── data/
│   ├── input/products.csv
│   ├── html/
│   ├── screenshots/
│   └── output/
├── docs/local-selectors.md
├── drivers/README.md
├── src/main/java/org/research/amazonbuybox/
│   ├── catalog/
│   ├── config/
│   ├── controller/
│   ├── crawling/
│   ├── extraction/
│   ├── io/
│   ├── messaging/
│   └── model/
├── src/main/resources/selectors.properties
├── .env.example
├── docker-compose.yml
├── Dockerfile
└── pom.xml
```

## Data flow

1. `ProductScrapingController` receives a scraping-job request.
2. `ProductCatalogProcessor` reads products from `data/input/products.csv`.
3. `CrawlingProcessor` opens each product URL, expands the offer listing, and saves HTML and screenshots.
4. `ExtractionProcessor` parses saved HTML with local CSS selectors.
5. `ProductOfferCsvWriter` appends extracted offer variables to `data/output/product_offers.csv`.

Kafka is used internally to preserve a staged scraping architecture. It is part of the local execution environment and is not an external data dependency.

## Product catalog format

The default input file is:

```text
data/input/products.csv
```

Expected columns:

```csv
experiment_id,name,product_url
1,example_product,https://www.amazon.it/dp/EXAMPLE
```

The `name` field is used when generating local HTML and screenshot filenames.

## Selector configuration

Selectors are stored locally in:

```text
src/main/resources/selectors.properties
```

Each selector key may contain multiple fallback CSS selectors separated by `||`. The extractor evaluates selectors from left to right and uses the first matching non-empty value. The selector convention is documented in `docs/local-selectors.md`.

## Runtime configuration

The following variables can be set in the shell or in a local `.env` file:

```text
CHROME_BINARY_PATH=/absolute/path/to/chrome-or-chromium
CHROME_DRIVER_PATH=/absolute/path/to/chromedriver
PRODUCT_CATALOG_FILE=data/input/products.csv
OFFER_OUTPUT_FILE=data/output/product_offers.csv
HTML_OUTPUT_DIRECTORY=data/html
SCREENSHOT_OUTPUT_DIRECTORY=data/screenshots
```

`CHROME_BINARY_PATH` and `CHROME_DRIVER_PATH` are optional when the browser and driver are available on the system path. The Dockerfile installs Chromium and ChromeDriver inside the image.

## Local execution

Start Kafka with Docker Compose:

```bash
docker compose up kafka zookeeper kafka-ui
```

Run the application:

```bash
./mvnw spring-boot:run
```

Submit a scraping job using the default catalog file:

```bash
curl -X POST "http://localhost:8081/scraping-jobs"
```

Submit a scraping job using an explicit catalog path:

```bash
curl -X POST "http://localhost:8081/scraping-jobs?catalogPath=data/input/products.csv"
```

## Docker Compose execution

```bash
docker compose up --build
```

Then submit a job:

```bash
curl -X POST "http://localhost:8081/scraping-jobs"
```

Generated files are written under `data/`.

## Version-control policy

Generated HTML files, screenshots, output CSV files, local credentials, and browser-driver binaries are excluded from version control. Source code, configuration templates, and documentation are retained in the repository.

## Legal and ethical scope

Data collection from third-party websites must comply with applicable terms of service, copyright constraints, robots policies, and institutional research requirements. Scraping should be rate-limited and designed to avoid unnecessary load on third-party services.
