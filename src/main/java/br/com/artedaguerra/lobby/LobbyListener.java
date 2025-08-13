package br.com.artedaguerra.lobby;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

/**
 * Listener de eventos do sistema de lobby
 * Gerencia interações, proteções e eventos especiais do lobby
 * 
 * @author Arte da Guerra Team
 * @version 1.0.0-SNAPSHOT
 */
public class LobbyListener implements Listener {
    
    private final LobbyManager lobbyManager;
    private final LobbyGUI lobbyGUI;
    
    public LobbyListener(LobbyManager lobbyManager) {
        this.lobbyManager = lobbyManager;
        this.lobbyGUI = new LobbyGUI(lobbyManager);
    }
    
    /**
     * Gerencia cliques em itens de navegação
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || !isInLobby(player)) {
            return;
        }
        
        if (item.getType() == Material.COMPASS && 
            item.hasItemMeta() && 
            item.getItemMeta().getDisplayName().equals("§6Menu das Eras")) {
            
            event.setCancelled(true);
            lobbyGUI.abrirMenuSelecaoEras(player);
            
        } else if (item.getType() == Material.BOOK && 
                   item.hasItemMeta() && 
                   item.getItemMeta().getDisplayName().equals("§eInformações do Projeto")) {
            
            event.setCancelled(true);
            lobbyGUI.abrirMenuInformacoes(player);
        }
    }
    
    /**
     * Gerencia cliques em inventários (GUIs)
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();
        
        // Verificar se é uma GUI do lobby
        if (title.equals("§6Seleção de Eras") || 
            title.equals("§eInformações do Projeto") || 
            title.equals("§7Configurações")) {
            
            event.setCancelled(true);
            lobbyGUI.gerenciarClique(player, event);
        }
        
        // Proteger inventário no lobby
        if (isInLobby(player)) {
            event.setCancelled(true);
        }
    }
    
    /**
     * Protege contra quebra de blocos no lobby
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        
        if (isInLobby(player) && lobbyManager.isProtectionEnabled()) {
            if (!player.hasPermission("artedaguerra.admin")) {
                event.setCancelled(true);
                player.sendMessage("§cVocê não pode quebrar blocos no lobby!");
            }
        }
    }
    
    /**
     * Protege contra colocação de blocos no lobby
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        
        if (isInLobby(player) && lobbyManager.isProtectionEnabled()) {
            if (!player.hasPermission("artedaguerra.admin")) {
                event.setCancelled(true);
                player.sendMessage("§cVocê não pode colocar blocos no lobby!");
            }
        }
    }
    
    /**
     * Protege contra dano no lobby
     */
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            
            if (isInLobby(player) && lobbyManager.isProtectionEnabled()) {
                event.setCancelled(true);
            }
        }
    }
    
    /**
     * Protege contra fome no lobby
     */
    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            
            if (isInLobby(player) && lobbyManager.isProtectionEnabled()) {
                event.setCancelled(true);
                player.setFoodLevel(20);
            }
        }
    }
    
    /**
     * Protege contra drop de itens no lobby
     */
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        
        if (isInLobby(player) && lobbyManager.isProtectionEnabled()) {
            if (!player.hasPermission("artedaguerra.admin")) {
                event.setCancelled(true);
            }
        }
    }
    
    /**
     * Fornece itens de navegação quando o jogador entra no lobby
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        if (isInLobby(player) && lobbyManager.isNavigationItemsEnabled()) {
            lobbyManager.fornecerItensNavegacao(player);
        }
    }
    
    /**
     * Detecta movimento para mostrar informações de proximidade
     */
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        
        if (!isInLobby(player)) {
            return;
        }
        
        // Verificar proximidade com áreas das eras
        for (int era = 1; era <= 7; era++) {
            if (lobbyManager.getEraLocations().containsKey(era)) {
                double distance = player.getLocation().distance(lobbyManager.getEraLocations().get(era));
                
                if (distance <= 3.0) {
                    mostrarInformacaoEra(player, era);
                    break;
                }
            }
        }
    }
    
    /**
     * Mostra informações de uma era quando o jogador se aproxima
     */
    private void mostrarInformacaoEra(Player player, int era) {
        String nome = lobbyManager.obterNomeEra(era);
        String periodo = lobbyManager.obterPeriodoEra(era);
        boolean temAcesso = lobbyManager.verificarAcessoEra(player, era);
        
        player.sendActionBar("§6Era " + era + ": " + nome + " §7(" + periodo + ") " + 
                           (temAcesso ? "§a[Clique para entrar]" : "§c[Bloqueada]"));
    }
    
    /**
     * Verifica se o jogador está no lobby
     */
    private boolean isInLobby(Player player) {
        if (lobbyManager.getLobbySpawn() == null) {
            return false;
        }
        
        return player.getWorld().equals(lobbyManager.getLobbySpawn().getWorld());
    }
}