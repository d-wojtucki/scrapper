package com.company.scrapper.controllers;

import com.company.scrapper.api.AllegroApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @Autowired
    AllegroApi allegroApi;

    @GetMapping("/app")
    public String home() {
        try {
            allegroApi.renewToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Token: " + allegroApi.token);

        return "mainView";
    }

    @GetMapping("/search")
    public String search() {
        try {
            allegroApi.getListOfItemsWithKeywordsAsJson("Harry Potter");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "mainView";
    }
}
