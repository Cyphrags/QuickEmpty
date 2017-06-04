package com.cyphrags.minecraft.quickempty.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * Created by Cyphrags at 13:20 on 02.06.2017.
 */
public class GuiFactoryQuickEmpty implements IModGuiFactory
{
	@Override
	public void initialize(Minecraft minecraftInstance)
	{
	
	}
	
	/*
	@Override
	public boolean hasConfigGui()
	{
		return true;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen)
	{
		return new GuiConfigQuickEmpty(parentScreen);
	}
	*/
	
	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass()
	{
		return GuiConfigQuickEmpty.class;
	}
	
	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
	{
		return null;
	}
	
	@Nullable
	@Override
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element)
	{
		return null;
	}
}
