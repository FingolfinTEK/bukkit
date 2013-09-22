package com.github.fingolfintek.bukkit.invrestore.dao;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.ExpressionList;
import com.github.fingolfintek.bukkit.invrestore.InventorySnapshot;
import com.github.fingolfintek.bukkit.invrestore.RestoreTimeFrame;

import java.util.List;

public class InventorySnapshotDao {

    private final EbeanServer database;

    public InventorySnapshotDao(EbeanServer database) {
        this.database = database;
    }

    public InventorySnapshot findByPlayerNameAndTimeFrame(String playerName, RestoreTimeFrame timeFrame) {
        return findAllByPlayerNameAndTimeFrame(playerName, timeFrame).get(0);
    }

    public List<InventorySnapshot> findAllByPlayerNameAndTimeFrame(String playerName, RestoreTimeFrame timeFrame) {
        return toExpressionList(playerName, timeFrame).orderBy().asc("id").findList();
    }

    private ExpressionList<InventorySnapshot> toExpressionList(String playerName, RestoreTimeFrame timeFrame) {
        return database.find(InventorySnapshot.class)
                       .where()
                       .ieq("playerName", playerName)
                       .between("snapshotDate", timeFrame.getLowerBound(), timeFrame.getUpperBound());
    }

}
