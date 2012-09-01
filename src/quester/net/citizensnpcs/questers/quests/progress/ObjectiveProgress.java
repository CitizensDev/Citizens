package net.citizensnpcs.questers.quests.progress;

import net.citizensnpcs.questers.QuestCancelException;
import net.citizensnpcs.questers.api.QuestAPI;
import net.citizensnpcs.questers.quests.Objective;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public class ObjectiveProgress {
    private int amountCompleted = 0;
    private ItemStack lastItem;
    private Location lastLocation;
    private final Objective objective;
    private final Player player;
    private final String questName;
    private final QuestUpdater questUpdater;
    private final int UID;

    public ObjectiveProgress(int UID, Player player, String questName, Objective objective) {
        this.UID = UID;
        this.player = player;
        this.questName = questName;
        this.objective = objective;
        this.questUpdater = QuestAPI.getObjective(objective.getType());
    }

    public void addAmount(int i) {
        if (this.getObjective().getAmount() - this.getAmount() > 0)
            this.setAmountCompleted(this.getAmount() + 1);
        else
            this.amountCompleted = this.objective.getAmount();
    }

    public int getAmount() {
        return amountCompleted;
    }

    public Class<? extends Event>[] getEventTypes() {
        return this.questUpdater.getEventTypes();
    }

    public ItemStack getLastItem() {
        return lastItem;
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    public Objective getObjective() {
        return this.objective;
    }

    public Player getPlayer() {
        return player;
    }

    public int getQuesterUID() {
        return UID;
    }

    public String getQuestName() {
        return questName;
    }

    public QuestUpdater getQuestUpdater() {
        return questUpdater;
    }

    public String getStatusText(boolean override) throws QuestCancelException {
        if (override && objective.getStatusText().isEmpty())
            return "";
        return objective.getStatusText().isEmpty() ? questUpdater.getStatus(this) : objective.getStatusText();
    }

    public void setAmountCompleted(int amountCompleted) {
        this.amountCompleted = amountCompleted;
    }

    public void setLastItem(ItemStack lastItem) {
        this.lastItem = lastItem;
    }

    public void setLastLocation(Location lastLocation) {
        this.lastLocation = lastLocation;
    }

    public boolean update(Event event) {
        if (getPlayer() == null)
            return false;
        return getQuestUpdater().update(event, this);
    }
}