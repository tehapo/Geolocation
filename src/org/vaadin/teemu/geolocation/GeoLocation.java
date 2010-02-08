package org.vaadin.teemu.geolocation;

import java.util.Map;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;

/**
 * TODO - javadoc Server-side component for the VGeoLocation widget.
 */

/*- Related links:
 * 
 * http://teemu.virtuallypreinstalled.com/GeoLocation
 * 
 * https://developer.mozilla.org/En/Using_geolocation
 * https://developer.mozilla.org/en/XPCOM_Interface_Reference/NsIDOMGeoPositionCoords
 * http://smithsrus.com/gps-geolocation-in-safari-on-iphone-os-3-0/
 */
@com.vaadin.ui.ClientWidget(org.vaadin.teemu.geolocation.client.ui.VGeoLocation.class)
public class GeoLocation extends AbstractComponent {

    private boolean locationRequested;

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);

        if (locationRequested) {
            target.addAttribute("requestLocation", locationRequested);

            // reset location request state
            locationRequested = false;
        }
        // TODO Paint any component specific content by setting attributes
        // These attributes can be read in updateFromUIDL in the widget.

    }

    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        super.changeVariables(source, variables);

        if (variables.containsKey("latitude")
                && variables.containsKey("longitude")
                && variables.containsKey("accuracy")) {
            fireEvent(new GeoLocationRecievedEvent((Double) variables
                    .get("latitude"), (Double) variables.get("longitude"),
                    (Double) variables.get("accuracy")));
        }

        if (variables.containsKey("errorCode")) {
            fireEvent(new GeoLocationErrorEvent(GeoLocationError
                    .getErrorForCode(Integer.valueOf(variables.get("errorCode")
                            .toString()))));
        }

        if (variables.containsKey("unsupported")) {
            fireEvent(new GeoLocationUnsupportedEvent());
        }
    }

    public void addListener(GeoLocationListener listener) {
        addListener(GeoLocationRecievedEvent.class, listener,
                "geoLocationRecieved");
        addListener(GeoLocationUnsupportedEvent.class, listener,
                "geoLocationUnsupported");
        addListener(GeoLocationErrorEvent.class, listener, "geoLocationError");
    }

    public void requestGeoLocation() {
        locationRequested = true;
        requestRepaint();
    }

    /**
     * TODO javadoc
     * 
     */
    public class GeoLocationUnsupportedEvent extends Component.Event {
        public GeoLocationUnsupportedEvent() {
            super(GeoLocation.this);
        }
    }

    /**
     * TODO javadoc
     * 
     */
    public class GeoLocationRecievedEvent extends Component.Event {

        private final double latitude;

        private final double longitude;

        private final double accuracyInMeters;

        public GeoLocationRecievedEvent(double latitude, double longitude,
                double accuracy) {
            super(GeoLocation.this);
            this.latitude = latitude;
            this.longitude = longitude;
            this.accuracyInMeters = accuracy;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public double getAccuracyInMeters() {
            return accuracyInMeters;
        }
    }

    /**
     * TODO javadoc
     * 
     */
    public class GeoLocationErrorEvent extends Component.Event {

        private final GeoLocationError error;

        public GeoLocationErrorEvent(GeoLocationError error) {
            super(GeoLocation.this);
            this.error = error;
        }

        public GeoLocationError getError() {
            return error;
        }
    }

    /**
     * TODO javadoc
     * 
     */
    public interface GeoLocationListener {
        public void geoLocationRecieved(GeoLocationRecievedEvent event);

        public void geoLocationError(GeoLocationErrorEvent event);

        public void geoLocationUnsupported(GeoLocationUnsupportedEvent event);
    }
}
