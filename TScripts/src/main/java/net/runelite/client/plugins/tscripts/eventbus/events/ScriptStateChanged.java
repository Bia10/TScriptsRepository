package net.runelite.client.plugins.tscripts.eventbus.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ScriptStateChanged
{
    private final String scriptName;
    private final Boolean running;
}
