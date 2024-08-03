package org.firstinspires.ftc.teamcode;


import android.hardware.usb.UsbDevice;

import androidx.annotation.NonNull;

import com.hoho.android.usbserial.driver.CommonUsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;




import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.util.Log;

import com.hoho.android.usbserial.util.MonotonicClock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class VEX_GPS_Driver implements UsbSerialDriver {

    private UsbDevice mDevice;
    private List<UsbSerialPort> mPorts;

    public VEX_GPS_Driver(@NonNull UsbDevice device) {
        mDevice = device;
        mPorts = new ArrayList<>();
        for (int port = 0; port < device.getInterfaceCount(); port++) {
            mPorts.add(new GPS_Serial_Port(mDevice, port));
        }
    }

    @Override
    public UsbDevice getDevice() {
        return mDevice;
    }

    @Override
    public List<UsbSerialPort> getPorts() {
        return mPorts;
    }



    public class GPS_Serial_Port extends CommonUsbSerialPort {

        protected UsbDevice mDevice;
        protected  int mPortNumber;

        // constructor for GPS_Serial_Port
        public GPS_Serial_Port(UsbDevice device, int portNumber) {
            super(device, portNumber);
        }
        @Override
        protected void openInt() throws IOException {

        }

        @Override
        protected void closeInt() {

        }

        @Override
        public UsbSerialDriver getDriver() {
            return VEX_GPS_Driver.this;
        }

        @Override
        public void setParameters(int baudRate, int dataBits, int stopBits, int parity) throws IOException {

        }
    }









}