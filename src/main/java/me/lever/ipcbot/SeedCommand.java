package me.lever.ipcbot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class SeedCommand implements Command
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
        return "Gets the seed of the world loaded right now";
    }

    @Override
    public String getName() {
        return "getseed";
    }

    @Override
    public CommandData getCommandData() {
        return Utilities.createData(this);
    }

    @Override
    public int cooldownInSeconds() {
        return 5;
    }

    @Override
    public void onCommandEvent(SlashCommandInteractionEvent e) {
      //  e.reply("yes").queue(); hello just testing smth. this should be private
        e.replyEmbeds(new EmbedBuilder().setDescription("The seed of this server is " + Main.plugin.getServer().getWorlds().get(0).getSeed() + "!").build()).setEphemeral(isPrivate()).queue();
    }
}
