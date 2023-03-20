package me.lever.ipcbot;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public interface Command
{
    PermissionType getPermissionType();
    boolean isPrivate();
    String getDesc();
    String getName();
    CommandData getCommandData();
    int cooldownInSeconds();
    void onCommandEvent(SlashCommandInteractionEvent e);
}
