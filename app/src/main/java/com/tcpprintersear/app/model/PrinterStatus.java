package com.tcpprintersear.app.model;

public  class PrinterStatus {
    private final boolean isCoverOpen;
    private final boolean isPaperOut;
    private final boolean isPaperCut;
    private final boolean isPrinterBusy;

    public PrinterStatus(boolean isCoverOpen, boolean isPaperOut, boolean isPaperCut, boolean isPrinterBusy) {
        this.isCoverOpen = isCoverOpen;
        this.isPaperOut = isPaperOut;
        this.isPaperCut = isPaperCut;
        this.isPrinterBusy = isPrinterBusy;
    }

    public boolean isCoverOpen() {
        return isCoverOpen;
    }

    public boolean isPaperOut() {
        return isPaperOut;
    }

    public boolean isPaperCut() {
        return isPaperCut;
    }

    public boolean isPrinterBusy() {
        return isPrinterBusy;
    }
}




