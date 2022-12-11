package org.thinkingstudio.ftbqlocalizationkeys;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import dev.ftb.mods.ftbquests.FTBQuests;
import dev.ftb.mods.ftbquests.quest.*;
import dev.ftb.mods.ftbquests.quest.loot.RewardTable;
import dev.ftb.mods.ftbquests.quest.reward.Reward;
import dev.ftb.mods.ftbquests.quest.task.Task;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.language.LanguageInfo;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.minecraft.commands.Commands.literal;

public class FTBQLocalizationKeysMod implements ModInitializer{

    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static void serverRegisterCommandsEvent(CommandDispatcher<CommandSourceStack> commandDispatcher, Boolean dedicated){
        RootCommandNode<CommandSourceStack> rootCommandNode = commandDispatcher.getRoot();
        LiteralCommandNode<CommandSourceStack> commandNode = literal("ftb-lang-convert").executes(context -> {
            return 0;
        }).build();

        ArgumentCommandNode<CommandSourceStack, String> argumentCommandNode = Commands.argument("lang", StringArgumentType.word()).suggests((C1, c2) -> {
            return SharedSuggestionProvider.suggest(Minecraft.getInstance().getLanguageManager().getLanguages().stream().map(LanguageInfo::getCode).toList().toArray(new String[0]), c2);
        }).executes(Ctx -> {
            try{
                File parent = new File(FabricLoader.getInstance().getGameDir().toFile(), "ftb-conv");
                File transFiles = new File(parent, "kubejs/assets/kubejs/lang/");
                File questsFolder = new File(FabricLoader.getInstance().getGameDir().toFile(), "config/ftbquests/");

                if(questsFolder.exists()){
                    File backup = new File(parent, "backup/ftbquests");
                    FileUtils.copyDirectory(questsFolder, backup);
                }

                TreeMap<String, String> transKeys = new TreeMap<>();
                QuestFile file = FTBQuests.PROXY.getQuestFile(false);

                for(int i = 0; i < file.rewardTables.size(); i++){
                    RewardTable table = file.rewardTables.get(i);

                    transKeys.put("loot_table." + (i + 1), table.title);
                    table.title = "{" + "loot_table." + (i + 1) + "}";
                }

                for(int i = 0; i < file.chapterGroups.size(); i++){
                    ChapterGroup chapterGroup = file.chapterGroups.get(i);

                    if(!chapterGroup.title.isBlank()){
                        transKeys.put("category." + (i + 1), chapterGroup.title);
                        chapterGroup.title = "{" + "category." + (i + 1) + "}";
                    }
                }

                for(int i = 0; i < file.getAllChapters().size(); i++){
                    Chapter chapter = file.getAllChapters().get(i);

                    String prefix = "chapter." + (i+1);

                    if(!chapter.title.isBlank()){
                        transKeys.put(prefix + ".title", chapter.title);
                        chapter.title = "{" + prefix + ".title" + "}";
                    }

                    if(chapter.subtitle.size() > 0){
                        transKeys.put(prefix + ".subtitle", String.join("\n", chapter.subtitle));
                        chapter.subtitle.clear();
                        chapter.subtitle.add("{" + prefix + ".subtitle" + "}");
                    }


                    for(int i1 = 0; i1 < chapter.images.size(); i1++){
                        ChapterImage chapterImage = chapter.images.get(i1);

                        if(!chapterImage.hover.isEmpty()){
                            transKeys.put(prefix + ".image." + (i1+1), String.join("\n", chapterImage.hover));
                            chapterImage.hover.clear();
                            chapterImage.hover.add("{" + prefix + ".image." + (i1+1) + "}");
                        }
                    }

                    for(int i1 = 0; i1 < chapter.quests.size(); i1++){
                        Quest quest = chapter.quests.get(i1);

                        if(!quest.title.isBlank()){
                            transKeys.put(prefix + ".quest." + (i1+1) + ".title", quest.title);
                            quest.title = "{" + prefix + ".quest." + (i1+1) + ".title}";
                        }

                        if(!quest.subtitle.isBlank()){
                            transKeys.put(prefix + ".quest." + (i1+1) + ".subtitle", quest.subtitle);
                            quest.subtitle = "{" + prefix + ".quest." + (i1+1) + ".subtitle" + "}";
                        }

                        if(quest.description.size() > 0){
                            List<String> descList = Lists.newArrayList();

                            StringJoiner joiner = new StringJoiner("\n");
                            int num = 1;

                            for(int i2 = 0; i2 < quest.description.size(); i2++){
                                String desc = quest.description.get(i2);

                                final String regex = "\\{image:.*?}";

                                if(desc.contains("{image:")){
                                    if(!joiner.toString().isBlank()){
                                        transKeys.put(prefix + ".quest." + (i1+1) + ".description." + num, joiner.toString());
                                        descList.add("{" + prefix + ".quest." + (i1+1) + ".description." + num + "}");
                                        joiner = new StringJoiner("\n");
                                        num++;
                                    }

                                    final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
                                    final Matcher matcher = pattern.matcher(desc);

                                    while (matcher.find()) {
                                        desc = desc.replace(matcher.group(0), "");
                                        descList.add(matcher.group(0));
                                    }
                                }else{
                                    if(desc.isBlank()){
                                        joiner.add("\n");
                                    }else{
                                        joiner.add(desc);
                                    }
                                }
                            }

                            if(!joiner.toString().isBlank()){
                                transKeys.put(prefix + ".quest." + (i1+1) + ".description." + num, joiner.toString());
                                descList.add("{" + prefix + ".quest." + (i1+1) + ".description." + num + "}");
                            }

                            quest.description.clear();
                            quest.description.addAll(descList);
                        }

                        for(int i2 = 0; i2 < quest.tasks.size(); i2++){
                            Task task = quest.tasks.get(i2);

                            if(!task.title.isBlank()){
                                transKeys.put(prefix + ".quest." + (i1+1) + ".task." + (i2+1) + ".title", task.title);
                                task.title = "{" + prefix + ".quest." + (i1+1) + ".task." + (i2+1) + ".title}";
                            }
                        }

                        for(int i2 = 0; i2 < quest.rewards.size(); i2++){
                            Reward reward = quest.rewards.get(i2);

                            if(!reward.title.isBlank()){
                                transKeys.put(prefix + ".quest." + (i1+1) + ".reward." + (i2+1) + ".title", reward.title);
                                reward.title = "{" + prefix + ".quest." + (i1+1) + ".reward." + (i2+1) + ".title}";
                            }
                        }
                    }
                }

                File output = new File(parent, "config/ftbquests");

                file.writeDataFull(output.toPath());

                String lang = Ctx.getArgument("lang", String.class);
                saveLang(transKeys, lang, transFiles);

                if(!lang.equalsIgnoreCase("en_us")){
                    saveLang(transKeys, "en_us", transFiles);
                }

                Ctx.getSource().getPlayerOrException().sendMessage(Component.nullToEmpty(I18n.get("command.ftbqlocalizationkeys.tooltip" + parent.getAbsolutePath())), Util.NIL_UUID);

            }catch(Exception e){
                e.printStackTrace();
            }

            return 1;
        }).build();

        rootCommandNode.addChild(commandNode);
        commandNode.addChild(argumentCommandNode);
    }

    private static void saveLang(TreeMap<String, String> transKeys, String lang, File parent) throws IOException{
        File fe = new File(parent, lang.toLowerCase(Locale.ROOT) + ".json");
        FileUtils.write(fe, FTBQLocalizationKeysMod.gson.toJson(transKeys), StandardCharsets.UTF_8);
    }

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register(FTBQLocalizationKeysMod::serverRegisterCommandsEvent);
    }
}
