package me.hypherionmc.cursegradle.jsonresponse

/**
 * Returned as JSON when an error occurs
 */
class CurseError {

    /**
     * The HTTP status code
     */
    int errorCode

    /**
     * The error message
     */
    String errorMessage

    @Override
    public String toString() {
        return "CurseError{" +
               "errorCode=" + errorCode +
               ", errorMessage='" + errorMessage + '\'' +
               '}'
    }
}
