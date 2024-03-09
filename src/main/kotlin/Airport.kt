class Airport(// Data: ICAO Ident, type, name, latitude_deg, longitude_deg, elevation_m, continent, iso_country, iso_region, municipality, runways, estimated_traffic
    var icaoIdent: String,
    var type: String,
    var name: String,
    var latitudeDeg: Double,
    var longitudeDeg: Double,
    elevationFt: Double,
    var continent: String,
    var isoCountry: String,
    var isoRegion: String,
    var municipality: String
) {
    private var elevationM: Double = 0.0
    var runways: List<Runway> = listOf()
    var estimatedTraffic: MutableMap<Int, Int> = mutableMapOf()

    init {
        this.elevationM = (elevationFt/3.2808)
    }

    fun addRunway(runway: Runway) {
        runways += runway
    }

    fun addTraffic(year: Int, traffic: Int) {
        // Traffic here is departure count, 250 is a rough average of passengers per flight
        estimatedTraffic[year] = (traffic * 250)
    }
}