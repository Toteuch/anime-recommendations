package com.toteuch.animerecommendations.malapi.response.animelistuser;

public class AnimelistUserResponse {
    private Data[] data;
    private Paging paging;

    public AnimelistUserResponse() {
    }

    public Data[] getData() {
        return data;
    }

    public void setData(Data[] data) {
        this.data = data;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }
}
