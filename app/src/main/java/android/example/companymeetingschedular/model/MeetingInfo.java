package android.example.companymeetingschedular.model;

import java.util.ArrayList;

public class MeetingInfo {
    private String start_time;
    private String end_time;
    private String description;
    private ArrayList<String> participants;

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getParticipants() {
        return participants;
    }
}
