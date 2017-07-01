// IBackService.aidl
package com.hwx.usbconnect.usbconncet;

// Declare any non-default types here with import statements
import com.hwx.usbconnect.usbconncet.MsgBackInterface;
interface IBackService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);
boolean isConnect();
 boolean sendMessage(String message);
 void getMessage(MsgBackInterface face);
}
