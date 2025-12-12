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

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("SmithingMisclickPrevention")
public interface SmithingMisclickPreventionConfig extends Config
{
	@ConfigSection( name = "Presets", description = "Enabling any of forces right-clicking", position = 0 )
	String SECTION_PRESETS = "presets";

	@ConfigSection( name = "Advanced", description = "Additional configuration", position = 1 )
	String SECTION_ADVANCED = "advanced";

	@ConfigItem( keyName = "twoHSword", name = "2h sword", description = "", section = SECTION_PRESETS)
	default boolean twoHSword() { return false; }

	@ConfigItem( keyName = "arrowtips", name = "Arrow tips", description = "", section = SECTION_PRESETS)
	default boolean arrowtips()
	{
		return false;
	}

	@ConfigItem( keyName = "axe", name = "Axe", description = "", section = SECTION_PRESETS)
	default boolean axe()
	{
		return false;
	}

	@ConfigItem( keyName = "battleaxe", name = "Battleaxe", description = "", section = SECTION_PRESETS)
	default boolean battleaxe() { return false; }

	@ConfigItem( keyName = "bullseyeLantern", name = "Bullseye lantern", description = "", section = SECTION_PRESETS)
	default boolean bullseyeLantern()
	{
		return false;
	}

	@ConfigItem( keyName = "chainbody", name = "Chainbody", description = "", section = SECTION_PRESETS)
	default boolean chainbody()
	{
		return false;
	}

	@ConfigItem( keyName = "claws", name = "Claws", description = "", section = SECTION_PRESETS)
	default boolean claws()
	{
		return false;
	}

	@ConfigItem( keyName = "dagger", name = "Dagger", description = "", section = SECTION_PRESETS)
	default boolean dagger()
	{
		return false;
	}

	@ConfigItem( keyName = "darttip", name = "Dart tip", description = "", section = SECTION_PRESETS)
	default boolean darttip()
	{
		return false;
	}

	@ConfigItem( keyName = "fullhelm", name = "Full helm", description = "", section = SECTION_PRESETS)
	default boolean fullhelm()
	{
		return false;
	}

	@ConfigItem( keyName = "grappletip", name = "Grapple tip", description = "", section = SECTION_PRESETS)
	default boolean grappletip()
	{
		return false;
	}

	@ConfigItem( keyName = "javelintip", name = "Javelin tip", description = "", section = SECTION_PRESETS)
	default boolean javelintip()
	{
		return false;
	}

	@ConfigItem( keyName = "keelparts", name = "Keel parts", description = "", section = SECTION_PRESETS)
	default boolean keelparts()
	{
		return false;
	}

	@ConfigItem( keyName = "kiteshield", name = "Kiteshield", description = "", section = SECTION_PRESETS)
	default boolean kiteshield() { return false; }

	@ConfigItem( keyName = "knife", name = "Throwing knife", description = "", section = SECTION_PRESETS)
	default boolean knife()
	{
		return false;
	}

	@ConfigItem( keyName = "longsword", name = "Longsword", description = "", section = SECTION_PRESETS)
	default boolean longsword()
	{
		return false;
	}

	@ConfigItem( keyName = "limbs", name = "Limbs", description = "", section = SECTION_PRESETS)
	default boolean limbs()
	{
		return false;
	}

	@ConfigItem( keyName = "mace", name = "Mace", description = "", section = SECTION_PRESETS)
	default boolean mace() { return false; }

	@ConfigItem( keyName = "medhelm", name = "Med helm", description = "", section = SECTION_PRESETS)
	default boolean medhelm() { return false; }

	@ConfigItem( keyName = "nails", name = "Nails", description = "", section = SECTION_PRESETS)
	default boolean nails() { return false; }

	@ConfigItem( keyName = "oilLantern", name = "Oil lantern frame", description = "", section = SECTION_PRESETS)
	default boolean oilLantern()
	{
		return false;
	}

	@ConfigItem( keyName = "platelegs", name = "Platelegs", description = "", section = SECTION_PRESETS)
	default boolean platelegs() { return false; }

	@ConfigItem( keyName = "plateskirt", name = "Plateskirt", description = "", section = SECTION_PRESETS)
	default boolean plateskirt() { return false; }

	@ConfigItem( keyName = "platebody", name = "Platebody", description = "", section = SECTION_PRESETS)
	default boolean platebody() { return false; }

	@ConfigItem( keyName = "scimitar", name = "Scimitar", description = "", section = SECTION_PRESETS)
	default boolean scimitar()
	{
		return false;
	}

	@ConfigItem( keyName = "sqshield", name = "Sq shield", description = "", section = SECTION_PRESETS)
	default boolean sqshield()
	{
		return false;
	}

	@ConfigItem( keyName = "spit", name = "Spit", description = "", section = SECTION_PRESETS)
	default boolean spit() { return false; }

	@ConfigItem( keyName = "studs", name = "Studs", description = "", section = SECTION_PRESETS)
	default boolean studs() { return false; }

	@ConfigItem( keyName = "sword", name = "Sword", description = "", section = SECTION_PRESETS)
	default boolean sword()
	{
		return false;
	}

	@ConfigItem( keyName = "unfinishedbolts", name = "Unfinished bolts", description = "", section = SECTION_PRESETS)
	default boolean unfinishedbolts()
	{
		return false;
	}

	@ConfigItem( keyName = "warhammer", name = "Warhammer", description = "", section = SECTION_PRESETS)
	default boolean warhammer()
	{
		return false;
	}

	@ConfigItem( keyName = "wire", name = "Wire", description = "", section = SECTION_PRESETS)
	default boolean wire()
	{
		return false;
	}

	@ConfigItem(keyName = "customItems", name = "Custom Items", description = "Comma-separated list of the names of items to set to right-click", position = 2000, section = SECTION_ADVANCED)
	default String customItems(){ return ""; }
}
