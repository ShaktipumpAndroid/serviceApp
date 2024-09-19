package com.shaktipumplimted.serviceapp.main.bootomTabs.routes.routeList.model;

import java.io.Serializable;

public class RouteListModel implements Serializable {
    String routeId,routeName;

    public RouteListModel(String routeId, String routeName) {
        this.routeId = routeId;
        this.routeName = routeName;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }
}
