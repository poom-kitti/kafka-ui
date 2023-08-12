FROM azul/zulu-openjdk-alpine@sha256:a36679ac0d28cb835e2a8c00e1e0d95509c6c51c5081c7782b85edb1f37a771a

RUN apk add --no-cache \
    # snappy codec
    gcompat \
    # configuring timezones
    tzdata \
    # bash
    bash

RUN mkdir -p /app/kafka-ui

COPY example/config /app/kafka-ui/config
COPY example/entrypoint.sh /app/kafka-ui/entrypoint.sh
COPY kafka-ui-api/target/kafka-ui-api-0.0.1-SNAPSHOT.jar /app/kafka-ui/kafka-ui-api.jar

RUN chmod +x /app/kafka-ui/entrypoint.sh

EXPOSE 8080

WORKDIR /app/kafka-ui

ENTRYPOINT ["./entrypoint.sh"]

