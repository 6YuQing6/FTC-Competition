package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp(name = "Test GPS", group = "Odom")
public class Test_GPS_Device extends LinearOpMode {


    @Override
    public void runOpMode() throws InterruptedException {

        telemetry.addData("Test Ports" +
                " >>", "Press start to continue");
        telemetry.update();
        waitForStart();
        VEX_GPS_Sensor GPS = new VEX_GPS_Sensor(0,0,0);
        while(opModeIsActive()) {
            if(GPS.isConnected) {
                telemetry.addData("Vex GPS Sensor is connected","");
                GPS.getData();
                telemetry.addData("Status",GPS.Status);
                telemetry.addData("X", GPS.X);
                telemetry.addData("Y", GPS.Y);
                telemetry.addData("Azimuth ", GPS.Az);
                telemetry.addData("Elevation", GPS.El);
                telemetry.addData("Rotation", GPS.Rot);

                telemetry.addData("Buffer Raw Data", "");
                for (int i = 0; i < GPS.gps_Buffer.length; i++) {
                    telemetry.addData("     Byte " + i, GPS.gps_Buffer[i]);
                }
            }
            else{
                telemetry.addData("Vex GPS Sensor is disconnected","");
            }
            telemetry.update();
        }
        if(!opModeIsActive())
        {
            GPS.Close_GPS();
        }
    }
}
