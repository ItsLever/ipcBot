package me.lever.ipcbot;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class Utilities
{
    static void sendMessage(TextChannel ch, String msg, ActionRow ar)
    {
        if(ar!=null)
            ch.sendMessage(new MessageCreateBuilder().addContent(msg).setActionRow(ar.getComponents()).build()).queue();
        else
            ch.sendMessage(msg).queue();
    }
    static CommandData createData(Command command) {return Commands.slash(command.getName(), command.getDesc());}
}
