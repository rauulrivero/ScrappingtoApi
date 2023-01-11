package org.example.api;

import com.google.gson.Gson;
import org.example.model.Hotel;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.halt;

public class WebService implements ApiServer{
    private static final String RESPONSE_HEADER = "content-type";
    private static final String RESPONSE_VALUE = "application/json";
    private static final String HOTEL_EXCEPTION = "Hotel not found";

    public void start(List<Hotel> hotelsscraped) {
        get("/hotels", (request, response) -> getHotels(response, hotelsscraped));
        get("/hotels/:name/services", (request, response) -> getServices(request, response, hotelsscraped));
        get("/hotels/:name/comments", (request, response) -> getComments(request, response, hotelsscraped));
        get("/hotels/:name", (request, response) -> getLocation(request, response, hotelsscraped));
        get("/hotels/:name/ratings", (request, response) -> getRatings(request, response, hotelsscraped));
    }

    private static String getHotels(Response response, List<Hotel> hotelsscraped) {
        response.header(RESPONSE_HEADER, RESPONSE_VALUE);
        List<String> hotelNames = new ArrayList<>();
        for (Hotel hotel : hotelsscraped) hotelNames.add(hotel.getName());
        return toJson(hotelNames);
    }

    private static String getServices(Request request, Response response, List<Hotel> hotelsscraped) {
        response.header(RESPONSE_HEADER, RESPONSE_VALUE);
        String name = request.params("name");
        for (Hotel hotel : hotelsscraped) {
            if (hotel.getName().equals(name)) return toJson(hotel.getServices());
        }
        halt(404, HOTEL_EXCEPTION);
        return "";
    }

    private static String getLocation(Request request, Response response, List<Hotel> hotelsscraped) {
        response.header(RESPONSE_HEADER, RESPONSE_VALUE);
        String name = request.params("name");
        for (Hotel hotel : hotelsscraped) {
            if (hotel.getName().equals(name)) return toJson(hotel.getLocation());
        }
        halt(404, HOTEL_EXCEPTION);
        return "";
    }

    private static String getComments(Request request, Response response, List<Hotel> hotelsscraped) {
        response.header(RESPONSE_HEADER, RESPONSE_VALUE);
        String name = request.params("name");
        for (Hotel hotel : hotelsscraped) {
            if (hotel.getName().equals(name)) return toJson(hotel.getComments());
        }
        halt(404, HOTEL_EXCEPTION);
        return "";
    }

    private static String getRatings(Request request, Response response, List<Hotel> hotelsscraped) {
        response.header(RESPONSE_HEADER, RESPONSE_VALUE);
        String name = request.params("name");
        for (Hotel hotel : hotelsscraped) {
            if (hotel.getName().equals(name)) return toJson(hotel.getRatings());
        }
        halt(404, HOTEL_EXCEPTION);
        return "";
    }



    private static String toJson(Object o) {
        return new Gson().toJson(o);
    }
}
