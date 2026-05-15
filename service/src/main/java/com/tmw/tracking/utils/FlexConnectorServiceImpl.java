package com.tmw.tracking.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.inject.Inject;
import com.tmw.tracking.domain.flex.entities.FlexExportPackage;
import com.tmw.tracking.domain.flex.entities.FlexImportPackage;
import com.tmw.tracking.domain.flex.onec.GetOrdersPortType;
import com.tmw.tracking.domain.flex.onec.GetOrders_Service;
import com.tmw.tracking.service.FlexConnectorService;
import com.tmw.tracking.web.service.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import javax.xml.ws.BindingProvider;

public class FlexConnectorServiceImpl implements FlexConnectorService {

    private String userName;
    private String password;

    @Inject
    public FlexConnectorServiceImpl(
            @Named("tracking.client.flex.1c.username") final String username,
            @Named("tracking.client.flex.1c.password") final String password) {
        this.userName = username;
        this.password = password;
    }

    private final static String EXPORT_PARAM = "2";
    private final static String IMPORT_PARAM = "1";

    private final static Logger logger = LoggerFactory.getLogger(FlexConnectorServiceImpl.class);


    @Override
    public FlexImportPackage retrieveImportPackage() {
        return executeLoadingPackage(IMPORT_PARAM, FlexImportPackage.class);
    }

    @Override
    public FlexExportPackage retrieveExportPackage() {
        return executeLoadingPackage(EXPORT_PARAM, FlexExportPackage.class);
    }

    private <T> T executeLoadingPackage(String payload, Class<T> packageClass) {

        GetOrders_Service service = new GetOrders_Service();
        GetOrdersPortType portType = service.getGetOrdersSoap();

        BindingProvider prov = (BindingProvider) portType;

        prov.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, userName);
        prov.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, password);

        String result = portType.getOrders(payload);

        result = "{ \"orders\": " + result + "}";
        try {
            logger.debug(result);
            System.out.println("from 1c:");
            System.out.println(result);

            Gson gson = new Gson();
            return gson.fromJson(result, packageClass);
        }
        catch (JsonSyntaxException ex) {
            throw new ValidationException("Incorrect json syntax, while getting import package from 1c.");
        }
    }

}
