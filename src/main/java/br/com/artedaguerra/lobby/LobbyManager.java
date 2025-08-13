package br.com.artedaguerra.lobby;

import br.com.artedaguerra.ArteDaGuerraCore;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * Gerenciador do sistema de lobby temático das 7 eras
 * Responsável por teletransportar jogadores, gerenciar áreas temáticas,
 * fornecer itens de navegação e controlar acesso às dimensões das eras
 * 
 * @author Arte da Guerra Team
 * @version 1.0.0-SNAPSHOT
 */
public class LobbyManager {
    
    private final ArteDaGuerraCore plugin;
    private Location lobbySpawn;
    private final Map<Integer, Location> eraLocations;
    private final Map<UUID, Long> teleportCooldowns;
    
    // Configurações
    private String lobbyWorldName;
    private int teleportCooldown;
    private boolean protectionEnabled;
    private boolean navigationItemsEnabled;
    
    public LobbyManager(ArteDaGuerraCore plugin) {
        this.plugin = plugin;
        this.eraLocations = new HashMap<>();
        this.teleportCooldowns = new HashMap<>();
        
        carregarConfiguracoes();
    }
    
    /**
     * Inicializa o sistema de lobby
     */
    public void inicializar() {
        carregarConfiguracoes();
        configurarLobbySpawn();
        configurarEraLocations();
        criarEstruturasLobby();
        
        plugin.getLogger().info("Sistema de Lobby inicializado com sucesso!");
    }
    
    /**
     * Finaliza o sistema de lobby
     */
    public void finalizar() {
        teleportCooldowns.clear();
        eraLocations.clear();
        
        plugin.getLogger().info("Sistema de Lobby finalizado!");
    }
    
    /**
     * Carrega as configurações do arquivo config.yml
     */
    private void carregarConfiguracoes() {
        lobbyWorldName = plugin.getConfig().getString("lobby.mundo_nome", "lobby");
        teleportCooldown = plugin.getConfig().getInt("lobby.teleport_cooldown", 3);
        protectionEnabled = plugin.getConfig().getBoolean("lobby.protecao_ativada", true);
        navigationItemsEnabled = plugin.getConfig().getBoolean("lobby.itens_navegacao", true);
    }
    
    /**
     * Configura o spawn do lobby
     */
    private void configurarLobbySpawn() {
        World lobbyWorld = Bukkit.getWorld(lobbyWorldName);
        
        if (lobbyWorld == null) {
            plugin.getLogger().warning("Mundo do lobby '" + lobbyWorldName + "' não encontrado! Criando...");
            lobbyWorld = criarMundoLobby();
        }
        
        if (lobbyWorld != null) {
            double x = plugin.getConfig().getDouble("lobby.spawn_x", 0);
            double y = plugin.getConfig().getDouble("lobby.spawn_y", 100);
            double z = plugin.getConfig().getDouble("lobby.spawn_z", 0);
            
            lobbySpawn = new Location(lobbyWorld, x, y, z);
            lobbySpawn.setYaw(0);
            lobbySpawn.setPitch(0);
        }
    }
    
    /**
     * Cria o mundo do lobby se não existir
     */
    private World criarMundoLobby() {
        try {
            WorldCreator creator = new WorldCreator(lobbyWorldName);
            creator.type(WorldType.FLAT);
            creator.generateStructures(false);
            
            World world = creator.createWorld();
            
            if (world != null) {
                // Configurações do mundo
                world.setDifficulty(Difficulty.PEACEFUL);
                world.setSpawnFlags(false, false);
                world.setKeepSpawnInMemory(true);
                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
                world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
                world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
                world.setTime(6000); // Meio-dia
                
                plugin.getLogger().info("Mundo do lobby criado com sucesso!");
            }
            
            return world;
        } catch (Exception e) {
            plugin.getLogger().severe("Erro ao criar mundo do lobby: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Configura as localizações das eras no lobby
     */
    private void configurarEraLocations() {
        if (lobbySpawn == null) return;
        
        World lobbyWorld = lobbySpawn.getWorld();
        
        // Configurar localizações em círculo ao redor do spawn
        double radius = 20.0;
        double angleStep = 360.0 / 7.0; // 7 eras
        
        for (int era = 1; era <= 7; era++) {
            double angle = Math.toRadians((era - 1) * angleStep);
            double x = lobbySpawn.getX() + (radius * Math.cos(angle));
            double z = lobbySpawn.getZ() + (radius * Math.sin(angle));
            double y = lobbySpawn.getY();
            
            Location eraLocation = new Location(lobbyWorld, x, y, z);
            eraLocations.put(era, eraLocation);
        }
    }
    
    /**
     * Cria as estruturas temáticas do lobby
     */
    private void criarEstruturasLobby() {
        if (lobbySpawn == null) return;
        
        // Criar plataforma central
        criarPlataformaCentral();
        
        // Criar áreas temáticas das eras
        for (int era = 1; era <= 7; era++) {
            criarAreaTematica(era);
        }
    }
    
    /**
     * Cria a plataforma central do lobby
     */
    private void criarPlataformaCentral() {
        World world = lobbySpawn.getWorld();
        int centerX = lobbySpawn.getBlockX();
        int centerY = lobbySpawn.getBlockY() - 1;
        int centerZ = lobbySpawn.getBlockZ();
        
        // Criar plataforma circular
        for (int x = -5; x <= 5; x++) {
            for (int z = -5; z <= 5; z++) {
                double distance = Math.sqrt(x * x + z * z);
                if (distance <= 5) {
                    Block block = world.getBlockAt(centerX + x, centerY, centerZ + z);
                    block.setType(Material.QUARTZ_BLOCK);
                }
            }
        }
        
        // Adicionar decorações
        Block centerBlock = world.getBlockAt(centerX, centerY + 1, centerZ);
        centerBlock.setType(Material.BEACON);
    }
    
    /**
     * Cria uma área temática para uma era específica
     */
    private void criarAreaTematica(int era) {
        Location eraLocation = eraLocations.get(era);
        if (eraLocation == null) return;
        
        World world = eraLocation.getWorld();
        int x = eraLocation.getBlockX();
        int y = eraLocation.getBlockY() - 1;
        int z = eraLocation.getBlockZ();
        
        // Material base da era
        Material baseMaterial = obterMaterialEra(era);
        
        // Criar plataforma da era
        for (int dx = -3; dx <= 3; dx++) {
            for (int dz = -3; dz <= 3; dz++) {
                Block block = world.getBlockAt(x + dx, y, z + dz);
                block.setType(baseMaterial);
            }
        }
        
        // Adicionar estrutura temática
        criarEstruturaTematica(era, eraLocation);
    }
    
    /**
     * Obtém o material base para uma era
     */
    private Material obterMaterialEra(int era) {
        switch (era) {
            case 1: return Material.DIRT; // Primitiva
            case 2: return Material.STONE; // Pós-Primitiva
            case 3: return Material.COBBLESTONE; // Medieval
            case 4: return Material.IRON_BLOCK; // Industrial
            case 5: return Material.WHITE_CONCRETE; // Moderna
            case 6: return Material.REDSTONE_BLOCK; // Contemporânea
            case 7: return Material.DIAMOND_BLOCK; // Futurista
            default: return Material.STONE;
        }
    }
    
    /**
     * Cria estrutura temática específica da era
     */
    private void criarEstruturaTematica(int era, Location location) {
        World world = location.getWorld();
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        
        switch (era) {
            case 1: // Primitiva - Fogueira
                world.getBlockAt(x, y, z).setType(Material.CAMPFIRE);
                break;
                
            case 2: // Pós-Primitiva - Altar de pedra
                world.getBlockAt(x, y, z).setType(Material.STONE_SLAB);
                world.getBlockAt(x, y + 1, z).setType(Material.TORCH);
                break;
                
            case 3: // Medieval - Torre
                for (int i = 0; i < 3; i++) {
                    world.getBlockAt(x, y + i, z).setType(Material.COBBLESTONE);
                }
                world.getBlockAt(x, y + 3, z).setType(Material.COBBLESTONE_STAIRS);
                break;
                
            case 4: // Industrial - Chaminé
                for (int i = 0; i < 4; i++) {
                    world.getBlockAt(x, y + i, z).setType(Material.BRICK_STAIRS);
                }
                break;
                
            case 5: // Moderna - Prédio
                world.getBlockAt(x, y, z).setType(Material.WHITE_CONCRETE);
                world.getBlockAt(x, y + 1, z).setType(Material.GLASS);
                world.getBlockAt(x, y + 2, z).setType(Material.WHITE_CONCRETE);
                break;
                
            case 6: // Contemporânea - Antena
                world.getBlockAt(x, y, z).setType(Material.REDSTONE_BLOCK);
                world.getBlockAt(x, y + 1, z).setType(Material.IRON_BARS);
                world.getBlockAt(x, y + 2, z).setType(Material.IRON_BARS);
                break;
                
            case 7: // Futurista - Portal
                world.getBlockAt(x, y, z).setType(Material.DIAMOND_BLOCK);
                world.getBlockAt(x, y + 1, z).setType(Material.END_ROD);
                break;
        }
    }
    
    /**
     * Teletransporta um jogador para o lobby
     */
    public boolean teletransportarParaLobby(Player player) {
        if (lobbySpawn == null) {
            player.sendMessage("§cLobby não configurado!");
            return false;
        }
        
        if (!verificarCooldown(player)) {
            return false;
        }
        
        player.teleport(lobbySpawn);
        
        if (navigationItemsEnabled) {
            fornecerItensNavegacao(player);
        }
        
        player.sendMessage("§aBem-vindo ao Lobby das 7 Eras!");
        player.sendMessage("§7Explore as diferentes eras da humanidade!");
        
        aplicarCooldown(player);
        return true;
    }
    
    /**
     * Teletransporta um jogador para uma era específica
     */
    public boolean teletransportarParaEra(Player player, int era) {
        if (era < 1 || era > 7) {
            player.sendMessage("§cEra inválida! Use números de 1 a 7.");
            return false;
        }
        
        if (!verificarAcessoEra(player, era)) {
            player.sendMessage("§cVocê não tem acesso a esta era!");
            return false;
        }
        
        if (!verificarCooldown(player)) {
            return false;
        }
        
        String nomeEra = obterNomeEra(era);
        String mundoEra = plugin.getConfig().getString("eras.era_" + era + ".mundo");
        
        if (mundoEra == null) {
            player.sendMessage("§cMundo da era não configurado!");
            return false;
        }
        
        World world = Bukkit.getWorld(mundoEra);
        if (world == null) {
            player.sendMessage("§cMundo da era '" + mundoEra + "' não encontrado!");
            return false;
        }
        
        Location spawnEra = world.getSpawnLocation();
        player.teleport(spawnEra);
        
        player.sendMessage("§aBem-vindo à Era " + era + ": " + nomeEra + "!");
        
        aplicarCooldown(player);
        return true;
    }
    
    /**
     * Verifica se o jogador tem acesso a uma era
     */
    public boolean verificarAcessoEra(Player player, int era) {
        // Verificar permissão
        if (!player.hasPermission("artedaguerra.era." + era)) {
            return false;
        }
        
        // Verificar se a era está desbloqueada
        boolean desbloqueada = plugin.getConfig().getBoolean("eras.era_" + era + ".desbloqueada", false);
        return desbloqueada;
    }
    
    /**
     * Verifica o cooldown de teletransporte
     */
    private boolean verificarCooldown(Player player) {
        UUID playerId = player.getUniqueId();
        
        if (teleportCooldowns.containsKey(playerId)) {
            long tempoRestante = (teleportCooldowns.get(playerId) + (teleportCooldown * 1000)) - System.currentTimeMillis();
            
            if (tempoRestante > 0) {
                int segundos = (int) (tempoRestante / 1000) + 1;
                player.sendMessage("§cAguarde " + segundos + " segundos antes de se teletransportar novamente!");
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Aplica cooldown de teletransporte
     */
    private void aplicarCooldown(Player player) {
        teleportCooldowns.put(player.getUniqueId(), System.currentTimeMillis());
        
        // Remover cooldown após o tempo especificado
        new BukkitRunnable() {
            @Override
            public void run() {
                teleportCooldowns.remove(player.getUniqueId());
            }
        }.runTaskLater(plugin, teleportCooldown * 20L);
    }
    
    /**
     * Fornece itens de navegação para o jogador
     */
    public void fornecerItensNavegacao(Player player) {
        player.getInventory().clear();
        
        // Item para abrir menu de eras
        ItemStack menuEras = new ItemStack(Material.COMPASS);
        ItemMeta metaMenu = menuEras.getItemMeta();
        metaMenu.setDisplayName("§6Menu das Eras");
        metaMenu.setLore(Arrays.asList(
            "§7Clique para abrir o menu",
            "§7de seleção de eras"
        ));
        menuEras.setItemMeta(metaMenu);
        
        // Item de informações
        ItemStack info = new ItemStack(Material.BOOK);
        ItemMeta metaInfo = info.getItemMeta();
        metaInfo.setDisplayName("§eInformações do Projeto");
        metaInfo.setLore(Arrays.asList(
            "§7Clique para ver informações",
            "§7sobre o projeto Arte da Guerra"
        ));
        info.setItemMeta(metaInfo);
        
        player.getInventory().setItem(4, menuEras);
        player.getInventory().setItem(8, info);
    }
    
    /**
     * Obtém o nome de uma era
     */
    public String obterNomeEra(int era) {
        return plugin.getConfig().getString("eras.era_" + era + ".nome", "Era " + era);
    }
    
    /**
     * Obtém o período de uma era
     */
    public String obterPeriodoEra(int era) {
        return plugin.getConfig().getString("eras.era_" + era + ".periodo", "Período não definido");
    }
    
    /**
     * Lista todas as eras disponíveis
     */
    public void listarEras(Player player) {
        player.sendMessage("§6=== Eras Disponíveis ===");
        
        for (int era = 1; era <= 7; era++) {
            String nome = obterNomeEra(era);
            String periodo = obterPeriodoEra(era);
            boolean temAcesso = verificarAcessoEra(player, era);
            
            String status = temAcesso ? "§a✓" : "§c✗";
            player.sendMessage(status + " §7Era " + era + ": §e" + nome + " §7(" + periodo + ")");
        }
        
        player.sendMessage("§7Use /era <número> para viajar para uma era específica!");
    }
    
    // Getters
    public Location getLobbySpawn() {
        return lobbySpawn;
    }
    
    public Map<Integer, Location> getEraLocations() {
        return eraLocations;
    }
    
    public boolean isProtectionEnabled() {
        return protectionEnabled;
    }
    
    public boolean isNavigationItemsEnabled() {
        return navigationItemsEnabled;
    }
}