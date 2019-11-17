//package com.example.listquest.data.utils
//
//import androidx.databinding.ViewDataBinding
//import androidx.lifecycle.Lifecycle
//import androidx.lifecycle.LifecycleObserver
//import androidx.lifecycle.LifecycleOwner
//import androidx.lifecycle.OnLifecycleEvent
//import kotlin.properties.ReadWriteProperty
//import kotlin.reflect.KProperty
//
//class AutoClearedLifeCycleValue<T : ViewDataBinding>(lifecycleOwner: LifecycleOwner) :
//    ReadWriteProperty<LifecycleOwner, T> {
//    private var _value: T? = null
//
//    init {
//        lifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
//            @OnLifecycleEvent(Lifecycle.Event.ON_START)
//            fun onStart() {
//                _value?.setLifecycleOwner(lifecycleOwner)
//            }
//
//            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
//            fun onStop() {
//                _value?.setLifecycleOwner(null)
//            }
//
//            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
//            fun onDestroy() {
//                _value = null
//            }
//        })
//    }
//
//    override fun getValue(thisRef: LifecycleOwner, property: KProperty<*>): T {
//        return _value ?: throw IllegalStateException(
//            "should never call auto-cleared-value get when it might not be available"
//        )
//    }
//
//    override fun setValue(thisRef: LifecycleOwner, property: KProperty<*>, value: T) {
//        _value = value
//    }
//}
//
///**
// * Creates an [AutoClearedValue] associated with this lifecycleOwner.
// */
//fun <T : ViewDataBinding> LifecycleOwner.autoClearedLifeCycle() = AutoClearedLifeCycleValue<T>(this)
