package com.kevinho.WeatherApp.controller;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class WeatherService {
    private OkHttpClient client;
    private Response response;

    public JSONObject getWeather(String city) throws IOException, JSONException {
        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=imperial&appid=bd350e197371043a931f234348c643b8")
                .build();
        response = client.newCall(request).execute();
        return new JSONObject(response.body().string());
    }

    public JSONArray fetchWeatherArray(String city) throws IOException, JSONException {
        JSONArray weatherJsonArray = getWeather(city).getJSONArray("weather");
        return weatherJsonArray;
    }

    public JSONObject fetchMainObject(String city) throws IOException, JSONException {
        JSONObject mainObject = getWeather(city).getJSONObject("main");
        return mainObject;
    }

    public JSONObject fetchWindObject(String city) throws IOException, JSONException {
        JSONObject windObject = getWeather(city).getJSONObject("wind");
        return windObject;
    }

    public JSONObject fetchSysObject(String city) throws IOException, JSONException {
        JSONObject sysObject = getWeather(city).getJSONObject("sys");
        return sysObject;
    }

}
