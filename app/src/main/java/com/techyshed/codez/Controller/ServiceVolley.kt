package com.techyshed.codez.Controller

/**
 * Created by Mj 2 on 12-Jan-18.
 */

import android.util.Log
import android.widget.Toast

import com.android.volley.AuthFailureError
import com.android.volley.Request

import com.android.volley.Response

import com.android.volley.VolleyLog
import com.android.volley.toolbox.JsonArrayRequest

import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import org.json.JSONArray

import org.json.JSONObject

import java.util.HashMap



class ServiceVolley : ServiceInterface {

    val TAG = ServiceVolley::class.java.simpleName

    //val basePath = "https://www.techyshed.com/"


    override fun getArray(path: String, completionHandler: (response: JSONArray?) -> Unit) {

            val jsonObjReq = object : JsonArrayRequest(Method.GET, path, null,

                    Response.Listener<JSONArray> { response ->

                        Log.i(TAG, "/post request OK! Response: $response")

                        completionHandler(response)

                    },

                    Response.ErrorListener { error ->

                        //Log.i(TAG, "/post request fail! Error: ${error.message}")

                        completionHandler(null)

                    }) {

                @Throws(AuthFailureError::class)

                override fun getHeaders(): Map<String, String> {

                    val headers = HashMap<String, String>()

                    headers.put("Content-Type", "application/json")

                    return headers

                }

            }



            BackendVolley.instance?.addToRequestQueue(jsonObjReq, TAG)

        }

    override fun post(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit) {

        val jsonObjReq = object : JsonObjectRequest(Method.POST, path, params,

                Response.Listener<JSONObject> { response ->

                    //Log.i(TAG, "/post request OK! Response: $response")

                    completionHandler(response)

                },

                Response.ErrorListener { error ->

                    //VolleyLog.e(TAG, "/post request fail! Error: ${error.message}")

                    completionHandler(null)

                }) {

            @Throws(AuthFailureError::class)

            override fun getHeaders(): Map<String, String> {

                val headers = HashMap<String, String>()

                headers.put("Content-Type", "application/json")

                return headers

            }

        }



        BackendVolley.instance?.addToRequestQueue(jsonObjReq, TAG)

    }


    override fun getReq(path: String, completionHandler: (response: String?) -> Unit) {

        val jsonObject = object : StringRequest(Request.Method.GET, path,
                Response.Listener<String> { response ->

                    //Log.i(TAG, "get request OK! Response: $response")

                    completionHandler(response)

                }, Response.ErrorListener { error ->

                //Log.i(TAG, "get request fail! Error: ${error.message}")

                completionHandler(null)
        }) {

            @Throws(AuthFailureError::class)

            override fun getHeaders(): Map<String, String> {

                val headers = HashMap<String, String>()

                headers.put("Content-Type", "application/json")

                return headers

            }

        }
        BackendVolley.instance?.addToRequestQueue(jsonObject, TAG)

    }
}