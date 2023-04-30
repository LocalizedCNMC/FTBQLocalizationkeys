<center><div align="center">

<img height="120" src="icon/300x300.png" width="120"/>

# FTBQLocalizationKeys-Reconverted

A [FTB Quests: Localization keys](https://www.curseforge.com/minecraft/mc-mods/ftb-quests-localization-keys) unofficial fork. 
Support Forge/Fabric/Quilt.

[ZH-CN](README-ZHCN.md) / EN-US

</div></center>

### How to use ?
- Install FTB Quests and KubeJS
- Run your modpack with **fully written quests**
- Write in chat: `/ftbqkey <language code>` (/ftbqkey zh_cn). Language code affects only file name.
- All files will be in the folder `<gamedir>/ftbqkeys/`
- Move the generated files to the appropriate folders (`kubejs/assets/kubejs/lang` and `config/ftbquests/quests`).
- After generating the language you can delete this mod.

### Precautions
- **1.0.0-1.16.5 requires Java 16+**
- If you want to add new quests to the already generated keys, don't do it. All subsequent quests and their localization keys will have to be written manually. This mod should only be used when you have finished writing all the text for your quests.
- It is better to add images after generating localization.
- Images do not perform well in the center of the text.
- If a "Format error" error appears at the top of the text, rewrite the text and try not to use any symbols.
- Task backups are generated in the `ftbqkeys` folder.


### This mod code reference/use:
- [Horeak/ftb-quest-lang-generator (FTB Quests: Localization keys)](https://github.com/Horeak/ftb-quest-lang-generator) (Forge 1.18+) (CC-BY-4.0)
- [criscky/ftb-quest-lang-generator](https://github.com/criscky/ftb-quest-lang-generator) (Forge 1.16.5) (CC-BY-4.0)
- [DM-Fabric-Ports/ftb-quest-lang-generator (FTB Quests: Localization Keys Refabricated)](https://github.com/DM-Fabric-Ports/ftb-quest-lang-generator) (Fabric 1.18+) (CC-BY-4.0)