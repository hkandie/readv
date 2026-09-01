#!/bin/bash
# Renders a systemd unit file to stdout from environment-supplied parameters, so
# readvantage-app.service and readvantage-web.service don't have to be hand-maintained as
# near-duplicate static files. Required: SERVICE_NAME, SERVICE_DESCRIPTION, WORKING_DIR,
# JAR_PATH, ENV_FILE, RESTART_POLICY. Optional: RESTART_SEC, EXEC_START_PRE, RUN_USER
# (defaults to readvantage).
set -euo pipefail

: "${SERVICE_NAME:?SERVICE_NAME is required}"
: "${SERVICE_DESCRIPTION:?SERVICE_DESCRIPTION is required}"
: "${WORKING_DIR:?WORKING_DIR is required}"
: "${JAR_PATH:?JAR_PATH is required}"
: "${ENV_FILE:?ENV_FILE is required}"
: "${RESTART_POLICY:?RESTART_POLICY is required}"
RUN_USER="${RUN_USER:-readvantage}"

cat <<UNIT
[Unit]
Description=$SERVICE_DESCRIPTION
After=network.target

[Service]
Type=simple
WorkingDirectory=$WORKING_DIR
EnvironmentFile=-$ENV_FILE
UNIT

if [[ -n "${EXEC_START_PRE:-}" ]]; then
    echo "ExecStartPre=$EXEC_START_PRE"
fi

cat <<UNIT
ExecStart=/usr/bin/java -jar $JAR_PATH
Restart=$RESTART_POLICY
UNIT

if [[ "$RESTART_POLICY" != "no" && -n "${RESTART_SEC:-}" ]]; then
    echo "RestartSec=$RESTART_SEC"
fi

cat <<UNIT
User=$RUN_USER
SyslogIdentifier=$SERVICE_NAME

[Install]
WantedBy=multi-user.target
UNIT
