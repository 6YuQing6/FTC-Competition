package org.firstinspires.ftc.teamcode;
import static android.app.PendingIntent.getActivity;


import com.qualcomm.hardware.HardwareDeviceManager;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.ScannedDevices;
import com.qualcomm.robotcore.util.SerialNumber;


import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@TeleOp(name= "Positional Data", group="Odom")
public class GPS_Device extends LinearOpMode {


    AppUtil inst = AppUtil.getInstance();
    List<File> usb_Paths;
    HardwareDeviceManager DevManager = new HardwareDeviceManager(AppUtil.getDefContext(),null);
    ScannedDevices Devices = null;
    Set<SerialNumber> USB_Keys = null;
    SerialNumber[] USB_Devices ;
    SerialNumber[] All_Devices ;
    boolean Dev_Exist = false;
    ArrayList<SerialNumber> Serlist = new ArrayList<SerialNumber>();


    public void FindDevice(){
        try {
            Devices = DevManager.scanForUsbDevices();
        } catch (RobotCoreException e) {
            throw new RuntimeException(e);
        }
        USB_Keys = Devices.keySet();
        All_Devices = (SerialNumber[]) USB_Keys.toArray(new SerialNumber[0]);
        if(All_Devices.length > 1) {
            Dev_Exist = true;
            for (SerialNumber allDevice : All_Devices) {
                if (!allDevice.isEmbedded()) {
                    Serlist.add(allDevice);
                }
                USB_Devices = (SerialNumber[]) Serlist.toArray(new SerialNumber[0]);

            }
            File usb_Serial_Dir = new File("/dev/usb");
            usb_Paths = inst.filesIn(usb_Serial_Dir);

        }

    }

    public void runOpMode() {

        telemetry.addData(">>", "Press start to continue");
        telemetry.update();
        waitForStart();
        FindDevice();
        while (opModeIsActive()) {

            if(!Dev_Exist)
            {
                telemetry.addData("Device Error", "GPS Device is not connected");
            }
            else {
                for (SerialNumber usbDevice : USB_Devices) {
                    telemetry.addData("USB Serial Numbers" , usbDevice.toString());
                }
            }
            for(File File_Path : usb_Paths) {
                telemetry.addData("USB Serial Ports File Paths", File_Path.getAbsolutePath());
            }
            telemetry.update();


        }
    }
}
