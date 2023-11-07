package com.geekyants;

import com.geekyants.entity.PacketSsrc;
import com.geekyants.repository.PacketSsrcRepository;
import jakarta.annotation.PostConstruct;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.DefaultManagerConnection;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.action.EventsAction;
import org.asteriskjava.manager.event.HangupEvent;
import org.asteriskjava.manager.event.RtcpReceivedEvent;
import org.asteriskjava.manager.event.RtcpSentEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Component
public class AsteriskAMIListener {

    @Autowired
    private PacketSsrcRepository packetSsrcRepository;

    public String ssrcValue;

    @PostConstruct
    void main()
            throws InterruptedException, IllegalStateException, IOException, org.asteriskjava.manager.TimeoutException {
        String amiHost = "192.168.141.187";
        int amiPort = 5038;
        String amiUsername = "AMI-External";
        String amiPassword = "c2a857e0720271178886962fda99a057cc449625";
        ManagerConnection managerConnection = new DefaultManagerConnection(amiHost, amiPort, amiUsername, amiPassword);
        try {
            managerConnection.login();
            managerConnection.sendAction(new EventsAction("ALL"));
            managerConnection.addEventListener(event -> {
                System.out.println(event.getClass());
//                System.out.println("*");
//                System.out.println("*");
//                System.out.println("Received event: " + event);
                if (event instanceof RtcpSentEvent) {
                    System.out.println("********************************************");
                    System.out.println("RtcpSentEvent");
                    System.out.println("toAddress : " + ((RtcpSentEvent) event).getToAddress());
                    System.out.println("fromAddress" + ((RtcpSentEvent) event).getFromAddress());
                    System.out.println("ssrc" + ((RtcpSentEvent) event).getSsrc());
                    System.out.println("report0Sourcessrc" + ((RtcpSentEvent) event).getReport0Sourcessrc());
                    System.out.println("********************************************");
                } else if (event instanceof RtcpReceivedEvent) {
                    System.out.println("********************************************");
                    System.out.println("RtcpReceivedEvent");
                    System.out.println("toAddress : " + ((RtcpReceivedEvent) event).getToAddress());
                    System.out.println("fromAddress" + ((RtcpReceivedEvent) event).getFromAddress());
                    String rtcpReceivedEventSsrc = ((RtcpReceivedEvent) event).getSsrc();
                    System.out.println("ssrc : " + rtcpReceivedEventSsrc);
                    String rtcpReceivedEventReport0Sourcessrc = ((RtcpReceivedEvent) event).getReport0Sourcessrc();
                    System.out.println("report0Sourcessrc : " + rtcpReceivedEventReport0Sourcessrc);
                    System.out.println("********************************************");

                    if (rtcpReceivedEventSsrc.equals(rtcpReceivedEventReport0Sourcessrc)) {
                        ssrcValue = rtcpReceivedEventSsrc;
                    }
                }
            });
            Thread.sleep(120000);
            if(ssrcValue != null && !ssrcValue.isEmpty()) {
                System.out.println("ssrcValue : " + ssrcValue);
                PacketSsrc packetSsrc = new PacketSsrc();
                packetSsrc.setSsrcValue(ssrcValue);
                packetSsrcRepository.save(packetSsrc);
            }
        } catch (AuthenticationFailedException e) {
            e.printStackTrace();
        } finally {
            managerConnection.logoff();
        }
    }
}