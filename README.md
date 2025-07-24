# 🗳️ VoteApp – Aplikacja do głosowania (Spring Boot + React.js)

VoteApp to pełnoprawna aplikacja webowa typu full-stack stworzona z wykorzystaniem **Spring Boot** (backend) oraz **React.js** (frontend). Umożliwia tworzenie głosowań, oddawanie głosów, zarządzanie użytkownikami oraz śledzenie aktywności

---
#  Ograniczenia WebSocket w środowisku produkcyjnym

Powiadomienia w czasie rzeczywistym (WebSocket) są w pełni funkcjonalne w środowisku **lokalnym (localhost)**.  
Na środowisku produkcyjnym występują problemy związane z obsługą ciasteczek (cookies) przy różnych domenach (frontend/backend)
Niestesty usługa Azure Front Door która potrafiłaby rozwiązać ten problem nie jest dostępna za darmo w ramch subsykrybcji Azure for Studnets
---

## 🔧 Technologie

### Backend:
- Java 21
- Spring Boot (REST API, Spring Security, JPA)
- PostgreSQL
- JWT, OAuth2
- WebSocket (powiadomienia real-time)
- Rate Limiting
- Testy

### Frontend:
- React.js
- JavaScript (ES6+), HTML5, CSS3
- Axios, React Router
- TailwindCSS

### Inne:
- Docker
- Git & GitHub Actions (CI/CD)
- Hosting: Microsoft Azure (frontend + backend + PostgreSQL DB)

## ⚙️ Funkcjonalności

-  Rejestracja i logowanie (JWT + OAuth2)
-  Podział ról (użytkownik / moderator / admin)
-  SecurityFilterChain zz podziałem ról
-  Tworzenie i edytowanie głosowań (CRUD)
-  Dodawanie zdjęć i obrazów
-  Dodawanie komentarzy
-  Oddawanie głosów (jeden głos na użytkownika)
-  Zgłaszanie nadużyć
-  Powiadomienia WebSocket o nowych głosach
-  Logowanie zdarzeń i aktywności użytkowników
-  Analityka (frekwencja, liczba głosów)
-  Rate limiting – zabezpieczenie endpointów
