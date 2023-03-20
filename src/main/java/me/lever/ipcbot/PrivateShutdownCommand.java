package me.lever.ipcbot;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class PrivateShutdownCommand implements Command
{
    @Override
    public PermissionType getPermissionType() {
        return PermissionType.OWNER;
    }

    @Override
    public boolean isPrivate() {
        return true;
    }

    @Override
    public String getDesc() {
        return new String("Shutdown the bot (Only lever can use this).现在谁都能看到这个 command. 但是, 这个command 只是给我（Lever）用的");
    }

    @Override
    public String getName() {
        return new String("shutdown");
    }

    @Override
    public CommandData getCommandData() {
        return Utilities.createData(this);
    }

    @Override
    public int cooldownInSeconds() {
        return 0;
    }

    @Override
    public void onCommandEvent(SlashCommandInteractionEvent e) {
        e.reply("Shutting down!").setEphemeral(isPrivate()).queue();
        Main.jda.shutdown();
    }
}
