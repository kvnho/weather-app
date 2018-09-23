package com.kevinho.WeatherApp.view;

import com.kevinho.WeatherApp.controller.WeatherService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@SpringUI(path = "")
public class MainView extends UI {

    @Autowired
    private WeatherService weatherService;
    private VerticalLayout mainLayout;
    private TextField cityTestField;
    private Button weatherButton;
    private Label currentLocation;
    private Label temp;
    private Label weatherMinTemp;
    private Label weatherMaxTemp;
    private Label pressureLabel;
    private Label humidityLabel;
    private Label windSpeedLabel;
    private Label sunriseLabel;
    private Label sunsetLabel;


    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setLayout();
        setHeader();
        setForm();
        dashboardTitle();
        dashboardDescription();

        weatherButton.addClickListener(event -> {
            if(!cityTestField.getValue().equals("")){
                try {
                    updateUI();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else{
                Notification.show("Please enter a city");
            }
        });


    }


    public void setLayout(){
        mainLayout = new VerticalLayout();
        mainLayout.setWidth("100%");
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);

        mainLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        setContent(mainLayout);
    }

    private void setHeader(){
        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        Label title = new Label("Weather");
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_BOLD);
        title.addStyleName(ValoTheme.LABEL_COLORED);

        headerLayout.addComponents(title);

        mainLayout.addComponents(headerLayout);
    }

    private void setForm(){
        HorizontalLayout formLayout = new HorizontalLayout();
        formLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        formLayout.setSpacing(true);
        formLayout.setMargin(true);

        cityTestField = new TextField();
        cityTestField.setWidth("70%");
        cityTestField.setPlaceholder("Enter a city");

        weatherButton = new Button();
        weatherButton.setIcon(VaadinIcons.SEARCH);

        formLayout.addComponents(cityTestField, weatherButton);

        mainLayout.addComponents(formLayout);
    }

    private void dashboardTitle(){
        HorizontalLayout dashboard = new HorizontalLayout();
        dashboard.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        currentLocation = new Label();
        currentLocation.addStyleName(ValoTheme.LABEL_H2);
        currentLocation.addStyleName(ValoTheme.LABEL_LIGHT);

        temp = new Label();
        temp.addStyleName(ValoTheme.LABEL_BOLD);
        temp.addStyleName(ValoTheme.LABEL_H1);
        temp.addStyleName(ValoTheme.LABEL_LIGHT);

        dashboard.addComponents(currentLocation, temp);

        mainLayout.addComponent(dashboard);
    }

    private void dashboardDescription(){
        HorizontalLayout mainDescriptionLayout = new HorizontalLayout();
        mainDescriptionLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        VerticalLayout descriptionLayout = new VerticalLayout();
        descriptionLayout.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        descriptionLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        weatherMinTemp = new Label();
        weatherMaxTemp = new Label();

        descriptionLayout.addComponents(weatherMinTemp, weatherMaxTemp);

        VerticalLayout pressureLayout = new VerticalLayout();
        pressureLayout.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        pressureLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        pressureLabel = new Label();
        humidityLabel = new Label();
        windSpeedLabel = new Label();
        sunriseLabel = new Label();
        sunsetLabel = new Label();

        pressureLayout.addComponents(pressureLabel, humidityLabel, windSpeedLabel, sunriseLabel, sunsetLabel);

        mainDescriptionLayout.addComponents(descriptionLayout, pressureLayout);

        mainLayout.addComponent(mainDescriptionLayout);
    }

    private void updateUI() throws IOException, JSONException {
        String city = cityTestField.getValue();
        String desc = "";

        JSONObject mainObject = weatherService.fetchMainObject(city);

        JSONArray weatherArray = weatherService.fetchWeatherArray(city);
        for(int i = 0; i < weatherArray.length(); i++){
            JSONObject weatherObject = weatherArray.getJSONObject(i);
            desc = weatherObject.getString("description");
        }

        JSONObject systemObject = weatherService.fetchSysObject(city);
        long sunrise = systemObject.getLong("sunrise") * 1000;
        long sunset = systemObject.getLong("sunset") * 1000;

        JSONObject windObject = weatherService.fetchWindObject(city);

        currentLocation.setValue(desc + " in " + city);
        temp.setValue(mainObject.getInt("temp") + " F");
        pressureLabel.setValue("Pressure: " + mainObject.getInt("pressure") + " HPA");
        humidityLabel.setValue("Humidity: " + mainObject.getInt("humidity") + "%");
        weatherMinTemp.setValue("Low: " + mainObject.getInt("temp_min") + " F");
        weatherMaxTemp.setValue("High: " + mainObject.getInt("temp_max") + " F");
        sunriseLabel.setValue("Sunrise: " + convertTime(sunrise));
        sunsetLabel.setValue("Sunset: " + convertTime(sunset));
        windSpeedLabel.setValue("Wind: " + windObject.getInt("speed") + " MPH");

    }

    private String convertTime(long time){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yy hh:mm aa");
        return  dateFormat.format(new Date(time));
    }

}
