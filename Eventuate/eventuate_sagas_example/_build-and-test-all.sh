#! /bin/bash

set -e


dockerinfrastructure="./gradlew ${DATABASE?}infrastructureCompose"
dockerall="./gradlew ${DATABASE?}Compose"

${dockerall}Down
${dockerinfrastructure}Up

#Testing db cli
if [ "${DATABASE}" == "mysql" ]; then
  echo 'show databases;' | ./mysql-cli.sh -i
else
  echo "Unknown Database"
  exit 99
fi

${dockerall}Up

./wait-for-services.sh localhost readers/${READER}/finished "8099"

compose="docker-compose -f docker-compose-${DATABASE}.yml "

$compose stop cdc-service
curl -s https://raw.githubusercontent.com/eventuate-foundation/eventuate-common/master/migration/db-id/migration.sh &> /dev/stdout | bash
$compose start cdc-service

${dockerall}Up -P envFile=docker-compose-env-files/db-id-gen.env

${dockerall}Down
