package org.firstinspires.ftc.teamcode;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;

import com.hoho.android.usbserial.driver.CdcAcmSerialDriver;
import com.hoho.android.usbserial.driver.Ch34xSerialDriver;
import com.hoho.android.usbserial.driver.ChromeCcdSerialDriver;
import com.hoho.android.usbserial.driver.Cp21xxSerialDriver;
import com.hoho.android.usbserial.driver.FtdiSerialDriver;
import com.hoho.android.usbserial.driver.GsmModemSerialDriver;
import com.hoho.android.usbserial.driver.ProbeTable;
import com.hoho.android.usbserial.driver.ProlificSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@TeleOp(name = "GPS Data", group = "Odom")
public class GPS_Data extends LinearOpMode{
    AppUtil appUtil = AppUtil.getInstance();
    protected int gps_vId = 10376;
    protected int gps_pId = 1313;
    protected int gps_baudrate = 115200;
    UsbManager manager;
    UsbDevice gps_Device = null;
    UsbDeviceConnection gps_Conn = null;
    ArrayList<UsbEndpoint> Read_Endpoints = new ArrayList<>();
    Context mContext = appUtil.getModalContext();
    ArrayList<UsbInterface> Interfaces = new ArrayList<>();
    List<UsbSerialDriver> drivers = null;
    UsbSerialPort gps_Port;
    byte[] data = new byte[16];


    public void Find_GPS(){
        manager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceMap = manager.getDeviceList();
        String[] usb_Keys = deviceMap.keySet().toArray(new String[0]);
        for (String usbKey : usb_Keys) {
            UsbDevice dev = deviceMap.get(usbKey);
            if (dev != null) {
                if (dev.getVendorId() == gps_vId) {
                    if (gps_Device == null) {
                        gps_Device = dev;
                    }
                }
            }
        }
    }

    private void getData(){
        int int_count = gps_Device.getInterfaceCount();
        for(int i = 0; i < int_count; i++) {
            if(gps_Device.getInterface(i).getInterfaceClass() != UsbConstants.USB_CLASS_VIDEO)
                Interfaces.add(gps_Device.getInterface(i));
        }
    }
    private void findPort() {
        ProbeTable customTable = new ProbeTable();
        customTable.addProduct(gps_vId, gps_pId, CdcAcmSerialDriver.class); // 2 Ports || (+,+)
        //customTable.addProduct(gps_vId, gps_pId, Cp21xxSerialDriver.class); // 7 Ports || (-,-,-,-,-,-,-)
        //customTable.addProduct(gps_vId, gps_pId, FtdiSerialDriver.class); // 7 Ports || (-,-,-,-,-,-,-)
        //customTable.addProduct(gps_vId, gps_pId, ProlificSerialDriver.class); // 1 Port || (-)
        //customTable.addProduct(gps_vId, gps_pId, Ch34xSerialDriver.class); // 1 Port || (-)
        //customTable.addProduct(gps_vId, gps_pId, GsmModemSerialDriver.class); // 1 Port || (-)
        //customTable.addProduct(gps_vId, gps_pId, ChromeCcdSerialDriver.class); // 3 Port (-,-,-)
        UsbSerialProber customProbe = new UsbSerialProber(customTable);
        drivers = customProbe.findAllDrivers(manager);

        UsbDeviceConnection connection = manager.openDevice(gps_Device);
        UsbSerialDriver gps_Driver = customProbe.findAllDrivers(manager).get(0);
         gps_Port = gps_Driver.getPorts().get(0);
        try {
            gps_Port.open(connection);
            gps_Port.setParameters(gps_baudrate,UsbSerialPort.DATABITS_8, UsbSerialPort.STOPBITS_2, UsbSerialPort.PARITY_NONE);
            gps_Port.read(data,16,10);
            gps_Port.getReadEndpoint().getAddress();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Test Port 1" +
                " >>", "Press start to continue");
        telemetry.update();
        waitForStart();
        Find_GPS();
        getData();
        findPort();
        while(opModeIsActive()){
            if(drivers != null){
                for(int i = 0; i < drivers.size();i++) {
                    if (drivers.get(i).getPorts() != null) {
                        for(int j = 0; j < drivers.get(i).getPorts().size();j++) {
                            telemetry.addData("Driver " + i + " || Port " + j, drivers.get(i).getPorts().get(j).getPortNumber());
                        }
                    }
                }
            }
            if(!Interfaces.isEmpty()) {
                telemetry.addData("# of Interfaces inside GPS", Interfaces.size());
                for (int i = 0; i < Interfaces.size(); i++) {
                    int Intindex = i + 1;
                    telemetry.addData("Interface " + Intindex + " Name", Interfaces.get(i).getName());
                    telemetry.addData("     Class", Interfaces.get(i).getInterfaceClass());
                    telemetry.addData("     Number of Endpoints", Interfaces.get(i).getEndpointCount());
                    for (int j = 0; j < Interfaces.get(i).getEndpointCount(); j++) {
                        int EPindex = j + 1;
                        telemetry.addData("     Endpoint " + EPindex, "");
                        telemetry.addData("             Type", Interfaces.get(i).getEndpoint(j).getType());
                        telemetry.addData("             Direction", Interfaces.get(i).getEndpoint(j).getDirection());
                        telemetry.addData("             Address", Interfaces.get(i).getEndpoint(j).getAddress());

                    }

                }
            }
            if(gps_Port.isOpen()) {
                telemetry.addData("Port Status", "Opened");
                telemetry.addData("Byte Array Size", data.length);
                telemetry.addData("Raw Data", new String(data, StandardCharsets.US_ASCII));

            }
            else
                telemetry.addData("Port Status","Closed");
            telemetry.update();
        }

    }
}
