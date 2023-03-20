package me.lever.ipcbot;

import me.lever.ipcbot.Command;
import me.lever.ipcbot.CommandManager;
import me.lever.ipcbot.Main;
import me.lever.ipcbot.Utilities;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.logging.Level;

import static org.bukkit.Bukkit.getServer;

public class CommandListener extends ListenerAdapter
{
    public CommandManager commandManager;
    @Override
    public void onReady(ReadyEvent event)
    {
        System.out.println("ready!");
        commandManager = new CommandManager(Main.testingGuild, null, false);
        // add people to testerlist (temporary solution)
        Permissions.TESTERLIST.add(710671298317254748L); //neon(cscxdx)
        Permissions.TESTERLIST.add(410199639095246849L); //sir caio
    }
    @Override
    public void onMessageReceived(MessageReceivedEvent e)
    {
        if(e.getAuthor().getIdLong()!=Permissions.OWNER)
            return;
        if(e.getMessage().getContentRaw().startsWith("/reload jda confirm"))
        {
            JDA restartJda = e.getJDA();
            commandManager = new CommandManager(null, restartJda, true);
        }
        else if (e.getMessage().getContentRaw().startsWith("/reload guild confirm"))
        {
            Guild restartGuild = Main.testingGuild;
            commandManager = new CommandManager(restartGuild, null, true);
        }
    }
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {commandManager.runCommandLogic(event);}
    @Override
    public void onButtonInteraction(ButtonInteractionEvent e){commandManager.runButtonLogic(e);}
}
