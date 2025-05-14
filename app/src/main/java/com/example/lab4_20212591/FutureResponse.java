package com.example.lab4_20212591;

import java.util.List;

public class FutureResponse {

    private Forecast forecast;

    public Forecast getForecast() {
        return forecast;
    }

    public static class Forecast {
        private List<ForecastDay> forecastday;
        public List<ForecastDay> getForecastday() {
            return forecastday;
        }
    }

    public static class ForecastDay {
        private String date;
        private List<Hour> hour;
        public String getDate() { return date; }
        public List<Hour> getHour() { return hour; }
    }

    public static class Hour {
        private String time;
        private double temp_c;
        private int humidity;
        private int chance_of_rain;
        private Condition condition;

        public String getTime() { return time; }
        public double getTemp_c() { return temp_c; }
        public int getHumidity() { return humidity; }
        public int getChance_of_rain() { return chance_of_rain; }
        public Condition getCondition() { return condition; }
    }

    public static class Condition {
        private String text;
        public String getText() { return text; }
    }
}