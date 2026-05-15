package com.tmw.tracking.service;

import com.tmw.tracking.domain.flex.entities.FlexExportPackage;
import com.tmw.tracking.domain.flex.entities.FlexImportPackage;

public interface FlexConnectorService {

    FlexImportPackage retrieveImportPackage();

    FlexExportPackage retrieveExportPackage();
}
