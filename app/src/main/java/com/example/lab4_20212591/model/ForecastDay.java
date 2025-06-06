package com.example.lab4_20212591.model;

public class ForecastDay {
    private String date;
    private Day day;

    public String getDate() {
        return date;
    }

    public Day getDay() {
        return day;
    }

    public static class Day {
        private double maxtemp_c;
        private double mintemp_c;
        private Condition condition;

        public double getMaxtemp_c() {
            return maxtemp_c;
        }

        public double getMintemp_c() {
            return mintemp_c;
        }

        public Condition getCondition() {
            return condition;
        }

        public static class Condition {
            private String text;

            public String getText() {
                return text;
            }
        }
    }
}