package me.lever.ipcbot;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SpawnWitherCommand implements Command
{
    @Override
    public PermissionType getPermissionType() {
        return PermissionType.TESTERS;
    }

    @Override
    public boolean isPrivate() {
        return false;
    }

    @Override
    public String getDesc() {
        return new String("Spawns an entity on me");
    }

    @Override
    public String getName() {
        return new String("spawn");
    }

    @Override
    public CommandData getCommandData() {
        return Utilities.createData(this);
    }

    @Override
    public int cooldownInSeconds() {
        return 30;
    }

    @Override
    public void onCommandEvent(SlashCommandInteractionEvent event)
    {
        if(event.getName().equalsIgnoreCase(this.getName()))
        {
            OptionMapping opt = event.getOption("entity");
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    if(Main.plugin.getServer().getOnlinePlayers().size()==0)
                    {
                        event.reply("There are no players currently on the server").queue();
                        return;
                    }
                    Player p = (Player)Main.plugin.getServer().getOnlinePlayers().toArray()[0];
                    if(p!=null && EntityType.valueOf(opt.getAsString()).isSpawnable())
                        event.reply("Successfully spawned " + opt.getAsString() + " on " + p.getDisplayName()).setEphemeral(isPrivate()).queue();
                    else
                        event.reply("This entity does not exist or there are 0 players on right now").queue();
                    if(p.getLocation().getWorld()!=null)
                        p.getLocation().getWorld().spawnEntity(p.getLocation(), EntityType.valueOf(opt.getAsString()));
                }
            }.runTask(Main.plugin);

        }
    }
}
