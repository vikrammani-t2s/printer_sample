package com.tcpprintersear.app;
import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class ThermalPrinterDeviceName {

    static public String getPrinterManufaturerName(String ipAddress){
        String sysNameOid = "1.3.6.1.2.1.1.5.0"; // OID for sysName
        String deviceName = null;

        try {
            Address targetAddress = GenericAddress.parse("udp:" + ipAddress + "/161");
            TransportMapping<? extends Address> transport = new DefaultUdpTransportMapping();
            Snmp snmp = new Snmp(transport);
            transport.listen();

            CommunityTarget target = new CommunityTarget();
            target.setCommunity(new OctetString("public"));
            target.setAddress(targetAddress);
            target.setRetries(2);
            target.setTimeout(1500);
            target.setVersion(SnmpConstants.version2c);

            PDU pdu = new PDU();
            pdu.add(new VariableBinding(new OID(sysNameOid)));
            pdu.setType(PDU.GET);

            ResponseEvent response = snmp.send(pdu, target);

            if (response != null && response.getResponse() != null) {
                deviceName = response.getResponse().getVariableBindings().get(0).getVariable().toString();
                System.out.println("enter the device name"+deviceName);
            }

            snmp.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return deviceName;

    }
}
