package ch.mofobo.foodscanner.domain.model

data class Shards (
	val total : Int,
	val successful : Int,
	val failed : Int
)