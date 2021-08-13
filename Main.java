import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

import static java.lang.Math.abs;

public class Main extends JavaPlugin implements Listener {
    public static Player player1=null;
    public static Player player2=null;
    @Override
    public void onEnable() {
        System.out.println("攻击等级差插件已启动");
        this.saveDefaultConfig();
        this.reloadConfig();
        this.getCommand("pld").setExecutor((CommandExecutor)this);
        getServer().getPluginManager().registerEvents(this, this);
    }
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (args.length ==0&&sender.isOp()){
            sender.sendMessage("§a/pld reload  §f重载配置文件");
            sender.sendMessage("§a/pld set 等级  §f设置攻击等级差");
            return true;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            if (sender.isOp()) {
                this.reloadConfig();
                sender.sendMessage("§2配置文件重载完毕");
                final File file = new File(this.getDataFolder(), "config.yml");
                if (!file.exists()) {
                    this.saveDefaultConfig();
                }
            }
            else {
                sender.sendMessage("§c你没有权限使用此指令");
            }
        }
        if (args[0].equalsIgnoreCase("set")&&sender.isOp()){
            int level1= Integer.parseInt(args[1]);
            this.getConfig().set("Level",level1);
            this.saveConfig();
            this.reloadConfig();
            sender.sendMessage("§a你已将攻击等级差设置成"+level1+"级");
        }
        return true;
    }
    @EventHandler
    public void pvp(EntityDamageByEntityEvent level){
        if (!(level.getEntity() instanceof Player)){
            return;
        }
        if (!(level.getDamager() instanceof Player)){
            return;
        }
        else {
            Player player1 = (Player)level.getDamager();
            Player player2= (Player)level.getEntity();
            int level2= player2.getLevel();
            int level1 = player1.getLevel();
            int level3 = abs(level1-level2);
            if (level3>this.getConfig().getInt("Level")){
                player1.sendMessage("§c你的等级与被攻击者的等级相差超过"+this.getConfig().getInt("Level")+"级，无法互相伤害");
                level.setCancelled(true);
            }
        }
    }
}

