#!/bin/bash
# Configure proxy Java options
PROXY_JAVA_OPTS=''
if [[ -n $PROXY_HOST && -n $PROXY_PORT ]]; then
    PROXY_JAVA_OPTS="$PROXY_JAVA_OPTS -Dhttp.proxyHost=$PROXY_HOST -Dhttp.proxyPort=$PROXY_PORT -Dhttps.proxyHost=$PROXY_HOST -Dhttps.proxyPort=$PROXY_PORT"
fi
if [[ -n $NON_PROXY_HOSTS ]]; then
    PROXY_JAVA_OPTS="$PROXY_JAVA_OPTS -Dhttp.nonProxyHosts=$NON_PROXY_HOSTS -Dhttps.nonProxyHosts=$NON_PROXY_HOSTS"
fi

CMD="java --add-opens java.rmi/javax.rmi.ssl=ALL-UNNAMED $PROXY_JAVA_OPTS -jar -Dspring.profiles.active=test kafka-ui-api.jar"
echo "Starting Kafka UI with command : $CMD"

eval "exec $CMD"
