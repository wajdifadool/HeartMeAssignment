package com.tests.wajde.heartmeassignment.data_manager;

import java.util.Map;

/**
 * Created by wajdi on 09/06/2018.
 */

public class Model {
    /*class vars*/
    public static final String UNKNOWN = "Unknown";
    private  static final String BAD_RESULT = "Bad Result !";
    private  static final String GOOD_RESULT = "Good Result !";

    /*Singleton Pattern */
    private static final Model INSTANCE = new Model();
    private static Map<String, Integer> dataSet;


    public static Model getINSTANCE() {
        return INSTANCE;
    }

    private Model() {
    }


    void setDataSetasMap(Map<String, Integer> map) {
        this.dataSet = map;
    }

    public Map<String, Integer> getDataSet() {
        return this.dataSet;
    }

    public final static class Attribuites {
        public static final String KEY_RESULT = "intentServiceKeyResult";
        public static final String ACTION_RESP =
                "com.tests.wajde.heartmeassignment.intent.action.MESSAGE_PROCESSED";
    }

    public static final class Arguments {
        public static final String TEST_NAME = "blood_test_name";
        public static final String TEST_CATEGORY = "blood_test_category";
        public static final String TEST_RESULT = "blood_test_result";
    }

    /**
     * the last resort for identifying blood test category
     * loops blood test names (the category ) from the data set,
     *
     * @param name the user input for the blood test name
     * @return the user category as string form the Data set
     */
    public String identifyCategory(String name) {
        String value = UNKNOWN;

        name = name.toLowerCase().replaceAll("[^a-zA-Z0-9]+", "");
        for (String category : dataSet.keySet()) {

            /*less dynamic check but its a must */
            if (name.toLowerCase().contains("hdl") && category.toLowerCase().contains("hdl")) {
                return category;
            }
            if (name.toLowerCase().contains("ldl") && category.toLowerCase().contains("ldl")) {
                return category;
            }
            if (name.toLowerCase().contains("a1c") && category.toLowerCase().contains("a1c")) {
                return category;
            }
        }
        return value;
    }

    public String evaluateResultString(String category, double result) {
        String evulated = "Unknown Result";
        int value = (Integer) dataSet.get(category);
        if (result >= value) {
            evulated = BAD_RESULT;
        } else {
            evulated = GOOD_RESULT;
        }
        return evulated;
    }

    public int getThreshold(String category) {
        return dataSet.get(category);
    }

    public boolean evaluateResultstatus
            (String category, double userResult) {
        int threshold = (Integer) dataSet.get(category);
        return userResult >= threshold;
    }

}