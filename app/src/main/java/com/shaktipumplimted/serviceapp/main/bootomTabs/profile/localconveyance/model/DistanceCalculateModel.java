package com.shaktipumplimted.serviceapp.main.bootomTabs.profile.localconveyance.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DistanceCalculateModel {
     @SerializedName("routes")
    @Expose
    private List<Route> routes;
    @SerializedName("status")
    @Expose
    private String status;
    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public class Route {
        @SerializedName("copyrights")
        @Expose
        private String copyrights;
        @SerializedName("legs")
        @Expose
        private List<Leg> legs;
        @SerializedName("summary")
        @Expose
        private String summary;
        @SerializedName("warnings")
        @Expose
        private List<Object> warnings;
        @SerializedName("waypoint_order")
        @Expose
        private List<Object> waypointOrder;

        public String getCopyrights() {
            return copyrights;
        }

        public void setCopyrights(String copyrights) {
            this.copyrights = copyrights;
        }

        public List<Leg> getLegs() {
            return legs;
        }

        public void setLegs(List<Leg> legs) {
            this.legs = legs;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public List<Object> getWarnings() {
            return warnings;
        }

        public void setWarnings(List<Object> warnings) {
            this.warnings = warnings;
        }

        public List<Object> getWaypointOrder() {
            return waypointOrder;
        }

        public void setWaypointOrder(List<Object> waypointOrder) {
            this.waypointOrder = waypointOrder;
        }

        public class Leg {

            @SerializedName("distance")
            @Expose
            private Distance distance;
            @SerializedName("duration")
            @Expose
            private Duration duration;
            @SerializedName("end_address")
            @Expose
            private String endAddress;
            @SerializedName("end_location")
            @Expose
            private EndLocation endLocation;
            @SerializedName("start_address")
            @Expose
            private String startAddress;
            @SerializedName("start_location")
            @Expose
            private StartLocation startLocation;
            @SerializedName("traffic_speed_entry")
            @Expose
            private List<Object> trafficSpeedEntry;
            @SerializedName("via_waypoint")
            @Expose
            private List<Object> viaWaypoint;

            public Distance getDistance() {
                return distance;
            }

            public void setDistance(Distance distance) {
                this.distance = distance;
            }

            public Duration getDuration() {
                return duration;
            }

            public void setDuration(Duration duration) {
                this.duration = duration;
            }

            public String getEndAddress() {
                return endAddress;
            }

            public void setEndAddress(String endAddress) {
                this.endAddress = endAddress;
            }

            public EndLocation getEndLocation() {
                return endLocation;
            }

            public void setEndLocation(EndLocation endLocation) {
                this.endLocation = endLocation;
            }

            public String getStartAddress() {
                return startAddress;
            }

            public void setStartAddress(String startAddress) {
                this.startAddress = startAddress;
            }

            public StartLocation getStartLocation() {
                return startLocation;
            }

            public void setStartLocation(StartLocation startLocation) {
                this.startLocation = startLocation;
            }


            public List<Object> getTrafficSpeedEntry() {
                return trafficSpeedEntry;
            }

            public void setTrafficSpeedEntry(List<Object> trafficSpeedEntry) {
                this.trafficSpeedEntry = trafficSpeedEntry;
            }

            public List<Object> getViaWaypoint() {
                return viaWaypoint;
            }

            public void setViaWaypoint(List<Object> viaWaypoint) {
                this.viaWaypoint = viaWaypoint;
            }


            public class Distance {

                @SerializedName("text")
                @Expose
                private String text;
                @SerializedName("value")
                @Expose
                private Integer value;

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }

                public Integer getValue() {
                    return value;
                }

                public void setValue(Integer value) {
                    this.value = value;
                }

                @Override
                public String toString() {
                    return "Distance{" +
                            "text='" + text + '\'' +
                            ", value=" + value +
                            '}';
                }
            }

            public class Duration {

                @SerializedName("text")
                @Expose
                private String text;
                @SerializedName("value")
                @Expose
                private Integer value;

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }

                public Integer getValue() {
                    return value;
                }

                public void setValue(Integer value) {
                    this.value = value;
                }

                @Override
                public String toString() {
                    return "Duration{" +
                            "text='" + text + '\'' +
                            ", value=" + value +
                            '}';
                }
            }

            public class StartLocation {

                @SerializedName("lat")
                @Expose
                private Double lat;
                @SerializedName("lng")
                @Expose
                private Double lng;

                public Double getLat() {
                    return lat;
                }

                public void setLat(Double lat) {
                    this.lat = lat;
                }

                public Double getLng() {
                    return lng;
                }

                public void setLng(Double lng) {
                    this.lng = lng;
                }

                @Override
                public String toString() {
                    return "StartLocation{" +
                            "lat=" + lat +
                            ", lng=" + lng +
                            '}';
                }
            }
            public class EndLocation {

                @SerializedName("lat")
                @Expose
                private Double lat;
                @SerializedName("lng")
                @Expose
                private Double lng;

                public Double getLat() {
                    return lat;
                }

                public void setLat(Double lat) {
                    this.lat = lat;
                }

                public Double getLng() {
                    return lng;
                }

                public void setLng(Double lng) {
                    this.lng = lng;
                }

                @Override
                public String toString() {
                    return "EndLocation{" +
                            "lat=" + lat +
                            ", lng=" + lng +
                            '}';
                }
            }

            @Override
            public String toString() {
                return "Leg{" +
                        "distance=" + distance +
                        ", duration=" + duration +
                        ", endAddress='" + endAddress + '\'' +
                        ", endLocation=" + endLocation +
                        ", startAddress='" + startAddress + '\'' +
                        ", startLocation=" + startLocation +
                        ", trafficSpeedEntry=" + trafficSpeedEntry +
                        ", viaWaypoint=" + viaWaypoint +
                        '}';
            }
        }
    }

    @Override
    public String toString() {
        return "DistanceCalculateModel{" +
                "routes=" + routes +
                ", status='" + status + '\'' +
                '}';
    }
}
