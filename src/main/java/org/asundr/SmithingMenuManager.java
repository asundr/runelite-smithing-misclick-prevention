/*
 * Copyright (c) 2025, Arun <smithing-misclick-prevention-plugin.octad@dralias.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.asundr;

import net.runelite.api.gameval.AnimationID;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.MenuEntry;
import net.runelite.api.Player;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.MenuShouldLeftClick;
import net.runelite.api.events.WidgetClosed;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.Notifier;
import net.runelite.client.ui.overlay.OverlayManager;

import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmithingMenuManager
{
    enum PrefixedItems
    {
        TWO_H_SWORD("2h sword", config::twoHSword),
        ARROWTIPS("arrowtips", config::arrowtips),
        AXE("axe", config::axe),
        BATTLEAXE("battleaxe", config::battleaxe),
        CHAINBODY("chainbody", config::chainbody),
        CLAWS("claws", config::claws),
        DAGGER("dagger", config::dagger),
        DART_TIP("dart tip", config::darttip),
        FULL_HELM("full helm", config::fullhelm),
        GRAPPLE_TIP("grapple tip", config::grappletip),
        KEEL_PARTS("keel parts", config::keelparts),
        KITESHIELD("kiteshield", config::kiteshield),
        KNIFE("knife", config::kiteshield),
        LONGSWORD("longsword", config::longsword),
        LIMBS("limbs", config::limbs),
        MACE("mace", config::mace),
        MED_HELM("med helm", config::medhelm),
        NAILS("nails", config::nails),
        PLATELEGS("platelegs", config::platelegs),
        PLATESKIRT("plateskirt", config::plateskirt),
        PLATEBODY("platebody", config::platebody),
        SCIMITAR("scimitar", config::scimitar),
        SQ_SHIELD("sq shield", config::sqshield),
        SPIT("spit", config::spit),
        STUDS("studs", config::studs),
        SWORD("sword", config::sword),
        UNFINISHED_BOLTS("unfinished bolts", config::unfinishedbolts),
        WARHAMMER("warhammer", config::warhammer),
        WIRE("wire", config::wire),

        ;
        public final String text;
        public final Supplier<Boolean> query;
        PrefixedItems(String text, Supplier<Boolean> query) { this.text = text; this.query = query; }
    }

    enum UniqueItems
    {
        BULLSEYE_LANTERN("Bullseye lantern (unf)", config::bullseyeLantern),
        OIL_LANTERN("Oil lantern frame", config::oilLantern);
        public final String text;
        public final Supplier<Boolean> query;
        UniqueItems(String text, Supplier<Boolean> query) { this.text = text; this.query = query; }
    }

    private static final int SMITHING_TIMEOUT_MS = 4500;
    private static final int SMITHING_MENU_ID = 312;
    private static final String TEMPLATE_NOTIFY_WARNING = "Misclick on: %s!";
    private static final String OPTION_SMITH = "Smith";
    private static final String OPTION_SMITH_SET = "Smith set";
    private static final ArrayList<String> MATERIALS = new ArrayList<>(Arrays.asList("Bronze", "Iron", "Steel", "Gold", "Mithril", "Adamant(?:ite)?", "Rune"));
    private static final Pattern PREFIXED_PATTERN = constructPattern();


    private static SmithingMisclickPreventionConfig config;
    private static Client client;
    private static Notifier notifier;

    private final WarningOverlay warningOverlay;

    private boolean forceRightClick = false;
    private boolean smithingMenuOpen = false;
    private boolean currentlySmithing = false;
    private Instant lastSmithTime = null;
    private final HashSet<String> enabledUniques = new HashSet<>(), enabledPrefixed = new HashSet<>();


    SmithingMenuManager(Client client, SmithingMisclickPreventionConfig config, Notifier notifier, EventBus eventBus, OverlayManager overlayManager)
    {
        SmithingMenuManager.client = client;
        SmithingMenuManager.config = config;
        SmithingMenuManager.notifier = notifier;

        warningOverlay = new WarningOverlay(config, client);
        eventBus.register(warningOverlay);
        eventBus.register(this);
        overlayManager.add(warningOverlay);
        updateEnabled();
    }

    void shutdown(EventBus eventBus, OverlayManager overlayManager)
    {
        overlayManager.remove(warningOverlay);
        eventBus.unregister(warningOverlay);
        eventBus.unregister(this);
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged evt)
    {
        updateEnabled();
    }

    @Subscribe
    public void onMenuEntryAdded(MenuEntryAdded evt)
    {
        if (!smithingMenuOpen)
        {
            return;
        }
        if (isOptionEnabled(evt.getOption()) && isTargetEnabled(evt.getTarget()))
        {
            forceRightClick = true;
        }
    }

    @Subscribe
    public void onMenuShouldLeftClick(MenuShouldLeftClick evt)
    {
        if (!smithingMenuOpen || !forceRightClick)
        {
            return;
        }
        forceRightClick = false;
        MenuEntry[] menuEntries = client.getMenuEntries();
        for (final MenuEntry entry : menuEntries)
        {
            if (isOptionEnabled(entry.getOption()) && isTargetEnabled(entry.getTarget()))
            {
                evt.setForceRightClick(true);
                return;
            }
        }
    }

    @Subscribe
    private void onWidgetLoaded(WidgetLoaded evt)
    {
        if (evt.getGroupId() == SMITHING_MENU_ID)
        {
            smithingMenuOpen = true;
            setCurrentlySmithing(false);
        }
    }

    @Subscribe
    private void onWidgetClosed(WidgetClosed evt)
    {
        if (evt.getGroupId() == SMITHING_MENU_ID)
        {
            smithingMenuOpen = false;
        }
    }

    @Subscribe
    private void onMenuOptionClicked(MenuOptionClicked event)
    {
        if (!config.notifyMisclick() || !isOptionEnabled(event.getMenuOption()) || !isTargetEnabled(event.getMenuTarget()))
        {
            return;
        }
        final String targetName = CommonUtils.sanitizeTradeMenuTarget(event.getMenuTarget());
        notifier.notify(String.format(TEMPLATE_NOTIFY_WARNING, targetName), TrayIcon.MessageType.WARNING);
        warningOverlay.setWarningName(targetName);
    }

    @Subscribe
    private void onGameTick(GameTick event)
    {
        if (lastSmithTime != null && currentlySmithing && (Instant.now().toEpochMilli() - lastSmithTime.toEpochMilli() > SMITHING_TIMEOUT_MS))
        {
            setCurrentlySmithing(false);
        }
    }

    @Subscribe
    private void onAnimationChanged(AnimationChanged event)
    {
        final Player player = client.getLocalPlayer();
        if (player != event.getActor())
        {
            return;
        }
        switch(player.getAnimation())
        {
            case AnimationID.HUMAN_SMITHING:
            case AnimationID.HUMAN_SMITHING_IMCANDO_HAMMER:
                setCurrentlySmithing(true);
                lastSmithTime = Instant.now();
                break;
            case -1:
                break;
            default:
                setCurrentlySmithing(false);
                return;
        }
    }
    @Subscribe
    public void onGameStateChanged(GameStateChanged event)
    {
        switch (event.getGameState())
        {
            case LOGIN_SCREEN: case HOPPING:
                setCurrentlySmithing(false);
                break;
        }
    }


    void setCurrentlySmithing(boolean isSmithing)
    {
        if (currentlySmithing == isSmithing)
        {
            return;
        }
        if (!isSmithing)
        {
            lastSmithTime = null;
            warningOverlay.clearWarningName();
        }
        currentlySmithing = isSmithing;
    }


    private static Pattern constructPattern()
    {
        if (MATERIALS.isEmpty())
        {
            return Pattern.compile("");
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("(?:").append(MATERIALS.get(0));
        for (int i=1; i <MATERIALS.size(); ++i)
        {
            sb.append("|" + MATERIALS.get(i));
        }
        sb.append(") (.*)$");
        return Pattern.compile(sb.toString());
    }

    private void updateEnabled()
    {
        enabledPrefixed.clear();
        for (final PrefixedItems item : PrefixedItems.values())
        {
            if (item.query.get())
            {
                enabledPrefixed.add(item.text);
            }
        }
        enabledUniques.clear();
        for (final UniqueItems item : UniqueItems.values())
        {
            if (item.query.get())
            {
                enabledUniques.add(item.text);
            }
        }
        for (final String item : config.customItems().split(","))
        {
            enabledUniques.add(item.trim());
        }
    }

    private boolean isOptionEnabled(final String option)
    {
        return option.equalsIgnoreCase(OPTION_SMITH) || option.equalsIgnoreCase(OPTION_SMITH_SET);
    }

    private boolean isTargetEnabled(final String target)
    {
        final String sanitized = CommonUtils.sanitizeTradeMenuTarget(target);
        final Matcher matcher = PREFIXED_PATTERN.matcher(sanitized);
        return matcher.find() && enabledPrefixed.contains(matcher.group(1)) || enabledUniques.contains(sanitized);
    }
}
