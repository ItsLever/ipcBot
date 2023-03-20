package me.lever.ipcbot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;

public class InfoCommand implements Command//extends ListenerAdapter
{
    @Override
    public void onCommandEvent(SlashCommandInteractionEvent event)
    {
        {
            Color hex;
            if(Main.plugin.getServer().getOnlinePlayers().size()<1)
                hex = new Color(255, 17, 0);
            else if(Main.plugin.getServer().getOnlinePlayers().size()<3)
                hex = new Color(255, 153, 0);
            else if(Main.plugin.getServer().getOnlinePlayers().size()<5)
                hex = new Color(238, 255, 0);
            else
                hex = new Color(0, 255, 13);
            event.replyEmbeds(new EmbedBuilder()
                    .setDescription("Server: " + Main.plugin.config.getString("ServerName")
                            +"\n"+"Player count: " + Main.plugin.getServer().getOnlinePlayers().size() +"/"+ Main.plugin.getServer().getMaxPlayers() +
                    "\n" + "Server IP: " + Main.plugin.config.getString("ServerIP") +
                            "\nServer port: " + Main.plugin.getServer().getPort() +
                            "\nServer uptime: " + (int)Math.floor((System.currentTimeMillis()-Main.plugin.lastLoadedUnixTime)/3600000f) + " hours, "
                            + (int)Math.floor((System.currentTimeMillis()-Main.plugin.lastLoadedUnixTime)/60000f)%60 + " minutes, "
                    + (int)Math.floor((System.currentTimeMillis()-Main.plugin.lastLoadedUnixTime)/1000f)%60 + " seconds")
                    .setColor(hex).build()).setEphemeral(isPrivate()).queue();
        }
    }

    @Override
    public PermissionType getPermissionType() {
        return PermissionType.NONE;
    }

    @Override
    public boolean isPrivate() {
        return false;
    }

    @Override
    public String getDesc() {return new String("Get basic server info");}

    @Override
    public String getName() {return new String("serverinfo");}

    @Override
    public CommandData getCommandData() {return Utilities.createData(this);}

    @Override
    public int cooldownInSeconds() {
        return 15;
    }
}
