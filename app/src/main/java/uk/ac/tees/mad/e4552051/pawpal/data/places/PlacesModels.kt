package uk.ac.tees.mad.e4552051.pawpal.data.places

data class PlacesResponse(
    val results: List<PlaceResult>
)

data class PlaceResult(
    val name: String,
    val geometry: Geometry
)

data class Geometry(
    val location: LatLngLiteral
)

data class LatLngLiteral(
    val lat: Double,
    val lng: Double
)