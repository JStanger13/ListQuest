package com.example.listquest.ui.dialogs.calendar

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
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.example.listquest.BR
import com.example.listquest.R
import com.example.listquest.databinding.CustomFragmentCalenderBinding
import com.example.listquest.ui.mainquest.create_main_quest.CreateMainDialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.custom_fragment_calender.*
import java.text.SimpleDateFormat
import java.util.*


class CalenderFragment(val callback: CreateMainDialogFragment.CreateQuestListener,
                       private val bossInt: Int,
                       private val dateText: String,
                       private val timeText: String): DialogFragment(){
    private lateinit var viewModel: CalenderFragmentViewModel

    lateinit var binding: CustomFragmentCalenderBinding

    override fun getTheme(): Int {
        return R.style.AppAlertTheme
    }

    fun newInstance(): CalenderFragment {
        val frag = CalenderFragment(callback, bossInt, dateText, timeText)
        val args = Bundle()
        frag.arguments = args
        frag.isCancelable = false

        return frag
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(arg0: Bundle?) {
        super.onActivityCreated(arg0)

        setUpViewModel()
        setUpCalendar()
        setUpTime()

        dialog.window!!
            .attributes.windowAnimations = R.style.DialogSlideAnim
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
    
    private fun setUpViewModel(){
        viewModel = ViewModelProviders.of(this,
            CalenderFragmentViewModelFactory(callback, context!!, fragmentManager!!, dialog,bossInt, dateText, timeText)
        ).get(CalenderFragmentViewModel::class.java)
        binding.setVariable(BR.viewModel, viewModel)
    }

    private fun setUpCalendar(){

        calendar_view.minDate = Calendar.getInstance().timeInMillis


        calendar_view.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val newMonth = month + 1
            // Note that months are indexed from 0. So, 0 means January, 1 means february, 2 means march etc.
            //animateText()

            viewModel.calendarString.value = "$dayOfMonth/$newMonth/$year"
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

        //time_txt.text = selectedTime

        timePicker.setOnTimeChangedListener{ _, hour, minute ->
            viewModel.timeString.value = checkIfPM(hour).toString() + ":" +  checkIfMinIsSingleDigit(minute.toString()) + " " + displayAM_PM(hour)
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.custom_fragment_calender, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //KeyboardUtils.showKeyboard(edit_text_name, activity!!)
    }

    override fun onResume() {
        val params = dialog.window!!.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window!!.attributes = params as WindowManager.LayoutParams

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

