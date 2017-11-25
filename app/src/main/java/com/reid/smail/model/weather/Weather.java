package com.reid.smail.model.weather;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by reid on 2017/11/25.
 */

public class Weather implements Serializable {

    public Aqi aqi;
    public Basic basic;
    public List<DailyForecast> daily_forecast;
    public List<HourlyForecast> hourly_forecast;
    public HourlyForecast now;
    public String status;
    public Map<String, Suggestion> suggestion;

    public boolean isValid(){
        return "ok".equals(status) && aqi != null && basic != null
                && daily_forecast != null && hourly_forecast != null
                && now != null && suggestion != null;
    }


    public static class Aqi implements Serializable{
        public City city;

        @Override
        public String toString() {
            return "Aqi{" +
                    "city=" + city +
                    '}';
        }
    }

    public static class City implements Serializable{
        public String aqi;
        public String co;
        public String no2;
        public String o3;
        public String pm10;
        public String pm25;
        public String qlty;
        public String so2;

        @Override
        public String toString() {
            return "City{" +
                    "aqi='" + aqi + '\'' +
                    ", co='" + co + '\'' +
                    ", no2='" + no2 + '\'' +
                    ", o3='" + o3 + '\'' +
                    ", pm10='" + pm10 + '\'' +
                    ", pm25='" + pm25 + '\'' +
                    ", qlty='" + qlty + '\'' +
                    ", so2='" + so2 + '\'' +
                    '}';
        }
    }

    public static class Basic implements Serializable{
        public String city;
        public String cnty;
        public String id;
        public String lat;
        public String lon;
        public Time update;

        @Override
        public String toString() {
            return "Basic{" +
                    "city='" + city + '\'' +
                    ", cnty='" + cnty + '\'' +
                    ", id='" + id + '\'' +
                    ", lat='" + lat + '\'' +
                    ", lon='" + lon + '\'' +
                    ", update=" + update +
                    '}';
        }
    }

    public static class Time implements Serializable{
        public String loc;
        public String utc;

        @Override
        public String toString() {
            return "Time{" +
                    "loc='" + loc + '\'' +
                    ", utc='" + utc + '\'' +
                    '}';
        }
    }

    public static class Forecast implements Serializable{
        public Astro astro;
        public Cond cond;
        public String date;
        public String hum;
        public String pcpn;
        public String pop;
        public String pres;
        public String uv;
        public String vis;
        public Wind wind;

        @Override
        public String toString() {
            return "Forecast{" +
                    "astro=" + astro +
                    ", cond=" + cond +
                    ", date='" + date + '\'' +
                    ", hum='" + hum + '\'' +
                    ", pcpn='" + pcpn + '\'' +
                    ", pop='" + pop + '\'' +
                    ", pres='" + pres + '\'' +
                    ", uv='" + uv + '\'' +
                    ", vis='" + vis + '\'' +
                    ", wind=" + wind +
                    '}';
        }
    }

    public static class DailyForecast extends Forecast{
        public Tmp tmp;

        @Override
        public String toString() {
            return "DailyForecast{" +
                    "astro=" + astro +
                    ", cond=" + cond +
                    ", date='" + date + '\'' +
                    ", hum='" + hum + '\'' +
                    ", pcpn='" + pcpn + '\'' +
                    ", pop='" + pop + '\'' +
                    ", pres='" + pres + '\'' +
                    ", uv='" + uv + '\'' +
                    ", vis='" + vis + '\'' +
                    ", wind=" + wind +
                    ", tmp=" + tmp +
                    '}';
        }
    }

    public static class HourlyForecast extends Forecast{
        public String tmp;

        @Override
        public String toString() {
            return "HourlyForecast{" +
                    "astro=" + astro +
                    ", cond=" + cond +
                    ", date='" + date + '\'' +
                    ", hum='" + hum + '\'' +
                    ", pcpn='" + pcpn + '\'' +
                    ", pop='" + pop + '\'' +
                    ", pres='" + pres + '\'' +
                    ", uv='" + uv + '\'' +
                    ", vis='" + vis + '\'' +
                    ", wind=" + wind +
                    ", tmp='" + tmp + '\'' +
                    '}';
        }
    }

    public static class Astro implements Serializable{
        public String mr;
        public String ms;
        public String sr;
        public String ss;

        @Override
        public String toString() {
            return "Astro{" +
                    "mr='" + mr + '\'' +
                    ", ms='" + ms + '\'' +
                    ", sr='" + sr + '\'' +
                    ", ss='" + ss + '\'' +
                    '}';
        }
    }

    public static class Cond implements Serializable{
        public String code;
        public String code_d;
        public String code_n;
        public String txt;
        public String txt_d;
        public String txt_n;

        @Override
        public String toString() {
            return "Cond{" +
                    "code='" + code + '\'' +
                    ", code_d='" + code_d + '\'' +
                    ", code_n='" + code_n + '\'' +
                    ", txt='" + txt + '\'' +
                    ", txt_d='" + txt_d + '\'' +
                    ", txt_n='" + txt_n + '\'' +
                    '}';
        }
    }

    public static class Tmp implements Serializable{
        public String max;
        public String min;

        @Override
        public String toString() {
            return "Tmp{" +
                    "max='" + max + '\'' +
                    ", min='" + min + '\'' +
                    '}';
        }
    }

    public static class Wind implements Serializable{
        public String deg;
        public String dir;
        public String sc;
        public String spd;

        @Override
        public String toString() {
            return "Wind{" +
                    "deg='" + deg + '\'' +
                    ", dir='" + dir + '\'' +
                    ", sc='" + sc + '\'' +
                    ", spd='" + spd + '\'' +
                    '}';
        }
    }

    public static class Suggestion implements Serializable{
        public String brf;
        public String txt;

        @Override
        public String toString() {
            return "Suggestion{" +
                    "brf='" + brf + '\'' +
                    ", txt='" + txt + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Weather{" +
                "aqi=" + aqi +
                ", basic=" + basic +
                ", daily_forecast=" + daily_forecast +
                ", hourly_forecast=" + hourly_forecast +
                ", now=" + now +
                ", status='" + status + '\'' +
                ", suggestion=" + suggestion +
                '}';
    }
}
