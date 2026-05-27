package domain.model

data class NobelPrize(
	val year: String,
	val category: String,
	val categoryFullName: String,
	val laureates: List<Laureate>
)