package com.kevinho.WeatherApp.view;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

@SpringUI(path = "")
public class MainView extends UI {
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Label label = new Label("Hello");

        setContent(label);
    }
}
