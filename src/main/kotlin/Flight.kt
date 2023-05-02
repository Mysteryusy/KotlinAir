import kotlin.math.*

class Flight {
    // Data: Timestamp, UTC, Callsign, Position, Altitude, Speed, Direction
    var timestamp: List<Int> = listOf()
    var utc: List<String> = listOf()
    var callsign: List<String> = listOf()
    var latitude: List<Double> = listOf()
    var longitude: List<Double> = listOf()
    var altitude: List<Double> = listOf()
    var speed: List<Double> = listOf()
    var direction: List<Double> = listOf()

    init {
        this.timestamp = listOf()
        this.utc = listOf()
        this.callsign = listOf()
        this.latitude = listOf()
        this.longitude = listOf()
        this.altitude = listOf()
        this.speed = listOf()
        this.direction = listOf()
    }

    fun addInfo(timestamp: Int, utc: String, callsign: String, latitude: Double, longitude: Double, altitude: Double, speed: Double, direction: Double) {
        this.timestamp += timestamp
        this.utc += utc
        this.callsign += callsign
        this.latitude += latitude
        this.longitude += longitude
        this.altitude += (altitude/3.2808)
        this.speed += (speed*1.852)
        this.direction += direction
    }

    fun determineTakeoff() : Int {
        var takeoff = 0
        var i = 0
        while (i < this.altitude.size) {
            if (this.altitude[i] > 0) {
                takeoff = i
                break
            }
            i++
        }
        return takeoff
    }

    fun determineLanding() : Int {
        var landing = 0
        var i = this.altitude.size - 1
        while (i > 0) {
            if (this.altitude[i] > 0) {
                landing = i
                break
            }
            i--
        }
        return landing
    }

    fun totalTime(): Double {
        val takeoffTime = timestamp[determineTakeoff()].toDouble()
        val landingTime = timestamp[determineLanding()].toDouble()
        return (landingTime - takeoffTime) / 60.0
    }

    fun totalDistance(): Double {
        var totalDistance = 0.0
        for (i in 0 until latitude.size - 1) {
            val lat1 = Math.toRadians(latitude[i])
            val lon1 = Math.toRadians(longitude[i])
            val lat2 = Math.toRadians(latitude[i + 1])
            val lon2 = Math.toRadians(longitude[i + 1])

            val deltaLat = lat2 - lat1
            val deltaLon = lon2 - lon1

            val a = sin(deltaLat / 2).pow(2) + cos(lat1) * cos(lat2) * sin(deltaLon / 2).pow(2)
            val c = 2 * atan2(sqrt(a), sqrt(1 - a))

            val altitudeChange = altitude[i + 1] - altitude[i]
            val distance = 6371 * c + altitudeChange
            totalDistance += distance
        }
        return totalDistance
    }

}