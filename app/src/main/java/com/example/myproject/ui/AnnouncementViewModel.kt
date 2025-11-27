package com.example.myproject.ui

import androidx.lifecycle.*
import com.example.myproject.data.model.Announcement
import com.example.myproject.data.model.AnnouncementRequest
import com.example.myproject.data.remote.NetworkResult
import com.example.myproject.data.repository.AnnouncementRepository
import kotlinx.coroutines.launch

class AnnouncementViewModel(private val repository: AnnouncementRepository) : ViewModel() {

    // LiveData pour la liste des annonces (MyAnnouncementsActivity)
    private val _announcements = MutableLiveData<NetworkResult<List<Announcement>>?>()
    val announcements: LiveData<NetworkResult<List<Announcement>>?> = _announcements

    // LiveData pour le statut de création (NewAnnouncementActivity)
    private val _creationStatus = MutableLiveData<NetworkResult<Announcement>?>()
    val creationStatus: LiveData<NetworkResult<Announcement>?> = _creationStatus

    // État de chargement général
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading


    /**
     * Récupère la liste des annonces du marchand.
     */
    fun fetchMyAnnouncements() {
        if (_isLoading.value == true) return

        _isLoading.value = true
        _announcements.value = null // Réinitialise le dernier résultat en attendant le nouveau

        viewModelScope.launch {
            _announcements.value = repository.getMyAnnouncements()
            _isLoading.value = false
        }
    }

    /**
     * Crée une nouvelle annonce.
     */
    fun createAnnouncement(request: AnnouncementRequest) {
        if (_isLoading.value == true) return

        _isLoading.value = true
        _creationStatus.value = null // Réinitialise le dernier statut

        viewModelScope.launch {
            _creationStatus.value = repository.createAnnouncement(request)
            _isLoading.value = false
        }
    }

    /**
     * Réinitialise le statut de création après affichage du dialogue.
     */
    fun resetCreationStatus() {
        _creationStatus.value = null
    }
}

// -----------------------------------------------------------------------
// FACTORY POUR LE VIEWMODEL
// -----------------------------------------------------------------------
// Cette Factory permet d'injecter le Repository dans le ViewModel.
class AnnouncementViewModelFactory(
    private val repository: AnnouncementRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AnnouncementViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AnnouncementViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}