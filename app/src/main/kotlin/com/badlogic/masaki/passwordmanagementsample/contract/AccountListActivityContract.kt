package com.badlogic.masaki.passwordmanagementsample.contract

import android.os.Bundle
import com.badlogic.masaki.passwordmanagementsample.room.pojo.AccountImage

/**
 * Created by masaki on 2017/11/25.
 */
interface AccountListActivityContract {
    interface View {
        fun initViews(savedInstanceState: Bundle?)
        fun replaceDetailFragment(account: AccountImage)
        fun startAccountDetailActivity(account: AccountImage)
    }
}