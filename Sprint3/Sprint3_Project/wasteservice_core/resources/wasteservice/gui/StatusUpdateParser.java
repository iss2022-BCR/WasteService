package wasteservice.gui;

import com.google.gson.Gson;

public class StatusUpdateParser {
    private static Gson gson = new Gson();

    public static String toJsonString(StatusUpdate statusUpdate)
    {
        return gson.toJson(statusUpdate);
    }

    public static StatusUpdate fromJsonString(String statusUpdateString)
    {
        return gson.fromJson(statusUpdateString, StatusUpdate.class);
    }
}
