package net.citizensnpcs.questers.quests.progress;

import net.citizensnpcs.questers.QuestCancelException;
import net.citizensnpcs.questers.api.QuestAPI;
import net.citizensnpcs.questers.quests.Objective;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public class ObjectiveProgress {
    private final int UID;
    private final Objective objective;
    private final Player player;
    private final QuestUpdater questUpdater;
    private final String questName;
    private int amountCompleted = 0;
    private ItemStack lastItem;
    private Location lastLocation;

    public ObjectiveProgress(int UID, Player player, String questName, Objective objective) {
        this.UID = UID;
        this.player = player;
        this.questName = questName;
        this.objective = objective;
        this.questUpdater = QuestAPI.getObjective(objective.getType());
    }

    public boolean update(Event event) {
        return getQuestUpdater().update(event, this);
    }

    public Class<? extends Event>[] getEventTypes() {
        return this.questUpdater.getEventTypes();
    }

    public int getQuesterUID() {
        return UID;
    }

    public Player getPlayer() {
        return player;
    }

    public Objective getObjective() {
        return this.objective;
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

    public void setAmountCompleted(int amountCompleted) {
        this.amountCompleted = amountCompleted;
    }

    public void setLastItem(ItemStack lastItem) {
        this.lastItem = lastItem;
    }

    public ItemStack getLastItem() {
        return lastItem;
    }

    public void setLastLocation(Location lastLocation) {
        this.lastLocation = lastLocation;
    }

    public Location getLastLocation() {
        return lastLocation;
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
}