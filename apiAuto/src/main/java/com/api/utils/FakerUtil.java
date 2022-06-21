package com.api.utils;

import com.github.javafaker.Faker;
import java.util.Locale;

public class FakerUtil {
    private static final Faker faker = new Faker(Locale.CHINA);

    public static String getFunnyName() {
        return faker.funnyName().name();
    }

    public static String getName() {
        return faker.name().name();
    }

    public static String getPhone() {
        return faker.phoneNumber().cellPhone();
    }

    public static String getCompany() {
        return faker.company().name();
    }


}
