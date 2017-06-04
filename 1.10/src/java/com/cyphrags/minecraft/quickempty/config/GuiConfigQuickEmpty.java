package com.cyphrags.minecraft.quickempty.config;

import com.cyphrags.minecraft.quickempty.QuickEmptyMod;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;

/**
 * Created by Cyphrags at 13:21 on 02.06.2017.
 */
public class GuiConfigQuickEmpty extends GuiConfig
{
	public GuiConfigQuickEmpty(GuiScreen parentScreen)
	{
		super(parentScreen,
				new ConfigElement(QuickEmptyMod.configuration.getCategory(Configuration.CATEGORY_CLIENT)).getChildElements(),
				QuickEmptyMod.MODID, false, false, "QuickEmpty to the rescue!", QuickEmptyMod.configurationFile.getAbsolutePath());
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
	}
	
	@Override
	public void confirmClicked(boolean result, int id)
	{
		super.confirmClicked(result, id);
		QuickEmptyMod.syncConfig();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void actionPerformed(GuiButton button)
	{
		super.actionPerformed(button);
	}
}
