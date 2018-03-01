
package com.example.mac.airnow;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public SearchResponse withStatus(String status) {
        this.status = status;
        return this;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public SearchResponse withData(List<Datum> data) {
        this.data = data;
        return this;
    }

    public static class Datum {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("aqi")
        @Expose
        private String aqi;
        @SerializedName("time")
        @Expose
        private Time time;
        @SerializedName("station")
        @Expose
        private Station station;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public Datum withUid(String uid) {
            this.uid = uid;
            return this;
        }

        public String getAqi() {
            return aqi;
        }

        public void setAqi(String aqi) {
            this.aqi = aqi;
        }

        public Datum withAqi(String aqi) {
            this.aqi = aqi;
            return this;
        }

        public Time getTime() {
            return time;
        }

        public void setTime(Time time) {
            this.time = time;
        }

        public Datum withTime(Time time) {
            this.time = time;
            return this;
        }

        public Station getStation() {
            return station;
        }

        public void setStation(Station station) {
            this.station = station;
        }

        public Datum withStation(Station station) {
            this.station = station;
            return this;
        }

    }

    public static class Station {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("geo")
        @Expose
        private List<String> geo = null;
        @SerializedName("url")
        @Expose
        private String url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Station withName(String name) {
            this.name = name;
            return this;
        }

        public List<String> getGeo() {
            return geo;
        }

        public void setGeo(List<String> geo) {
            this.geo = geo;
        }

        public Station withGeo(List<String> geo) {
            this.geo = geo;
            return this;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Station withUrl(String url) {
            this.url = url;
            return this;
        }

    }

    public static class Time {

        @SerializedName("tz")
        @Expose
        private String tz;
        @SerializedName("stime")
        @Expose
        private String stime;
        @SerializedName("vtime")
        @Expose
        private String vtime;

        public String getTz() {
            return tz;
        }

        public void setTz(String tz) {
            this.tz = tz;
        }

        public Time withTz(String tz) {
            this.tz = tz;
            return this;
        }

        public String getStime() {
            return stime;
        }

        public void setStime(String stime) {
            this.stime = stime;
        }

        public Time withStime(String stime) {
            this.stime = stime;
            return this;
        }

        public String getVtime() {
            return vtime;
        }

        public void setVtime(String vtime) {
            this.vtime = vtime;
        }

        public Time withVtime(String vtime) {
            this.vtime = vtime;
            return this;
        }

    }
}
