package org.vaadin.teemu.geolocation;

/**
 * 
 * Related links:
 * http://dev.w3.org/geo/api/spec-source.html#position_error_interface
 * 
 */
public enum GeoLocationError {

    /**
     * Requesting location information failed for an unknown reason.
     */
    UNKNOWN_ERROR(0),

    /**
     * The location acquisition process failed because the application origin
     * does not have permission to use the Geolocation API.
     */
    PERMISSION_DENIED(1),

    /**
     * The position of the device could not be determined. For instance, one or
     * more of the location providers used in the location acquisition process
     * reported an internal error that caused the process to fail entirely.
     */
    POSITION_UNAVAILABLE(2),

    /**
     * The length of time specified by the timeout property has elapsed before
     * the implementation could successfully acquire a new Position object.
     */
    TIMEOUT(3);

    private final int errorCode;

    private GeoLocationError(int errorCode) {
        this.errorCode = errorCode;
    }

    static GeoLocationError getErrorForCode(int errorCode) {
        for (GeoLocationError error : GeoLocationError.values()) {
            if (error.errorCode == errorCode) {
                return error;
            }
        }
        return UNKNOWN_ERROR;
    }

}
