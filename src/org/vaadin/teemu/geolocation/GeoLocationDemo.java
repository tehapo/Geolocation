package org.vaadin.teemu.geolocation;

import java.awt.geom.Point2D;

import org.vaadin.hezamu.googlemapwidget.GoogleMap;
import org.vaadin.hezamu.googlemapwidget.overlay.BasicMarker;
import org.vaadin.teemu.geolocation.GeoLocation.GeoLocationErrorEvent;
import org.vaadin.teemu.geolocation.GeoLocation.GeoLocationRecievedEvent;
import org.vaadin.teemu.geolocation.GeoLocation.GeoLocationUnsupportedEvent;

import com.vaadin.Application;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

public class GeoLocationDemo extends Application implements
        Button.ClickListener, GeoLocation.GeoLocationListener {

    private Label latitude;
    private Label longitude;
    private GeoLocation geoLocation;
    private Window mainWindow;
    private GoogleMap map;

    @Override
    public void init() {
        mainWindow = new Window("GeoLocationDemo");
        setMainWindow(mainWindow);

        HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.setSizeFull();
        mainWindow.setContent(mainLayout);

        VerticalLayout leftSide = new VerticalLayout();
        leftSide.setWidth("250px");
        leftSide.setMargin(true);
        leftSide.setSpacing(true);
        mainLayout.addComponent(leftSide);
        leftSide
                .addComponent(new Label(
                        "<h1>GeoLocation demo</h1>"
                                + "<p>Supports Firefox 3.5+</p>"
                                + "<p>This demo application uses the <a href=\"http://vaadin.com/forum/-/message_boards/message/95530\">Google Maps Widget</a> by Henri Muurimaa.</p>",
                        Label.CONTENT_XHTML));

        latitude = new Label("not yet requested");
        longitude = new Label("not yet requested");

        GridLayout layout = new GridLayout(2, 2);
        layout.addComponent(new Label("Latitude:"));
        layout.addComponent(latitude);
        layout.addComponent(new Label("Longitude:"));
        layout.addComponent(longitude);
        leftSide.addComponent(layout);

        Button b = new Button("Show me on the map!", this);
        leftSide.addComponent(b);

        map = new GoogleMap(this, new Point2D.Double(21.0, 55.0), 3);
        map.setSizeFull();
        mainLayout.addComponent(map);

        mainLayout.setExpandRatio(map, 1f);

        // GeoLocation must be added to the Application even though
        // it's not visible in any way.
        geoLocation = new GeoLocation();
        geoLocation.addListener(this);
        leftSide.addComponent(geoLocation);
    }

    public void buttonClick(ClickEvent event) {
        geoLocation.requestGeoLocation();
    }

    public void geoLocationRecieved(GeoLocationRecievedEvent event) {
        mainWindow.showNotification(event.getLatitude() + ", "
                + event.getLongitude());
        latitude.setValue(event.getLatitude());
        longitude.setValue(event.getLongitude());

        Point2D.Double point = new Point2D.Double(event.getLongitude(), event
                .getLatitude());
        map.setCenter(point);
        map.addMarker(new BasicMarker(1L, point, "Your current location"));
        map.setZoom(14);
    }

    public void geoLocationUnsupported(GeoLocationUnsupportedEvent event) {
        mainWindow.showNotification("GeoLocation unsupported by the terminal.");
    }

    public void geoLocationError(GeoLocationErrorEvent event) {
        String message = event.getError().name();
        if (event.getError() == GeoLocationError.PERMISSION_DENIED) {
            message = "Come out, come out where ever you are!";
        }
        mainWindow.showNotification(message);
    }

}
