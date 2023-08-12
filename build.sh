#!/bin/bash
./mvnw -pl kafka-ui-api -am -Dmaven.test.skip=true -Ddocker.skip=true -Dcheckstyle.skip clean package -Pprod
