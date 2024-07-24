package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.qualcomm.robotcore.hardware.usb.RobotUsbDeviceImplBase;
import com.qualcomm.robotcore.util.SerialNumber;

import org.firstinspires.ftc.robotcore.internal.hardware.TimeWindow;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbException;

public class GPSDevice extends RobotUsbDeviceImplBase {


    @Override
    public String getTag() {
        return "GPS Device";
    }

    protected GPSDevice(SerialNumber serialNumber) {
        super(serialNumber);
    }

    @Override
    public void setDebugRetainBuffers(boolean retain) {

    }

    @Override
    public boolean getDebugRetainBuffers() {
        return false;
    }

    @Override
    public void logRetainedBuffers(long nsOrigin, long nsTimerExpire, String tag, String format, Object... args) {

    }

    @Override
    public void setBaudRate(int rate) throws RobotUsbException {

    }

    @Override
    public void setDataCharacteristics(byte dataBits, byte stopBits, byte parity) throws RobotUsbException {

    }

    @Override
    public void setLatencyTimer(int latencyTimer) throws RobotUsbException {

    }

    @Override
    public void setBreak(boolean enable) throws RobotUsbException {

    }

    @Override
    public void resetAndFlushBuffers() throws RobotUsbException {

    }

    @Override
    public void write(byte[] data) throws InterruptedException, RobotUsbException {

    }

    @Override
    public void skipToLikelyUsbPacketStart() {

    }

    @Override
    public boolean mightBeAtUsbPacketStart() {
        return false;
    }

    @Override
    public int read(byte[] data, int ibFirst, int cbToRead, long msTimeout, @Nullable TimeWindow timeWindow) throws RobotUsbException, InterruptedException {
        return 0;
    }

    @Override
    public void requestReadInterrupt(boolean interruptRequested) {

    }

    @Override
    public void close() {

    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public boolean isAttached() {
        return false;
    }

    @Override
    public USBIdentifiers getUsbIdentifiers() {
        return null;
    }

    @NonNull
    @Override
    public String getProductName() {
        return "GPS Device";
    }
}
