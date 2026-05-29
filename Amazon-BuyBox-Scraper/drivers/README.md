# Browser Driver Directory

Browser-driver binaries are not committed to the repository.

For local execution, Chrome or Chromium and a compatible ChromeDriver must be installed on the machine running the scraper. If the browser and driver are not available on the system path, the following variables specify their locations:

```text
CHROME_BINARY_PATH=/absolute/path/to/chrome-or-chromium
CHROME_DRIVER_PATH=/absolute/path/to/chromedriver
```

The Dockerfile installs Chromium and ChromeDriver inside the image and configures the corresponding paths automatically.
