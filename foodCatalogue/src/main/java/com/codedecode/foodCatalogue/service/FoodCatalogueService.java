package com.codedecode.foodCatalogue.service;

import com.codedecode.foodCatalogue.dto.FoodCataloguePage;
import com.codedecode.foodCatalogue.dto.FoodItemDTO;
import com.codedecode.foodCatalogue.dto.Restaurant;
import com.codedecode.foodCatalogue.entity.FoodItem;
import com.codedecode.foodCatalogue.mapper.FoodItemMapper;
import com.codedecode.foodCatalogue.repo.FoodItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class FoodCatalogueService {
    @Autowired
    FoodItemRepo foodItemRepo;

    @Autowired
    RestTemplate restTemplate;

    public FoodItemDTO addFoodItem(FoodItemDTO foodItemDTO) {
        FoodItem foodItem = FoodItemMapper.INSTANCE.mapFoodItemDTOToFoodItem(foodItemDTO);
        FoodItem foodItemSaved = foodItemRepo.save(foodItem);
        return FoodItemMapper.INSTANCE.foodItemToFoodItemDTO(foodItemSaved);
    }

    public FoodCataloguePage fetchFoodCataloguePageDetails(int restaurantId) {
        // food item list from foodcataloguedb.food_item
        List<FoodItem> foodItemList = fetchFoodItemList(restaurantId);
        // restaurant details from restaurantdb.restaurant
        Restaurant restaurant = fetchRestaurantDetailsFromRestaurantMS(restaurantId);
        return createFoodCataloguePage(foodItemList, restaurant);
    }

    private FoodCataloguePage createFoodCataloguePage(List<FoodItem> foodItemList, Restaurant restaurant) {
    FoodCataloguePage foodCataloguePage = new FoodCataloguePage();
    foodCataloguePage.setRestaurant(restaurant);
    foodCataloguePage.setFoodItemList(foodItemList);
    return foodCataloguePage;
    }

    private Restaurant fetchRestaurantDetailsFromRestaurantMS(int restaurantId) {
        return restTemplate.getForObject("http://RESTAURANT-SERVICE/restaurant/fetchById/"+restaurantId, Restaurant.class);
    }

    private List<FoodItem> fetchFoodItemList(int restaurantId) {
        return foodItemRepo.findByRestaurantId(restaurantId);
    }


}
