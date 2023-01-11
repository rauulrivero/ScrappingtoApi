package org.example.api;

import org.example.model.Hotel;

import java.util.List;

public interface ApiServer {
    void start(List<Hotel> hotelscrapped);
}
