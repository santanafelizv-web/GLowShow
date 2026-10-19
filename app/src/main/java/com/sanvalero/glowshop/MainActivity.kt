package com.sanvalero.glowshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import com.sanvalero.glowshop.data.ProductRepository
import com.sanvalero.glowshop.data.CustomerRepository
import com.sanvalero.glowshop.data.local.GlowShopDatabase
import com.sanvalero.glowshop.data.local.ProductEntity
import com.sanvalero.glowshop.data.local.CustomerEntity
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

private val Wine = Color(0xFF7B234F)
private val Blush = Color(0xFFFFEDF4)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = Room.databaseBuilder(applicationContext, GlowShopDatabase::class.java, "glowshop.db").build()
        val repository = ProductRepository(database.productDao())
        val customerRepository = CustomerRepository(database.customerDao())
        setContent {
            MaterialTheme(colorScheme = lightColorScheme(primary = Wine, secondary = Color(0xFFD88CAC))) {
                Surface(modifier = Modifier.fillMaxSize(), color = Blush) {
                    GlowShopScreen(repository, customerRepository)
                }
            }
        }
    }
}

@Composable
private fun GlowShopScreen(repository: ProductRepository, customerRepository: CustomerRepository) {
    val products by repository.products().collectAsState(initial = emptyList())
    val customers by customerRepository.customers().collectAsState(initial = emptyList())
    val lowStockCount = products.count { it.stock <= it.minimumStock }
    val scope = rememberCoroutineScope()
    var editing by remember { mutableStateOf<ProductEntity?>(null) }
    var showForm by remember { mutableStateOf(false) }
    var showCustomerForm by remember { mutableStateOf(false) }
    var selectedCustomer by remember { mutableStateOf<CustomerEntity?>(null) }
    var page by remember { mutableStateOf("Productos") }

    Scaffold(
        containerColor = Blush,
        floatingActionButton = {
            Button(onClick = {
                if (page == "Productos") { editing = null; showForm = true }
                else { selectedCustomer = null; showCustomerForm = true }
            }) { Text(if (page == "Productos") "+ Agregar producto" else "+ Agregar cliente") }
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 18.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("GlowShop", color = Wine, fontSize = 30.sp, fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Productos", "Clientes").forEach { tab ->
                    if (page == tab) Button(onClick = { page = tab }) { Text(tab) }
                    else OutlinedButton(onClick = { page = tab }) { Text(tab) }
                }
            }
            if (page == "Productos") {
            Text("Inventario de cosméticos", color = Color(0xFF684B5A))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                SummaryCard("Productos", products.size.toString(), Modifier.weight(1f))
                SummaryCard("Stock bajo", lowStockCount.toString(), Modifier.weight(1f))
            }
            if (products.isEmpty()) {
                Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Text("Todavía no hay productos. Pulsa «+ Agregar producto» para registrar el primero.", Modifier.padding(16.dp))
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(products, key = { it.id }) { product ->
                        ProductCard(
                            product = product,
                            onEdit = { editing = product; showForm = true },
                            onDeactivate = { scope.launch { repository.deactivate(product.id) } }
                        )
                    }
                }
            }
            } else {
                CustomerList(
                    customers = customers,
                    onEdit = { selectedCustomer = it; showCustomerForm = true },
                    onDeactivate = { scope.launch { customerRepository.deactivate(it.id) } }
                )
            }
        }
    }

    if (showForm) {
        ProductForm(
            product = editing,
            onDismiss = { showForm = false },
            onSave = { product ->
                scope.launch { repository.save(product) }
                showForm = false
            }
        )
    }
    if (showCustomerForm) {
        CustomerForm(
            customer = selectedCustomer,
            onDismiss = { showCustomerForm = false },
            onSave = { customer ->
                scope.launch { customerRepository.save(customer) }
                showCustomerForm = false
            }
        )
    }
}

@Composable
private fun CustomerList(customers: List<CustomerEntity>, onEdit: (CustomerEntity) -> Unit, onDeactivate: (CustomerEntity) -> Unit) {
    if (customers.isEmpty()) {
        Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Text("Todavía no hay clientes. Pulsa «+ Agregar cliente» para registrar el primero.", Modifier.padding(16.dp))
        }
        return
    }
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(customers, key = { it.id }) { customer ->
            Card(
                modifier = Modifier.fillMaxWidth().clickable { onEdit(customer) },
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(Modifier.padding(15.dp)) {
                    Text(customer.name, color = Wine, fontWeight = FontWeight.SemiBold, fontSize = 17.sp)
                    Text(customer.phone, color = Color.DarkGray)
                    if (customer.email.isNotBlank()) Text(customer.email)
                    TextButton(onClick = { onDeactivate(customer) }) { Text("Desactivar cliente") }
                }
            }
        }
    }
}

@Composable
private fun CustomerForm(customer: CustomerEntity?, onDismiss: () -> Unit, onSave: (CustomerEntity) -> Unit) {
    var name by remember(customer) { mutableStateOf(customer?.name.orEmpty()) }
    var phone by remember(customer) { mutableStateOf(customer?.phone.orEmpty()) }
    var email by remember(customer) { mutableStateOf(customer?.email.orEmpty()) }
    var address by remember(customer) { mutableStateOf(customer?.address.orEmpty()) }
    var document by remember(customer) { mutableStateOf(customer?.document.orEmpty()) }
    var error by remember(customer) { mutableStateOf<String?>(null) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (customer == null) "Nuevo cliente" else "Editar cliente") },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(name, { name = it }, label = { Text("Nombre") }, singleLine = true)
                OutlinedTextField(phone, { phone = it }, label = { Text("Teléfono WhatsApp") }, singleLine = true)
                OutlinedTextField(email, { email = it }, label = { Text("Correo (opcional)") }, singleLine = true)
                OutlinedTextField(address, { address = it }, label = { Text("Dirección (opcional)") }, singleLine = true)
                OutlinedTextField(document, { document = it }, label = { Text("Cédula o RNC (opcional)") }, singleLine = true)
                error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            }
        },
        confirmButton = {
            Button(onClick = {
                val normalizedPhone = com.sanvalero.glowshop.domain.usecase.NormalizeDominicanPhone()(phone)
                error = when {
                    name.isBlank() -> "Escribe el nombre del cliente."
                    normalizedPhone == null -> "Escribe un número dominicano válido (809, 829 o 849)."
                    else -> null
                }
                if (error == null) onSave(CustomerEntity(customer?.id ?: 0, name.trim(), normalizedPhone!!, email.trim(), address.trim(), document.trim()))
            }) { Text("Guardar") }
        },
        dismissButton = { OutlinedButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

@Composable
private fun SummaryCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(18.dp)) {
        Column(Modifier.padding(16.dp)) {
            Text(title, color = Color.Gray, fontSize = 13.sp)
            Spacer(Modifier.height(5.dp))
            Text(value, color = Wine, fontWeight = FontWeight.Bold, fontSize = 22.sp)
        }
    }
}

@Composable
private fun ProductCard(product: ProductEntity, onEdit: () -> Unit, onDeactivate: () -> Unit) {
    var confirmDeactivate by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onEdit),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(product.name, color = Wine, fontWeight = FontWeight.SemiBold, fontSize = 17.sp)
            Text("${product.brand} · ${product.category}", color = Color.DarkGray)
            Text("${formatMoney(product.priceCents)}  ·  Existencias: ${product.stock}")
            if (product.stock <= product.minimumStock) Text("Stock bajo", color = Wine, fontWeight = FontWeight.Bold)
            TextButton(onClick = { confirmDeactivate = true }) { Text("Desactivar producto") }
        }
    }
    if (confirmDeactivate) {
        AlertDialog(
            onDismissRequest = { confirmDeactivate = false },
            title = { Text("¿Desactivar ${product.name}?") },
            text = { Text("Dejará de aparecer en el inventario activo.") },
            confirmButton = { TextButton(onClick = { confirmDeactivate = false; onDeactivate() }) { Text("Desactivar") } },
            dismissButton = { TextButton(onClick = { confirmDeactivate = false }) { Text("Volver") } }
        )
    }
}

@Composable
private fun ProductForm(product: ProductEntity?, onDismiss: () -> Unit, onSave: (ProductEntity) -> Unit) {
    var name by remember(product) { mutableStateOf(product?.name.orEmpty()) }
    var brand by remember(product) { mutableStateOf(product?.brand.orEmpty()) }
    var category by remember(product) { mutableStateOf(product?.category.orEmpty()) }
    var price by remember(product) { mutableStateOf(product?.let { "%.2f".format(Locale.US, it.priceCents / 100.0) }.orEmpty()) }
    var stock by remember(product) { mutableStateOf(product?.stock?.toString().orEmpty()) }
    var minimum by remember(product) { mutableStateOf(product?.minimumStock?.toString().orEmpty()) }
    var error by remember(product) { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (product == null) "Nuevo producto" else "Editar producto") },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(name, { name = it }, label = { Text("Nombre") }, singleLine = true)
                OutlinedTextField(brand, { brand = it }, label = { Text("Marca") }, singleLine = true)
                OutlinedTextField(category, { category = it }, label = { Text("Categoría") }, singleLine = true)
                OutlinedTextField(price, { price = it }, label = { Text("Precio de venta (RD$)") }, singleLine = true)
                OutlinedTextField(stock, { stock = it }, label = { Text("Existencias") }, singleLine = true)
                OutlinedTextField(minimum, { minimum = it }, label = { Text("Avisar cuando queden") }, singleLine = true)
                error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            }
        },
        confirmButton = {
            Button(onClick = {
                val priceValue = price.replace(',', '.').toBigDecimalOrNull()
                val stockValue = stock.toIntOrNull()
                val minimumValue = minimum.toIntOrNull()
                error = when {
                    name.isBlank() || brand.isBlank() || category.isBlank() -> "Completa nombre, marca y categoría."
                    priceValue == null || priceValue < java.math.BigDecimal.ZERO -> "Escribe un precio válido."
                    stockValue == null || stockValue < 0 -> "Escribe existencias válidas."
                    minimumValue == null || minimumValue < 0 -> "Escribe un mínimo válido."
                    else -> null
                }
                if (error == null) {
                    val cents = priceValue!!.movePointRight(2).setScale(0, java.math.RoundingMode.HALF_UP).longValueExact()
                    onSave(ProductEntity(product?.id ?: 0, name.trim(), brand.trim(), category.trim(), cents, stockValue!!, minimumValue!!))
                }
            }) { Text("Guardar") }
        },
        dismissButton = { OutlinedButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

private fun formatMoney(cents: Long): String {
    val format = NumberFormat.getCurrencyInstance(Locale("es", "DO"))
    return format.format(cents / 100.0)
}
