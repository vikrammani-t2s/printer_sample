package com.tcpprintersear.app.printerStatus;

import com.tcpprintersear.app.model.PrinterStatus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class PrinterStatusChecker {

    private static final int PRINTER_PORT = 9100;
    private static final int CONNECTION_TIMEOUT = 5000; // Connection timeout in milliseconds
    public static PrinterStatus isPrinterIdle(String printerIpAddress) {
        try {
            Socket socket = new Socket(printerIpAddress, PRINTER_PORT);
            socket.setSoTimeout(CONNECTION_TIMEOUT);

            // Get the input and output streams
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();

            // Send the ESC/POS command to request printer status
            byte[] command = {0x1B, 0x76};
            outputStream.write(command);
            outputStream.flush();

            // Read the response from the printer
            byte[] response = new byte[1];
            int bytesRead = inputStream.read(response);

            // Close the socket and streams
            outputStream.close();
            inputStream.close();
            socket.close();

            if (bytesRead == 1) {

                byte statusByte = response[0];

                // Check the status byte to get various information
                boolean isCoverOpen = (statusByte& 0b00000001) != 0;
                boolean isPaperCut = (statusByte & 0b00000100) != 0;
                boolean isOutOfPaper = (statusByte & 0b00001000) != 0;
                boolean isPrinterBusy = (statusByte& 0b00010000) != 0;
                boolean isNearToEndOfPaper = (statusByte & 0b10000000) != 0;

                // Use the status information as needed
                System.out.println("Cover Open: " + isCoverOpen);
                System.out.println("Paper Cut: " + isPaperCut);
                System.out.println("Out of Paper: " + isOutOfPaper);
                System.out.println("Printer Busy: " + isPrinterBusy);
                System.out.println("Near to Paper End: " + isNearToEndOfPaper);



            }else{
                System.out.println("Failed to get printer status.");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Error occurred or invalid response
        return null;
    }


}
