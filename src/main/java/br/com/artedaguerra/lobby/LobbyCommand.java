package br.com.artedaguerra.lobby;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Executor de comandos do sistema de lobby
 * Gerencia os comandos /lobby, /era e /eras
 * 
 * @author Arte da Guerra Team
 * @version 1.0.0-SNAPSHOT
 */
public class LobbyCommand implements CommandExecutor {
    
    private final LobbyManager lobbyManager;
    
    public LobbyCommand(LobbyManager lobbyManager) {
        this.lobbyManager = lobbyManager;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cEste comando só pode ser usado por jogadores!");
            return true;
        }
        
        Player player = (Player) sender;
        String commandName = command.getName().toLowerCase();
        
        switch (commandName) {
            case "lobby":
                return executarComandoLobby(player, args);
                
            case "era":
                return executarComandoEra(player, args);
                
            case "eras":
                return executarComandoEras(player, args);
                
            default:
                return false;
        }
    }
    
    /**
     * Executa o comando /lobby
     */
    private boolean executarComandoLobby(Player player, String[] args) {
        if (!player.hasPermission("artedaguerra.lobby")) {
            player.sendMessage("§cVocê não tem permissão para usar este comando!");
            return true;
        }
        
        lobbyManager.teletransportarParaLobby(player);
        return true;
    }
    
    /**
     * Executa o comando /era <numero>
     */
    private boolean executarComandoEra(Player player, String[] args) {
        if (!player.hasPermission("artedaguerra.era")) {
            player.sendMessage("§cVocê não tem permissão para usar este comando!");
            return true;
        }
        
        if (args.length != 1) {
            player.sendMessage("§cUso correto: /era <número>");
            player.sendMessage("§7Exemplo: /era 1");
            return true;
        }
        
        try {
            int numeroEra = Integer.parseInt(args[0]);
            
            if (numeroEra < 1 || numeroEra > 7) {
                player.sendMessage("§cNúmero de era inválido! Use números de 1 a 7.");
                return true;
            }
            
            lobbyManager.teletransportarParaEra(player, numeroEra);
            
        } catch (NumberFormatException e) {
            player.sendMessage("§cNúmero inválido! Use /era <número>");
            player.sendMessage("§7Exemplo: /era 1");
        }
        
        return true;
    }
    
    /**
     * Executa o comando /eras
     */
    private boolean executarComandoEras(Player player, String[] args) {
        if (!player.hasPermission("artedaguerra.eras")) {
            player.sendMessage("§cVocê não tem permissão para usar este comando!");
            return true;
        }
        
        lobbyManager.listarEras(player);
        return true;
    }
}