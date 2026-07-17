package com.pactorratt.alpha.serial;

@FunctionalInterface
public interface SerialByteListener {
    /** @param transmit true=TX (host→TNC), false=RX */
    void onSerialBytes(boolean transmit, byte[] data, int offset, int length);
}
