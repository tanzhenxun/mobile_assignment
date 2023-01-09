package com.example.project

import io.grpc.Deadline

data class Submission(
    val label:String?=null,
    val deadline:String?=null,
    val submission_status:String?=null,
    val submission_id:String?=null,
    val title:String?=null
)
