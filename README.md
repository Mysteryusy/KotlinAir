# KotlinAir
## Description
The program makes various observations based on several data files containing airport and runway data. In addition, the program optimizes these data files on first load and saves them separately without rows that do not contain useful data. In addition, after loading the data, a separate .csv file or folder can be specified, which can contain several such flight data files. These files contain the information for a particular route of a flight. From this data, the program can determine where the aircraft flew from and to, how long it took, which runway it used, what its maximum speed was, and, if you specify several of these trips that flew the same route, it can determine which time flew the distance "better" (whether it be speed or fuel efficiency.)  

## Running
The program can be launched by compiling it in an IDE like IntelliJ. The program displays upon running a disclaimer which can be dismissed by pressing the <kbd>ENTER</kbd> key. The program then displays the following:
- top 3 airports with the most runways
- top 3 airports with the most estimated passenger traffic
- top 3 countries with the most airports
- longest and shortest runways  

After this you can enter a location (latitude, longitude) and the program will tell you the nearest airport and its distance. Then you can enter a file or directory containing flight data. The program will then make observations on these files and create a .air file for each of them. If you specify a folder, the program will also create a .air file for the folder, which will contain the average of the data in the folder. The outputs can be found in the `output/` directory.

## Example usage
### Coordinate based search (Example is for the coordinates of the Cargodomb next to Liszt Ferenc International Airport)
**Input:**  
Enter a location (latitude, longitude or leave empty to skip): `47.4461953, 19.2189375`

**Output:**  
Nearest airport: Budapest Liszt Ferenc International Airport (LHBP) - 5,03 km  

### Specified file or directory (Example data is included in the repository, but can also be specified when running the program)

**Input:**  
Enter a file or directory containing flight data: `data/flights/wizzair/berbud/W62315_2bbce126.csv`  

*or*  

Enter a file or directory containing flight data: `data/flights/wizzair/berbud`


## Data
The .csv-s containing data on airports and runways are from https://ourairports.com/data/. Data in these are separated by commas.  

airports.csv: Contains information about airports, main columns: ICAO ident, size and type, name, location (latitude_deg, longitude_deg, elevation_ft, continent, iso_country, iso_region, municipality).

runways.csv: Contains information on runways, main columns: airport ICAO identifier (airport_ident), size (length_ft, width_ft), landing start point/direction (le_ident, le_latitude_deg, le_longitude_deg, le_elevation_ft [le=landing end]), take-off start point/direction (he_ident, he_latitude_deg, he_longitude_deg, he_elevation_ft [he=heading end]).

countries.csv: Contains pairings of countries' codes to their names.

traffic.csv: Contains traffic data for airports (https://ansperformance.eu/data/).

Information on a specific flight can be obtained from https://www.flightradar24.com/. Here you can download in two formats, which are kml/csv, but only csv is used by this program. The structure of the csv is as follows: UNIX timestamp, UTC time, aircraft callsign, position, altitude, speed, direction.

## WIP
- .exe and release