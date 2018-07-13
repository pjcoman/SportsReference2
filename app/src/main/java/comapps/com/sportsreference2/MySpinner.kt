package comapps.com.sportsreference2

import android.content.Context
import android.util.AttributeSet
import android.util.Log


internal class MySpinner : android.support.v7.widget.AppCompatSpinner {


    constructor(context: Context) : super(context) {
        // TODO Auto-generated constructor stub
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun setSelection(position: Int, animate: Boolean) {
        ignoreOldSelectionByReflection()
        super.setSelection(position, animate)
    }

    private fun ignoreOldSelectionByReflection() {
        try {
            val c = this.javaClass.superclass.superclass.superclass
            val reqField = c.getDeclaredField("mOldSelectedPosition")
            reqField.isAccessible = true
            reqField.setInt(this, -1)
        } catch (e: Exception) {
            Log.d("Exception Private", "ex", e)
            // TODO: handle exception
        }

    }

    override fun setSelection(position: Int) {
        ignoreOldSelectionByReflection()
        super.setSelection(position)
    }

}