# Crypto Offline App

**Crypto Offline App** es una aplicación de Android moderna construida con Kotlin y Jetpack Compose, diseñada para monitorear los precios de las criptomonedas. 
Su arquitectura permite consultar datos en tiempo real de la API de CoinGecko, almacenarlos localmente para un funcionamiento sin conexión y mantener
los precios actualizados en segundo plano.

## Funcionalidades Implementadas

- **Visualización de Mercado:** Muestra una lista de las principales criptomonedas, incluyendo su precio actual, ícono y cambio de precio en las últimas 24 horas.
- **Caché Local:** La aplicación guarda los datos de las monedas en una base de datos local (Room), permitiendo que la app sea funcional incluso sin conexión a internet, mostrando la última información conocida.
- **Sistema de Favoritos:** Permite a los usuarios marcar y desmarcar monedas como favoritas, y esta selección persiste entre sesiones.
- **Actualización en Segundo Plano:** Utiliza `WorkManager` para ejecutar una tarea periódica que actualiza los precios de las monedas favoritas, incluso cuando la aplicación está cerrada.
- **Selección de Divisa:** El usuario puede cambiar la moneda de cotización (USD/MXN) a través de un menú en la pantalla principal, y esta preferencia se guarda localmente usando `DataStore`.

### Componentes Principales:

*   **Lenguaje:** [Kotlin]
*   **UI:** [Jetpack Compose] para toda la interfaz de usuario, utilizando `LazyColumn` para listas eficientes.
*   **Arquitectura:** MVVM (Model-View-ViewModel).
*   **Inyección de Dependencias:** [Hilt] para gestionar las dependencias en toda la aplicación, incluyendo ViewModels y Workers.
*   **Networking:**
    *   [Retrofit]: Para consumir de forma declarativa la API REST de CoinGecko.
    *   [Moshi]: Para el parseo eficiente de JSON a objetos Kotlin (con soporte para KSP).
*   **Persistencia de Datos:**
    *   [Room (SQLite)]: Para crear y gestionar la base de datos local que sirve como caché y para guardar los favoritos.
    *   [DataStore]: Para almacenar preferencias simples del usuario (como la divisa seleccionada) de forma asíncrona.
*   **Tareas en Segundo Plano:** [WorkManager] para programar la actualización periódica de los precios.
*   **Carga de Imágenes:** [Coil] para cargar y cachear los íconos de las criptomonedas de forma asíncrona.
*   **Procesamiento de Anotaciones:** [KSP (Kotlin Symbol Processing)] para un rendimiento de compilación más rápido con Hilt, Room y Moshi.


