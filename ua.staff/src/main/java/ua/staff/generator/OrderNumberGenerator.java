package ua.staff.generator;

import java.util.Random;

public class OrderNumberGenerator {

    public static String generateOrderNumber(char letter){
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(letter);



        for (int i = 0; i < 10; i++) {
            stringBuilder.append(random.nextInt(10));
        }
        return stringBuilder.toString();
    }
}
