/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package motif;

/**
 *
 * @author Róbert Cuprik <robertcuprik at hotmail.com>
 */
public class PFMNotLoadedException extends Exception {

    public PFMNotLoadedException() {
    }

    public PFMNotLoadedException(String message) {
        super(message);
    }

    public PFMNotLoadedException(String message, Throwable cause) {
        super(message, cause);
    }

    public PFMNotLoadedException(Throwable cause) {
        super(cause);
    }

    public PFMNotLoadedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
