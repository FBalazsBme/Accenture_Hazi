# Pokémon Harc Szimulátor

## Áttekintés

A Pokémon Harc Szimulátor egy webalkalmazás, amely lehetővé teszi a felhasználók számára, hogy Pokémon harcokat szimuláljanak. Az alkalmazás két fő részből áll: egy Frontend és egy Backend. A Frontend Angular keretrendszerben készült, míg a Backend Java Spring segítségével nyújt API szolgáltatásokat a Pokémon adatok lekérdezésére és a harcok szimulációjára. A két rész REST API-n keresztül kommunikál egymással.

## Funkcionalitás

### Frontend

1. **Harc indítása**:
    - Egy "Harc" gomb megnyomására a Backend API két véletlenszerű Pokémon adatot kérdez le, és elindítja a harcot.
    - A harc eredménye megjelenik a felhasználó számára a képernyőn.

2. **Korábbi harcok listája**:
    - A felhasználó megtekintheti az eddigi harcok listáját (mindkét Pokémon neve, típusa, ereje és a győztes).
    - A lista maximum 20 harcot tartalmaz, és lehetőség van név szerinti keresésre a kliens oldalon.

3. **Technológiai követelmények**:
    - **Angular** keretrendszer a Frontend fejlesztéséhez.
    - API kommunikáció Angular által biztosított eszközökkel.
    - CSS preprocesszor használata (pl. SCSS).
    - Dizájn: egyszerű, esetleg design library használata.

### Backend

1. **API végpontok**:
    - **Véletlenszerű Pokémonok lekérdezése**:
        - Két véletlenszerű Pokémon adatának lekérdezése a PokéAPI-ról (név, típus, kép URL).
        - Az erő értéke 1 és 20 között generálódik.

    - **Harc szimulációja**:
        - Két Pokémon név alapján lekérdezi a PokéAPI adatokat, majd elvégzi a harc szimulációját.
        - A típuselőnyök és erő alapján meghatározza a győztest.

    - **Harcok listája**:
        - Visszaadja az eddigi harcok listáját, tartalmazza mindkét Pokémon nevét, típusát, erejét, és a győztes nevét.

2. **Technológiai követelmények**:
    - **Java Spring** keretrendszer a Backend fejlesztéséhez.
    - API REST elveknek megfelelően.
    - Az API válaszai **JSON** formátumban.
    - **PokéAPI** (https://pokeapi.co/) integrálása Pokémon adatok lekérdezésére.

3. **Specifikációk**:
    - Véletlenszerű Pokémonok lekérdezése a PokéAPI `pokemon/` és `pokemon-species/` végpontjain keresztül.
    - Típuselőnyök a PokéAPI `type/` végpontja alapján.
    - Harc szimuláció: ha típuselőny van, az előnyben lévő Pokémon nyer. Ha nincs típuselőny, az erősebb Pokémon nyer.

4. **Unit tesztek**:
    - A harcok logikájának tesztelése (típuselőny, erő alapú kimenet).

## Telepítési és futtatási útmutató

### Előfeltételek

- **Node.js** és **npm** telepítése a Frontend futtatásához.
- **Java JDK 11** vagy újabb telepítése a Backendhez.
- **Maven** telepítése a Backend építéséhez.
- **PokéAPI** internetkapcsolat, mivel az API a külső PokéAPI-t használja.

### Backend telepítése

1. Klónozd a repot:
   ```bash
   git clone <repo-url>
