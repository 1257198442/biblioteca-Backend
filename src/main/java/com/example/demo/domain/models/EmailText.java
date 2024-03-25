package com.example.demo.domain.models;

import lombok.Builder;

@Builder
public class EmailText {
    public String title;
    public String content;

    public String getEmailText(){
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<title>" + title + "</title>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; }" +
                ".order-details { border: 1px solid #ccc; padding: 20px; width: 500px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='order-details'>" +
                content +
                "</div>" +
                "</body>" +
                "</html>";
    }
}
