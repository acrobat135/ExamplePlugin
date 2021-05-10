package example;

import arc.util.*;
import arc.*;
import mindustry.core.*;
import mindustry.game.*;
import mindustry.mod.*;
import mindustry.gen.*;
import mindustry.Vars;

public class ExamplePlugin extends Plugin{

    //called when game initializes
    @Override
    public void init() {

        Events.on(EventType.PlayerChatEvent.class, event -> {
            if (!event.message.startsWith("/")) {
                String prefix = event.player.admin() ? "[scarlet]АДМИН[] | " : "[cyan]ИГРОК[] |";
                String playerName = NetClient.colorizeName(event.player.id, event.player.name);
                Call.sendMessage(prefix + playerName + event.message);
            }
        });

        Vars.netServer.admins.addChatFilter((player, text) -> null);
    }

    //register commands that player can invoke in-game
    @Override
    public void registerClientCommands(CommandHandler handler){
        handler.removeCommand("a");
        handler.removeCommand("t");
        handler.<Player>register("a", "<текст...>", "Отправить сообщение от имени админа", (args, player) -> {
            if (!player.admin()) {
                player.sendMessage("[scarlet]Сначала купи админку))0)!");
                return;
            }

            String message = args[0];
            String prefix = "[scarlet]АДМИН[] | ";
            String playerName = NetClient.colorizeName(player.id, player.name);

            Groups.player.each(Player::admin, otherPlayer -> {
                otherPlayer.sendMessage("<[scarlet]A[]>" + prefix + playerName + message);
            });
        });
        handler.<Player>register("t", "<текст...>", "Отправить командное сообщение", (args, player) -> {
            String message = args[0];
            String playerName = NetClient.colorizeName(player.id, player.name);
            String prefix = player.admin() ? "[scarlet]АДМИН[] | " : "[cyan]ИГРОК[] | ";

            Groups.player.each(o -> o.team() == player.team(), otherPlayer -> {
                otherPlayer.sendMessage("<[#" + player.team().color + "]T[]>" + prefix + playerName + message);
            });
        });
    }
}
