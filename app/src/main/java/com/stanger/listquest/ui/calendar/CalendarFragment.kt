package com.stanger.listquest.ui.calendar


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.stanger.listquest.BR
import com.stanger.listquest.R
import com.stanger.listquest.data.models.MainQuestModel
import com.stanger.listquest.databinding.DialogCalendarBinding
import com.stanger.listquest.utils.CreateQuestListener
import kotlinx.android.synthetic.main.dialog_calendar.*
import java.util.*

class CalenderFragment(val callback: CreateQuestListener,
                       private val bossInt: Int,
                       private val dateText: String,
                       private val timeText: String,
                       private val isEditDialog: Boolean,
                       private var editMainQuestModel: MainQuestModel,
                       private val oldName: String
): DialogFragment(){
    private lateinit var viewModel: CalenderFragmentViewModel

    lateinit var binding: DialogCalendarBinding

    fun newInstance(): CalenderFragment {
        val frag = CalenderFragment(callback, bossInt, dateText, timeText, isEditDialog, editMainQuestModel, oldName)
        val args = Bundle()
        frag.arguments = args

        return frag
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(arg0: Bundle?) {
        super.onActivityCreated(arg0)

        setUpViewModel()
        setUpCalendar()
        setUpTime()

        dialog!!.window!!.attributes.windowAnimations = R.style.DialogSlideAnim
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun setUpViewModel(){
        viewModel = ViewModelProviders.of(this,
            CalenderFragmentViewModelFactory(callback, context!!, fragmentManager!!, dialog!!,bossInt, dateText, timeText, isEditDialog, editMainQuestModel, oldName)
        ).get(CalenderFragmentViewModel::class.java)
        binding.setVariable(BR.viewModel, viewModel)
    }

    @SuppressLint("SimpleDateFormat")
    private fun setUpCalendar(){

        calendar_view.minDate = Calendar.getInstance().timeInMillis

        calendar_view.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val newMonth = month + 1

            viewModel.calendarString.value = "$newMonth/$dayOfMonth/$year"
        }
    }

    private fun checkIfPM(num: Int): Int {
        if(num ==0) return 12
        if(num > 12) return num - 12
        return num
    }

    private fun displayAM_PM(num: Int): String{
        var pm = "AM"
        if(num >= 12){
            pm = "PM"
        }
        return pm
    }

    private fun checkIfMinIsSingleDigit(min: String): String{
        if(min.length == 1 ){
            return "0$min"
        }
        return min
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.M)
    fun setUpTime(){
        viewModel.currentTime = checkIfPM(timePicker.hour).toString() + ":" + checkIfMinIsSingleDigit(timePicker.minute.toString()) + " " + displayAM_PM(timePicker.hour)

        timePicker.setOnTimeChangedListener{ _, hour, minute ->
            viewModel.timeString.value = checkIfPM(hour).toString() + ":" +  checkIfMinIsSingleDigit(minute.toString()) + " " + displayAM_PM(hour)
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_calendar, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //KeyboardUtils.showKeyboard(edit_text_name, activity!!)
    }

    override fun onResume() {
        val params = dialog!!.window!!.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams

        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        var view = activity!!.currentFocus
        if (view == null) view = View(activity)
        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

