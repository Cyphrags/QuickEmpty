# QuickEmpty
![QuickEmpty Logo](https://github.com/cyphrags/quickempty/Logo.png)

## About
QuickEmpty is a small mod that basically has just a single feature: empty out any inventory without opening it's GUI.

It was inspired by Factorio, because there it is just so much easier.


Let's imagine you just started your new World, did some strip-mining and now you've got 12 furnaces running.
To empty out those, you need to open the GUI and shift-left-click the ingots. But this is rather tedious, isn't it?
That's where QuickEmptys comes in: Instead of opening every single GUI, just hold down CTRL and left click the block.
And there you go, no need for opening GUIs!


## How to use
1. Have a block which contains items you want to take out
2. Have enough space in your inventory
3. Hold down CTRL while left-clicking the block
4. Your inventory will fill with the items from the block (if they fit)


## Configuration
By default, every time you take something out of an inventory using the quick empty method, a summary of what you've taken out will be displayed in your chat window. This can be turned off in either:
1. the file config/quickempty.cfg inside your minecraft folder
2. in the Main Menu, click on "Mods" > select "Quick Empty" > hit "Config"
3. in the in-game Menu (hit escape while playing), click on "Mod Options..." > select "Quick Empty" > hit "Config"


## For Devs
[Source Code found at GitHub](https://github.com/cyphrags/quickempty)

QuickEmpty basically checks for IInventory and ISidedInventory for both blocks and entities.
So if you don't want any special handling, you won't need to do anything.

For blocks which implement IInventory or ISidedInventory there are two ways to specify how the block should be handled:
1. Specify the index or indices where to pull items from with: `com.cyphrags.minecraft.quickempty.registry.HandlerRegistry.Blocks.putFacing(Block b, EnumFacing facing)`

2. Specify the face (side) to pull from with: `com.cyphrags.minecraft.quickempty.registry.HandlerRegistry.Blocks.putFacing(Block b, EnumFacing facing)`

  if your block is an **ISidedInventory** and you have the **getSlotForFace(EnumFacing facing)** method setup correctly

If this is not enough or your block/entity has his inventory "hidden" (like an ender chest):
1. Create a special handler which implements `com.cyphrags.miunecraft.quickempty.registry.HandlerRegistry.ISpecialHandler`

2. And link it to your block using `com.cyphrags.miunecraft.quickempty.registry.HandlerRegistry.Special.putBlock(Block b, ISpecialHandler iSH)`

  or to your entity using `com.cyphrags.miunecraft.quickempty.registry.HandlerRegistry.Special.putEntity(Class<? extends Entity> e, ISpecialHandler iSH)`

An example usage can be found inside **QuickEmptyMod.java** inside the function **preInit**:

[Example #1: Registering indices - QuickEmptyMod.java, line 59](https://github.com/Cyphrags/QuickEmpty/blob/master/1.10/src/java/com/cyphrags/minecraft/quickempty/QuickEmptyMod.java#L59)

[Example #2: Creating an ISpecialHandler - QuickEmptyMod.java, line 63](https://github.com/Cyphrags/QuickEmpty/blob/master/1.10/src/java/com/cyphrags/minecraft/quickempty/QuickEmptyMod.java#L63)

[Example #3: Registering the iSH for a Block - QuickEmptyMod.java, line 83](https://github.com/Cyphrags/QuickEmpty/blob/master/1.10/src/java/com/cyphrags/minecraft/quickempty/QuickEmptyMod.java#L83)
