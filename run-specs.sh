#!/usr/bin/env bash
ENV=${1:-local}
DAST=${2:-false}

sbt -Denvironment=${ENV} -Dsecurity.assessment=${DAST} test