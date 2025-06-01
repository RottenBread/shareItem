package main.rottenbread.shareim.shareItem

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class ShareItem : JavaPlugin(), Listener {

    override fun onEnable() {
        server.pluginManager.registerEvents(this, this)
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (command.name.equals("공개", ignoreCase = true)) {
            if (sender !is Player) {
                sender.sendMessage("이 명령어는 플레이어만 사용할 수 있습니다.")
                return true
            }

            val item: ItemStack = sender.inventory.itemInMainHand
            if (item.type == Material.AIR) {
                sender.sendMessage("${ChatColor.RED}아무 아이템도 들고 있지 않습니다.")
                return true
            }

            val message =
                "${ChatColor.AQUA}${sender.name}${ChatColor.YELLOW}님이 공유했습니다!"

            val clone = item.clone()
            clone.amount = 1

            var shareCount = 0

            for (player in Bukkit.getOnlinePlayers()) {
                if (player == sender) continue

                val leftover = player.inventory.addItem(clone).values
                if (leftover.isEmpty()) {
                    player.sendMessage("${sender.name}이 아이템을 공유했습니다!")
                    shareCount++
                } else {
                    player.sendMessage("${sender.name}이 아이템을 공유했지만 인벤토리가 가득 찼습니다!")
                }
            }

            sender.sendMessage("아이템을 ${shareCount}명에게 공유했습니다.")
            Bukkit.broadcastMessage(message)
            return true

        }
        return false
    }
}
