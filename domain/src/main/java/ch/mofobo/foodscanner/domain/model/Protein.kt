package ch.mofobo.foodscanner.domain.model

data class Protein (

	val name_translations : Name_translations,
	val unit : String,
	val per_hundred : Double,
	val per_portion : Double,
	val per_day : Int
)