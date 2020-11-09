package ch.mofobo.foodscanner.domain.model

data class Images(
    val categories: List<String>,
    val thumb: String,
    val medium: String,
    val large: String,
    val xlarge: String
) {
    val isImageAvailable: Boolean
        get() = !(thumb.isNullOrBlank() &&
                medium.isNullOrBlank() &&
                large.isNullOrBlank() &&
                xlarge.isNullOrBlank())


}