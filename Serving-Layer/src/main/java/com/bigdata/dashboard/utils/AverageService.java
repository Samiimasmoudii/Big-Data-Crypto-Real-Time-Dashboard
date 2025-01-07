package com.bigdata.dashboard.utils;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.bigdata.dashboard.entity.AverageData;
import com.bigdata.dashboard.repository.AverageDataRepository;

@Service
public class AverageService {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private AverageDataRepository averageDataRepository;

    // Method sends data message every 15 seconds (fixedRate = 15000)
    @Scheduled(fixedRate = 15000)
    public void trigger() {

        // Get the current time and adjust it to get the most recent data
        Long time = new Date().getTime();
        Date date = new Date(time - time % ( 60 * 1000)); // Get data from the last minute

        // Fetch the average data (you can adjust the query to get data for the current period)
        AverageData data = averageDataRepository.find();

        if (data != null) {
            // Extract values from AverageData
            double averagePrice = data.getAveragePrice();
            double volume = data.getVolume();

            // Print the fetched data (for debugging)
            System.out.println("Average Price: " + averagePrice);
            System.out.println("Volume: " + volume);

            // Prepare a response object (use the values of AverageData)
            Response response = new Response();
            response.setAveragePrice(averagePrice);
            response.setVolume(volume);

            // Send the data to the UI (via WebSocket)
            this.template.convertAndSend("/topic/average", response);
        } else {
            System.out.println("No average data found.");
        }
    }
}
