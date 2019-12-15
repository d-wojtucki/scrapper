package com.company.scrapper.api;

import com.company.scrapper.models.Offer;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class AllegroApi {

    public String token;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    public String getToken() {
        return this.token;
    }

    private static HttpRequest.BodyPublisher buildFormDataFromMap(Map<Object, Object> data) {
        var builder = new StringBuilder();
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }
        System.out.println(builder.toString());
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }

    public void renewToken() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(buildFormDataFromMap(new HashMap<>()))
                .uri(URI.create("https://allegro.pl/auth/oauth/token?grant_type=client_credentials"))
                .header("Authorization", "Basic NDY1YjYyNmZkOWM0NDBkZDg1NDllZjcwZjIzNTg1ZTk6OWlaTmpEWEthMWdSTFh1TnB4eDNLTzlxMDlVeWtDZ1FiVFQxR2lQV1h5V1J0ZVRSclJEVFVWdlZDMDZpem5lWg==")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // print status code
        System.out.println(response.statusCode());

        // print response body
        System.out.println(response.body());

        JsonObject object = new JsonParser().parse(response.body()).getAsJsonObject();

        String retrievedToken = object.getAsJsonObject().get("access_token").getAsString();
        System.out.println("Retrieved: " + retrievedToken);

        this.token = retrievedToken;

    }

    public ArrayList<Offer> getListOfItemsWithKeywordsAsJson(String keywords, int limit, String sortingMethod) throws Exception {

        URI uri = new URI(("https://api.allegro.pl/offers/listing?phrase=" + keywords + "&sort=" + sortingMethod + "&limit=" + limit).replace(" ", "%20"));

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                //.setHeader("Accept", "application/vnd.allegro.public.v1+json")
                .header("Authorization", "Bearer " + token)
                .header("Accept", "application/vnd.allegro.public.v1+json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // print status code
        System.out.println(response.statusCode());

        // print response body
        System.out.println(response.body());

        JsonObject object = new JsonParser().parse(response.body()).getAsJsonObject();


        JsonObject itemsArray = object.getAsJsonObject("items");
        System.out.println(itemsArray.toString());
        JsonArray objectArray = itemsArray.getAsJsonArray("promoted");
        System.out.println(objectArray.size());

        return generateListOfOffersFromObjectArray(objectArray);
    }

    private ArrayList<Offer> generateListOfOffersFromObjectArray(JsonArray objectArray) {
        ArrayList<Offer> offerList = new ArrayList<>();

        for (int i=0;i<objectArray.size();i++) {
            JsonObject insideObject = objectArray.get(i).getAsJsonObject();
            String retrievedId = insideObject.getAsJsonPrimitive("id").toString();
            String retrievedTitle = insideObject.getAsJsonPrimitive("name").toString();
            String retrievedImage = insideObject.getAsJsonArray("images").get(0).getAsJsonObject().getAsJsonPrimitive("url").toString();
            String retrievedPrice = insideObject.getAsJsonObject("sellingMode").getAsJsonObject("price").getAsJsonPrimitive("amount").toString();

            offerList.add(new Offer(retrievedId, retrievedTitle, retrievedImage, retrievedPrice));
        }

        return offerList;

    }



}
