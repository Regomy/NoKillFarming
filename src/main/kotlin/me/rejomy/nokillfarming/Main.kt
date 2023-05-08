package me.rejomy.nokillfarming

import org.bukkit.Bukkit
import org.bukkit.Statistic
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin(), Listener {

    val kills = HashMap<Player, HashMap<Player, Int>>()
    val time = HashMap<Player, HashMap<Player, Long>>()

    override fun onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this)
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    fun onDamage(event: EntityDamageByEntityEvent) {
        val killer = event.damager
        val target = event.entity
        if (killer !is Player || target !is Player || event.finalDamage < target.health) return

        if (killer.address.address == target.address.address
            || kills.containsKey(killer) && kills[killer]!!.containsKey(target) && kills[killer]!![target]!! > 20
            || time.containsKey(killer) && time[killer]!!.containsKey(target) && time[killer]!![target]!! / 1000 > 10
        ) {
            killer.setStatistic(Statistic.PLAYER_KILLS, killer.getStatistic(Statistic.PLAYER_KILLS) - 1)
        }

        val kill = if(kills.containsKey(killer)) kills[killer]!! else HashMap()
        kill[target] = if(kill.containsKey(target)) kill[target]!! + 1 else 1
        kills[killer] = kill
        val tim = if(time.containsKey(killer)) time[killer]!! else HashMap()
        tim[target] = System.currentTimeMillis()
        time[killer] = tim
    }

}