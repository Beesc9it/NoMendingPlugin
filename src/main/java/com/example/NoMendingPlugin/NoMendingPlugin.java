package com.example.NoMendingPlugin;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class NoMendingPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (itemInHand.getType() == Material.ENCHANTED_BOOK) {
            // Если игрок держит книгу починки, удаляем ее
            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
            player.sendMessage(ChatColor.RED + "oops - repair");
        } else if (itemInHand.containsEnchantment(Enchantment.MENDING)) {
            // Если предмет имеет зачарование починки, удаляем это зачарование
            itemInHand.removeEnchantment(Enchantment.MENDING);
            player.sendMessage(ChatColor.RED + "oops - repair");
        }
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        checkArmorForMending(event.getPlayer());
        checkHeldItemForMending(event.getPlayer());
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        checkArmorForMending(event.getPlayer());
        checkHeldItemForMending(event.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        checkArmorForMending(event.getPlayer());
        checkHeldItemForMending(event.getPlayer());
    }

    @EventHandler
    public void onEntityDropItem(EntityDropItemEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            checkHeldItemForMending(player);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            checkHeldItemForMending(player);
            checkInventoryForMending(player.getInventory());
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            checkInventoryForMending(player.getInventory());
        }
    }

    private void checkArmorForMending(Player player) {
        EntityEquipment equipment = player.getEquipment();

        if (equipment != null) {
            // Проверяем каждый слот брони
            for (ItemStack armor : equipment.getArmorContents()) {
                if (armor != null && armor.containsEnchantment(Enchantment.MENDING)) {
                    // Если броня содержит зачарование починки, удаляем его
                    armor.removeEnchantment(Enchantment.MENDING);
                    player.sendMessage(ChatColor.RED + "oops - repair");
                }
            }

            // Проверяем слот для элитры
            ItemStack elytra = equipment.getChestplate();
            if (elytra != null && elytra.getType() == Material.ELYTRA
                    && elytra.containsEnchantment(Enchantment.MENDING)) {
                // Если элитра содержит зачарование починки, удаляем его
                elytra.removeEnchantment(Enchantment.MENDING);
                player.sendMessage(ChatColor.RED + "oops - repair");
            }
        }
    }

    private void checkHeldItemForMending(Player player) {
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (itemInHand.containsEnchantment(Enchantment.MENDING)) {
            // Если предмет в руке имеет зачарование починки, удаляем его
            itemInHand.removeEnchantment(Enchantment.MENDING);
            player.sendMessage(ChatColor.RED + "oops - repair");
        }
    }

    private void checkInventoryForMending(Inventory inventory) {
        // Проверяем каждый предмет в инвентаре
        for (ItemStack item : inventory.getContents()) {
            if (item != null && item.containsEnchantment(Enchantment.MENDING)) {
                // Если предмет в инвентаре имеет зачарование починки, удаляем его
                item.removeEnchantment(Enchantment.MENDING);
            }
        }
    }
}
