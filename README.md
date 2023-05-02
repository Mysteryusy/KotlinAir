# KotlinAir
## Leírás
A program több adatfájl alapján, ami reptereket és le/felszállópályákról tartalmaz adatot különböző megfigyeléseket tesz. Ezen kívül a program ezeket az adatfájlokat optimalizálja első betöltésnél és lementi külön olyan sorok nélkül, amik nem tartalmaznak használható adatot. Ezen kívül az adatok betöltése után megadható külön .csv fájl vagy mappa, ami több ilyen fájlt tartalmazhat. Ezek a fájlok egy repülőnek egy adott útjának az információit tartalmazza. Ezekből az adatokból a program megtudja állapítani, hogy honnan-hova repült a repülő, mennyi idő alatt, melyik felszállópályát használta, mekkora volt a legnagyobb sebessége, illetve, ha több ilyen utat is megadunk, ami ugyanazt az útvonalat repülte be akkor meg tudja állapítani, hogy melyik alkalommal repülték be „jobban” a távot (legyen ez gyorsaság, vagy üzemanyaghasználat hatékonyság.)

## Adatok
A repülőterekről és felszállópályákról adatokat tartalmazó .csv-k a https://ourairports.com/data/ weboldalról vannak. Ezekben lévő adatok vesszővel vannak elválasztva.  

airports.csv: Repülőterekről tartalmaz információt, fontosabb oszlopok: ICAO azonosító (ident), méret és típus (type), név (name), elhelyezkedés (latitude_deg, longitude_deg, elevation_ft, continent, iso_country, iso_region, municipality).  

runways.csv: Felszállópályákról tartalmaz információt, fontosabb oszlopok: Reptér ICAO azonosítója (airport_ident), méret (length_ft, width_ft), leszállási kezdőpont/irány (le_ident, le_latitude_deg, le_longitude_deg, le_elevation_ft [le=landing end]), felszállási kezdőpont/irány (he_ident, he_latitude_deg, he_longitude_deg, he_elevation_ft [he=heading end]).  

countries.csv: Országkód-országnév párosításokat tartalmaz.  

traffic.csv: Repterekről tartalmaz forgalmi adatokat. (https://ansperformance.eu/data/)

Egy adott repülésről információkat a https://www.flightradar24.com/ weboldalról lehet szerezni. Itt két formátumban lehet letölteni, ami kml/csv, de csak a csv-t használja a program. Sajnos alapból ez a része nem ingyenes a weboldalnak, illetve API se elérhető egyszerűen, de kérésre megadott repülésről információkat le tudok tölteni. (A programhoz elég sok járatról fogok információt csomagolni emiatt.) A csv felépítése az alábbi: UNIX időbélyeg (Timestamp), UTC idő (UTC), repülőgép hívójele (Callsign), pozíció (Position), magasság (Altitude), sebesség (Speed), irány (Direction).

## Kinézet/Output
A program alapvetően konzol ablakban fut le, itt vár a felhasználótól input és ír ki bármit, ami történik. Ezen kívül létrehoz futásidőben .csv fájlokat, amik az alap fájlok optimalizált verziói. Nem teljesen biztos még, de lehetséges, hogy több járat összehasonlításakor eredményként létre fog hozni egy .air fájlt amiben meg tudja tekinteni a felhasználó utólag is a megfigyeléseket.
