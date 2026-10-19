# GlowShop - guía para abrir y ejecutar

## Qué hace esta versión

- Guarda productos y clientes en el teléfono, sin internet.
- Permite agregar, editar y desactivar productos y clientes.
- Muestra alertas de productos con pocas existencias.
- Valida números dominicanos para WhatsApp.

Esta es una primera versión funcional. Todavía faltan el flujo de pedidos dentro de la app, el envío del pedido por WhatsApp, los reportes, el inicio de sesión y los respaldos.

## 1. Instalar Android Studio

Descarga Android Studio desde [developer.android.com/studio](https://developer.android.com/studio). En el instalador acepta la configuración recomendada. El asistente instalará el SDK de Android; cuando pregunte, incluye Android SDK Platform 35. No instales Java aparte: Android Studio trae su propio Java (JDK).

## 2. Abrir GlowShop

1. En Windows, haz clic derecho en `GlowShop.zip` y selecciona **Extraer todo**.
2. Abre Android Studio.
3. En la pantalla inicial pulsa **Open** (Abrir).
4. Selecciona la carpeta extraída llamada `GlowShop` (la carpeta principal, no `app`).
5. Si pregunta si confías en el proyecto, elige **Trust Project**.
6. Espera a que termine **Gradle Sync**. La primera vez necesita internet para descargar Gradle y las bibliotecas. Acepta instalar los componentes del SDK que solicite.
7. Si solicita una versión de Gradle, selecciona **8.13**. Si aparece otro error, toma una captura y te ayudo a resolverlo.

## 3. Ejecutar en un teléfono virtual

1. En Android Studio abre **Tools > Device Manager**.
2. Pulsa **Create device**, elige un teléfono como Pixel y continúa.
3. Descarga una imagen de Android si la solicita y termina la creación.
4. Selecciona ese teléfono en la barra superior y pulsa el botón verde **Run ▶**.
5. Espera a que aparezca GlowShop en el emulador.

## 4. Ejecutar en tu teléfono Android

1. Activa **Opciones de desarrollador** y **Depuración USB** en el teléfono.
2. Conéctalo a la computadora y acepta el aviso de depuración.
3. Selecciona tu teléfono en la lista de dispositivos de Android Studio.
4. Pulsa **Run ▶**.

## 5. Probar las funciones actuales

- En **Productos**, pulsa **+ Agregar producto**. Completa nombre, marca, categoría, precio, existencias y mínimo de alerta; pulsa **Guardar**.
- Toca un producto para editarlo. **Desactivar producto** lo oculta del inventario activo.
- Abre **Clientes**, pulsa **+ Agregar cliente**, registra nombre y teléfono dominicano (809, 829 o 849) y guarda.
- Cierra y vuelve a abrir la app: los datos permanecen guardados en el teléfono.

## Si algo falla

Si Android Studio muestra una franja amarilla, pulsa **Install missing platform** o **Sync Now**. Si aparece un error rojo, toma una captura o copia el texto completo y compártelo para ayudarte a resolverlo.

La instalación del emulador requiere bastante espacio; si tu computadora va lenta, es más sencillo ejecutar la app en un teléfono Android conectado por USB.
