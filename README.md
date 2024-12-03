# ¡MercadoLibre Test Cupon!

Este proyecto pretende presentar una solución al problema propuesto por **MercadoLibre** como prueba técnica.

### Presentación del problema

**MercadoLibre** está implementando un nuevo beneficio para los usuarios que más usan la plataforma con un cupón de cierto monto gratis que les permitirá comprar tantos items marcados como favoritos que no excedan el monto total. Para esto se está analizando construir una API que dado una lista de item_id y el monto total pueda darle la lista de items que maximice el total gastado sin excederlo.

### Diseño de la solución

Este proyecto se desarrolló para atender los requerimientos propuestos por MercadoLibre *(Reto #1 y Reto #2)* utilizando **Java 17**. La API se despliega en la nube utilizando **Railway**. La documentación de las APIs expuestas se encuentra en este [enlace](https://lucky-luck-production-651a.up.railway.app/mercado-libre/swagger-ui/index.html), y también se incluye una colección de Postman en la ruta `${root-project}/src/test/resources/postman` para probar las APIs tanto en ambiente local como en Railway.

### Arquitectura del Proyecto

El proyecto sigue una arquitectura hexagonal para segregar la lógica del requerimiento de las interacciones externas.

    com.mercadolibre.coupon
      ├── application
      ├── configuration 
      ├── crosscutting 
      ├── domain 
      ├── infrastructure 
      └── CouponApplication

### Tecnologías y herramientas empleadas

#### Desarrollo

- JAVA (JDK 17)
- IntelliJ IDE (No excluyente)

#### Gestor de dependencias

- Gradle

#### Frameworks

- Springboot
- Springboot-cloud
- Lombok
- Feign
- Resilience4j
- H2 Data base (Local env)
- MySQL Data base (Cloud env)

#### Pruebas

- JUnit5
- Postman

#### Documentación y reportes

- OpenAPI – Swagger
- JACOCO

## Requerimientos instalación

Para poder instalar y ejecutar correctamente este proyecto se debe contar con:

- Instalación JAVA JDK17 o superior
- Gradle
- Conexión a internet (Para poder conectar con el servicio de precios)
- Postman (Ejecutar pruebas de API adjuntas en el proyecto)

## Ejecución del proyecto

- Crear variable de entorno **‘SPRING_PROFILES_ACTIVE’** con valor *‘local’*
- Descargar el proyecto a su máquina local.
- Verificar instalación **Gradle**, para ello ejecutar comando *‘ .\gradlew.bat --version’* y verificar correcta instalación.
- Dirigirse a la ruta donde descargo el proyecto y ejecutar el siguiente comando *‘ .\gradlew.bat clean build’* para **compilar** y **ejecutar** **pruebas unitarias**.
- Para ver el **reporte** de las pruebas unitarias dirigirse a la ruta *${root-project}/reports/jacoco* y abrir el archivo **index.html**
- Para **ejecutar** el proyecto digite el siguiente comando *‘.\gradlew.bat bootrun ’*
- Para ver **documentación** del API digite en su navegador la siguiente ruta
    - **Local-UI**:*‘http://localhost:8080/mercadolibre/mercado-libre/swagger-ui/index.html’*
    - **Railway-UI**: *‘https://lucky-luck-production-651a.up.railway.app/mercado-libre/swagger-ui/index.html’*
    - **Local-Docs**:*‘http://localhost:8080/mercadolibre/mercado-libre/swagger-api-docs’*
    - **Railway-Docs**: *‘https://lucky-luck-production-651a.up.railway.app/mercado-libre/swagger-api-docs’*
- Ruta para verificar micro-servicio:
    - **Local**: *http://localhost:8080/mercado-libre/actuator/health*
    - **Railway**: *‘https://lucky-luck-production-651a.up.railway.app/mercado-libre/actuator/health’*
- Ruta para verificar estado de las caches implementadas:
  - **Local**: *http://localhost:8080/mercado-libre/actuator/caches*
  - **Railway**: *‘https://lucky-luck-production-651a.up.railway.app/mercado-libre/actuator/caches’*
- Ruta para verificar estado de circuitbreaker:
  - **Local**: *http://localhost:8080/mercado-libre/actuator/circuitbreaker*
  - **Railway**: *‘https://lucky-luck-production-651a.up.railway.app/mercado-libre/actuator/circuitbreaker’*
- Ruta acceder a la consola H2 Data base:
  - **Local**: *http://localhost:8080/mercado-libre/h2-console*
