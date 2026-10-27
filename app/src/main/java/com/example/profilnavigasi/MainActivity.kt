package com.example.profilnavigasi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

private object Rute {
    const val BERANDA = "beranda"
    const val PROFIL = "profil"
    // Route ini adalah layar detail profil sekaligus detail mata kuliah.
    const val MATKUL = "matakuliah"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ProfilNavigasiApp()
            }
        }
    }
}

@Composable
private fun ProfilNavigasiApp(vm: ProfilViewModel = viewModel()) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Rute.BERANDA,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(Rute.BERANDA) {
            BerandaScreen(onBukaProfil = { navController.navigate(Rute.PROFIL) })
        }
        composable(Rute.PROFIL) {
            FormProfilScreen(
                vm = vm,
                onSimpan = { navController.navigate(Rute.MATKUL) },
                onKembali = { navController.popBackStack() }
            )
        }
        composable(Rute.MATKUL) {
            DetailProfilScreen(
                vm = vm,
                onKembaliKeForm = { navController.popBackStack() }
            )
        }
    }
}

@Composable
private fun BerandaScreen(onBukaProfil: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Beranda Profil Mahasiswa", style = MaterialTheme.typography.headlineSmall)
        Text("Aplikasi ini memiliki tiga layar: Beranda, Form Profil, dan Detail Profil.")
        Button(onClick = onBukaProfil) {
            Text("Isi Profil")
        }
    }
}

@Composable
private fun FormProfilScreen(
    vm: ProfilViewModel,
    onSimpan: () -> Unit,
    onKembali: () -> Unit
) {
    // rememberSaveable menjaga isian sementara di layar Form ketika rotasi.
    // Profil yang sudah disimpan dikelola oleh ProfilViewModel.
    var namaInput by rememberSaveable(vm.nama) { mutableStateOf(vm.nama) }
    var nimInput by rememberSaveable(vm.nim) { mutableStateOf(vm.nim) }
    var prodiInput by rememberSaveable(vm.prodi) { mutableStateOf(vm.prodi) }
    val semuaValid = namaInput.isNotBlank() && nimInput.isNotBlank() && prodiInput.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Form Profil", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = namaInput,
            onValueChange = { namaInput = it },
            label = { Text("Nama") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = nimInput,
            onValueChange = { nimInput = it },
            label = { Text("NIM") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = prodiInput,
            onValueChange = { prodiInput = it },
            label = { Text("Program Studi") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        if (!semuaValid) {
            Text(
                text = "Nama, NIM, dan Program Studi wajib diisi.",
                color = MaterialTheme.colorScheme.error
            )
        }

        Button(
            onClick = {
                vm.simpan(namaInput, nimInput, prodiInput)
                onSimpan()
            },
            enabled = semuaValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Simpan dan Lihat Detail")
        }
        Button(onClick = onKembali, modifier = Modifier.fillMaxWidth()) {
            Text("Kembali ke Beranda")
        }
    }
}

@Composable
private fun DetailProfilScreen(
    vm: ProfilViewModel,
    onKembaliKeForm: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Detail Profil Mahasiswa", style = MaterialTheme.typography.headlineSmall)

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text("Profil Tersimpan", style = MaterialTheme.typography.titleMedium)
                Text("Nama: ${vm.nama}")
                Text("NIM: ${vm.nim}")
                Text("Program Studi: ${vm.prodi}")
            }
        }

        // Bagian Mata Kuliah menyatukan kebutuhan kedua panduan Praktikum 2.
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text("Mata Kuliah", style = MaterialTheme.typography.titleMedium)
                Text("Pemrograman Aplikasi Bergerak — Tugas Mandiri")
                Text("Topik: Jetpack Compose, Navigation, dan ViewModel")
            }
        }

        Button(onClick = onKembaliKeForm, modifier = Modifier.fillMaxWidth()) {
            Text("Kembali ke Form")
        }
    }
}