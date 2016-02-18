package uk.co.drnaylor.minecraft.quickstart.listeners;

import com.google.inject.Inject;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.option.OptionSubject;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;
import uk.co.drnaylor.minecraft.quickstart.Util;
import uk.co.drnaylor.minecraft.quickstart.api.PluginModule;
import uk.co.drnaylor.minecraft.quickstart.config.MainConfig;
import uk.co.drnaylor.minecraft.quickstart.internal.annotations.Modules;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

@Modules(PluginModule.CHAT)
public class ChatListener {
    private final Pattern p;

    private final Map<String, BiFunction<Player, Text, Text>> tokens;

    @Inject
    private MainConfig config;

    // Zero args for the injector
    public ChatListener() {
        tokens = createTokens();
        StringBuilder sb = new StringBuilder("(");
        tokens.forEach((k, v) -> sb.append(k.replaceAll("\\{", "\\\\{").replaceAll("\\}", "\\\\}")).append("|"));

        sb.deleteCharAt(sb.length() - 1).append(")");
        p = Pattern.compile(MessageFormat.format("(?<={0})|(?={0})", sb.toString()), Pattern.CASE_INSENSITIVE);
    }

    private Map<String, BiFunction<Player, Text, Text>> createTokens() {
        Map<String, BiFunction<Player, Text, Text>> t = new HashMap<>();

        t.put("{{name}}", (p, te) -> Text.of(p.getName()));
        t.put("{{prefix}}", (p, te) -> getTextFromOption(p, "prefix"));
        t.put("{{suffix}}", (p, te) -> getTextFromOption(p, "suffix"));
        t.put("{{displayname}}", (p, te) -> Util.getName(p));
        t.put("{{message}}", (p, te) -> te);
        return t;
    }

    @Listener(order = Order.LATE)
    public void onPlayerChat(MessageChannelEvent.Chat event, @First Player player) {
        if (!config.getModifyChat()) {
            return;
        }

        Text rawMessage = event.getRawMessage();

        // String -> Text parser. Should split on all {{}} tags, but keep the tags in. We can then use the target map
        // to do the replacements!
        String[] s = p.split(config.getChatTemplate());

        Text.Builder tb = Text.builder();

        for (String textElement : s) {
            if (p.matcher(textElement).matches()) {
                // If we have a token, do the replacement as specified by the function
                tb.append(tokens.get(textElement.toLowerCase()).apply(player, rawMessage));
            } else {
                // Just convert the colour codes, but that's it.
                tb.append(TextSerializers.formattingCode('&').deserialize(textElement));
            }
        }

        event.setMessage(tb.build());
    }

    private Text getTextFromOption(Player player, String option) {
        Optional<OptionSubject> oos = getSubject(player);
        if (!oos.isPresent()) {
            return Text.EMPTY;
        }

        return TextSerializers.formattingCode('&').deserialize(oos.get().getOption(option).orElse(""));
    }

    private Optional<OptionSubject> getSubject(Player player) {
        Subject subject = player.getContainingCollection().get(player.getIdentifier());
        return subject instanceof OptionSubject ? Optional.of((OptionSubject) subject) : Optional.empty();
    }
}
