package org.vaadin.teemu.geolocation;

public enum GeoLocationError {

    UNKNOWN_ERROR(0), PERMISSION_DENIED(1);

    private final int errorCode;

    private GeoLocationError(int errorCode) {
        this.errorCode = errorCode;
    }

    public static GeoLocationError getErrorForCode(int errorCode) {
        for (GeoLocationError error : GeoLocationError.values()) {
            if (error.errorCode == errorCode) {
                return error;
            }
        }
        return UNKNOWN_ERROR;
    }

}
