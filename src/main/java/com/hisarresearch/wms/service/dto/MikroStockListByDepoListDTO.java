package com.hisarresearch.wms.service.dto;

import java.util.List;

public class MikroStockListByDepoListDTO {
    private List<Integer> depoList;
    private List<String> stokList;

    public MikroStockListByDepoListDTO(List<Integer> depoList, List<String> stokList) {
        this.depoList = depoList;
        this.stokList = stokList;
    }

    public List<Integer> getDepoList() {
        return depoList;
    }

    public void setDepoList(List<Integer> depoList) {
        this.depoList = depoList;
    }

    public List<String> getStokList() {
        return stokList;
    }

    public void setStokList(List<String> stokList) {
        this.stokList = stokList;
    }
}
