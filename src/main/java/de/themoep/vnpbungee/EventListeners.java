package de.themoep.vnpbungee;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * VNPBungee - Bungee bridge for VanishNoPacket
 * Copyright (C) 2015 Max Lee (https://github.com/Phoenix616/)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
public class EventListeners implements Listener {
    
    @EventHandler
    public void onPluginMessageReceive(PluginMessageEvent event) {
        VNPBungee.getInstance().getLogger().info("Received new plugin message with tag " + event.getTag() + " instanceof ProxiedPlayer: " + (event.getReceiver() instanceof ProxiedPlayer));
        if(event.getReceiver() instanceof ProxiedPlayer && event.getTag().equals("vanishStatus")) {
            ProxiedPlayer player = (ProxiedPlayer) event.getReceiver();
            ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
            byte status = in.readByte();
            ProxyServer.getInstance().getPluginManager().callEvent(new VanishStatusChangeEvent(player, status == 1));
            VNPBungee.getInstance().getLogger().info(player.getName() + " received new plugin message with data " + status);
        }
    }

    @EventHandler
    public void onStatusChange(VanishStatusChangeEvent event) {
        VNPBungee.getInstance().setVanished(event.getPlayer(), event.isVanishing());
        VNPBungee.getInstance().getLogger().info(event.getPlayer().getName() + " " + (event.isVanishing() ? "" : "un") + "vanished!");
    }

    @EventHandler
    public void onPlayerLeave(PlayerDisconnectEvent event) {
        VNPBungee.getInstance().clearStatusData(event.getPlayer());
    }
}
