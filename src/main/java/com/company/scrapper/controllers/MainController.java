package com.company.scrapper.controllers;

import com.company.scrapper.api.AllegroApi;
import com.company.scrapper.models.Offer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;

@Controller
public class MainController {

    @Autowired
    AllegroApi allegroApi;

    @GetMapping("/app")
    public String home(
            Model model
    ) {
        try {
            allegroApi.renewToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Token: " + allegroApi.token);

        return "mainView";
    }

    @GetMapping("/search")
    public String search(
            Model model
    ) {
        ArrayList<Offer> offers = new ArrayList<Offer>();
        try {
            offers = allegroApi.getListOfItemsWithKeywordsAsJson("Harry Potter", 5, "+price");
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(Offer offer : offers) System.out.println(offer.toString());
        model.addAttribute("offers", offers);

        return "mainView";
    }
}
