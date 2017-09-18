package com.SampleApp.row;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 03-04-2016.
 */
public class GetAddressModel {

    private List<Result> results = new ArrayList<Result>();
    private String status;
    /**
     *
     * @return
     * The results
     */
    public List<Result> getResults() {
        return results;
    }

    /**
     *
     * @param results
     * The results
     */
    public void setResults(List<Result> results) {
        this.results = results;
    }

    /**
     *
     * @return
     * The status
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public class Result {

        private List<Address_component> address_components = new ArrayList<Address_component>();
        private String formatted_address;
        private Geometry geometry;
        private String place_id;
        private List<String> types = new ArrayList<String>();

        /**
         *
         * @return
         * The address_components
         */
        public List<Address_component> getAddress_components() {
            return address_components;
        }

        /**
         *
         * @param address_components
         * The address_components
         */
        public void setAddress_components(List<Address_component> address_components) {
            this.address_components = address_components;
        }

        /**
         *
         * @return
         * The formatted_address
         */
        public String getFormatted_address() {
            return formatted_address;
        }

        /**
         *
         * @param formatted_address
         * The formatted_address
         */
        public void setFormatted_address(String formatted_address) {
            this.formatted_address = formatted_address;
        }

        /**
         *
         * @return
         * The geometry
         */
        public Geometry getGeometry() {
            return geometry;
        }

        /**
         *
         * @param geometry
         * The geometry
         */
        public void setGeometry(Geometry geometry) {
            this.geometry = geometry;
        }

        /**
         *
         * @return
         * The place_id
         */
        public String getPlace_id() {
            return place_id;
        }

        /**
         *
         * @param place_id
         * The place_id
         */
        public void setPlace_id(String place_id) {
            this.place_id = place_id;
        }

        /**
         *
         * @return
         * The types
         */
        public List<String> getTypes() {
            return types;
        }

        /**
         *
         * @param types
         * The types
         */
        public void setTypes(List<String> types) {
            this.types = types;
        }

    }

    public class Address_component {

        private String long_name;
        private String short_name;
        private List<String> types = new ArrayList<String>();

        /**
         *
         * @return
         * The long_name
         */
        public String getLong_name() {
            return long_name;
        }

        /**
         *
         * @param long_name
         * The long_name
         */
        public void setLong_name(String long_name) {
            this.long_name = long_name;
        }

        /**
         *
         * @return
         * The short_name
         */
        public String getShort_name() {
            return short_name;
        }

        /**
         *
         * @param short_name
         * The short_name
         */
        public void setShort_name(String short_name) {
            this.short_name = short_name;
        }

        /**
         *
         * @return
         * The types
         */
        public List<String> getTypes() {
            return types;
        }

        /**
         *
         * @param types
         * The types
         */
        public void setTypes(List<String> types) {
            this.types = types;
        }

    }

    public class Bounds {

        private Northeast northeast;
        private Southwest southwest;

        /**
         *
         * @return
         * The northeast
         */
        public Northeast getNortheast() {
            return northeast;
        }

        /**
         *
         * @param northeast
         * The northeast
         */
        public void setNortheast(Northeast northeast) {
            this.northeast = northeast;
        }

        /**
         *
         * @return
         * The southwest
         */
        public Southwest getSouthwest() {
            return southwest;
        }

        /**
         *
         * @param southwest
         * The southwest
         */
        public void setSouthwest(Southwest southwest) {
            this.southwest = southwest;
        }

    }

    public class Geometry {

        private Bounds bounds;
        private Location location;
        private String location_type;
        private Viewport viewport;

        /**
         *
         * @return
         * The bounds
         */
        public Bounds getBounds() {
            return bounds;
        }

        /**
         *
         * @param bounds
         * The bounds
         */
        public void setBounds(Bounds bounds) {
            this.bounds = bounds;
        }

        /**
         *
         * @return
         * The location
         */
        public Location getLocation() {
            return location;
        }

        /**
         *
         * @param location
         * The location
         */
        public void setLocation(Location location) {
            this.location = location;
        }

        /**
         *
         * @return
         * The location_type
         */
        public String getLocation_type() {
            return location_type;
        }

        /**
         *
         * @param location_type
         * The location_type
         */
        public void setLocation_type(String location_type) {
            this.location_type = location_type;
        }

        /**
         *
         * @return
         * The viewport
         */
        public Viewport getViewport() {
            return viewport;
        }

        /**
         *
         * @param viewport
         * The viewport
         */
        public void setViewport(Viewport viewport) {
            this.viewport = viewport;
        }

    }

    public class Location {

        private Double lat;
        private Double lng;

        /**
         *
         * @return
         * The lat
         */
        public Double getLat() {
            return lat;
        }

        /**
         *
         * @param lat
         * The lat
         */
        public void setLat(Double lat) {
            this.lat = lat;
        }

        /**
         *
         * @return
         * The lng
         */
        public Double getLng() {
            return lng;
        }

        /**
         *
         * @param lng
         * The lng
         */
        public void setLng(Double lng) {
            this.lng = lng;
        }

    }

    public class Northeast {

        private Float lat;
        private Float lng;

        /**
         *
         * @return
         * The lat
         */
        public Float getLat() {
            return lat;
        }

        /**
         *
         * @param lat
         * The lat
         */
        public void setLat(Float lat) {
            this.lat = lat;
        }

        /**
         *
         * @return
         * The lng
         */
        public Float getLng() {
            return lng;
        }

        /**
         *
         * @param lng
         * The lng
         */
        public void setLng(Float lng) {
            this.lng = lng;
        }

    }

    public class Northeast_ {

        private Float lat;
        private Float lng;

        /**
         *
         * @return
         * The lat
         */
        public Float getLat() {
            return lat;
        }

        /**
         *
         * @param lat
         * The lat
         */
        public void setLat(Float lat) {
            this.lat = lat;
        }

        /**
         *
         * @return
         * The lng
         */
        public Float getLng() {
            return lng;
        }

        /**
         *
         * @param lng
         * The lng
         */
        public void setLng(Float lng) {
            this.lng = lng;
        }

    }

    public class Southwest {

        private Float lat;
        private Float lng;

        /**
         *
         * @return
         * The lat
         */
        public Float getLat() {
            return lat;
        }

        /**
         *
         * @param lat
         * The lat
         */
        public void setLat(Float lat) {
            this.lat = lat;
        }

        /**
         *
         * @return
         * The lng
         */
        public Float getLng() {
            return lng;
        }

        /**
         *
         * @param lng
         * The lng
         */
        public void setLng(Float lng) {
            this.lng = lng;
        }

    }

    public class Southwest_ {

        private Float lat;
        private Float lng;

        /**
         *
         * @return
         * The lat
         */
        public Float getLat() {
            return lat;
        }

        /**
         *
         * @param lat
         * The lat
         */
        public void setLat(Float lat) {
            this.lat = lat;
        }

        /**
         *
         * @return
         * The lng
         */
        public Float getLng() {
            return lng;
        }

        /**
         *
         * @param lng
         * The lng
         */
        public void setLng(Float lng) {
            this.lng = lng;
        }

    }


    public class Viewport {

        private Northeast_ northeast;
        private Southwest_ southwest;

        /**
         *
         * @return
         * The northeast
         */
        public Northeast_ getNortheast() {
            return northeast;
        }

        /**
         *
         * @param northeast
         * The northeast
         */
        public void setNortheast(Northeast_ northeast) {
            this.northeast = northeast;
        }

        /**
         *
         * @return
         * The southwest
         */
        public Southwest_ getSouthwest() {
            return southwest;
        }

        /**
         *
         * @param southwest
         * The southwest
         */
        public void setSouthwest(Southwest_ southwest) {
            this.southwest = southwest;
        }

    }

}

