package com.tmw.tracking.web.controller;

import com.tmw.tracking.entity.paging.Page;
import com.tmw.tracking.entity.paging.PageQuery;
import com.tmw.tracking.utils.I18NService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.UriInfo;

public class BaseController {
    protected final String environment = System.getProperties().getProperty("tracking.env", "N/A").toUpperCase();

    @Inject
    protected I18NService i18NService;


    protected PageQuery getPageQuery(UriInfo uriInfo) {
        return PageQuery.of(uriInfo.getQueryParameters());
    }

    protected void setPageHeaders(Page<?> page, HttpServletResponse response) {
        if (response == null) {
            return;
        }
        if (page == null) {
            response.setHeader("X-Total-Pages", "" + 1);
            response.setHeader("X-Page-Size", "" + Page.ITEMS_ON_PAGE);
            return;
        }

        response.setHeader("X-Total-Pages", "" + page.getTotalPages());
        response.setHeader("X-Page-Size", "" + Page.ITEMS_ON_PAGE);
    }

}
