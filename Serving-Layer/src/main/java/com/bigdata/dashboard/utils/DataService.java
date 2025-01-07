package com.bigdata.dashboard.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.bigdata.dashboard.repository.AverageDataRepository;
import com.bigdata.dashboard.repository.AggregatedDataRepository;
import com.bigdata.dashboard.entity.AverageData;
import com.bigdata.dashboard.entity.AggregatedData;

/**
 * Service class to send crypto data messages to dashboard UI at fixed intervals using
 * web-socket.
 */
@Service
public class DataService {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private AverageDataRepository averageDataRepository;

    @Autowired
    private AggregatedDataRepository aggregatedDataRepository;

    // Method sends data message in every 10 seconds.
    @Scheduled(fixedRate = 10000)
    public void trigger() {
        System.out.println("triggered");

        List<Double> averagePrices = new ArrayList<>();
        List<Double> volumes = new ArrayList<>();

        Long time = new Date().getTime();
        Date date = new Date(time - time % (60 * 1000)); // get data from the last minute

        // Fetch AverageData and AggregatedData from the repositories
        averageDataRepository.findByTimestampAfter(date).forEach(e -> {
            averagePrices.add(e.getAveragePrice());
            volumes.add(e.getVolume());
        });

        // Fallback values if no data is found
        double averagePrice = averagePrices.size() > 0 ? averagePrices.get(averagePrices.size() - 1) : 0;
        double volume = volumes.size() > 0 ? volumes.get(volumes.size() - 1) : 0;

        // Prepare response with the latest data
        Response response = new Response();
        response.setAveragePrice(averagePrice);
        response.setVolume(volume);

        // Send the response to the UI via WebSocket
        this.template.convertAndSend("/topic/data", response);
    }
}
