FROM maven:3.8.7-eclipse-temurin-11

WORKDIR /app

ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update \
    && apt-get install -y --no-install-recommends \
        chromium \
        chromium-driver \
        ca-certificates \
        fonts-liberation \
        xdg-utils \
    && rm -rf /var/lib/apt/lists/*

ENV CHROME_BINARY_PATH=/usr/bin/chromium
ENV CHROME_DRIVER_PATH=/usr/bin/chromedriver

COPY . /app
RUN chmod +x /app/mvnw \
    && mkdir -p /app/data/input /app/data/html /app/data/screenshots /app/data/output

EXPOSE 8081
ENTRYPOINT ["./mvnw", "spring-boot:run"]
