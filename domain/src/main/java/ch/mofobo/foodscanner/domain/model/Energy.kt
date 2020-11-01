package ch.mofobo.foodscanner.domain.model

data class Energy (

	val name_translations : Name_translations,
	val unit : String,
	val per_hundred : Int,
	val per_portion : String,
	val per_day : Int
)