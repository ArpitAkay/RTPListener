package com.geekyants;

import com.geekyants.entity.DtmfEventRequest;
import com.geekyants.repository.DtmfEventRequestRepository;
import jakarta.annotation.PostConstruct;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.DefaultManagerConnection;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.action.EventsAction;
import org.asteriskjava.manager.event.DtmfBeginEvent;
import org.asteriskjava.manager.event.HangupEvent;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class AsteriskAMIListener {

    private final DtmfEventRequestRepository dtmfEventRequestRepository;

    public AsteriskAMIListener(
            DtmfEventRequestRepository dtmfEventRequestRepository
    ) {
        this.dtmfEventRequestRepository = dtmfEventRequestRepository;
    }

    @PostConstruct
    void main()
            throws InterruptedException, IllegalStateException, IOException, org.asteriskjava.manager.TimeoutException {
        String amiHost = "192.168.124.185";
        int amiPort = 5038;
        String amiUsername = "AMI-External";
        String amiPassword = "c2a857e0720271178886962fda99a057cc449625";
        ManagerConnection managerConnection = new DefaultManagerConnection(amiHost, amiPort, amiUsername, amiPassword);
        try {
            managerConnection.login();
            managerConnection.sendAction(new EventsAction("ALL"));
            managerConnection.addEventListener(event -> {
                System.out.println(event.getClass());
                if(event instanceof DtmfBeginEvent) {
                    DtmfBeginEvent dtmfBeginEvent = (DtmfBeginEvent) event;
                    String digit = dtmfBeginEvent.getDigit();
                    if(digit.equals("*")) {
                        DtmfEventRequest dtmfEventRequest = new DtmfEventRequest();
                        dtmfEventRequest.setAsterisk(true);
                        dtmfEventRequestRepository.save(dtmfEventRequest);
                    }
                    else if(digit.equals("#")) {
                        List<DtmfEventRequest> dtmfEventRequestList = dtmfEventRequestRepository.findAll();
                        dtmfEventRequestList.get(0).setHash(true);
                        dtmfEventRequestRepository.save(dtmfEventRequestList.get(0));
                    }
                }
                else if (event instanceof HangupEvent) {
                    dtmfEventRequestRepository.deleteAll();
                }
            });
        } catch (AuthenticationFailedException e) {
            e.printStackTrace();
        }
    }
}