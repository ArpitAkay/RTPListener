package com.geekyants;

import java.util.ArrayList;
import java.util.List;

public class SsrcService {

    private List<String> ssrcList = new ArrayList<>();

    public String getSsrc() {
        return ssrcList.get(0);
    }

    public List<String> getSsrcList() {
        return ssrcList;
    }
}
