package org.example.scrapper;

import org.example.model.Hotel;

import java.util.List;

public interface Scrapper {
    List<Hotel> scrapper(int npaginas, String urlWithoutOffset);
}
