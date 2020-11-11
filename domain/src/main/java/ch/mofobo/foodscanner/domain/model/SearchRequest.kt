package ch.mofobo.foodscanner.domain.model

data class SearchRequest(val query: Query)
data class Query(val terms: Terms)
data class Terms(val barcode: List<String>)




