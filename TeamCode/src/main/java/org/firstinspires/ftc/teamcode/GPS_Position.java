package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.HardwareDeviceManager;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.exception.RobotCoreException;

import com.qualcomm.robotcore.hardware.DeviceManager;
import com.qualcomm.robotcore.hardware.ScannedDevices;
import com.qualcomm.robotcore.util.SerialNumber;

import java.util.Set;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.usb.serial.RobotUsbManagerTty;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDeviceImplBase;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;


@TeleOp(name = "GPS_Testing", group = "Senor")
//@Disabled
public class GPS_Position extends LinearOpMode
{



    HardwareDeviceManager DevManager = new HardwareDeviceManager(AppUtil.getDefContext(),null);
    ScannedDevices Devices = null;
    Set<SerialNumber> USB_Keys;
    SerialNumber[] USB_Devices;
    SerialNumber GPS_Serial_Num;
    RobotUsbManagerTty Tty_Manager = new RobotUsbManagerTty();
    DeviceManager.UsbDeviceType Dev_type;
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
        for (SerialNumber usbDevice : USB_Devices) {
            if (!usbDevice.isEmbedded()) {
                Dev_type = RobotUsbDeviceImplBase.getDeviceType(usbDevice);
            }
        }




        while(opModeIsActive())
        {
            if (USB_Devices.length <= 1) {
                telemetry.addData("Hardware Error", "No USB Device Connected");
            } else{
                telemetry.addData("USB Device Map", Devices.toSerializationString());
                telemetry.addData("# of USB devices Connected", USB_Devices.length-1);
                for (SerialNumber devices : USB_Devices) {
                    if (!devices.isEmbedded()) {
                        telemetry.addData("Serial Number", devices.toString());
                        telemetry.addData("Device Type:", Dev_type.toString());
                    }
                }

            }
            telemetry.update();
        }
    }


}
