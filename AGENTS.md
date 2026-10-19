# GlowShop project instructions

- App: Android, Kotlin, Jetpack Compose, Spanish interface, Dominican pesos (RD$).
- Minimum Android version: API 26. Use local persistence; core workflows work offline.
- Keep UI, domain rules, and data access in separate layers. UI must not call DAOs directly.
- Business rules: ITBIS is 18%; confirming an order reduces stock; cancellation restores it.
- Validate user input and preserve order history. Deactivate catalog records that are referenced by orders.
- Never commit signing keys, passwords, or local.properties.
- Explain changes in beginner-friendly Spanish and keep code names in English.
