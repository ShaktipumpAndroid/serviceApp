package com.shaktipumplimted.serviceapp.otpReader.BroadcastRecieverSMS;

public interface OTPReceiveListener {
    void onOTPReceived(String otp);

    void onOTPTimeOut();
}
