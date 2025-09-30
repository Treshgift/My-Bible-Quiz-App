package com.example.cs_350_assigment;

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;

public class Sample {
    public static void main(String[] args) {
        Gson gson = new Gson();

        Map<String, String> sample = new HashMap<>();
        sample.put("course", "CS350");
        sample.put("status", "complete");

        String json = gson.toJson(sample);
        System.out.println(json); // Output: {"course":"CS350","status":"complete"}
    }
}
