#!/bin/bash

cd ../wasteservice_core/

chmod +x gradlew
# Run Robot Context
./gradlew runCtx_Robot --stacktrace

# Run TransportTrolley Context
./gradlew runCtx_TransportTrolley --stacktrace

# Run WasteService Context
./gradlew runCtx_WasteService --stacktrace