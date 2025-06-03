# user-item-service
Prosty user-item serwis z uwierzytelnianiem JWT

## Konfiguracja bazy danych

Projekt korzysta z MySQL + Hibernate.

Przykładowa konfiguracja znajduje się w pliku `example.application.properties`

Aby skonfigurować środowisko lokalne:
1. Skopiuj plik `example.application.properties` do `application.properties`:

   ```bash
   cp src/main/resources/example.application.properties src/main/resources/application.properties
   ```

2. Dostosuj wpisy w `application.properties` do swojej faktycznej konfiguracji.

Aby uruchomić aplikację lokalnie, potrzebujesz:

- Działającą instancję MySQL (np. na `localhost:3306`)
- Bazę danych o nazwie `your_database`
- Użytkownika i hasło zgodnych z tym, co podałeś w `application.properties`