package com.example.profilnavigasi

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/** Data yang sudah disimpan tetap tersedia ketika Activity dibuat ulang saat rotasi. */
class ProfilViewModel : ViewModel() {
    var nama by mutableStateOf("")
        private set

    var nim by mutableStateOf("")
        private set

    var prodi by mutableStateOf("")
        private set

    fun simpan(namaBaru: String, nimBaru: String, prodiBaru: String) {
        nama = namaBaru.trim()
        nim = nimBaru.trim()
        prodi = prodiBaru.trim()
    }
}