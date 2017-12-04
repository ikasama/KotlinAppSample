package com.badlogic.masaki.passwordmanagementsample.constants

/**
 * Created by masaki on 2017/11/05.
 */

object RequestCode {
    const val CODE_CAMERA: Int = 1
    const val CODE_GALLERY: Int = 2
    const val CODE_DELETE_CATEGORY: Int = 3
    const val CODE_DELETE_ACCOUNT: Int = 4
}

object ExtraCode {
    const val EXTRA_CATEGORY = "category"
    const val EXTRA_ACCOUNT = "account"
    const val EXTRA_CREATE_FLAG = "createFlag"
    const val EXTRA_CATEGORY_ID = "categoryId"
}

object PrefKeys {
    const val USER_CREATED = "8chWygjfarg&4gh;"
    const val IS_LOGGED_IN = "#bhtha)fb;BgG3&f"
    const val USER_ID = "Vhrq%Ug#jb}fb=ga"
    const val NICKNAME = "M48'lX2(9g%pnj4s"
}

object Image {
    const val THUMBNAIL_WIDTH = 128
    const val THUMBNAIL_HEIGHT = 128
}

enum class ErrorType {
    FIND,
    EDIT,
    DELETE
}
