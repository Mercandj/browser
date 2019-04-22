#!/bin/bash

BASEDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"
pushd "$BASEDIR"

    COMPLIANCE_SIGNATURE_FILE="compliance-signature.txt"

    while read -r line; do
        FILE=$(echo $line | cut -d '|' -f1)
        SHASUM_REFERENCE=$(echo $line | cut -d '|' -f2)
        SHASUM_BRUT=$(shasum $FILE)
        SHASUM=(${SHASUM_BRUT//  / })

        if [[ "$SHASUM_REFERENCE" = "$SHASUM" ]]; then
            printf "Compliant: $FILE\n"
        else
            if [[ "$FILE" = "compliance-signature.txt" ]]; then
                printf "Compliant: $FILE\n"
            else
                printf "Wrong signature, not compliant.\n"
                printf "File: $FILE\n"
                printf "Expected reference shasum signature: $SHASUM_REFERENCE\n"
                printf "Current shasum signature: $SHASUM\n\n\n"
                printf "Note:\n\n"
                printf "* Updated the event feature.\n"
                printf "  Please recreate the signature with the script ./compliance-create.sh\n"
                exit 1
            fi
        fi

    done < "$COMPLIANCE_SIGNATURE_FILE"

popd