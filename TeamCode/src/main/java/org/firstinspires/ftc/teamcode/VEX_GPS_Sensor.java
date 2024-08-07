package org.firstinspires.ftc.teamcode;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import com.hoho.android.usbserial.driver.CdcAcmSerialDriver;
import com.hoho.android.usbserial.driver.ProbeTable;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.SerialInputOutputManager;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import java.io.IOException;
import java.util.HashMap;

public class VEX_GPS_Sensor {
    public boolean isConnected = false;
    private boolean usbConnected = false;
    int gps_vId = 10376;
    int gps_pId = 1313;
    int gps_baudrate = 115200;
    UsbManager manager;
    UsbDevice gps_Device = null;
    UsbSerialPort gps_Port = null;
    byte[] gps_Buffer = new byte[16];
    protected float X_Offset ;
    protected float Y_Offset ;
    protected float Angle_Offset ;
    public int Status;
    public double X = 0;
    public double Y = 0;
    public double Z = 0;
    public double Az = 0;
    public double El = 0;
    public double Rot = 0;


    public  VEX_GPS_Sensor(float X_off, float Y_off, float A_off) {
        this.X_Offset = X_off;
        this.Y_Offset = Y_off;
        this.Angle_Offset = A_off;
        Find_GPS();
        if (usbConnected)
            Connect_GPS();
    }

    public void Find_GPS() {
        AppUtil appUtil = AppUtil.getInstance();
        Context mContext = appUtil.getModalContext();
        manager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceMap = manager.getDeviceList();
        String[] usb_Keys = deviceMap.keySet().toArray(new String[0]);
        for (String usbKey : usb_Keys) {
            UsbDevice dev = deviceMap.get(usbKey);
            if (dev != null) {
                if (dev.getVendorId() == gps_vId) {
                    if (gps_Device == null) {
                        gps_Device = dev;
                        usbConnected = true;
                    }
                }
            }
        }
    }


    private void Connect_GPS() {
        if (gps_Device != null) {
            UsbDeviceConnection gps_Conn = manager.openDevice(gps_Device);
            ProbeTable customTable = new ProbeTable();
            customTable.addProduct(gps_vId, gps_pId, CdcAcmSerialDriver.class); // 2 Ports || (+,+)
            UsbSerialProber customProbe = new UsbSerialProber(customTable);
            UsbSerialDriver gps_Driver = customProbe.findAllDrivers(manager).get(0);
            gps_Port = gps_Driver.getPorts().get(1);
            try {
                gps_Port.open(gps_Conn);
                gps_Port.setParameters(gps_baudrate, UsbSerialPort.DATABITS_8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
                gps_Port.setBreak(false);
                gps_Port.setDTR(true);
            } catch (IOException e) {
                throw new RuntimeException("GPS Sensor is connected but can't open a connection try power cycling via the USB cable", e);
            }
            isConnected = true;
            SerialInputOutputManager serIoManager = new SerialInputOutputManager(gps_Port);
            serIoManager.start();

        }
    }

    public void Close_GPS(){
        try {
            gps_Port.close();
        } catch (IOException e) {
            throw new RuntimeException("Could not close GPS port", e);
        }
    }

    public void getData() {
        try {
            gps_Port.read(gps_Buffer, 16, 10);

        } catch (IOException e) {
            throw new RuntimeException("GPS cant read data from virtual Com", e);
        }
        if (gps_Port.isOpen()) {
            short Xi = Short.parseShort(gps_Buffer[3]+"");
            short Xd = Short.parseShort(gps_Buffer[2]+"");
            short Yi = Short.parseShort(gps_Buffer[5]+"");
            short Yd = Short.parseShort(gps_Buffer[4]+"");
            short Zi = Short.parseShort(gps_Buffer[7]+"");
            short Zd = Short.parseShort(gps_Buffer[6]+"");
            short Azi = Short.parseShort(gps_Buffer[9]+"");
            short Azd = Short.parseShort(gps_Buffer[8]+"");
            short Eli = Short.parseShort(gps_Buffer[11]+"");
            short Eld = Short.parseShort(gps_Buffer[10]+"");
            short Roti = Short.parseShort(gps_Buffer[13]+"");
            short Rotd = Short.parseShort(gps_Buffer[12]+"");


            Status = gps_Buffer[1];
            X = Float.parseFloat(Xi + "." + Math.abs(Xd));
            Y = Float.parseFloat(Yi + "." + Math.abs(Yd));
            Z = Float.parseFloat(Zi + "." + Math.abs(Zd));
            Az = Float.parseFloat(Azi + "." + Math.abs(Azd));
            El = Float.parseFloat(Eli + "." + Math.abs(Eld));
            Rot = Float.parseFloat(Roti + "." + Math.abs(Rotd));
            if (Status == 20) {
                X = X / 1000;
                Y = Y * 1000;
                Az = ((Az / 32768.0 * 180.0) - Angle_Offset) % 360;
                El = (El / 32768.0 * 180.0);
                Rot = (Rot / 32768.0 * 180.0);

                double theta = Math.toRadians(Az);
                double GPS_X_Offset = X_Offset * Math.cos(theta) + Y_Offset * Math.sin(theta);
                double GPS_Y_Offset = -X_Offset * Math.sin(theta) + Y_Offset * Math.cos(theta);
                X = X - GPS_X_Offset;
                Y = Y - GPS_Y_Offset;
            }
        }
    }
}
