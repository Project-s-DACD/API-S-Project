options(bitmapType = "cairo")

library(DBI)
library(RSQLite)
library(dplyr)
library(wordcloud)
library(RColorBrewer)
library(ggplot2)

generarGraficos <- function() {
  ruta_db <- "business-unit/datamart.db"
  ruta_out <- "business-unit/graficos"
  
  if (!dir.exists(ruta_out)) dir.create(ruta_out, recursive = TRUE)
  con <- dbConnect(RSQLite::SQLite(), ruta_db)
  flights <- dbGetQuery(con, "SELECT * FROM flights")
  names(flights) <- make.names(names(flights), unique = TRUE)
  
  top_airports <- flights %>%
    group_by(departure_airport) %>%
    summarise(cantidad = n()) %>%
    arrange(desc(cantidad)) %>%
    slice_head(n = 50)
  
  png(file.path(ruta_out, "grafico_test2.png"), width = 800, height = 600)
  wordcloud(words = top_airports$departure_airport,
            freq = top_airports$cantidad,
            min.freq = 1,
            max.words = 30,
            random.order = FALSE,
            colors = brewer.pal(8, "Dark2"),
            scale = c(3, 0.5))
  dev.off()
  
  top_airline <- flights %>%
    group_by(airline) %>%
    summarise(cantidad_airlines = n()) %>%
    arrange(desc(cantidad_airlines))
  
  png(file.path(ruta_out, "grafico_test.png"), width = 800, height = 600)
  ggplot(top_airline, aes(x = airline, y = cantidad_airlines)) +
    geom_bar(stat = "identity", fill = "steelblue") +
    labs(title = "Número de llegadas por aerolínea",
         x = "Aerolínea",
         y = "Cantidad de vuelos") +
    theme_minimal() +
    theme(axis.text.x = element_text(angle = 45, hjust = 1))
  dev.off()
  
  print("gráficos generados desde R.")
}

