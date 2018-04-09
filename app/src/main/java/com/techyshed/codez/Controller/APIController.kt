package com.techyshed.codez.Controller

import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by Mj 2 on 12-Jan-18.
 */

class APIController constructor(serviceInjection: ServiceInterface): ServiceInterface {

    private val service: ServiceInterface = serviceInjection


    override fun post(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit) {

        service.post(path, params, completionHandler)

    }

    override fun getReq(path: String, completionHandler: (response: String?) -> Unit) {

        service.getReq(path, completionHandler)
    }

    override fun getArray(path: String, completionHandler: (response: JSONArray?) -> Unit) {

        service.getArray(path,completionHandler)

    }

}