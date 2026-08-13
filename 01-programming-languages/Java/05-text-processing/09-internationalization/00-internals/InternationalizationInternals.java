package academy.javaengineering.text.internals;

import java.text.*;
import java.util.*;

public class InternationalizationInternals {

    public static void main(String[] args) {
        System.out.println("=== Internationalization Internals ===\n");

        // 1. Locale
        System.out.println("--- Locale ---");
        Locale us = Locale.US;
        Locale france = Locale.FRANCE;
        System.out.println("US: " + us.getDisplayCountry());
        System.out.println("France: " + france.getDisplayCountry());

        // 2. NumberFormat
        System.out.println("\n--- NumberFormat ---");
        double number = 1234567.89;
        NumberFormat usFormat = NumberFormat.getNumberInstance(Locale.US);
        NumberFormat deFormat = NumberFormat.getNumberInstance(Locale.GERMANY);
        System.out.println("US: " + usFormat.format(number));
        System.out.println("Germany: " + deFormat.format(number));

        // 3. DateFormat
        System.out.println("\n--- DateFormat ---");
        Date now = new Date();
        DateFormat usDate = DateFormat.getDateInstance(DateFormat.LONG, Locale.US);
        DateFormat deDate = DateFormat.getDateInstance(DateFormat.LONG, Locale.GERMANY);
        System.out.println("US: " + usDate.format(now));
        System.out.println("Germany: " + deDate.format(now));
    }
}
