package com.toteuch.animerecommendations.malapi.response.animelistuser;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.toteuch.animerecommendations.malapi.response.Node;

public class Data {
    private Node node;
    @JsonProperty("list_status")
    private ListStatus listStatus;

    public Data() {
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public ListStatus getListStatus() {
        return listStatus;
    }

    public void setListStatus(ListStatus listStatus) {
        this.listStatus = listStatus;
    }
}
