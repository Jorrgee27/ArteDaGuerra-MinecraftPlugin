package br.com.artedaguerra;

import br.com.artedaguerra.lobby.LobbyCommand;
import br.com.artedaguerra.lobby.LobbyListener;
import br.com.artedaguerra.lobby.LobbyManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Classe principal do plugin Arte da Guerra
 * Gerencia a inicialização e coordenação de todos os sistemas
 * 
 * @author Arte da Guerra Team
 * @version 1.0.0-SNAPSHOT
 */
public class ArteDaGuerraCore extends JavaPlugin {
    
    private static ArteDaGuerraCore instance;
    private LobbyManager lobbyManager;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Salvar configuração padrão
        saveDefaultConfig();
        
        // Validar configuração
        if (!validarConfiguracao()) {
            getLogger().severe("Erro na configuração! Desabilitando plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        // Inicializar sistemas
        inicializarSistemas();
        
        // Registrar comandos
        registrarComandos();
        
        // Registrar eventos
        registrarEventos();
        
        getLogger().info("Plugin Arte da Guerra ativado com sucesso!");
        getLogger().info("Sistema de Lobby das 7 Eras inicializado!");
    }
    
    @Override
    public void onDisable() {
        // Finalizar sistemas
        finalizarSistemas();
        
        getLogger().info("Plugin Arte da Guerra desativado!");
    }
    
    /**
     * Inicializa todos os sistemas do plugin
     */
    private void inicializarSistemas() {
        // Inicializar LobbyManager
        lobbyManager = new LobbyManager(this);
        lobbyManager.inicializar();
    }
    
    /**
     * Finaliza todos os sistemas do plugin
     */
    private void finalizarSistemas() {
        if (lobbyManager != null) {
            lobbyManager.finalizar();
        }
    }
    
    /**
     * Registra todos os comandos do plugin
     */
    private void registrarComandos() {
        // Comandos do lobby
        getCommand("lobby").setExecutor(new LobbyCommand(lobbyManager));
        getCommand("era").setExecutor(new LobbyCommand(lobbyManager));
        getCommand("eras").setExecutor(new LobbyCommand(lobbyManager));
    }
    
    /**
     * Registra todos os eventos do plugin
     */
    private void registrarEventos() {
        // Eventos do lobby
        getServer().getPluginManager().registerEvents(new LobbyListener(lobbyManager), this);
    }
    
    /**
     * Valida a configuração do plugin
     */
    private boolean validarConfiguracao() {
        try {
            // Verificar seções obrigatórias
            if (!getConfig().contains("geral") || 
                !getConfig().contains("lobby") || 
                !getConfig().contains("eras")) {
                getLogger().severe("Configuração incompleta! Seções obrigatórias não encontradas.");
                return false;
            }
            
            // Verificar configurações do lobby
            if (!getConfig().contains("lobby.mundo_nome")) {
                getLogger().severe("Nome do mundo do lobby não configurado!");
                return false;
            }
            
            return true;
        } catch (Exception e) {
            getLogger().severe("Erro ao validar configuração: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("artedaguerra")) {
            return executarComandoPrincipal(sender, args);
        }
        return false;
    }
    
    /**
     * Executa o comando principal do plugin
     */
    private boolean executarComandoPrincipal(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§6=== Arte da Guerra v" + getDescription().getVersion() + " ===");
            sender.sendMessage("§7Plugin de evolução da humanidade através de 7 eras históricas");
            sender.sendMessage("§7Comandos disponíveis:");
            sender.sendMessage("§e/lobby §7- Ir para o lobby das eras");
            sender.sendMessage("§e/eras §7- Listar todas as eras");
            sender.sendMessage("§e/era <número> §7- Ir para uma era específica");
            return true;
        }
        
        String subcomando = args[0].toLowerCase();
        
        switch (subcomando) {
            case "reload":
                if (!sender.hasPermission("artedaguerra.admin")) {
                    sender.sendMessage("§cVocê não tem permissão para usar este comando!");
                    return true;
                }
                reloadConfig();
                sender.sendMessage("§aConfiguração recarregada com sucesso!");
                return true;
                
            case "info":
                sender.sendMessage("§6=== Informações do Plugin ===");
                sender.sendMessage("§7Nome: §e" + getDescription().getName());
                sender.sendMessage("§7Versão: §e" + getDescription().getVersion());
                sender.sendMessage("§7Autor: §e" + getDescription().getAuthors());
                sender.sendMessage("§7Website: §e" + getDescription().getWebsite());
                return true;
                
            case "status":
                if (!sender.hasPermission("artedaguerra.admin")) {
                    sender.sendMessage("§cVocê não tem permissão para usar este comando!");
                    return true;
                }
                sender.sendMessage("§6=== Status do Sistema ===");
                sender.sendMessage("§7Lobby Manager: §a" + (lobbyManager != null ? "Ativo" : "Inativo"));
                sender.sendMessage("§7Jogadores Online: §e" + Bukkit.getOnlinePlayers().size());
                return true;
                
            default:
                sender.sendMessage("§cSubcomando não reconhecido! Use /artedaguerra para ver os comandos disponíveis.");
                return true;
        }
    }
    
    /**
     * Obtém a instância do plugin
     */
    public static ArteDaGuerraCore getInstance() {
        return instance;
    }
    
    /**
     * Obtém o gerenciador do lobby
     */
    public LobbyManager getLobbyManager() {
        return lobbyManager;
    }
}