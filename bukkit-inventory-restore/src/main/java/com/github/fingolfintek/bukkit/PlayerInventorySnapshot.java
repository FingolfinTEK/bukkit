package com.github.fingolfintek.bukkit;

import com.avaje.ebean.validation.NotNull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "invrestore_inv_snapshot")
public class PlayerInventorySnapshot {
    @Id
    private long id;

    @NotNull
    private String playerName;

    @NotNull
    private byte[] serializedInventory;

    @NotNull
    private Date date;

    public PlayerInventorySnapshot(Player player) {
        this(player.getName(), player.getInventory(), new Date());
    }

    public PlayerInventorySnapshot(String playerName, PlayerInventory inventory, Date date) {
        this.playerName = playerName;
        this.serializedInventory = KryoSerializationUtil.serialize(inventory);
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public PlayerInventory getInventory() {
        return KryoSerializationUtil.deserialize(serializedInventory);
    }

    public byte[] getSerializedInventory() {
        return serializedInventory;
    }

    public void setSerializedInventory(byte[] serializedInventory) {
        this.serializedInventory = serializedInventory;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
