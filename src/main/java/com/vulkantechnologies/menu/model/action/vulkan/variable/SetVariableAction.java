package com.vulkantechnologies.menu.model.action.vulkan.variable;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.annotation.ComponentName;
import com.vulkantechnologies.menu.annotation.Single;
import com.vulkantechnologies.menu.model.action.Action;
import com.vulkantechnologies.menu.model.menu.Menu;
import com.vulkantechnologies.menu.model.variable.MenuVariable;
import com.vulkantechnologies.menu.utils.VariableUtils;

import redempt.crunch.CompiledExpression;
import redempt.crunch.Crunch;
import redempt.crunch.functional.EvaluationEnvironment;

@ComponentName("set-variable")
public record SetVariableAction(@Single String name, @Single String value) implements Action {

    @Override
    public void accept(Player player, Menu menu) {
        MenuVariable<?> variable = menu.variable(name)
                .orElseThrow(() -> new IllegalArgumentException("Variable not found: " + name));

        // Inject placeholders
        String formattedValue = VulkanMenu.get().processPlaceholders(player, value);

        // Check if the value contains any operators
        if (formattedValue.matches(".*[+\\-*/%].*")) {
            // Parse variables name
            String[] parts = formattedValue.split("[+\\-*/%]");
            List<String> variablesName = new ArrayList<>();
            for (String part : parts) {
                part = part.trim();
                if (part.isEmpty()
                    || VariableUtils.isNumeric(part))
                    continue;

                String filteredPart = part.replaceAll("[^a-zA-Z0-9]", "");
                if (!menu.hasVariable(filteredPart))
                    throw new IllegalArgumentException("Variable not found: " + filteredPart);

                variablesName.add(filteredPart);
            }

            // Compile expression
            EvaluationEnvironment env = new EvaluationEnvironment();
            env.setVariableNames(variablesName.toArray(String[]::new));
            CompiledExpression exp = Crunch.compileExpression(formattedValue, env);

            // Evaluate expression
            double[] values = new double[variablesName.size()];
            for (int i = 0; i < variablesName.size(); i++) {
                String varName = variablesName.get(i);
                MenuVariable<?> var = menu.variable(varName)
                        .orElseThrow(() -> new IllegalArgumentException("Variable not found: " + varName));
                Object varValue = var.value();
                if (varValue instanceof Number) {
                    values[i] = ((Number) varValue).doubleValue();
                } else {
                    throw new IllegalArgumentException("Variable is not a number: " + varName);
                }
            }
            double result = exp.evaluate(values);

            // Set the variable value
            if (variable.type() == Integer.class) {
                variable.value(String.valueOf((int) result));
            } else {
                variable.value(String.valueOf(result));
            }

            return;
        }

        variable.value(formattedValue);
    }

}
