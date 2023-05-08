package data;

import model.Order;

import java.util.List;

public class OrderData {
    public static Order defaultOrder(){
        return new Order(List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f"));
    }

    public static Order emptyOrder(){
        return new Order();
    }

    public static Order orderWithWrongIDs(){
        return new Order(List.of("60d3463f4a000269f45e7"));
    }

}
