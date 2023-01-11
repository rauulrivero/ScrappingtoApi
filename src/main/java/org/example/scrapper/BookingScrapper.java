package org.example.scrapper;

import org.example.model.Comment;
import org.example.model.Hotel;
import org.example.model.Location;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingScrapper implements Scrapper{

    private Document getHTML (String url) {
        Document html = null;

        try {
            html = Jsoup.connect(url).timeout(100000).get();
        } catch (Exception e) {
            System.out.println("Error al obtener el codigo HTML");
        }
        return html;
    }

    public List<Hotel> scrapper(int npaginas, String urlWithoutOffset) {
        List<Hotel> hotelsscraped = new ArrayList<>();

        int offset = 0;

        for (int i = 0; i < npaginas; i++) {

            Document document = getHTML(urlWithoutOffset + offset);

            offset += 25;

            if (document == null) continue;
            Elements hotels = document.select("h3.a4225678b2");

            int npagina = i+1;
            System.out.println("Número de entradas en la página " + npagina + " de Booking: " + hotels.size() + "\n");
            for (Element hotel : hotels) {
                try {
                    String totalurlhotel = hotel.select("a").attr("abs:href");
                    String urlhotel = totalurlhotel.substring(0, totalurlhotel.indexOf("?"));
                    Document hotelHTML = getHTML(urlhotel);

                    String sub = "https://www.booking.com/reviews/es/hotel/";
                    String reviewurlhotel = sub + urlhotel.substring(33);
                    Document reviewhotelHTML = getHTML(reviewurlhotel);


                    String hotelname = namescrapper(hotelHTML);
                    Location hotellocation = locationscrapper(hotelHTML);
                    List<String> hotelservices = servicescrapper(hotelHTML);
                    Map<String, String> hotelratings = ratingscrapper(hotelHTML);
                    List<Comment> hotelcomments = commentscrapper(reviewhotelHTML);



                    Hotel hotelscraped = new Hotel(hotelname, hotellocation, hotelratings, hotelservices, hotelcomments);
                    hotelsscraped.add(hotelscraped);


                    System.out.println(hotelname);
                    System.out.println(hotelservices);
                    System.out.println(hotelratings);
                    System.out.println(hotellocation.direction);
                    System.out.println(hotelcomments.get(0).getCommentdate());
                    System.out.println("\n");
                } catch (Exception e) {
                    System.out.println("Error al encontrar los hoteles");
                }
            }
        }
        return hotelsscraped;
    }

    private String namescrapper(Document hotelHTML) {
        Elements names = hotelHTML.getElementsByClass("d2fee87262 pp-header__title");
        return names.get(0).text();
    }

    private Location locationscrapper(Document hotelHTML) {
        String hoteldirection = null;
        String coordinates = hotelHTML.select("a").attr("data-atlas-latlng");
        Elements locations = hotelHTML.getElementsByClass("\n" +
                "hp_address_subtitle\n" +
                "js-hp_address_subtitle\n" +
                "jq_tooltip\n"
        );
        for (Element location: locations) hoteldirection = location.text();
        return new Location(hoteldirection, coordinates);
    }

    private List<String> servicescrapper(Document hotelHTML) {
        List<String> hotelservices = new ArrayList<>();
        Elements services = hotelHTML.getElementsByClass("bui-list__description");
        Elements services2 = hotelHTML.getElementsByClass("db312485ba");
        for (Element service: services) hotelservices.add(service.text());
        for (Element service: services2) hotelservices.add(service.text());
        return hotelservices;
    }

    private Map<String, String> ratingscrapper(Document hotelHTML) {
        Map<String, String> hotelratings = new HashMap<>();
        Elements nameratings = hotelHTML.getElementsByClass("d6d4671780");
        Elements numberratings = hotelHTML.getElementsByClass("ee746850b6 b8eef6afe1");

        for (int j = 0; j < 7; j++) hotelratings.put(nameratings.get(j).text(), numberratings.get(j).text());

        return hotelratings;
    }

    private List<Comment> commentscrapper(Document reviewhotelHTML) {
        List<Comment> hotelcomments = new ArrayList<>();

        Elements comments = reviewhotelHTML.getElementsByClass( "review_item_header_content\n");
        Elements ratingscomment = reviewhotelHTML.getElementsByClass("review-score-badge");
        Elements dates = reviewhotelHTML.getElementsByClass("review_item_date");

        for (int j = 0; j < comments.size(); j++) hotelcomments.add(new Comment(comments.get(j).text(),
                ratingscomment.get(j).text(), dates.get(j).text()));
        return hotelcomments;
    }

}
