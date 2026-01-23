# Crypto Offline App

**Crypto Offline App** es una aplicación de Android construida con Kotlin y Jetpack Compose, diseñada para monitorear los precios de las criptomonedas. 
Su arquitectura permite consultar datos en tiempo real de la API de CoinGecko, almacenarlos localmente para un funcionamiento sin conexión y mantener
los precios actualizados en segundo plano.

## Demostración

<table>
  <tr>
    <td><b>Lista de Monedas</b></td>
    <td><b>Cambio de Divisa (USD/MXN)</b></td>
    <td><b>Gráficos de Tendencia</b></td>
  </tr>
  <tr>
    <td><img width="300" alt="Screenshot Lista de Monedas" src="https://github.com/user-attachments/assets/9c08dd4c-950f-49d1-8f41-3b04ee21e1ee" /></td>
    <td><img width="300" alt="Screenshot Detalle de Gráfico" src="https://github.com/user-attachments/assets/e9450cd0-daa1-4a6e-af3c-cdc91a1f37a1" /></td>
    <td><img width="300" alt="Screenshot Cambio de Divisa" src="https://github.com/user-attachments/assets/6c640d54-51ac-483f-9e65-19424a9b59c6" /></td>
  </tr>
</table>

## Funcionalidades Implementadas

-   **Visualización de Mercado:** Muestra una lista de las principales criptomonedas con su ícono, precio, par de divisas y cambio porcentual en 24h.
-   **Gráficos de Tendencia (Sparklines):** Cada fila de la lista incluye una pequeña gráfica dibujada con `Canvas` que visualiza la tendencia del precio de los últimos 7 días.
-   **Caché Local:** La aplicación guarda todos los datos de las monedas, incluyendo los puntos de la gráfica, en una base de datos local (Room). Esto permite un funcionamiento completo sin conexión a internet, mostrando siempre la última información conocida.
-   **Sistema de Favoritos Persistente:** Los usuarios pueden marcar monedas como favoritas. Esta selección se guarda localmente y se preserva incluso cuando los datos se actualizan desde la API.
-   **Actualización Eficiente en Segundo Plano:** Utiliza `WorkManager` para ejecutar una tarea periódica que actualiza **únicamente** los precios de las monedas marcadas como favoritas, optimizando el uso de datos y batería.
-   **Selección de Divisa:** A través de un menú, el usuario puede cambiar la moneda de cotización (USD/MXN). Esta preferencia se guarda con `DataStore` y la UI reacciona automáticamente a los cambios.
-   **Pruebas Unitarias y de UI:** El proyecto incluye pruebas para garantizar la robustez del código:
    -   **Pruebas Unitarias:** Para el `CoinRepository`, verificando que la lógica de actualización de datos y preservación de favoritos funcione correctamente.
    -   **Pruebas de UI:** Para la `MarketScreen`, asegurando que la funcionalidad de cambio de divisa se refleje correctamente en la interfaz.

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
*   **Testing:**
    *   [JUnit4]: Framework base para las pruebas.
    *   [MockK]: Para crear mocks y fakes en las pruebas unitarias.
    *   [Turbine]: Para probar `Flows` de Kotlin de manera sencilla.
    *   [Compose Test]: Para escribir pruebas de UI e interactuar con los `Composables`.
    *   [Hilt Test Support]: Para la inyección de dependencias en pruebas de instrumentación.

## Próximos Pasos

El proyecto tiene una base sólida y probada. Las próximas funcionalidades planeadas son:

1.  **Navegación:** Implementar `Navigation Compose` para moverse entre pantallas.
2.  **Pantalla de Detalles:** Crear una pantalla que se abra al hacer clic en una moneda, mostrando un gráfico histórico más grande y datos adicionales.
3.  **Pantalla de Ajustes:** Desarrollar una sección dedicada para configurar la frecuencia de actualización del `WorkManager` y gestionar otras preferencias de la app.


