package org.localmc.ftbqkeys;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import org.apache.commons.io.FileUtils;
import org.localmc.ftbqkeys.command.FTBQKeysCommand;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.TreeMap;

public class FTBQKeysMod implements ModInitializer{

    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register(FTBQKeysCommand::serverRegisterCommandsEvent);
    }

    public static void saveLang(TreeMap<String, String> transKeys, String lang, File parent) throws IOException{
        File fe = new File(parent, lang.toLowerCase(Locale.ROOT) + ".json");
        FileUtils.write(fe, FTBQKeysMod.gson.toJson(transKeys), StandardCharsets.UTF_8);
    }
}
