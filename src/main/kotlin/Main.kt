import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.pow

var airports = mutableMapOf<String, Airport>()
var traffic = mutableMapOf<String, MutableMap<Int, Int>>()
var flights = ArrayList<Flight>()
var countries = mutableMapOf<String, String>()

fun main() {
    printDisclaimerOnStart()
    optimizeDataFiles()
    loadData()
    analyseData()
    customFlightAnalysis()
}

fun printDisclaimerOnStart() {
    println("----- DISCLAIMER -----")

    println("Source: https://ansperformance.eu/data/")
    println("© European Organisation for the Safety of Air Navigation (EUROCONTROL)\n")
    println("This data is published by the EUROCONTROL Performance Review Unit in the interest of the exchange of information. It may be copied in whole or in part providing that this copyright notice and disclaimer are included.")
    println("The information may not be modified without prior written permission from the EUROCONTROL Performance Review Unit.\n")
    println("The information does not necessarily reflect the official views or policy of EUROCONTROL, which makes no warranty, either implied or express, for the information contained in this document, including its accuracy, completeness or usefulness.")

    println("\n-----\n")

    println("Source: https://openflights.org/data.html")
    println("The OpenFlights Airport, Airline, Plane and Route Databases are made available under the Open Database License. Any rights in individual contents of the database are licensed under the Database Contents License. In short, these mean that you are welcome to use the data as you wish, if and only if you both acknowledge the source and and license any derived works made available to the public with a free license as well.\n")

    println("Press ENTER to continue...")
    readlnOrNull()

    // Clear the console
    print("\u001b[H\u001b[2J")
}

fun optimizeDataFiles() {
    if(!File("data/airports_optimized.csv").exists()) {
        // Read data/airports.csv, check if the type is "large_airport" or "medium_airport" or "small_Airport", and write to data/airports_optimized.csv

        if(!File("data/airports.csv").exists()) {
            throw Exception("data/airports.csv does not exist.")
        }
        else{
            val airportsFile = File("data/airports.csv")
            val airportsFileLines = airportsFile.readLines().drop(1)

            val airportsOptimizedFile = File("data/airports_optimized.csv")

            for(line in airportsFileLines) {
                val lineSplit = line.replace("\"", "").split(",")

                if((lineSplit[2] == "large_airport" || lineSplit[2] == "medium_airport" || lineSplit[2] == "small_airport") && lineSplit[6] != "") {
                    airportsOptimizedFile.appendText(line.replace("\"", "") + "\n")
                }
            }
        }
    }

    if(!File("data/runways_optimized.csv").exists()) {
        if(!File("data/runways.csv").exists()) {
            throw Exception("data/runways.csv does not exist.")
        }
        else{
            // Read data/runways.csv, check if the location data is not empty, and write to data/runways_optimized.csv

            val runwaysFile = File("data/runways.csv")
            val runwaysFileLines = runwaysFile.readLines().drop(1)

            val runwaysOptimizedFile = File("data/runways_optimized.csv")

            for(line in runwaysFileLines) {
                val lineSplit = line.replace("\"", "").split(",")

                if(lineSplit[3] != "" && lineSplit[4] != "" && lineSplit[9] != "" && lineSplit[10] != "" && lineSplit[11] != "" && lineSplit[15] != "" && lineSplit[16] != "" && lineSplit[17] != "") {
                    runwaysOptimizedFile.appendText(line.replace("\"", "") + "\n")
                }
            }
        }
    }

    if(!File("data/traffic_optimized.csv").exists()) {
        if(!File("data/traffic.csv").exists()) {
            throw Exception("data/traffic.csv does not exist.")
        }
        else{
            // Read data/traffic.csv, sum the traffic for each airport by year, and write to data/traffic_optimized.csv
            // Data: YEAR;MONTH_NUM;MONTH_MON;FLT_DATE;APT_ICAO;APT_NAME;STATE_NAME;FLT_DEP_1;FLT_ARR_1;FLT_TOT_1;FLT_DEP_IFR_2;FLT_ARR_IFR_2;FLT_TOT_IFR_2;Pivot Label

            val trafficFile = File("data/traffic.csv")
            val trafficFileLines = trafficFile.readLines().drop(1) // To drop the header

            val trafficOptimizedFile = File("data/traffic_optimized.csv")

            val airportTrafficByYear = mutableMapOf<String, MutableMap<Int, Int>>()

            for(line in trafficFileLines){
                val values = line.split(";")
                val year = values[0].toInt()
                val airportICAO = values[4]
                val traffic = values[7].toInt()
                val yearTraffic = airportTrafficByYear.getOrDefault(airportICAO, mutableMapOf())
                yearTraffic[year] = yearTraffic.getOrDefault(year, 0) + traffic
                airportTrafficByYear[airportICAO] = yearTraffic
            }

            trafficOptimizedFile.bufferedWriter().use { out ->
                out.write("ICAO;YEAR;TRAFFIC\n")
                for((airport, trafficByYear) in airportTrafficByYear){
                    for((year, traffic) in trafficByYear){
                        out.write("$airport;$year;$traffic\n")
                    }
                }
            }
        }
    }
}

fun loadData(){
    if(File("data/countries.csv").exists()){
        val countriesFile = File("data/countries.csv")
        val countriesFileLines = countriesFile.readLines().drop(1)

        for(line in countriesFileLines){
            val values = line.replace("\"", "").split(",")
            countries[values[1]] = values[2]
        }
    }

    if(File("data/traffic_optimized.csv").exists()){
        val trafficFile = File("data/traffic_optimized.csv")
        val trafficFileLines = trafficFile.readLines().drop(1)

        for(line in trafficFileLines){
            val values = line.split(";")
            val airportICAO = values[0]
            val year = values[1].toInt()
            val traff = values[2].toInt()

            traffic.putIfAbsent(airportICAO, mutableMapOf())
            traffic[airportICAO]!![year] = traff
        }
    }
    else{
        throw Exception("data/traffic_optimized.csv does not exist.")
    }

    if(File("data/airports_optimized.csv").exists() && File("data/runways_optimized.csv").exists()){
        val airportsFile = File("data/airports_optimized.csv")
        val airportsFileLines = airportsFile.readLines()

        for(line in airportsFileLines) {
            val lineSplit = line.split(",")

            val icao = lineSplit[1]
            val type = lineSplit[2]
            val name = lineSplit[3]
            val latitude = lineSplit[4].toDouble()
            val longitude = lineSplit[5].toDouble()
            val elevation = lineSplit[6].toDouble()
            val continent = lineSplit[7]
            val isoCountry = lineSplit[8]
            val isoRegion = lineSplit[9]
            val municipality = lineSplit[10]


            val airport = Airport(
                icao,
                type,
                name,
                latitude,
                longitude,
                elevation,
                continent,
                isoCountry,
                isoRegion,
                municipality
            )

            airports[icao] = airport

            if (traffic.containsKey(icao)) {
                val years = traffic[icao]!!.keys

                for (year in years) {
                    airport.addTraffic(year, traffic[icao]!![year]!!)
                }
            }
        }

        val runwaysFile = File("data/runways_optimized.csv")
        val runwaysFileLines = runwaysFile.readLines()

        for(line in runwaysFileLines) {
            val lineSplit = line.split(",")

            val airportICAO = lineSplit[2]
            val length = lineSplit[3].toInt()
            val width = lineSplit[4].toInt()
            val surface = lineSplit[5]
            val leIdent = lineSplit[8]
            val leLatitude = lineSplit[9].toDouble()
            val leLongitude = lineSplit[10].toDouble()
            val leElevation = lineSplit[11].toDouble()
            val heIdent = lineSplit[14]
            val heLatitude = lineSplit[15].toDouble()
            val heLongitude = lineSplit[16].toDouble()
            val heElevation = lineSplit[17].toDouble()

            val runway = Runway(
                airportICAO,
                length,
                width,
                surface,
                leIdent,
                leLatitude,
                leLongitude,
                leElevation,
                heIdent,
                heLatitude,
                heLongitude,
                heElevation
            )


            if (airports.containsKey(airportICAO)) {
                airports[airportICAO]!!.addRunway(runway)
            }
        }
    }
    else{
        throw Exception("data/airports_optimized.csv or data/runways_optimized.csv does not exist.")
    }
}

fun analyseData() {
    val topAirportsWithHighestNumberOfRunways = airports.values.sortedByDescending { it.runways.size }.take(3)
    println("Top 3 airports with highest number of runways:")
    for (airport in topAirportsWithHighestNumberOfRunways) {
        println("\t${airport.name} (${airport.icaoIdent}) - ${airport.runways.size} runways")
    }

    println()

    val topAirportsWithHighestTraffic2022 = airports.values.sortedByDescending { it.estimatedTraffic[2022] }.take(3)
    println("Top 3 airports with highest traffic in 2022:")
    for (airport in topAirportsWithHighestTraffic2022) {
        println("\t${airport.name} (${airport.icaoIdent}) - ${airport.estimatedTraffic[2022]} passengers")
    }

    println()

    val airportsByCountry = mutableMapOf<String, Int>()
    for (airport in airports.values) {
        airportsByCountry[airport.isoCountry] = airportsByCountry.getOrDefault(airport.isoCountry, 0) + 1
    }
    val topCountriesWithHighestNumberOfAirports =
        airportsByCountry.toList().sortedByDescending { (_, value) -> value }.take(3)
    println("Top 3 countries with highest number of airports:")
    for (country in topCountriesWithHighestNumberOfAirports) {
        println("\t${countries[country.first]} (${country.first}) - ${country.second} airports")
    }

    println()

    val longestRunway = airports.values.flatMap { it.runways }.maxBy { it.lengthM }
    println("Longest runway: ${airports[longestRunway.icaoIdent]!!.name} (${longestRunway.icaoIdent}) - ${longestRunway.leIdent} - ${longestRunway.lengthM}m")

    val shortestRunway = airports.values.flatMap { it.runways }.minBy { it.lengthM }
    println("Shortest runway: ${airports[shortestRunway.icaoIdent]!!.name} (${shortestRunway.icaoIdent}) - ${shortestRunway.leIdent} - ${shortestRunway.lengthM}m")

    println()

    print("Enter a location (latitude, longitude or leave empty to skip): ")
    val location = readlnOrNull()
    if (location != null && location != "") {
        val locationSplit = location.split(",")
        val latitude = locationSplit[0].toDouble()
        val longitude = locationSplit[1].toDouble()

        // Distance calculation
        val distances = mutableMapOf<Double, Airport>()
        for (airport in airports.values) {
            val distance = kotlin.math.sqrt(
                (airport.latitudeDeg - latitude).pow(2) + (airport.longitudeDeg - longitude).pow(2)
            )
            distances[distance] = airport
        }

        val nearestAirport = distances.toList().minBy { it.first }.second

        val distance = distances.toList().minBy { it.first }.first * 111.2
        println("Nearest airport: ${nearestAirport.name} (${nearestAirport.icaoIdent}) - $distance km")
    }

    println()
}

fun customFlightAnalysis(){
    print("Enter a file or directory containing flight data: ")
    val fileOrDirectory = readlnOrNull()
    if (fileOrDirectory != null && fileOrDirectory != "") {
        val fileOrDirectoryFile = File(fileOrDirectory)
        if (fileOrDirectoryFile.exists()) {
            if (fileOrDirectoryFile.isDirectory) {
                // Directory
                val files = fileOrDirectoryFile.listFiles()
                if (files != null) {
                    val filename = System.currentTimeMillis().toString()
                    for (file in files) {
                        if (file.isFile) {
                            val fileLines = file.readLines().drop(1)
                            val flight = Flight()
                            for (line in fileLines) {
                                val lineSplit = line.split(",")

                                val latitude = lineSplit[3].replace("\"", "").toDouble()
                                val longitude = lineSplit[4].replace("\"", "").toDouble()

                                flight.addInfo(lineSplit[0].toInt(), lineSplit[1], lineSplit[2], latitude, longitude, lineSplit[5].toDouble(), lineSplit[6].toDouble(), lineSplit[7].toDouble())
                            }
                            flights.add(flight)
                            flightAnalysis(flight, filename)
                        }
                    }
                    groupFlightAnalysis(filename)
                }
            } else {
                // File
                val fileLines = fileOrDirectoryFile.readLines().drop(1)
                val flight = Flight()
                for (line in fileLines) {
                    val lineSplit = line.split(",")

                    val latitude = lineSplit[3].replace("\"", "").toDouble()
                    val longitude = lineSplit[4].replace("\"", "").toDouble()

                    flight.addInfo(lineSplit[0].toInt(), lineSplit[1], lineSplit[2], latitude, longitude, lineSplit[5].toDouble(), lineSplit[6].toDouble(), lineSplit[7].toDouble())
                }
                flights.add(flight)
                flightAnalysis(flight, flight.timestamp[0].toString() + "_" + flight.callsign[0])
            }
        }
    }
}

fun flightAnalysis(flight: Flight, fileName: String = ""){
    if(!File("output").isDirectory){
        Files.createDirectory(Paths.get("output"))
    }

    val outputFile = File("output/$fileName.air")

    println("--- Flight ${flight.utc[0]} (${flight.callsign[0]}) ---")
    outputFile.appendText("--- Flight ${flight.utc[0]} (${flight.callsign[0]}) ---\n")

    println("Total time: ${String.format("%.2f", flight.totalTime())} minutes")
    outputFile.appendText("Total time: ${String.format("%.2f", flight.totalTime())} minutes\n")
    println("Total distance: ${String.format("%.2f", flight.totalDistance())} km")
    outputFile.appendText("Total distance: ${String.format("%.2f", flight.totalDistance())} km\n")


    val takeoffLat = flight.latitude[flight.determineTakeoff()]
    val takeoffLon = flight.longitude[flight.determineTakeoff()]

    // Determine nearest airport to takeoff location
    val distances = mutableMapOf<Double, Airport>()
    for (airport in airports.values) {
        val distance = kotlin.math.sqrt(
            (airport.latitudeDeg - takeoffLat).pow(2) + (airport.longitudeDeg - takeoffLon).pow(2)
        )
        distances[distance] = airport
    }

    val nearestAirport = distances.toList().minBy { it.first }.second

    // Nearest runway to takeoff location
    val distancesRunway = mutableMapOf<Double, Runway>()
    for (runway in nearestAirport.runways) {
        val distance = kotlin.math.sqrt(
            (runway.leLatitudeDeg - takeoffLat).pow(2) + (runway.leLongitudeDeg - takeoffLon).pow(2)
        )
        distancesRunway[distance] = runway
    }

    val nearestRunway = distancesRunway.toList().minBy { it.first }.second

    println("Departure airport: ${nearestAirport.name} (${nearestAirport.icaoIdent})")
    outputFile.appendText("Departure airport: ${nearestAirport.name} (${nearestAirport.icaoIdent})\n")
    println("Takeoff runway: ${nearestRunway.heIdent}")
    outputFile.appendText("Takeoff runway: ${nearestRunway.heIdent}\n")


    // Determine nearest airport to landing location
    val landingLat = flight.latitude[flight.determineLanding()]
    val landingLon = flight.longitude[flight.determineLanding()]

    val distancesLanding = mutableMapOf<Double, Airport>()
    for (airport in airports.values) {
        val distance = kotlin.math.sqrt(
            (airport.latitudeDeg - landingLat).pow(2) + (airport.longitudeDeg - landingLon).pow(2)
        )
        distancesLanding[distance] = airport
    }

    val nearestAirportLanding = distancesLanding.toList().minBy { it.first }.second

    // Nearest runway to landing location
    val distancesRunwayLanding = mutableMapOf<Double, Runway>()
    for (runway in nearestAirportLanding.runways) {
        val distance = kotlin.math.sqrt(
            (runway.leLatitudeDeg - landingLat).pow(2) + (runway.leLongitudeDeg - landingLon).pow(2)
        )
        distancesRunwayLanding[distance] = runway
    }

    val nearestRunwayLanding = distancesRunwayLanding.toList().minBy { it.first }.second

    println("Arrival airport: ${nearestAirportLanding.name} (${nearestAirportLanding.icaoIdent})")
    outputFile.appendText("Arrival airport: ${nearestAirportLanding.name} (${nearestAirportLanding.icaoIdent})\n")
    println("Landing runway: ${nearestRunwayLanding.leIdent}")
    outputFile.appendText("Landing runway: ${nearestRunwayLanding.leIdent}\n")

    println("---")
    outputFile.appendText("---\n")
}

fun groupFlightAnalysis(fileName: String){
    if(!File("output").isDirectory){
        Files.createDirectory(Paths.get("output"))
    }

    val outputFile = File("output/$fileName.air")

    // Shortest flight in group by time
    val shortestFlightTime = flights.minBy { it.totalTime() }
    println("Shortest flight in group by Time: ${shortestFlightTime.utc[0]} (${shortestFlightTime.callsign[0]}) - ${String.format("%.2f", shortestFlightTime.totalTime())} minutes")
    outputFile.appendText("Shortest flight in group by Time: ${shortestFlightTime.utc[0]} (${shortestFlightTime.callsign[0]}) - ${String.format("%.2f", shortestFlightTime.totalTime())} minutes\n")

    // Shortest flight in group by distance
    val shortestFlightDistance = flights.minBy { it.totalDistance() }
    println("Shortest flight in group by Distance: ${shortestFlightDistance.utc[0]} (${shortestFlightDistance.callsign[0]}) - ${String.format("%.2f", shortestFlightDistance.totalDistance())} km")
    outputFile.appendText("Shortest flight in group by Distance: ${shortestFlightDistance.utc[0]} (${shortestFlightDistance.callsign[0]}) - ${String.format("%.2f", shortestFlightDistance.totalDistance())} km\n")
}