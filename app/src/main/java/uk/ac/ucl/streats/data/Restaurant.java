package uk.ac.ucl.streats.data;

public class Restaurant {
    private String restaurantName;
    private String restaurantAdd;
    private int restaurantImageID;

    public Restaurant (String restaurantName, String restaurantAdd, int restaurantImageID) {
        this.restaurantName = restaurantName;
        this.restaurantAdd = restaurantAdd;
        this.restaurantImageID = restaurantImageID;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public String getRestaurantAdd() {
        return restaurantAdd;
    }

    public int getRestaurantImageID() {
        return restaurantImageID;
    }
}
