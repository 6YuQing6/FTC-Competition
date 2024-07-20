package org.firstinspires.ftc.teamcode;
import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.usb.serial.SerialPort;

import com.qualcomm.robotcore.hardware.usb.RobotUsbManager;
import com.qualcomm.robotcore.hardware.usb.serial.RobotUsbDeviceTty;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.List;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;



@TeleOp(name = "GPS_Testing", group = "Senor")
//@Disabled
public class GPS_Position extends LinearOpMode
{
   RobotUsbManager manager;
   List<SerialNumber> list;


    public void runOpMode()
    {
        telemetry.addData(">>", "Press start to continue");
        telemetry.update();

        try {
            list = manager.scanForDevices();
        } catch (RobotCoreException e) {
            throw new RuntimeException(e);
        }
        waitForStart();



        while(opModeIsActive())
        {
            telemetry.addData("List Size:", list.size());
            telemetry.addData("Serial Number:", list.get(0).getString());
            telemetry.update();
        }
    }


}
