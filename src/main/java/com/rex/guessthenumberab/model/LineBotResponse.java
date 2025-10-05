package com.rex.guessthenumberab.model;


import lombok.Data;

@Data
public class LineBotResponse {
    private boolean isSuccess = true;
    private String message = "";

    public LineBotResponse() {
    }

    public LineBotResponse(String message) {
        this.message = message;
    }
}
