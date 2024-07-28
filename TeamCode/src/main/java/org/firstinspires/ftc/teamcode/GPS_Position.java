package org.firstinspires.ftc.teamcode;

import com.google.blocks.ftcrobotcontroller.util.Identifier;
import com.qualcomm.hardware.HardwareDeviceManager;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.exception.RobotCoreException;

import com.qualcomm.robotcore.hardware.DeviceManager;
import com.qualcomm.robotcore.hardware.ScannedDevices;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.SerialNumber;

import java.util.Set;
import java.io.File;
import java.util.Vector;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.usb.serial.RobotUsbManagerTty;
import com.qualcomm.robotcore.hardware.usb.serial.RobotUsbDeviceTty;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDeviceImplBase;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice.USBIdentifiers;
import com.qualcomm.hardware.modernrobotics.comm.ModernRoboticsUsbUtil;

import org.firstinspires.ftc.robotcore.internal.hardware.usb.ArmableUsbDevice;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Iterator;
import java.util.Vector;

import android.util.Log;

//@TeleOp(name = "GPS_Testing", group = "Senor")
@Disabled
public class GPS_Position extends LinearOpMode
{
    private String mDriverName;
    private String mDeviceRoot;
    ArmableUsbDevice.OpenRobotUsbDevice Robot_USB_Dev;
    HardwareDeviceManager DevManager = new HardwareDeviceManager(AppUtil.getDefContext(),null);
    ScannedDevices Devices = null;
    Set<SerialNumber> USB_Keys;
    SerialNumber[] USB_Devices;
    SerialNumber GPS_Serial_Num;
    RobotUsbManagerTty Tty_Manager = new RobotUsbManagerTty();
    DeviceManager.UsbDeviceType Dev_type;
    Vector<File> mDevices;

    RobotUsbDevice deviceTTY;
    USBIdentifiers USB_Identity;
    int Pid;
    int Vid;
    int Bcd;
    private static final String TAG = "SerialPort";
    String[] allDevices;


//    public List<SerialNumber> findDevices() throws RobotCoreException {
//        return deviceMan.scanForDevices();
//    }

//    public void connectToGPS(int i) throws RobotCoreException {
//        serialNumbers = findDevices();
//        SerialNumber dev = serialNumbers.get(i);
////        RobotUsbDevice gps = DeviceManager.openBySerialNumber(dev);
////        RobotUsbDevice.USBIdentifiers identifiers = gps.getUsbIdentifiers();
////        productId = identifiers.productId;
////        vendorId = identifiers.vendorId;
////        bcdDevice = identifiers.bcdDevice;
//
////        for (SerialNumber i : serialNumbers) {
////            RobotUsbDevice gps = managerCombining.openBySerialNumber(i);
////            RobotUsbDevice.USBIdentifiers identifiers = gps.getUsbIdentifiers();
////            productId = identifiers.productId;
////            vendorId = identifiers.vendorId;
////            bcdDevice = identifiers.bcdDevice;
////        }
//    }
    public Vector<File> getDevices() {
        if (mDevices == null) {
            mDevices = new Vector<File>();
            File dev = new File("/dev");
            File[] files = dev.listFiles();
            int i;
            for (i=0; i<files.length; i++) {
                if (files[i].getAbsolutePath().startsWith(mDeviceRoot)) {
                    Log.d(TAG, "Found new device: " + files[i]);
                    mDevices.add(files[i]);
                }
            }
        }
        return mDevices;
    }



    //@Override
    public void runOpMode() {
        telemetry.addData(">>", "Press start to continue");
        telemetry.update();
        waitForStart();
        try {
            Devices = DevManager.scanForUsbDevices();
        } catch (RobotCoreException e) {
            throw new RuntimeException(e);
        }
        USB_Keys = Devices.keySet();
        USB_Devices = (SerialNumber[]) USB_Keys.toArray(new SerialNumber[0]);
//        for (SerialNumber usbDevice : USB_Devices) {
//            if (usbDevice.isEmbedded()) {
//                try {
//                    deviceTTY = Tty_Manager.openBySerialNumber(usbDevice);
//                } catch (RobotCoreException e) {
//                    throw new RuntimeException(e);
//                }
//                USB_Identity = deviceTTY.getUsbIdentifiers();
//                Pid = USB_Identity.productId;
//                Vid = USB_Identity.vendorId;
//                Bcd = USB_Identity.bcdDevice;
//            }
//        }




        while(opModeIsActive())
        {
            if (USB_Devices.length <= 1) {
                telemetry.addData("Hardware Error", "No USB Device Connected");
            } else{
                telemetry.addData("USB Device Map", Devices.toSerializationString());
                telemetry.addData("# of USB devices Connected", USB_Devices.length-1);
//                for (SerialNumber devices : USB_Devices) {
//                    if (!devices.isEmbedded()) {
//                        telemetry.addData("Serial Number", devices.toString());
//                        telemetry.addData("Device Type:", Dev_type.toString());
//                    }
//                }
                for (String allDevice : allDevices) {
                    telemetry.addData("Device Name:", allDevice);

                }
                telemetry.addData("Product ID", Pid);
                telemetry.addData("Vendor ID", Vid);
                telemetry.addData("bcdDevice ID", Bcd);

            }
            telemetry.update();
        }
    }


}
