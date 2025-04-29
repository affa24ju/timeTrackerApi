### Timetracker API
Det här är ett API som byggds med Spring Boot och MongoDB för att lägga till en kategori och lägga till en uppgift. Man får en statistik hur mycket tid man har lagt på en uppgift per vecka. 

### Funktionalitet
- Lägga till en kategori.
- Uppdatera en kategori.
- Lägga till en task (uppgift) och starta tiden (check in)
- Avsluta uppgiften (checkout)
- Får statistik om hur många minuter man har spenderat med tasken på en vecka.

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

## Hämta veckans task:
  # GET http://localhost:8080/api/tasks/week
  
## Se veckans statistik
  # GET http://localhost:8080/api/tasks/stats/week
  
