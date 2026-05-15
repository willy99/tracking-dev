package com.tmw.tracking.web.service.util.request;

import com.tmw.tracking.entity.enums.Langs;
import com.tmw.tracking.utils.DomainUtils;

import javax.inject.Singleton;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Singleton
public class TrackingRequestFilter implements Filter {

    /*@Override
    public ContainerRequest filter(ContainerRequest containerRequest) {
        String locale = containerRequest.getHeaderValue("locale");
        if (locale != null) {
            DomainUtils.requestLocale.set(new Locale(locale));
        }
        return containerRequest;
    }*/

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String locale = ((HttpServletRequest)servletRequest).getHeader ("locale");
        try {
            // enable global filters
            if (locale != null) {
                Langs map = Langs.getLangs(locale);
                DomainUtils.requestLocale.set(map.getLocale());
            }

            filterChain.doFilter(servletRequest, servletResponse);
        }finally {
        }
    }

    @Override
    public void destroy() {

    }
}
