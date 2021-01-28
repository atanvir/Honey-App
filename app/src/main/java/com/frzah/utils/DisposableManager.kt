package com.frzah.utils

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class DisposableManager private constructor(){
    companion object {
        private var compositeDisposable: CompositeDisposable?=null
        fun getCompositeDisposable() : CompositeDisposable{
            if(compositeDisposable==null || compositeDisposable?.isDisposed!!) compositeDisposable= CompositeDisposable()
            return compositeDisposable!!
        }

        public fun add(disposable: Disposable)=getCompositeDisposable().add(disposable)
        public fun dispose()=getCompositeDisposable().dispose()
    }
}