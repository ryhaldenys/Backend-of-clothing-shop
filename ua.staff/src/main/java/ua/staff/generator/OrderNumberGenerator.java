package ua.staff.generator;

import java.util.Random;

public class OrderNumberGenerator {

    public static String generateOrderNumber(char letter){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(letter);

        for (int i = 0; i < 10; i++) {
            stringBuilder.append(Math.random()*10);
        }
        return stringBuilder.toString();
    }
}
