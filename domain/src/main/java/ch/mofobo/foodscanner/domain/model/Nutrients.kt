package ch.mofobo.foodscanner.domain.model

data class Nutrients (

	val energy : Energy,
	val energy_kcal : Energy_kcal,
	val fat : Fat,
	val saturated_fat : Saturated_fat,
	val carbohydrates : Carbohydrates,
	val sugars : Sugars,
	val fiber : Fiber,
	val protein : Protein,
	val salt : Salt
)