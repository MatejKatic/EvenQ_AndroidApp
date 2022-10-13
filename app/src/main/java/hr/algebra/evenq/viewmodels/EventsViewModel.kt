package hr.algebra.evenq.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.algebra.evenq.network.Network
import hr.algebra.evenq.network.model.Event
import kotlinx.coroutines.launch



class EventsViewModel: ViewModel() {

    val eventsList = MutableLiveData<ArrayList<Event>>()

    fun getEvents(){
        viewModelScope.launch {
            eventsList.value = Network().getService().getUpcomingEvents()
        }
    }
}