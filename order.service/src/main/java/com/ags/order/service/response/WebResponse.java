package com.ags.order.service.response;

import lombok.Data;

@Data
public class WebResponse {
    private String message;
    public WebResponse(String message) {
        this.message = message;
    }
}
