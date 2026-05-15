package com.tmw.tracking.domain;

import com.google.common.base.Strings;
import com.tmw.tracking.entity.RootEntity;
import com.tmw.tracking.entity.User;
import com.tmw.tracking.entity.paging.PageQuery;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlRootElement
public class SearchCriteria {

    private User user;
    private String matcher;
    private List<String> matcherFields = new ArrayList<String>();
    private Map<String, Object> additionalFilters = new HashMap<String, Object>();
    private Boolean activity; //entity active/nonactive
    private List<Class<? extends RootEntity>> entitiesForSearch = new ArrayList<Class<? extends RootEntity>>();
    private Long roleId;

    private int page = 1;
    private String sortColumn = "";
    private SortOrder sortOrder = SortOrder.ASC;

    public SearchCriteria() {
    }

    public SearchCriteria(String matcher, PageQuery pageQuery) {
        if (pageQuery != null) {
            this.page = pageQuery.getPage();
            this.sortColumn = pageQuery.getSortColumn();
            this.sortOrder = pageQuery.getSortOrder();
        }
        this.matcher = matcher;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMatcher() {
        return matcher;
    }

    public void setMatcher(String matcher) {
        this.matcher = matcher;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public boolean isSortable() {
        return !Strings.isNullOrEmpty(sortColumn);
    }

    public String getSortColumn() {
        return sortColumn;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }




    public void addAdditionalFilters(String key, Object value){
        additionalFilters.put(key, value);
    }

    public <T extends Class<? extends RootEntity>> void addEntitiesForSearch( T entity){
        entitiesForSearch.add(entity);
    }

    public List<String> getMatcherFields() {
        return matcherFields;
    }

    public void setMatcherFields(List<String> matcherFields) {
        this.matcherFields = matcherFields;
    }

    public Map<String, Object> getAdditionalFilters() {
        return additionalFilters;
    }

    public List<Class<? extends RootEntity>> getEntitiesForSearch() {
        return entitiesForSearch;
    }


    public void setAdditionalFilters(Map<String, Object> additionalFilters) {
        this.additionalFilters = additionalFilters;
    }

    public Boolean getActivity() {
        return activity;
    }

    public void setActivity(Boolean activity) {
        this.activity = activity;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

}
