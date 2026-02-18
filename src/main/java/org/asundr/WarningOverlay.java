package org.asundr;


import net.runelite.client.eventbus.Subscribe;

import net.runelite.api.Client;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.ui.overlay.components.TitleComponent;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.util.ColorUtil;

import java.awt.*;
import java.time.Instant;

public class WarningOverlay extends OverlayPanel
{
    private static final int BG_ALPHA = 128;
    private static final int OFFSET_Y = 30;
    private static final String TEMPLATE_WARNING_MESSAGE = "WARNING: Smithing %s!";
    private static SmithingMisclickPreventionConfig config;
    private static Client client;
    private String warningName = null;


    WarningOverlay(SmithingMisclickPreventionConfig config, Client client)
    {
        WarningOverlay.config = config;
        WarningOverlay.client = client;

        setLayer(OverlayLayer.ALWAYS_ON_TOP);
        setBounds(new Rectangle(100,100));
    }


    @Subscribe
    private void onConfigChanged(ConfigChanged event)
    {
        if (event.getGroup().equals(SmithingMisclickPreventionConfig.GROUP_NAME))
        {
            switch (event.getKey())
            {
                case SmithingMisclickPreventionConfig.KEY_SHOW_MISCLICK_OVERLAY:
                    if (!config.showMisclickOverlay())
                    {
                        warningName = null;
                    }
                    break;
                case SmithingMisclickPreventionConfig.KEY_OVERLAY_BACKGROUND_COLOR:
                    setPreferredColor(ColorUtil.colorWithAlpha(config.misclickOverlayColorBackground(), BG_ALPHA));
                    break;
            }
        }
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (!config.showMisclickOverlay() || warningName == null)
        {
            return null;
        }
        final String lastMessage = String.format(TEMPLATE_WARNING_MESSAGE, warningName);
        panelComponent.getChildren().add(TitleComponent.builder()
                .text(lastMessage)
                .color(config.misclickOverlayColorText())
                .build());
        panelComponent.setPreferredSize(new Dimension(
                graphics.getFontMetrics().stringWidth(lastMessage) + 10,
                40));
        setPreferredLocation(new java.awt.Point(client.getCenterX() - getBounds().width/2, client.getCenterY() + OFFSET_Y));
        return super.render(graphics);
    }

    void setWarningName(final String warningName)
    {
        this.warningName = warningName;
        setPreferredColor(ColorUtil.colorWithAlpha(config.misclickOverlayColorBackground(), BG_ALPHA));
    }

    void clearWarningName()
    {
        this.warningName = null;
    }
}
