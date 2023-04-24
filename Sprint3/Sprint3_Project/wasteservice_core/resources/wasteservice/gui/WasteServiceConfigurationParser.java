package wasteservice.gui;

import com.google.gson.Gson;

public class WasteServiceConfigurationParser {
    private static Gson gson = new Gson();

    public static String toJsonString(WasteServiceConfiguration wsConfig)
    {
        return gson.toJson(wsConfig);
    }

    public static WasteServiceConfiguration fromJsonString(String wsConfig)
    {
        return gson.fromJson(wsConfig, WasteServiceConfiguration.class);
    }
}
