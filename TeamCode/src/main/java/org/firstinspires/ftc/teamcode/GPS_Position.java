package org.firstinspires.ftc.teamcode;
import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.hardware.usb.serial.SerialPort;

import com.qualcomm.robotcore.hardware.usb.RobotUsbManager;
import com.qualcomm.robotcore.hardware.usb.serial.RobotUsbDeviceTty;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.List;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


import com.qualcomm.robotcore.hardware.usb.RobotUsbDeviceImplBase;
import com.qualcomm.robotcore.hardware.usb.RobotUsbManagerCombining;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.content.Context;



@TeleOp(name = "GPS_Testing", group = "Senor")
//@Disabled
public class GPS_Position extends LinearOpMode
{
   RobotUsbManagerCombining managerCombining = new RobotUsbManagerCombining();
   List<SerialNumber> serialNumbers;
   RobotUsbDeviceImplBase gpsSensor;
   int productId;
   int vendorId;
   int bcdDevice;


    public List<SerialNumber> findDevices() throws RobotCoreException {
        return managerCombining.scanForDevices();
    }

    public void connectToGPS(int i) throws RobotCoreException {
        serialNumbers = findDevices();
        SerialNumber dev = serialNumbers.get(i);
        RobotUsbDevice gps = managerCombining.openBySerialNumber(dev);
        RobotUsbDevice.USBIdentifiers identifiers = gps.getUsbIdentifiers();
        productId = identifiers.productId;
        vendorId = identifiers.vendorId;
        bcdDevice = identifiers.bcdDevice;

//        for (SerialNumber i : serialNumbers) {
//            RobotUsbDevice gps = managerCombining.openBySerialNumber(i);
//            RobotUsbDevice.USBIdentifiers identifiers = gps.getUsbIdentifiers();
//            productId = identifiers.productId;
//            vendorId = identifiers.vendorId;
//            bcdDevice = identifiers.bcdDevice;
//        }
    }

    public void runOpMode()
    {
        telemetry.addData(">>", "Press start to continue");



        telemetry.update();

        try {
            serialNumbers = managerCombining.scanForDevices();
        } catch (RobotCoreException e) {
            throw new RuntimeException(e);
        }
        try {
            connectToGPS(0);
        } catch (RobotCoreException e) {
            throw new RuntimeException(e);
        }

        waitForStart();



        while(opModeIsActive())
        {
            telemetry.addData("List Size:", serialNumbers.size());
            telemetry.addData("Serial Number:", serialNumbers.get(0).getString());
            telemetry.addData("product id: ", productId);
            telemetry.addData("vendor id: ", vendorId);
            telemetry.addData("bcdDevice: ", bcdDevice);
            telemetry.update();
        }
    }


}
