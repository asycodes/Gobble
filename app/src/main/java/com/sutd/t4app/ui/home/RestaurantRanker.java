package com.sutd.t4app.ui.home;

import com.sutd.t4app.data.model.Restaurant;

import java.util.*;

public class RestaurantRanker {
    private PriorityQueue<RestaurantScore> maxHeap;

    public RestaurantRanker() {
        // Initialize max heap with a comparator that orders RestaurantScores by score in descending order
        maxHeap = new PriorityQueue<>((score1, score2) -> Integer.compare(score2.getScore(), score1.getScore()));
    }

    public void addRestaurantScore(RestaurantScore restaurantScore) {
        maxHeap.add(restaurantScore);
    }

    public List<Restaurant> getRankedRestaurants() {
        List<Restaurant> rankedRestaurants = new ArrayList<>();
        while (!maxHeap.isEmpty()) {
            rankedRestaurants.add(maxHeap.poll().getRestaurant());
        }
        return rankedRestaurants;
    }

}
