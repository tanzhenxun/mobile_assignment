package com.example.project.student

import io.grpc.Deadline

data class Submission(
    val label:String?=null,
    val deadline:String?=null,
    val submission_status:String?=null,
    val submission_date:String?=null,
    val submission_id:String?=null,
    val title:String?=null,
    val abstract:String?=null
)
