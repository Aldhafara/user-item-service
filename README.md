# user-item-service

Prosty serwis REST dla użytkowników i ich przedmiotów, z uwierzytelnianiem opartym o JWT.

## Spis treści

- [Opis](#opis)
- [Technologie](#technologie)
- [Konfiguracja bazy danych](#konfiguracja-bazy-danych)
- [Uruchomienie lokalne](#uruchomienie-lokalne)
- [Dostępne endpointy](#dostępne-endpointy)
- [Swagger / Dokumentacja API](#swagger--dokumentacja-api)
- [Testowanie](#testowanie)

---

## Opis

Aplikacja umożliwia:
- Rejestrację i logowanie użytkowników (JWT)
- Dodawanie i pobieranie przedmiotów powiązanych z użytkownikiem
- Ochronę zasobów z wykorzystaniem Spring Security
- Walidację danych wejściowych
- Dokumentację API poprzez Swagger UI

Dane każdego użytkownika są odseparowane – użytkownik widzi tylko swoje zasoby.

---

## Technologie

- Java 21
- Spring Boot 3.5.0
- Spring Security
- Spring Data JPA (Hibernate)
- MySQL
- H2 (testy)
- JWT (`io.jsonwebtoken`)
- Bean Validation (JSR-380)
- Lombok
- Swagger / OpenAPI (`springdoc-openapi`)
- JUnit 5

---

## Konfiguracja bazy danych

Projekt korzysta z MySQL. Przykładowa konfiguracja znajduje się w pliku `example.application.properties`.

### Krok po kroku:

1. Skopiuj plik konfiguracyjny:
   ```bash
   cp src/main/resources/example.application.properties src/main/resources/application.properties
   ```

2. Dostosuj wpisy w `application.properties` do swojej faktycznej konfiguracji:
   - adres hosta
   - port (domyślnie 3306)
   - nazwę bazy danych
   - dane logowania

## Uruchomienie lokalne
1. Upewnij się, że masz zainstalowaną JDK 21 i działającą bazę MySQL
2. Skonfiguruj plik application.properties (jak wyżej)
3. Uruchom aplikację:
   ```bash
   ./mvnw spring-boot:run
   ```
4. Aplikacja domyślnie będzie dostępna pod adresem:
   ```
   http://localhost:3000
   ```
## Dostępne endpointy

| Metoda | Endpoint  | Opis                              | Autoryzacja |
| ------ | --------- | --------------------------------- |-------------|
| POST   | /register | Rejestracja nowego użytkownika    | ❌           |
| POST   | /login    | Logowanie i otrzymanie tokenu JWT | ❌           |
| GET    | /items    | Pobierz swoje przedmioty          | ✅           |
| POST   | /items    | Dodaj nowy przedmiot              | ✅           |

## Swagger / Dokumentacja API
Po uruchomieniu aplikacji dokumentacja dostępna jest pod:
   ```
   http://localhost:3000/swagger-ui.html
   ```
Możesz tam przetestować wszystkie endpointy.

## Testowanie
Aby uruchomić testy:
   ```bash
   ./mvnw test
   ```
Testy wykorzystują bazę H2 (in-memory). Dane testowe nie mają wpływu na środowisko produkcyjne.
