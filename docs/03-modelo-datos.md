# Modelo de datos

| Entidad | Datos principales | Relación |
|---|---|---|
| Usuario | usuario, hash, sal, rol, activo | Registra pedidos |
| Marca | nombre, país, activa | Una marca agrupa productos |
| Producto | código, nombre, categoría, marca, precios, stock | Se vende en detalles de pedido |
| Cliente | nombre, teléfono, correo, dirección, documento | Tiene pedidos |
| Pedido | número, fecha, estado, subtotal, ITBIS, total | Pertenece a cliente y usuario |
| DetallePedido | pedido, producto, cantidad, precio, subtotal | Une pedido y producto |

No se borra físicamente un producto o cliente que tenga historial de pedidos: se desactiva para conservar el historial.
