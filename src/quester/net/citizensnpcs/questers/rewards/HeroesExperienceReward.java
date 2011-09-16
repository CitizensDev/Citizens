package net.citizensnpcs.questers.rewards;

import net.citizensnpcs.properties.Storage;
import net.citizensnpcs.questers.Reward;
import net.citizensnpcs.questers.RewardBuilder;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.herocraftonline.dev.heroes.Heroes;
import com.herocraftonline.dev.heroes.classes.HeroClass.ExperienceType;
import com.herocraftonline.dev.heroes.persistence.Hero;

public class HeroesExperienceReward implements Reward {
	private final double reward;
	private final boolean take;
	private static final Heroes plugin;

	public HeroesExperienceReward(double reward, boolean take, Heroes plugin) {
		this.reward = reward;
		this.take = take;
		
		if (plugin == null)
			HeroesExperienceReward.plugin = plugin;
	}

	@Override
	public void grant(Player player, HumanNPC npc) {
		if (plugin != null) {
			Hero hero = plugin.getHeroManager().getHero(player);
			
			if (this.take) {
				hero.gainExp(-reward, ExperienceType.ADMIN, false);
			} else {
				hero.gainExp(reward, ExperienceType.ADMIN, false);
			}
		}
	}

	@Override
	public boolean isTake() {
		return take;
	}

	@Override
	public boolean canTake(Player player) {
		Hero hero = plugin.getHeroManager().getHero(player);
		
		return hero.getExperience() >= reward;
	}

	@Override
	public String getRequiredText(Player player) {
		Hero hero = plugin.getHeroManager().getHero(player);
		
		return ChatColor.GRAY + "You need "
				+ StringUtils.wrap(hero.getExperience() - reward, ChatColor.GRAY)
				+ " more experience.";
	}

	@Override
	public void save(Storage storage, String root) {
		storage.setInt(root + ".amount", reward);
	}

	public static class HeroesExperienceRewardBuilder implements RewardBuilder {
		@Override
		public Reward build(Storage storage, String root, boolean take) {
			return new HeroesExperienceReward(storage.getInt(root + ".amount"), take);
		}
	}
}