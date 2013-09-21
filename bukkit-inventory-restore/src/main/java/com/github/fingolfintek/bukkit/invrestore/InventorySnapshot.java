package com.github.fingolfintek.bukkit.invrestore;

import com.github.fingolfintek.bukkit.util.KryoSerializationUtil;
import com.github.fingolfintek.bukkit.util.PlayerInventoryUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Entity
@Table(name = "invrestore_inv_snapshot")
public class InventorySnapshot {

    private final Logger logger = Logger.getLogger(getClass().getName());

    @Id
    private long id;

    @Column(nullable = false)
    private String playerName;

    @Column(nullable = false, length = 10240)
    private byte[] serializedInventory;

    @Column(nullable = false)
    private Date snapshotDate;

    public InventorySnapshot() {
    }

    public InventorySnapshot(Player player) {
        this(player.getName(), player.getInventory(), new Date());
    }

    public InventorySnapshot(String playerName, PlayerInventory inventory, Date snapshotDate) {
        this.playerName = playerName;
        this.serializedInventory = KryoSerializationUtil.serialize(PlayerInventoryUtil.getItemStacks(inventory));
        this.snapshotDate = snapshotDate;
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

    public List<ItemStack[]> getInventory() {
        return KryoSerializationUtil.deserialize(serializedInventory);
    }

    public byte[] getSerializedInventory() {
        return serializedInventory;
    }

    public void setSerializedInventory(byte[] serializedInventory) {
        this.serializedInventory = serializedInventory;
    }

    public Date getSnapshotDate() {
        return snapshotDate;
    }

    public void setSnapshotDate(Date snapshotDate) {
        this.snapshotDate = snapshotDate;
    }


    @Override
    public String toString() {
        return "InventorySnapshot{" +
                "id=" + id +
                ", playerName='" + playerName + '\'' +
                ", serializedInventory=" + serializedInventory.length +
                ", snapshotDate=" + snapshotDate +
                '}';
    }

}
