package ru.lytvest.chess.net;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SearchRequest extends AuthRequest {
    String id;

    public SearchRequest(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
