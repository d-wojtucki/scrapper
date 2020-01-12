package com.company.scrapper.helpers;

import com.company.scrapper.models.Offer;

import java.util.List;

public class OfferHelper {

    public static String generateAvg(List<Offer> offers) {
        double sum = 0.0d;
        for(Offer offer : offers) {
            Double price = Double.parseDouble(offer.getPrice());
            sum+=price;
        }
        double avg = sum/offers.size();
        String avgString = String.format("%.2f", avg);
        return avgString;
    }

    public static void calculateDifferenceFromAverage(List<Offer> offers, String average) {
        for (Offer offer : offers) {
            Double price = Double.parseDouble(offer.getPrice());
            Double avr = Double.parseDouble(average);
            String percentage = String.format("%.1f", (1-price/avr)*100);
            String difference = "";
            if(percentage.startsWith("-"))
                difference = "Droższy o " + percentage.substring(1) + "%";
            else if(Double.parseDouble(percentage) == 1.0d)
                difference = "Średnia cena";
            else
                difference = "Tańszy o " + percentage + "%";
            offer.setDifference(difference);
        }
    }

}
