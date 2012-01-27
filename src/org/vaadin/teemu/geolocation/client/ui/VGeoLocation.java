package org.vaadin.teemu.geolocation.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VGeoLocation extends Widget implements Paintable {

    /** Set the CSS class name to allow styling. */
    public static final String CLASSNAME = "v-geolocation";

    /** The client side widget identifier */
    protected String paintableId;

    /** Reference to the server connection object. */
    ApplicationConnection client;

    private boolean immediate = true;

    /**
     * The constructor should first call super() to initialize the component and
     * then handle any initialization relevant to Vaadin.
     */
    public VGeoLocation() {
        // TODO Example code is extending GWT Widget so it must set a root
        // element.
        // Change to proper element or remove if extending another widget
        setElement(Document.get().createDivElement());
        getElement().getStyle().setProperty("display", "none");

        // This method call of the Paintable interface sets the component
        // style name in DOM tree
        setStyleName(CLASSNAME);
    }

    /**
     * Called whenever an update is received from the server
     */
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        // This call should be made first.
        // It handles sizes, captions, tooltips, etc. automatically.
        if (client.updateComponent(this, uidl, true)) {
            // If client.updateComponent returns true there has been no changes
            // and we do not need to update anything.
            return;
        }

        // Save reference to server connection object to be able to send
        // user interaction later
        this.client = client;

        // Save the client side identifier (paintable id) for the widget
        paintableId = uidl.getId();

        if (uidl.hasAttribute("requestLocation")) {
            if (isGeoLocationSupported()) {
                getCurrentPosition(this);
            } else {
                client.updateVariable(paintableId, "unsupported", true,
                        immediate);
            }
        }
    }

    private void locationCallback(double latitude, double longitude,
            double accuracy) {
        client.updateVariable(paintableId, "accuracy", accuracy, false);
        client.updateVariable(paintableId, "latitude", latitude, false);
        client.updateVariable(paintableId, "longitude", longitude, immediate);
    }

    private void errorCallback(int errorCode) {
        client.updateVariable(paintableId, "errorCode", errorCode, immediate);
    }

    private native void getCurrentPosition(VGeoLocation instance)
    /*-{
        if (navigator.geolocation) {
            var callbackInstance = instance;
            navigator.geolocation.getCurrentPosition(
                function(position) {
                    var latitude = position.coords.latitude;
                    var longitude = position.coords.longitude;
                    var accuracy = position.coords.accuracy;
                    callbackInstance.@org.vaadin.teemu.geolocation.client.ui.VGeoLocation::locationCallback(DDD)(latitude, longitude, accuracy);
                },
                function(error) {
                    callbackInstance.@org.vaadin.teemu.geolocation.client.ui.VGeoLocation::errorCallback(I)(error.code);
                }
            );
        }
    }-*/;

    private native boolean isGeoLocationSupported()
    /*-{
        if (navigator.geolocation) {
            return true;
        } else {
            return false;
        }
    }-*/;

}
