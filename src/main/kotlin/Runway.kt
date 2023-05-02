class Runway(// Data: ICAO Ident, length_m, width_m, surface, le_ident, le_latitude_deg, le_longitude_deg, le_elevation_m, he_ident, he_latitude_deg, he_longitude_deg, he_elevation_m
    var icaoIdent: String,
    lengthFt: Int,
    widthFt: Int,
    var surface: String,
    var leIdent: String,
    var leLatitudeDeg: Double,
    var leLongitudeDeg: Double,
    leElevationFt: Double,
    var heIdent: String,
    var heLatitudeDeg: Double,
    var heLongitudeDeg: Double,
    heElevationFt: Double
) {
    var lengthM: Int = 0
    var widthM: Int = 0
    var leElevationM: Double = 0.0
    var heElevationM: Double = 0.0

    init {
        this.lengthM = (lengthFt/3.2808).toInt()
        this.widthM = (widthFt/3.2808).toInt()
        this.leElevationM = leElevationFt/3.2808
        this.heElevationM = heElevationFt/3.2808
    }
}