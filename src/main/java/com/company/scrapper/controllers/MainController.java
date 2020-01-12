package com.company.scrapper.controllers;

import com.company.scrapper.api.AllegroApi;
import com.company.scrapper.helpers.OfferHelper;
import com.company.scrapper.models.Offer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@RequestMapping("/app")
@Controller
public class MainController {

    @Autowired
    AllegroApi allegroApi;


    //TODO main view - strona glowna z search boxem strzelajaca getem na /search z 3 parametrami - opisanie nizej
    @GetMapping("/")
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

    @Deprecated
    @GetMapping("/search_test")
    public String searchTest(
            Model model
    ) {
        String keywords = "Harry Potter";
        ArrayList<Offer> offers = new ArrayList<Offer>();
        try {
            offers = allegroApi.getListOfItemsWithKeywordsAsJson(keywords, 5, "+price");
        } catch (Exception e) {
            e.printStackTrace();
        }



        String offerAvg = OfferHelper.generateAvg(offers);
        OfferHelper.calculateDifferenceFromAverage(offers, offerAvg);

        for(Offer offer : offers) System.out.println(offer.toString());
        System.out.println("Offer avg: " + offerAvg);

        model.addAttribute("keywords", keywords);
        model.addAttribute("offers", offers);
        model.addAttribute("offerCount", offers.size());
        model.addAttribute("offerAvg", offerAvg);
        return "mainView";
    }

    /*
     * keywords - slowa kluczowe, po ktorych bedzie nastepowac wyszukiwanie, np "harry potter ksiazka"
     * limit - ilosc wyswietlonych ofert
     * sorting method - sortowanie ofert, domyslnie "+price" dla sortowania po cenie rosnaco
     */
    //TODO someview - widok listy itemow wraz z hiperlinkami budowanymi: allegro.pl/oferta/id
    @GetMapping("/search")
    public String search(
            @ModelAttribute String keywords,
            @ModelAttribute int limit,
            @ModelAttribute String sortingMethod,
            Model model
            ) {
        ArrayList<Offer> offers = new ArrayList<Offer>();
        try {
            offers = allegroApi.getListOfItemsWithKeywordsAsJson(keywords, limit, sortingMethod);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String offerAvg = OfferHelper.generateAvg(offers);
        OfferHelper.calculateDifferenceFromAverage(offers, offerAvg);

        System.out.println("Offer avg: " + offerAvg);
        for(Offer offer : offers) System.out.println(offer.toString());


        model.addAttribute("keywords", keywords);
        model.addAttribute("offers", offers);
        model.addAttribute("offerCount", offers.size());
        model.addAttribute("offerAvg", offerAvg);
        return "someView";
    }


}
