package com.tmw.tracking.web.controller;

import com.google.inject.Singleton;
import com.sun.jersey.api.view.Viewable;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.rmi.ServerException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by pzhelnov on 2/20/2017.
 */
@Path("/anon")
@Singleton

public class AnonController extends BaseController {

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/index")
    public Viewable getIndexPage() {
        final Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("angular", true);
        vars.put("environment", environment);
        vars.put("locale", Locale.ENGLISH.getLanguage());
        return new Viewable("/anon/index", vars);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/test")
    public String getTest() {
        final Map<String, Object> vars = new HashMap<String, Object>();
        return "{\"id\":\"https://login.salesforce.com/id/00D6A000001Wc9uUAC/0056A000000NjbwQAC\",\"asserted_user\":true,\"user_id\":\"0056A000000NjbwQAC\",\"organization_id\":\"00D6A000001Wc9uUAC\",\"username\":\"admin@pzhelnov.modeln.gpm.pd\",\"nick_name\":\"admin1.4979586756908335E12\",\"display_name\":\"Pavel Zhelnov\",\"email\":\"irp-pd-qa@modeln.com\",\"email_verified\":true,\"first_name\":\"Pavel\",\"last_name\":\"Zhelnov\",\"timezone\":\"Europe/Rome\",\"photos\":{\"picture\":\"https://c.na124.content.force.com/profilephoto/005/F\",\"thumbnail\":\"https://c.na124.content.force.com/profilephoto/005/T\"},\"addr_street\":null,\"addr_city\":null,\"addr_state\":null,\"addr_country\":\"US\",\"addr_zip\":null,\"mobile_phone\":null,\"mobile_phone_verified\":false,\"is_lightning_login_user\":false,\"status\":{\"created_date\":null,\"body\":null},\"urls\":{\"enterprise\":\"https://na124.salesforce.com/services/Soap/c/{version}/00D6A000001Wc9u\",\"metadata\":\"https://na124.salesforce.com/services/Soap/m/{version}/00D6A000001Wc9u\",\"partner\":\"https://na124.salesforce.com/services/Soap/u/{version}/00D6A000001Wc9u\",\"rest\":\"https://na124.salesforce.com/services/data/v{version}/\",\"sobjects\":\"https://na124.salesforce.com/services/data/v{version}/sobjects/\",\"search\":\"https://na124.salesforce.com/services/data/v{version}/search/\",\"query\":\"https://na124.salesforce.com/services/data/v{version}/query/\",\"recent\":\"https://na124.salesforce.com/services/data/v{version}/recent/\",\"tooling_soap\":\"https://na124.salesforce.com/services/Soap/T/{version}/00D6A000001Wc9u\",\"tooling_rest\":\"https://na124.salesforce.com/services/data/v{version}/tooling/\",\"profile\":\"https://na124.salesforce.com/0056A000000NjbwQAC\",\"feeds\":\"https://na124.salesforce.com/services/data/v{version}/chatter/feeds\",\"groups\":\"https://na124.salesforce.com/services/data/v{version}/chatter/groups\",\"users\":\"https://na124.salesforce.com/services/data/v{version}/chatter/users\",\"feed_items\":\"https://na124.salesforce.com/services/data/v{version}/chatter/feed-items\",\"feed_elements\":\"https://na124.salesforce.com/services/data/v{version}/chatter/feed-elements\"},\"active\":true,\"user_type\":\"STANDARD\",\"language\":\"en_US\",\"locale\":\"en_US\",\"utcOffset\":3600000,\"last_modified_date\":\"2019-11-28T08:37:23Z\",\"is_app_installed\":true}";
    }




    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/menu")
    public Viewable getIndexPageWithMenu() {
        final Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("angular", true);
        vars.put("environment", environment);
        vars.put("locale", Locale.ENGLISH.getLanguage());
        return new Viewable("/anon/menu", vars);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/showpage")
    public Viewable getContact(@QueryParam("page") String page) throws  ServerException {
        if ("test".equals(page)) {
            throw new ServerException("Test exception");
        }
        final Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("angular", true);
        vars.put("environment", environment);
        vars.put("locale", Locale.ENGLISH.getLanguage());
        return new Viewable("/anon/" + page, vars);
    }

}
