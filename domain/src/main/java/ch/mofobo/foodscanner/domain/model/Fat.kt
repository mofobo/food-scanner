package ch.mofobo.foodscanner.domain.model

data class Fat (

	val name_translations : Name_translations,
	val unit : String,
	val per_hundred : Int,
	val per_portion : Int,
	val per_day : Int
)