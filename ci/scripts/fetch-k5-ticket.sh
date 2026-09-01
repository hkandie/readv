#!/bin/bash
set -euo pipefail

log() { echo "$@" >&2; }

if ! command -v klist >/dev/null 2>&1; then
    log "ERROR: klist not found. Install Kerberos client tools."
    exit 1
fi

if ! klist -s; then
    if [[ -z "${KERBEROS_PRINCIPAL:-}" || -z "${KERBEROS_KEYTAB:-}" ]]; then
        log "ERROR: KERBEROS_PRINCIPAL and KERBEROS_KEYTAB environment variables must be set."
        exit 1
    fi

    if [[ ! -f "$KERBEROS_KEYTAB" ]]; then
        log "ERROR: Kerberos keytab file not found at $KERBEROS_KEYTAB"
        exit 1
    fi

    log "No Kerberos ticket found. Acquiring one with keytab..."
    kinit -kt "$KERBEROS_KEYTAB" "$KERBEROS_PRINCIPAL"
fi

if ! klist -s; then
    log "ERROR: Kerberos authentication failed. No valid ticket is available after kinit."
    exit 1
fi

log "Valid Kerberos (K5) ticket available"
