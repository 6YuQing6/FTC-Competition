package org.firstinspires.ftc.teamcode;

import static android.app.PendingIntent.getActivity;


import com.qualcomm.hardware.HardwareDeviceManager;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.ScannedDevices;
import com.qualcomm.robotcore.hardware.usb.serial.SerialPort;
import com.qualcomm.robotcore.util.SerialNumber;


import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@TeleOp(name = "Positional Data", group = "Odom")
public class GPS_Device extends LinearOpMode {

    // vex reference: https://github.com/VEX-Robotics-AI/VAIC-23-24/blob/main/JetsonExample/V5Position.py

    // instance used for application management / utility https://javadoc.io/doc/org.firstinspires.ftc/RobotCore/latest/org/firstinspires/ftc/robotcore/internal/system/AppUtil.html
    AppUtil inst = AppUtil.getInstance();
    List<File> usb_Paths;
    HardwareDeviceManager DevManager = new HardwareDeviceManager(AppUtil.getDefContext(), null);
    ScannedDevices Devices = null;
    Set<SerialNumber> USB_Keys = null;
    SerialNumber[] USB_Devices;
    SerialNumber[] All_Devices;
    boolean Dev_Exist = false;
    ArrayList<SerialNumber> Serlist = new ArrayList<SerialNumber>();
    boolean usbRead = false;
    boolean isFile = false;


    public void FindDevice() {
        try {
            Devices = DevManager.scanForUsbDevices(); // scans for usb device hardware, puts into map of scanned devices
        } catch (RobotCoreException e) {
            throw new RuntimeException(e);
        }
        USB_Keys = Devices.keySet(); // returns Set of usb serial numbers https://javadoc.io/doc/org.firstinspires.ftc/RobotCore/latest/com/qualcomm/robotcore/util/SerialNumber.html
        All_Devices = (SerialNumber[]) USB_Keys.toArray(new SerialNumber[0]); // converts Set to array type
        if (All_Devices.length > 1) {
            Dev_Exist = true; // check to see if devices were found
            for (SerialNumber allDevice : All_Devices) { // checks each device to see if it is not embedded (not ports inside control hub)
                if (!allDevice.isEmbedded()) {
                    Serlist.add(allDevice);
                }
                // converts all devices that are embedded into an array of serial numbers
                USB_Devices = (SerialNumber[]) Serlist.toArray(new SerialNumber[0]);

            }
            // creates file at path /dev/usb
            File usb_Serial_Dir = new File("/dev/usb");
            usb_Paths = inst.filesIn(usb_Serial_Dir); // finds all files inside of /dev/usb path

        }

    }

    public void ReadDevice(File usb) {
        usbRead = usb.canRead();
        isFile = usb.isFile();
        InputStream inputStream = null;

        // two methods to test below

        // reading from usb file https://stackoverflow.com/questions/16951667/serial-port-data-through-dev-ttys0-and-file-read-write
        if (!usbRead || !isFile) {
            telemetry.addData(">>", "File cannot be read");
        } else {
            try (FileInputStream fis = new FileInputStream(usb)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while (opModeIsActive()) {
                    bytesRead = fis.read(buffer);
                    if (bytesRead == -1) break; // end of file
                    String data = new String(buffer, 0, bytesRead);
                    telemetry.addData("Data", data);
                    telemetry.update();
                }
            } catch (IOException e) {
                telemetry.addData("Error", e.getMessage());
                telemetry.update();
            }
        }

        // https://javadoc.io/doc/org.firstinspires.ftc/RobotCore/latest/com/qualcomm/robotcore/hardware/usb/serial/SerialPort.html#baudRate
        // reading from serial port
        try {
            SerialPort serialPort = new SerialPort(usb, 115200);
            while (opModeIsActive()) {
                inputStream = serialPort.getInputStream();
                telemetry.addData("Data from SerialPort:", inputStream); // prob need to convert to string data
                telemetry.update();
            }
        } catch (IOException e) {
            telemetry.addData("Error", e.getMessage());
            telemetry.update();
        }


    }

    public void runOpMode() {

        telemetry.addData(">>", "Press start to continue");
        telemetry.update();
        waitForStart();
        FindDevice();
        while (opModeIsActive()) {

            if (!Dev_Exist) {
                telemetry.addData("Device Error", "GPS Device is not connected");
            } else {
                for (SerialNumber usbDevice : USB_Devices) {
                    telemetry.addData("USB Serial Numbers", usbDevice.toString());
                }
            }
            for (File File_Path : usb_Paths) {
                telemetry.addData("USB Serial Ports File Paths", File_Path.getAbsolutePath());
                ReadDevice(File_Path);
            }
            telemetry.update();


        }
    }
}
