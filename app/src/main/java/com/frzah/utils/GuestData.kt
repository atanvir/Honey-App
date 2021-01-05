package com.frzah.utils

import com.frzah.model.request.GuestDataModel
import java.util.*

class GuestData private constructor() {

    companion object {
        private var guestData: GuestData? = null
        private var list: MutableList<GuestDataModel>? = null
        val instance: GuestData?
            get() {
                if (guestData == null) {
                    guestData = GuestData()
                    list = ArrayList()
                }
                return guestData
            }
    }

    val allData: List<GuestDataModel>? get() = list

    fun removeData(product_id: String) {
        if(list!=null) {
            for (i in 0..list!!.size - 1) {
                if (list!!.get(i).product_id.equals(product_id)) {
                    list!!.removeAt(i)
                    break
                }
            }
        }
    }

    fun addData(dataModel: GuestDataModel) {
        if (list != null) list!!.add(dataModel)
    }

    fun removeAllData() {
        if (list != null) list!!.clear()
    }

    fun updateQuantity(quanutity : Long,product_id:String)
    {
        if(list!=null) {
            for (i in 0..(list!!.size - 1)) {
                if (list!!.get(i).product_id.equals(product_id)) {
                    list!!.get(i).quanutity = quanutity
                    break
                }
            }
        }

    }

    fun getQuantity(product_id: String): Int? {
        var quanutity=0
        if(list!=null) {
            for (i in 0..(list!!.size - 1)) {
                if (list!!.get(i).product_id.equals(product_id)) {
                    quanutity= list!!.get(i).quanutity.toInt()
                    break
                }
            }
        }
        return quanutity
    }


}