package me.lever.ipcbot;

import com.sun.org.apache.bcel.internal.generic.NEW;
import jdk.jfr.internal.LogLevel;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.w3c.dom.Text;

import javax.sound.sampled.DataLine;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class Main extends JavaPlugin
{
    public FileConfiguration config;
    public static JDA jda;
    public static Main plugin;
    public long lastLoadedUnixTime;
    public static Guild testingGuild;
    public static String tokenToUse;
    @Override
    public void onEnable()
    {
        plugin = this;
        config = this.getConfig();
        config.addDefault("ServerName", "Paper server");
        config.addDefault("BotToken", "Discord Bot Token");
        config.addDefault("ServerIP", "play.itslatch.net");
        config.addDefault("TestingGuildID", "guildID");
        config.addDefault("EveryHourMessageChannel", "channel ID");
        config.addDefault("EveryHourMessage", "BONK");
        config.addDefault("ActivityName", "activity name");
        config.options().copyDefaults(true);
        saveConfig();
        if(config.get("BotToken").equals("bot_main_lever_test"))
            tokenToUse = Hidden.BOTTOKENMAIN;
        else
            tokenToUse = config.getString("BotToken");
        lastLoadedUnixTime = System.currentTimeMillis();
        try {
        jda = JDABuilder.createDefault(tokenToUse).setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.playing(config.getString("ActivityName"))).addEventListeners(new CommandListener()).enableIntents(GatewayIntent.MESSAGE_CONTENT).build().awaitReady();
    }
    catch (InterruptedException exception){}
        Button conf = Button.primary("confirmationButton", "Bonk me :)");
        ActionRow ar = ActionRow.of(conf);
        TextChannel channel = jda.getTextChannelById("1017547110897422399");
        if(channel == null)
            getLogger().log(Level.WARNING,"The channel " + channel.getId() + " is null!");
        Utilities.sendMessage(channel, "The server " + getServer().getName() +
                " is now loaded! Please join with the ip " + getServer().getIp() + " on version " + getServer().getBukkitVersion(),ar);
        Guild guild = jda.getGuildById("1017547110897422396");
        testingGuild = guild;


        Date cD = new Date();
        int diffMin = 60 - cD.getMinutes() - 1;
        int diffSec = 60 - cD.getSeconds();
        int res = (diffSec) + (diffMin * 60);
        System.out.println("Hours is " + cD.getHours() + " Min left is " + diffMin + " and seconds left is "+ diffSec + "res is " + res);
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Utilities.sendMessage(guild.getTextChannelById( config.getString("EveryHourMessageChannel")), config.getString("EveryHourMessage"), ar);
            }
        }, res, 3600, TimeUnit.SECONDS);
       // jda.upsertCommand("serverinfo", "Get basic server info").queue();
       // guild.upsertCommand("spawn", "Spawns an entity on me").addOption(OptionType.STRING, "entity", "Id of entity to spawn", true).queue();
    }
}
