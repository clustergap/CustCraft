package cn.mscraft.custcraft.Loader.Lang;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class GetLangYaml {
    public static List<String> COMMAND_HELP;

    public static List<String> PANEL_NOT_EXIST;

    public static List<String> RECIPE_NOT_EXIST;

    public static List<String> OPEN_WRONG_FORMAT;

    public static List<String> VIEW_WRONG_FORMAT;

    public static List<String> ADD_WRONG_FORMAT;

    public static List<String> DEL_WRONG_FORMAT;

    public static List<String> EDIT_WRONG_FORMAT;

    public static List<String> PLAYER_NOT_ONLINE;

    public static List<String> RECIPE_EXISTED;

    public static List<String> RECIPE_SUCCESSFULLY_DEL;

    public static List<String> RECIPE_SUCCESSFULLY_EDIT;

    public static void get() {
        FileConfiguration langYaml = CreateLangYaml.langYaml;
        COMMAND_HELP = langYaml.getStringList("COMMAND_HELP");
        PANEL_NOT_EXIST = langYaml.getStringList("PANEL_NOT_EXIST");
        RECIPE_NOT_EXIST = langYaml.getStringList("RECIPE_NOT_EXIST");
        OPEN_WRONG_FORMAT = langYaml.getStringList("OPEN_WRONG_FORMAT");
        VIEW_WRONG_FORMAT = langYaml.getStringList("VIEW_WRONG_FORMAT");
        ADD_WRONG_FORMAT = langYaml.getStringList("ADD_WRONG_FORMAT");
        DEL_WRONG_FORMAT = langYaml.getStringList("DEL_WRONG_FORMAT");
        EDIT_WRONG_FORMAT = langYaml.getStringList("EDIT_WRONG_FORMAT");
        PLAYER_NOT_ONLINE = langYaml.getStringList("PLAYER_NOT_ONLINE");
        RECIPE_EXISTED = langYaml.getStringList("RECIPE_EXISTED");
        RECIPE_SUCCESSFULLY_DEL = langYaml.getStringList("RECIPE_SUCCESSFULLY_DEL");
        RECIPE_SUCCESSFULLY_EDIT = langYaml.getStringList("RECIPE_SUCCESSFULLY_EDIT");
    }
}