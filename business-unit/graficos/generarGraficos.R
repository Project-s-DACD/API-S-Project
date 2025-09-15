options(bitmapType = "cairo")

library(DBI)
library(RSQLite)
library(dplyr)
library(tidyr)
library(wordcloud)
library(RColorBrewer)
library(ggplot2)

generarGraficos <- function() {
  ruta_db <- "business-unit/datamart.db"
  ruta_out <- "business-unit/graficos"

  if (!dir.exists(ruta_out)) dir.create(ruta_out, recursive = TRUE)

  # Connect to the database
  con <- dbConnect(RSQLite::SQLite(), ruta_db)

  # --- Flights charts ---
  flights <- dbGetQuery(con, "SELECT * FROM flights")
  names(flights) <- make.names(names(flights), unique = TRUE)

  message("Rows in flights: ", nrow(flights))
  message("Column names:")
  print(names(flights))

  # Wordcloud of departure airports
  top_airports <- flights %>%
    group_by(departure_airport) %>%
    summarise(cantidad = n()) %>%
    arrange(desc(cantidad)) %>%
    slice_head(n = 50)

  if (nrow(top_airports) > 0) {
    png(file.path(ruta_out, "grafico_test2.png"), width = 800, height = 600)
    wordcloud(words = top_airports$departure_airport,
              freq = top_airports$cantidad,
              min.freq = 1,
              max.words = 30,
              random.order = FALSE,
              colors = brewer.pal(8, "Dark2"),
              scale = c(3, 0.5))
    dev.off()
    message("Wordcloud generated: grafico_test2.png")
  } else {
    message("No departure airport data available for grafico_test2.png")
  }

  # Bar chart by airline
  if ("airline" %in% names(flights)) {
    top_airline <- flights %>%
      group_by(airline) %>%
      summarise(cantidad_airlines = n()) %>%
      arrange(desc(cantidad_airlines))

    if (nrow(top_airline) > 0) {
      png(file.path(ruta_out, "grafico_test.png"), width = 800, height = 600)
      print(
        ggplot(top_airline, aes(x = airline, y = cantidad_airlines)) +
          geom_bar(stat = "identity", fill = "steelblue") +
          labs(title = "Number of arrivals by airline",
               x = "Airline",
               y = "Number of flights") +
          theme_minimal() +
          theme(axis.text.x = element_text(angle = 45, hjust = 1))
      )
      dev.off()
      message("Airline bar chart generated: grafico_test.png")
    }
  }

  # --- Last weather chart ---
  weather <- dbGetQuery(con, "SELECT * FROM weather")
  names(weather) <- make.names(names(weather), unique = TRUE)

  last_weather <- weather %>%
    arrange(desc(ts)) %>%
    slice_head(n = 1)

  if (nrow(last_weather) > 0) {
    message("Latest weather record:")
    print(last_weather)

    # Pivot for bar chart
    weather_long <- last_weather %>%
      pivot_longer(
        cols = c(temperature, humidity, visibility, windSpeed, precipitation, cloudiness),
        names_to = "metric",
        values_to = "value"
      )

    png(file.path(ruta_out, "grafico_weather.png"), width = 800, height = 600)
    print(
      ggplot(weather_long, aes(x = metric, y = value, fill = metric)) +
        geom_bar(stat = "identity") +
        labs(title = paste("Latest weather metrics"),
             x = "Metric",
             y = "Value") +
        theme_minimal() +
        theme(axis.text.x = element_text(angle = 45, hjust = 1)) +
        scale_fill_brewer(palette = "Set2")
    )
    dev.off()
    message("Latest weather chart generated: grafico_weather.png")
  } else {
    message("No weather data available to plot.")
  }

    # --- Nuevo grafico: Delay vs Wind Speed ---
    library(data.table)

    flights_dt <- as.data.table(flights)
    weather_dt <- as.data.table(weather)

    flights_dt[, ts := as.POSIXct(ts)]
    weather_dt[, ts := as.POSIXct(ts)]

    flights_dt[, ts_hour := as.POSIXct(format(ts, "%Y-%m-%d %H:00:00"))]
    weather_dt[, ts_hour := as.POSIXct(format(ts, "%Y-%m-%d %H:00:00"))]

    setorder(flights_dt, ts_hour)
    setorder(weather_dt, ts_hour)

    delay_dt <- weather_dt[flights_dt, on = "ts_hour", roll = "nearest", allow.cartesian = TRUE]

    delay <- delay_dt %>%
      mutate(windSpeed_round = round(windSpeed, 0)) %>%
      group_by(windSpeed_round) %>%
      summarise(delay_avg = mean(departure_delay, na.rm = TRUE)) %>%
      arrange(desc(delay_avg))

    print(delay)

    if (nrow(delay) > 0) {
      png(file.path(ruta_out, "grafico_weathervsdelay.png"), width = 800, height = 600)
      print(
        ggplot(delay, aes(x = windSpeed_round, y = delay_avg, fill = windSpeed_round)) +
          geom_bar(stat = "identity") +
          labs(title = "Delay related to Wind Speed",
               x = "Wind Speed (rounded)",
               y = "Average Departure Delay (minutes)") +
          theme_minimal() +
          theme(axis.text.x = element_text(angle = 45, hjust = 1)) +
          scale_fill_gradient(low = "lightblue", high = "darkblue")
      )
      dev.off()
      message("Latest weather chart generated: grafico_weathervsdelay.png")
    } else {
      message("No data available to plot Delay vs Wind.")
    }


  dbDisconnect(con)
  print("Graphs generated from R.")
}

generarGraficos()
