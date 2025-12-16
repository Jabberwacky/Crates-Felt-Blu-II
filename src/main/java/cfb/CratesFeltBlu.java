package cfb;

import java.util.ArrayList;
import java.util.List;

import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIISmallCrate;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.BlockIIMetalDevice;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.BlockIIMetalDevice.IIBlockTypes_MetalDevice;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.common.blocks.wooden.BlockWoodenDevice0;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = CratesFeltBlu.MODID, name = CratesFeltBlu.NAME, version = CratesFeltBlu.VERSION, dependencies = CratesFeltBlu.DEPENDENCIES)
public class CratesFeltBlu {

	public static final String MODID = "cfb";
	public static final String NAME = "Crates Felt Blu";
	public static final String VERSION = "1.0";
	public static final String DEPENDENCIES = "required-after:immersiveengineering;required-after:immersiveintelligence";

	@Mod.Instance
	public static CratesFeltBlu instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {}

	@SubscribeEvent
	public void crateHarvest(BlockEvent.HarvestDropsEvent event) {
		IBlockState state = event.getState();

		// Проверяем, является ли блок одним из нужных нам типов
		if (state.getBlock() instanceof BlockWoodenDevice0 ||
				state.getBlock() instanceof BlockIIMetalDevice ||
				state.getBlock() instanceof BlockIISmallCrate) {

			List<ItemStack> drops = event.getDrops(); // получаем список дропнутых предметов
			List<ItemStack> inventory = new ArrayList<>(); // создадим временный список для инвентаря

			// Перебираем каждый дропнутый предмет
			for (ItemStack stack : event.getDrops()) {

				// Проверяем, является ли дропнутая вещь блоком нужного типа и содержит ли метаданные
				if (stack.getItem() instanceof ItemBlock && (
						((ItemBlock) stack.getItem()).getBlock() instanceof BlockWoodenDevice0 ||
								((ItemBlock) stack.getItem()).getBlock() instanceof BlockIIMetalDevice ||
								((ItemBlock) stack.getItem()).getBlock() instanceof BlockIISmallCrate) &&
						stack.hasTagCompound() && stack.getTagCompound().hasKey("inventory", 9)) {

					// Читаем инвентарь из метаданных
					NBTTagList invTagList = stack.getTagCompound().getTagList("inventory", 10);
					inventory.addAll(Utils.readInventory(invTagList, 27)); // 27 слот — стандартное значение размера инвентаря

					// Очищаем метаданные блока
					stack.getTagCompound().removeTag("inventory");
					if (stack.getTagCompound().hasNoTags()) stack.setTagCompound(null);
				}
			}

			// Добавляем содержимое прочитанного инвентаря обратно в основной список дропа
			drops.addAll(inventory);
		}
	}
}