package com.toteuch.animerecommendations.malapi.response.animedetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.toteuch.animerecommendations.malapi.response.Node;

public class RelatedAnimeResponse {
    private Node node;
    @JsonProperty("relation_type")
    private String relationType;

    public RelatedAnimeResponse() {
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }
}
