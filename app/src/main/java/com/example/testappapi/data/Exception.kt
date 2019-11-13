package com.example.testappapi.data

import java.lang.Exception

open class RepositoryException(msg : String?= null) : Exception(msg)

class NetworkException : RepositoryException("Don`t have connection")

class FailureException : RepositoryException("fail for the retrieve data")