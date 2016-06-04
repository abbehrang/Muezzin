package com.mehmetakiftutuncu.muezzin.utilities;

import android.content.Context;

import java.text.Collator;
import java.util.Locale;

/**
 * Created by akif on 11/05/16.
 */
public class LocaleUtils {
    public static boolean isLanguageTurkish(Context context) {
        return getCurrentLanguage(context).contains("tr");
    }

    public static boolean isLanguageEnglish(Context context) {
        return getCurrentLanguage(context).contains("en");
    }

    public static Collator getTurkishCollator() {
        return Collator.getInstance(new Locale("tr", "TR"));
    }

    private static String getCurrentLanguage(Context context) {
        return context.getResources().getConfiguration().locale.getLanguage();
    }
}
