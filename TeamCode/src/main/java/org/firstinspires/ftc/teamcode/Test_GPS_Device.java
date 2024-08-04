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
            GPS.getData();
            telemetry.addData("X",GPS.X);
            telemetry.addData("Y",GPS.Y);
            telemetry.addData("Azimuth ",GPS.Az);
            telemetry.addData("Elevation",GPS.El);
            telemetry.addData("Rotation",GPS.Rot);
            telemetry.update();

        }
    }
}
