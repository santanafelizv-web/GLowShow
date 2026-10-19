# Casos de uso principales

## Confirmar un pedido

1. Elegir cliente y productos.
2. Validar que haya al menos una línea y que cada cantidad sea positiva y esté disponible.
3. Calcular subtotal, ITBIS = subtotal × 0.18 y total.
4. Guardar el pedido confirmado y descontar el stock en una transacción.
5. Preparar el mensaje de WhatsApp con el número dominicano del cliente.

## Cancelar un pedido confirmado

Cambiar el estado a Cancelado y devolver una sola vez las cantidades al inventario.

## Compartir por WhatsApp

Preparar un mensaje con cliente, productos, cantidades, subtotal, ITBIS y total. Si no hay WhatsApp disponible, abrir el selector para compartir con otra aplicación.
