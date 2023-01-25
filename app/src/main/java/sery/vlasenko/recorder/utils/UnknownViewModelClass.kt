package sery.vlasenko.recorder.utils

class UnknownViewModelClass(viewModelClass: String):
    ClassNotFoundException("Unknown view model class $viewModelClass")