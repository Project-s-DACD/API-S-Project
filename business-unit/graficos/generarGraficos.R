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

  message("Filas en flights: ", nrow(flights))
  message("Nombres de columnas:")
  print(names(flights))

  # === Wordcloud de aeropuertos de salida ===
  top_airports <- flights %>%
    group_by(departure_airport) %>%
    summarise(cantidad = n()) %>%
    arrange(desc(cantidad)) %>%
    slice_head(n = 50)

  if (nrow(top_airports) > 0) {
    message("Top aeropuertos:")
    print(top_airports)

    png(file.path(ruta_out, "grafico_test2.png"), width = 800, height = 600)
    wordcloud(words = top_airports$departure_airport,
              freq = top_airports$cantidad,
              min.freq = 1,
              max.words = 30,
              random.order = FALSE,
              colors = brewer.pal(8, "Dark2"),
              scale = c(3, 0.5))
    dev.off()
    message("Wordcloud generado: grafico_test2.png")
  } else {
    message("No hay datos de aeropuertos para grafico_test2.png")
  }

  # === Gráfico de barras por aerolínea ===
  if ("airline" %in% names(flights)) {
    top_airline <- flights %>%
      group_by(airline) %>%
      summarise(cantidad_airlines = n()) %>%
      arrange(desc(cantidad_airlines))

    if (nrow(top_airline) > 0) {
      message("Top aerolíneas:")
      print(top_airline)

      png(file.path(ruta_out, "grafico_test.png"), width = 800, height = 600)
      print(
        ggplot(top_airline, aes(x = airline, y = cantidad_airlines)) +
          geom_bar(stat = "identity", fill = "steelblue") +
          labs(title = "Número de llegadas por aerolínea",
               x = "Aerolínea",
               y = "Cantidad de vuelos") +
          theme_minimal() +
          theme(axis.text.x = element_text(angle = 45, hjust = 1))
      )
      dev.off()
      message("Gráfico de aerolíneas generado: grafico_test.png")
    } else {
      message("No hay datos de aerolíneas para grafico_test.png")
    }
  } else {
    message("Columna 'airline' no encontrada en la tabla flights.")
  }

  print("Gráficos generados desde R.")
}

generarGraficos()
