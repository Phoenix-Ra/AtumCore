package me.phoenixra.atum.craft.misc

import me.phoenixra.atum.core.placeholders.PlaceholderManager
import me.phoenixra.atum.core.placeholders.context.PlaceholderContext
import redempt.crunch.Crunch
import redempt.crunch.functional.EvaluationEnvironment
import redempt.crunch.functional.Function

class ExpressionEvaluator {
    private val environment = EvaluationEnvironment()
    init {
        environment.addFunction(
            Function(
                "min",
                2
            ) { it: DoubleArray -> Math.min(it[0], it[1]) }
        )
    }

    fun evaluate(expression: String, context: PlaceholderContext): Double {
        return Crunch.compileExpression(
            PlaceholderManager.translatePlaceholders(expression, context),
            environment
        ).evaluate()
    }
}