package org.firstinspires.ftc.teamcode;
import static android.app.PendingIntent.getActivity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
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
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@TeleOp(name = "Positional Data", group = "Odom")
@Disabled
public class GPS_Device extends LinearOpMode {

    // vex reference: https://github.com/VEX-Robotics-AI/VAIC-23-24/blob/main/JetsonExample/V5Position.py

    // instance used for application management / utility https://javadoc.io/doc/org.firstinspires.ftc/RobotCore/latest/org/firstinspires/ftc/robotcore/internal/system/AppUtil.html


//    HardwareDeviceManager DevManager = new HardwareDeviceManager(AppUtil.getDefContext(), null);
//    boolean Dev_Exist = false;
//    ArrayList<SerialNumber> Serlist = new ArrayList<SerialNumber>();
//    RobotUsbDeviceTty GPS;
//    Application application;
//
//    boolean usbRead = false;
//    boolean isFile = false;


    private static final String ACTION_USB_PERMISSION = "org.firstinspires.ftc.teamcode";
    AppUtil appUtil = AppUtil.getInstance();
    protected int gps_vId = 10376;
    protected int gps_pId = 1313;
    protected int gps_baudrate = 115200;
    UsbManager manager;
    UsbDevice GPS_Sensor;
    private boolean has_Driver = false;
    private boolean is_Connected = false;
    private boolean conn_est = false;
    private boolean port_created = false;
    List<UsbSerialDriver> availableDrivers;
    List<UsbSerialPort> gps_Ports;
    UsbSerialDriver gps_Driver = null;
    UsbSerialPort gps_Port = null;
    UsbDevice[] devices;
    ArrayList<UsbInterface> gps_Interface_List = new ArrayList<>(0);



    public void FindDevice() {

        boolean conn_status = false;
        Context mContext = appUtil.getModalContext();
        manager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
        PendingIntent permissionIntent = PendingIntent.getBroadcast(appUtil.getModalContext(), 0, new Intent(ACTION_USB_PERMISSION), 0); //
        HashMap<String, UsbDevice> deviceMap = manager.getDeviceList();
        Collection<UsbDevice> values = deviceMap.values();
        devices = values.toArray(new UsbDevice[0]);

        for (UsbDevice device : devices) {
            if (device.getVendorId() == gps_vId) {
                GPS_Sensor = device;
                is_Connected = true;
            }
        }









        if(is_Connected){
            ProbeTable customTable = new ProbeTable();
            customTable.addProduct(gps_vId, gps_pId, CdcAcmSerialDriver.class);
            customTable.addProduct(gps_vId, gps_pId, Cp21xxSerialDriver.class);
            customTable.addProduct(gps_vId, gps_pId, FtdiSerialDriver.class);
            customTable.addProduct(gps_vId, gps_pId, ProlificSerialDriver.class);
            customTable.addProduct(gps_vId, gps_pId, Ch34xSerialDriver.class);
            customTable.addProduct(gps_vId, gps_pId, GsmModemSerialDriver.class);
            customTable.addProduct(gps_vId, gps_pId, ChromeCcdSerialDriver.class);

            UsbSerialProber customProbe = new UsbSerialProber(customTable);
           availableDrivers = customProbe.findAllDrivers(manager);
            if (!availableDrivers.isEmpty()) {
                has_Driver = true;
                gps_Driver = availableDrivers.get(0);
                UsbDeviceConnection gps_connection = manager.openDevice(gps_Driver.getDevice());
                if(gps_connection != null)
                {
                    conn_est = true;
                    gps_Ports = gps_Driver.getPorts();
                    try {
                        gps_Port.open(gps_connection);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    if(gps_Port == null) {
                        port_created = true;
                    }
                }

            }
        }
    }







    public void get_data(){
        byte[] buffer = new byte[1024];
        UsbDeviceConnection connection = manager.openDevice(GPS_Sensor);

        if (connection == null) {
            return;
        }
        else{
            for(int i = 0; i < availableDrivers.size(); i++) {
                if (availableDrivers.get(i).getDevice() == GPS_Sensor) {
                    gps_Driver = availableDrivers.get(i);

                }
            }
            gps_Port = gps_Driver.getPorts().get(0);
            try {
                gps_Port.open(connection);
                gps_Port.read(buffer,16,10);
            } catch (IOException e) {
                throw new RuntimeException("No Data Stream", e);
            }
        
        }
    }
//    public void Find_GPS(){
//        ScannedDevices Devices ;
//        try {
//             Devices = DevManager.scanForUsbDevices(); // scans for usb device hardware, puts into map of scanned devices
//        } catch (RobotCoreException e) {
//            throw new RuntimeException(e);
//        }
//        Set<SerialNumber> USB_Keys = Devices.keySet(); // returns Set of usb serial numbers https://javadoc.io/doc/org.firstinspires.ftc/RobotCore/latest/com/qualcomm/robotcore/util/SerialNumber.html
//        SerialNumber[] All_Devices = (SerialNumber[]) USB_Keys.toArray(new SerialNumber[0]); // converts Set to array type
//        if (All_Devices.length > 1) {
//            Dev_Exist = true; // check to see if devices were found
//            for (SerialNumber allDevice : All_Devices) { // checks each device to see if it is not embedded (not ports inside control hub)
//                if (!allDevice.isEmbedded()) {
//                    Serlist.add(allDevice);
//                }
//            }
//        }
//            // creates file at path /dev/usb
//            File usb_Serial_Dir = new File("/dev/usb");
//            usb_Paths = appUtil.filesIn(usb_Serial_Dir); // finds all files inside of /dev/usb path
//
//    }




//    public void ReadDevice(File usb) {
//        usbRead = usb.canRead();
//        isFile = usb.isFile();
//        InputStream inputStream = null;
//
//        // two methods to test below
//
//        // reading from usb file https://stackoverflow.com/questions/16951667/serial-port-data-through-dev-ttys0-and-file-read-write
//        if (!usbRead || !isFile) {
//            telemetry.addData(">>", "File cannot be read");
//        } else {
//            try (FileInputStream fis = new FileInputStream(usb)) {
//                byte[] buffer = new byte[1024];
//                int bytesRead;
//                while (opModeIsActive()) {
//                    bytesRead = fis.read(buffer);
//                    if (bytesRead == -1) break; // end of file
//                    String data = new String(buffer, 0, bytesRead);
//                    telemetry.addData("Data", data);
//                    telemetry.update();
//                }
//            } catch (IOException e) {
//                telemetry.addData("Error", e.getMessage());
//                telemetry.update();
//            }
//        }
//
//        // https://javadoc.io/doc/org.firstinspires.ftc/RobotCore/latest/com/qualcomm/robotcore/hardware/usb/serial/SerialPort.html#baudRate
//        // reading from serial port
//        try {
//            SerialPort serialPort = new SerialPort(usb, 115200);
//            while (opModeIsActive()) {
//                inputStream = serialPort.getInputStream();
//                telemetry.addData("Data from SerialPort:", inputStream); // prob need to convert to string data
//                telemetry.update();
//            }
//        } catch (IOException e) {
//            telemetry.addData("Error", e.getMessage());
//            telemetry.update();
//        }
//
//
//    }

    public void runOpMode() {

        telemetry.addData(">>", "Press start to continue");
        telemetry.update();
        waitForStart();
        FindDevice();
        for(int i = 0; i < GPS_Sensor.getInterfaceCount(); i++)
        {
            gps_Interface_List.add(GPS_Sensor.getInterface(i));

        }
        File usb_Serial_Dir = new File("/dev/usb");
        List<File> usb_Paths = appUtil.filesIn(usb_Serial_Dir); // finds all files inside of /dev/usb path

        //get_data();

        boolean has_Per = manager.hasPermission(GPS_Sensor);


        while (opModeIsActive()) {
            if (!is_Connected) {
                telemetry.addData("Device Status", "GPS Device is not connected");

            } else {
                telemetry.addData("Device Status", "GPS_Device is connected");

                telemetry.addData("USB Name",GPS_Sensor.getDeviceName());
                telemetry.addData("     Device Id", GPS_Sensor.getDeviceId());
                telemetry.addData("     Product Id", GPS_Sensor.getProductId());
                telemetry.addData("     Vendor Id", GPS_Sensor.getVendorId());
                telemetry.addData("     Serial Number", GPS_Sensor.getSerialNumber());
                telemetry.addData("     Manufacturer", GPS_Sensor.getManufacturerName());
                telemetry.addData("Interface Coount", GPS_Sensor.getInterfaceCount());
            }

//            if(port_created) {
//                telemetry.addData("GPS port", "true");
//            }
//            else {
//                telemetry.addData("GPS port", "false");
//            }
//
//            if(conn_est) {
//                telemetry.addData("UsbDeviceConnection", "true");
//            }
//            else {
//                telemetry.addData("UsbDeviceConnection", "false");
//            }
            for(int i = 0; i < gps_Interface_List.size();i++) {
                if (!Objects.equals(gps_Interface_List.get(i).getName(), "")) {
                    telemetry.addData("GPS Interface " + i + " Name", gps_Interface_List.get(i).getName());
                    telemetry.addData("     Number of endpoints", gps_Interface_List.get(i).getEndpointCount());
                    for (int j = 0; j < gps_Interface_List.get(i).getEndpointCount(); j++) {
                        telemetry.addData("     Endpoint " + j + " #", gps_Interface_List.get(i).getEndpoint(j).getEndpointNumber());
                        telemetry.addData("     Endpoint " + j + " Address", gps_Interface_List.get(i).getEndpoint(j).getAddress());
                        telemetry.addData("     Endpoint " + j + " Direction", gps_Interface_List.get(i).getEndpoint(j).getDirection());

                    }
                }
            }






            for (File File_Path : usb_Paths) {
                telemetry.addData("USB Serial Ports File Paths", File_Path.getAbsolutePath());
            }

            telemetry.update();

        }
    }
}





