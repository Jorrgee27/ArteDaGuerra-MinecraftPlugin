package br.com.artedaguerra.lobby;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

/**
 * Gerenciador de interfaces gráficas do lobby
 * Responsável por criar e gerenciar menus de seleção de eras,
 * informações detalhadas e configurações
 * 
 * @author Arte da Guerra Team
 * @version 1.0.0-SNAPSHOT
 */
public class LobbyGUI {
    
    private final LobbyManager lobbyManager;
    
    public LobbyGUI(LobbyManager lobbyManager) {
        this.lobbyManager = lobbyManager;
    }
    
    /**
     * Abre o menu de seleção de eras
     */
    public void abrirMenuSelecaoEras(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, "§6Seleção de Eras");
        
        // Configurar itens das eras
        configurarItensEras(gui, player);
        
        // Configurar itens de navegação
        configurarItensNavegacao(gui);
        
        player.openInventory(gui);
    }
    
    /**
     * Abre o menu de informações do projeto
     */
    public void abrirMenuInformacoes(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, "§eInformações do Projeto");
        
        // Item de informações gerais
        ItemStack info = new ItemStack(Material.BOOK);
        ItemMeta metaInfo = info.getItemMeta();
        metaInfo.setDisplayName("§6Arte da Guerra");
        metaInfo.setLore(Arrays.asList(
            "§7Plugin de evolução da humanidade",
            "§7através de 7 eras históricas",
            "",
            "§eCaracterísticas:",
            "§7• Sistema de lobby temático",
            "§7• 7 eras com progressão",
            "§7• Comandos interativos",
            "§7• Interface gráfica completa",
            "",
            "§7Versão: 1.0.0-SNAPSHOT",
            "§7Autor: Arte da Guerra Team"
        ));
        info.setItemMeta(metaInfo);
        
        gui.setItem(13, info);
        
        // Item de voltar
        ItemStack voltar = new ItemStack(Material.ARROW);
        ItemMeta metaVoltar = voltar.getItemMeta();
        metaVoltar.setDisplayName("§cVoltar ao Menu Principal");
        voltar.setItemMeta(metaVoltar);
        
        gui.setItem(22, voltar);
        
        player.openInventory(gui);
    }
    
    /**
     * Abre o menu de configurações
     */
    public void abrirMenuConfiguracoes(Player player) {
        if (!player.hasPermission("artedaguerra.admin")) {
            player.sendMessage("§cVocê não tem permissão para acessar as configurações!");
            return;
        }
        
        Inventory gui = Bukkit.createInventory(null, 27, "§7Configurações");
        
        // Itens de configuração aqui...
        
        player.openInventory(gui);
    }
    
    /**
     * Configura os itens das eras no menu
     */
    private void configurarItensEras(Inventory gui, Player player) {
        int[] slots = {10, 11, 12, 13, 14, 15, 16}; // Slots para as 7 eras
        
        for (int era = 1; era <= 7; era++) {
            ItemStack item = criarItemEra(era, player);
            gui.setItem(slots[era - 1], item);
        }
    }
    
    /**
     * Configura os itens de navegação no menu
     */
    private void configurarItensNavegacao(Inventory gui) {
        // Item de voltar ao lobby
        ItemStack lobby = new ItemStack(Material.EMERALD);
        ItemMeta metaLobby = lobby.getItemMeta();
        metaLobby.setDisplayName("§aVoltar ao Lobby");
        metaLobby.setLore(Arrays.asList(
            "§7Clique para voltar ao",
            "§7spawn do lobby"
        ));
        lobby.setItemMeta(metaLobby);
        
        // Item de informações
        ItemStack info = new ItemStack(Material.PAPER);
        ItemMeta metaInfo = info.getItemMeta();
        metaInfo.setDisplayName("§eInformações do Projeto");
        metaInfo.setLore(Arrays.asList(
            "§7Clique para ver mais",
            "§7informações sobre o projeto"
        ));
        info.setItemMeta(metaInfo);
        
        // Item de fechar
        ItemStack fechar = new ItemStack(Material.BARRIER);
        ItemMeta metaFechar = fechar.getItemMeta();
        metaFechar.setDisplayName("§cFechar Menu");
        fechar.setItemMeta(metaFechar);
        
        gui.setItem(18, lobby);
        gui.setItem(22, info);
        gui.setItem(26, fechar);
    }
    
    /**
     * Cria um item para uma era específica
     */
    private ItemStack criarItemEra(int era, Player player) {
        Material material = obterMaterialEra(era);
        String nome = lobbyManager.obterNomeEra(era);
        String periodo = lobbyManager.obterPeriodoEra(era);
        boolean temAcesso = lobbyManager.verificarAcessoEra(player, era);
        
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName((temAcesso ? "§a" : "§c") + "Era " + era + ": " + nome);
        
        List<String> lore = Arrays.asList(
            "§7Período: " + periodo,
            "",
            temAcesso ? "§aClique para viajar!" : "§cEra bloqueada",
            temAcesso ? "" : "§7Você não tem acesso a esta era"
        );
        
        meta.setLore(lore);
        item.setItemMeta(meta);
        
        return item;
    }
    
    /**
     * Obtém o material visual para uma era
     */
    private Material obterMaterialEra(int era) {
        switch (era) {
            case 1: return Material.WOODEN_PICKAXE; // Primitiva
            case 2: return Material.STONE_PICKAXE; // Pós-Primitiva
            case 3: return Material.IRON_SWORD; // Medieval
            case 4: return Material.IRON_INGOT; // Industrial
            case 5: return Material.DIAMOND; // Moderna
            case 6: return Material.REDSTONE_BLOCK; // Contemporânea
            case 7: return Material.NETHER_STAR; // Futurista
            default: return Material.STONE;
        }
    }
    
    /**
     * Verifica se o jogador tem acesso a uma era
     */
    private boolean verificarAcessoEra(Player player, int era) {
        return lobbyManager.verificarAcessoEra(player, era);
    }
    
    /**
     * Gerencia cliques nos menus
     */
    public void gerenciarClique(Player player, InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        
        if (item == null || !item.hasItemMeta()) {
            return;
        }
        
        String displayName = item.getItemMeta().getDisplayName();
        String title = event.getView().getTitle();
        
        if (title.equals("§6Seleção de Eras")) {
            gerenciarCliqueSelecaoEras(player, displayName, item);
        } else if (title.equals("§eInformações do Projeto")) {
            gerenciarCliqueInformacoes(player, displayName);
        }
    }
    
    /**
     * Gerencia cliques no menu de seleção de eras
     */
    private void gerenciarCliqueSelecaoEras(Player player, String displayName, ItemStack item) {
        // Verificar cliques em eras
        for (int era = 1; era <= 7; era++) {
            if (displayName.contains("Era " + era)) {
                if (verificarAcessoEra(player, era)) {
                    player.closeInventory();
                    lobbyManager.teletransportarParaEra(player, era);
                } else {
                    player.sendMessage("§cVocê não tem acesso a esta era!");
                }
                return;
            }
        }
        
        // Verificar outros cliques
        if (displayName.equals("§aVoltar ao Lobby")) {
            player.closeInventory();
            lobbyManager.teletransportarParaLobby(player);
            
        } else if (displayName.equals("§eInformações do Projeto")) {
            abrirMenuInformacoes(player);
            
        } else if (displayName.equals("§cFechar Menu")) {
            player.closeInventory();
        }
    }
    
    /**
     * Gerencia cliques no menu de informações
     */
    private void gerenciarCliqueInformacoes(Player player, String displayName) {
        if (displayName.equals("§cVoltar ao Menu Principal")) {
            abrirMenuSelecaoEras(player);
        }
    }
}