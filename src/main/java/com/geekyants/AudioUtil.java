package com.geekyants;

import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AudioUtil {
    public void convertPcapToRtpFile(String ssrc) {
        System.out.println("ssrc : " + ssrc);
        String cmd1 = "tshark -n -r call.pcap -2 -R rtp -R \"rtp.ssrc == " + ssrc + "\" -T fields -e rtp.payload | tr -d '\\n',':' | xxd -r -ps >call.rtp";
        try {
            // Execute the command
            Process process = Runtime.getRuntime().exec(new String[] { "bash", "-c", cmd1 });
            int exitCode = process.waitFor();

            // Check the exit code to see if the command was successful
            if (exitCode == 0) {
                System.out.println("First command executed successfully.");
                String cmd2 = "sox -t ul -r 8000 -c 1 call.rtp call.wav";
                Process process2 = Runtime.getRuntime().exec(new String[] { "bash", "-c", cmd2 });
                int exitCode2 = process2.waitFor();

                // Check the exit code of the second command
                if (exitCode2 == 0) {
                    System.out.println("Second command executed successfully.");
                } else {
                    System.err.println("Second command failed with exit code: " + exitCode2);
                }
            } else {
                System.err.println("First command failed with exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
