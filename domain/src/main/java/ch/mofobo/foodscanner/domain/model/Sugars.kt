package ch.mofobo.foodscanner.domain.model

data class Sugars (

	val name_translations : Name_translations,
	val unit : String,
	val per_hundred : Int,
	val per_portion : Double,
	val per_day : Int
)