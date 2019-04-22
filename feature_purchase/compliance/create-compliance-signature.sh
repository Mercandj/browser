#!/bin/bash

BASEDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"
pushd "$BASEDIR"

    COMPLIANCE_SIGNATURE_FILE="compliance-signature.txt"
    rm ${COMPLIANCE_SIGNATURE_FILE}
    touch ${COMPLIANCE_SIGNATURE_FILE}
    FILES=($(git ls-files ../))

    for FILE in "${FILES[@]}"
    do :
        SHASUM_BRUT=$(shasum $FILE)
        SHASUM=(${SHASUM_BRUT//  / })

        echo "$FILE|$SHASUM" >> ${COMPLIANCE_SIGNATURE_FILE}

    done

popd