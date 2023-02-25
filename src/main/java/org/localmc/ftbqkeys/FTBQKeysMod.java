package org.localmc.ftbqkeys;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.TreeMap;

@Mod(FTBQKeysMod.MODID)
public class FTBQKeysMod {

	public static final String MODID = "ftbqlocalkeys";

	public FTBQKeysMod(){
		MinecraftForge.EVENT_BUS.register(this);
	}

	public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public static void saveLang(TreeMap<String, String> transKeys, String lang, File parent) throws IOException{
		File fe = new File(parent, lang.toLowerCase(Locale.ROOT) + ".json");
		FileUtils.write(fe, FTBQKeysMod.gson.toJson(transKeys), StandardCharsets.UTF_8);
	}
}