package data;

import model.User;

public class UserData {
    public static User defaultUser(){
        return new User("jostar@yandex.ru", "qatest123","Usertest");
    }

    public static User userWithoutEmail(){
        return new User("", "qatest123", "Usertest");
    }

    public static User userWithoutPassword(){
        return new User("test@yandex.ru", "", "Usertest");
    }

    public static User userWithoutName(){
        return new User("test@yandex.ru", "qatest123", "");
    }
}
