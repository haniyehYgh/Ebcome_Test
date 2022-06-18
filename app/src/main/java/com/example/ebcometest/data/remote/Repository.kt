package com.example.ebcometest.data.remote

import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class Repository @Inject constructor(
    val remoteRepository: RemoteRepository,

)