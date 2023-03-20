package me.lever.ipcbot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.bukkit.util.permissions.CommandPermissions;

import java.util.HashMap;

public class CommandManager
{
    private HashMap<String, Command> cmds = new HashMap();
    private HashMap<Long, HashMap<String, Long>> cmdRegTimePerUser = new HashMap<Long, HashMap<String, Long>>();
    public CommandManager(Guild guild, JDA jda, boolean doAppropriateUpdate)
    {
        if(doAppropriateUpdate)
        {
            if(guild!=null)
                upsertAllCommands(guild);
            if(jda!=null)
                upsertAllCommands(jda);
        }
        registerCommands();
    }
    private void upsertAllCommands(JDA jdaInstance)
    {
        System.out.println("Upserting all commands global scope");
        jdaInstance.updateCommands().addCommands(new InfoCommand().getCommandData(), new SpawnWitherCommand().getCommandData(), new PrivateShutdownCommand().getCommandData()).queue();
    }
    private void upsertAllCommands(Guild guild)
    {
        System.out.println( "Upserting all commands in the guild " + guild.getName());
        guild.updateCommands().addCommands(new InfoCommand().getCommandData(), new SpawnWitherCommand().getCommandData(), new PrivateShutdownCommand().getCommandData()).queue();
        CommandData spawnCmd = new SpawnWitherCommand().getCommandData();
        CommandData seedCmd = new SeedCommand().getCommandData();
        guild.updateCommands().addCommands(spawnCmd.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),
                seedCmd.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))).queue();
    }
    public static void addEventListeners(JDA jdaInstance)
    {

    }
    public void registerCommands()
    {
        InfoCommand iC = new InfoCommand();
        SpawnWitherCommand sP = new SpawnWitherCommand();
        PrivateShutdownCommand shC = new PrivateShutdownCommand();
        SeedCommand sC = new SeedCommand();
        cmds.put(iC.getName(), iC);
        cmds.put(sP.getName(), sP);
        cmds.put(shC.getName(), shC);
        cmds.put(sC.getName(), sC);
        System.out.println("All commands have sucessfully been registered");
    }
    public void runCommandLogic(SlashCommandInteractionEvent e)
    {
        if(cmds.containsKey(e.getName()))
        {
            if(cmdRegTimePerUser.containsKey(e.getUser().getIdLong())) {
                if (cmdRegTimePerUser.get(e.getUser().getIdLong()).containsKey(e.getName())) {
                    long cTMs = System.currentTimeMillis();
                    if (cTMs - cmdRegTimePerUser.get(e.getUser().getIdLong()).get(e.getName()) < (cmds.get(e.getName()).cooldownInSeconds() * 1000L)) {
                        e.replyEmbeds(new EmbedBuilder().setDescription("There is still " +
                                (((cmdRegTimePerUser.get(e.getUser().getIdLong()).get(e.getName()) + (cmds.get(e.getName()).cooldownInSeconds() * 1000L)) - cTMs) / 1000) + " seconds before you may use " + e.getName()).build()).queue();
                        return;
                    }
                }
            }
            switch (cmds.get(e.getName()).getPermissionType())
            {
                case OWNER:
                    if (e.getUser().getIdLong() == Permissions.OWNER)
                        cmds.get(e.getName()).onCommandEvent(e);
                    else
                        e.reply("You are required to be in OWNER scope to be able to use this command").setEphemeral(true).queue();
                    break;
                case TESTERS:
                    if (Permissions.TESTERLIST.contains(e.getUser().getIdLong()) || e.getUser().getIdLong() == Permissions.OWNER)
                        cmds.get(e.getName()).onCommandEvent(e);
                    else
                        e.reply("You are required to be in TESTERS scope to be able to use this command. Ask Lever to become a tester and perhaps he will let you").setEphemeral(true).queue();
                    break;
                case NONE:
                    cmds.get(e.getName()).onCommandEvent(e);
                    break;
            }
                if (!cmdRegTimePerUser.containsKey(e.getUser().getIdLong()))
                {
                    HashMap<String, Long> cmdCooldownDurations = new HashMap<>();
                    cmdCooldownDurations.put(e.getName(), System.currentTimeMillis());
                    cmdRegTimePerUser.put(e.getUser().getIdLong(), cmdCooldownDurations);
                }
                else {
                    cmdRegTimePerUser.get(e.getUser().getIdLong()).put(e.getName(), System.currentTimeMillis());
                    cmdRegTimePerUser.put(e.getUser().getIdLong(), cmdRegTimePerUser.get(e.getUser().getIdLong()));
                }
        }
    }
    public void runButtonLogic(ButtonInteractionEvent e)
    {
        //for now its hardcoded, if there are more buttons it will not be
        if(e.getButton().getId().equals("confirmationButton"))
            e.getMessage().editMessage("Thank you " + e.getUser().getAsTag()).queue();
        e.reply("Thanks for the bonk, " + e.getUser().getAsTag()).setEphemeral(true).queue();
            e.getInteraction().editButton(e.getButton().asDisabled()).queue();
    }
}
