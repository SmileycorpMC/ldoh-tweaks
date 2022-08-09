package net.smileycorp.ldoh.common.loot.function;

import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.smileycorp.atlas.api.recipe.WeightedOutputs;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class RandomFunction extends LootFunction {

	protected WeightedOutputs<List<LootFunction>> functions = new WeightedOutputs<List<LootFunction>>(
			Maps.newHashMap());

	protected RandomFunction(LootCondition[] conditionsIn) {
		super(conditionsIn);
	}

	@Override
	public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
		if (!functions.isEmpty()) {
			for (LootFunction function : functions.getResult(rand)) {
				function.apply(stack, rand, context);
			}
		}
		return stack;
	}

	public static class Serializer extends
	LootFunction.Serializer<RandomFunction> {
		public Serializer() {
			super(new ResourceLocation("random_function"), RandomFunction.class);
		}

		@Override
		public void serialize(JsonObject object, RandomFunction instance,
				JsonSerializationContext serializationContext) {
			JsonArray list = new JsonArray();
			for (Entry<List<LootFunction>, Integer> entry : instance.functions
					.getTable()) {
				JsonObject weightedFunction = new JsonObject();
				weightedFunction.addProperty("weight", entry.getValue());
				JsonArray functions = new JsonArray();
				for (LootFunction function : entry.getKey()) {
					JsonObject serialized = new JsonObject();
					LootFunctionManager.getSerializerFor(function).serialize(
							serialized, instance, serializationContext);
					functions.add(serialized);
				}
				weightedFunction.add("functions", functions);
				list.add(weightedFunction);
			}
			object.add("functions", list);
		}

		@Override
		public RandomFunction deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootCondition[] conditions) {
			RandomFunction instance = new RandomFunction(conditions);
			if (object.has("functions")) {
				JsonArray list = object.get("functions").getAsJsonArray();
				for (JsonElement element : list) {
					JsonObject weightedFunction = element.getAsJsonObject();
					if (weightedFunction.has("weight") && weightedFunction.has("function")) {
						List<LootFunction> functions = Lists.newArrayList();
						for (JsonElement functionElement : weightedFunction.get("functions").getAsJsonArray()) {
							JsonObject function = functionElement.getAsJsonObject();
							if (function.has("function")) {
								functions.add(LootFunctionManager.getSerializerForName(new ResourceLocation(function.get("function").getAsString()))
										.deserialize(function, deserializationContext, new LootCondition[]{}));

							}
						}
						instance.functions.addEntry(functions, weightedFunction.get("weight").getAsInt());
					}
				}
			}
			return instance;
		}
	}

}
