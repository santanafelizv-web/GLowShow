# Arquitectura

La aplicación se organiza en tres capas:

1. **Presentación (`ui`)**: pantallas Compose y ViewModels.
2. **Dominio (`domain`)**: reglas de pedidos, stock, validaciones y modelos.
3. **Datos (`data`)**: Room, DAO, repositorios y conversiones.

Recorrido de una acción: pantalla → ViewModel → caso de uso → repositorio → Room. Una pantalla nunca consulta la base directamente.

## Entidades

Usuario, Marca, Producto, Cliente, Pedido y DetallePedido. Los productos pertenecen a una marca; los pedidos guardan su cliente, usuario y líneas de detalle.
