package com.hisarresearch.wms.service.dto.mikro.v15;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MikroV15QueryDTO {
    private String service;
    private List<String> filters;
    private List<String> sort;
    int currentPage;
    int pageSize;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public List<String> getFilters() {
        return filters;
    }

    public void setFilters(List<String> filters) {
        this.filters = filters;
    }

    public List<String> getSort() {
        return sort;
    }

    public void setSort(List<String> sort) {
        this.sort = sort;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        if (service != null) {
            map.put("service", service);
        }
        if (filters != null && !filters.isEmpty()) {
            map.put("filters", String.join(",", filters));
        }
        if (sort != null && !sort.isEmpty()) {
            map.put("sort", String.join(",", sort));
        }
        map.put("currentPage", String.valueOf(currentPage));
        map.put("pageSize", String.valueOf(pageSize));
        return map;
    }
}
