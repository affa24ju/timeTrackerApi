### Timetracker API
Det här är ett API som byggds med Spring Boot och MongoDB för att skapa en kategori samt starta och avsluta en uppgift. Man får en statistik hur mycket tid man har lagt på en uppgift under veckan. Man får även välja en vecka för att se statistik för den där veckan.  

### Funktionalitet
- Lägga till en kategori.
- Uppdatera en kategori.
- Lägga till en task (uppgift) och starta tiden (check in)
- Avsluta uppgiften (checkout)
- Får statistik om hur många timmar/ minuter man har spenderat med tillagda uppgifter för nuvarande veckan.
- Visar statistik för en vald vecka. 

### Testa i Postman
  ## Skapa kategori
    # POST http://localhost:8080/api/categories
    Body(raw JSON):
    {
      "name": "Programmera"
    }

  ## Hämta alla kategorier
    # GET http://localhost:8080/api/categories

  ## Uppdatera kategori
    # PUT http://localhost:8080/api/categories/DITT_ID_HÄR
    Body (raw JSON):
    {
      "name": "Ny kategori"
    }

  ## Skapa en task & checka in
    # POST http://localhost:8080/api/tasks/checkin
    Body(raw JSON):
    {
    "categoryId": "DITT_CATEGORY_ID_HÄR"
  }

## Checka ut
    # POST http://localhost:8080/api/tasks/checkout/DITT_TASK_ID

## Hämta veckans task
    # GET http://localhost:8080/api/tasks/week
  
## Se nuvarande veckans statistik
    # GET http://localhost:8080/api/tasks/stats/week

## Se tidigara veckan statistik
    # GET http://localhost:8080/api/tasks/stats/specificweek?year=2025&week=18
    Obs! Det finns data tillagd bara för vecka 18 och 19.
  
