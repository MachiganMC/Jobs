/**
 * Jobs Plugin for Bukkit
 * Copyright (C) 2011 Zak Ford <zak.j.ford@gmail.com>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.gamingmesh.jobs.container;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.Zrips.CMILib.Colors.CMIChatColor;
import net.Zrips.CMILib.Container.CMINumber;
import net.Zrips.CMILib.Items.CMIItemStack;
import net.Zrips.CMILib.Items.CMIMaterial;

public class JobLimitedItems {
    private String node;
    CMIMaterial mat;
    private String name;
    private ItemStack item;
    private List<String> lore;
    private Map<Enchantment, Integer> enchants;
    private int level;

    public JobLimitedItems(String node, CMIMaterial material, int amount, String name, List<String> lore, Map<Enchantment, Integer> enchants, int level) {
        this.node = node;

        CMIItemStack ct = material.newCMIItemStack(CMINumber.clamp(amount, 1, material.getMaterial().getMaxStackSize()));
        ct.setDisplayName(name);
        ct.setLore(lore);
        for (Entry<Enchantment, Integer> one : enchants.entrySet()) {
            ct.addEnchant(one.getKey(), one.getValue());
        }
        this.item = ct.getItemStack();

        this.name = name;
        this.lore = lore;
        this.enchants = enchants;
        this.level = level;
        this.mat = material;
    }

    public JobLimitedItems(String node, ItemStack item, int level) {
        this.node = node;
        this.item = item;
        if (this.item.hasItemMeta()) {
            ItemMeta meta = this.item.getItemMeta();
            if (meta.hasDisplayName())
                name = meta.getDisplayName();
            if (meta.hasLore())
                lore = meta.getLore();
        }
        enchants = item.getEnchantments();

        this.level = level;
    }

    public String getNode() {
        return node;
    }

    public ItemStack getItemStack(Player player) {

        ItemStack item = this.item.clone();

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }

        if (lore != null && !lore.isEmpty()) {
            List<String> translatedLore = new ArrayList<>();
            for (String oneLore : lore) {
                translatedLore.add(CMIChatColor.translate(oneLore.replace("[player]", player.getName())));
            }

            meta.setLore(translatedLore);
        }

        item.setItemMeta(meta);
        return item;
    }

    @Deprecated
    public int getId() {
        return mat.getId();
    }

    public CMIMaterial getType() {
        return mat;
    }

    public String getName() {
        return name;
    }

    public List<String> getLore() {
        return lore;
    }

    public Map<Enchantment, Integer> getEnchants() {
        return enchants;
    }

    public int getLevel() {
        return level;
    }
}
