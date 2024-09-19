package com.shaktipumplimted.serviceapp.Utils.common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SpinnerDataModel {
    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("user_categories")
        @Expose
        private List<UserCategory> userCategories;
        @SerializedName("water_sources")
        @Expose
        private List<WaterSource> waterSources;
        @SerializedName("internet_connectivities")
        @Expose
        private List<InternetConnectivity> internetConnectivities;
        @SerializedName("irrigation_installed")
        @Expose
        private List<IrrigationInstalled> irrigationInstalled;
        @SerializedName("electric_connection_types")
        @Expose
        private List<ElectricConnectionType> electricConnectionTypes;
        @SerializedName("pump_categories")
        @Expose
        private List<PumpCategory> pumpCategories;
        @SerializedName("pump_types")
        @Expose
        private List<PumpType> pumpTypes;
        @SerializedName("pump_ratings")
        @Expose
        private List<PumpRating> pumpRatings;
        @SerializedName("neutral_availabilities")
        @Expose
        private List<NeutralAvailability> neutralAvailabilities;
        @SerializedName("current_types")
        @Expose
        private List<CurrentType> currentTypes;
        @SerializedName("borewell_sizes")
        @Expose
        private List<BorewellSize> borewellSizes;

        public List<UserCategory> getUserCategories() {
            return userCategories;
        }

        public void setUserCategories(List<UserCategory> userCategories) {
            this.userCategories = userCategories;
        }

        public List<WaterSource> getWaterSources() {
            return waterSources;
        }

        public void setWaterSources(List<WaterSource> waterSources) {
            this.waterSources = waterSources;
        }

        public List<InternetConnectivity> getInternetConnectivities() {
            return internetConnectivities;
        }

        public void setInternetConnectivities(List<InternetConnectivity> internetConnectivities) {
            this.internetConnectivities = internetConnectivities;
        }

        public List<IrrigationInstalled> getIrrigationInstalled() {
            return irrigationInstalled;
        }

        public void setIrrigationInstalled(List<IrrigationInstalled> irrigationInstalled) {
            this.irrigationInstalled = irrigationInstalled;
        }

        public List<ElectricConnectionType> getElectricConnectionTypes() {
            return electricConnectionTypes;
        }

        public void setElectricConnectionTypes(List<ElectricConnectionType> electricConnectionTypes) {
            this.electricConnectionTypes = electricConnectionTypes;
        }

        public List<PumpCategory> getPumpCategories() {
            return pumpCategories;
        }

        public void setPumpCategories(List<PumpCategory> pumpCategories) {
            this.pumpCategories = pumpCategories;
        }

        public List<PumpType> getPumpTypes() {
            return pumpTypes;
        }

        public void setPumpTypes(List<PumpType> pumpTypes) {
            this.pumpTypes = pumpTypes;
        }

        public List<PumpRating> getPumpRatings() {
            return pumpRatings;
        }

        public void setPumpRatings(List<PumpRating> pumpRatings) {
            this.pumpRatings = pumpRatings;
        }

        public List<NeutralAvailability> getNeutralAvailabilities() {
            return neutralAvailabilities;
        }

        public void setNeutralAvailabilities(List<NeutralAvailability> neutralAvailabilities) {
            this.neutralAvailabilities = neutralAvailabilities;
        }

        public List<CurrentType> getCurrentTypes() {
            return currentTypes;
        }

        public void setCurrentTypes(List<CurrentType> currentTypes) {
            this.currentTypes = currentTypes;
        }

        public List<BorewellSize> getBorewellSizes() {
            return borewellSizes;
        }

        public void setBorewellSizes(List<BorewellSize> borewellSizes) {
            this.borewellSizes = borewellSizes;
        }

        public class UserCategory {

            @SerializedName("code")
            @Expose
            private String code;
            @SerializedName("description")
            @Expose
            private String description;

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

        }
        public class WaterSource {

            @SerializedName("code")
            @Expose
            private String code;
            @SerializedName("description")
            @Expose
            private String description;

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

        }

        public class InternetConnectivity {

            @SerializedName("code")
            @Expose
            private String code;
            @SerializedName("description")
            @Expose
            private String description;

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

        }
        public class IrrigationInstalled {

            @SerializedName("code")
            @Expose
            private String code;
            @SerializedName("description")
            @Expose
            private String description;

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

        }
        public class ElectricConnectionType {

            @SerializedName("code")
            @Expose
            private String code;
            @SerializedName("description")
            @Expose
            private String description;

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

        }

        public class PumpCategory {

            @SerializedName("code")
            @Expose
            private String code;
            @SerializedName("description")
            @Expose
            private String description;

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

        }

        public class PumpType {

            @SerializedName("code")
            @Expose
            private String code;
            @SerializedName("description")
            @Expose
            private String description;

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

        }
        public class PumpRating {

            @SerializedName("code")
            @Expose
            private String code;
            @SerializedName("description")
            @Expose
            private String description;

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

        }

        public class NeutralAvailability {

            @SerializedName("code")
            @Expose
            private String code;
            @SerializedName("description")
            @Expose
            private String description;

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

        }

        public class CurrentType {

            @SerializedName("code")
            @Expose
            private String code;
            @SerializedName("description")
            @Expose
            private String description;

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

        }
        public class BorewellSize {

            @SerializedName("code")
            @Expose
            private String code;
            @SerializedName("description")
            @Expose
            private String description;

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

        }

    }
}
