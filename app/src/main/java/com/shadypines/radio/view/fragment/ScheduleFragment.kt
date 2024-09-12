package com.shadypines.radio.view.fragment

import android.app.Dialog
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.*
import com.facebook.FacebookSdk.getApplicationContext
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.shadypines.radio.R
import com.shadypines.radio.SharedPrefUtils
import com.shadypines.radio.SharedPrefUtils.readString
import com.shadypines.radio.SharedPrefUtils.write
import com.shadypines.radio.databinding.FragmentScheduleNewBinding
import com.shadypines.radio.model.schecule.Day
import com.shadypines.radio.model.schecule.Schedule
import com.shadypines.radio.view.activity.TempActivity
import com.shadypines.radio.view.fragment.adapter.ScheduleAdapter
import com.shadypines.radio.view.fragment.syncSchedule.MyViewModel
import com.shadypines.radio.view.fragment.syncSchedule.Resource
import com.shadypines.utils.Constants
import com.shadypines.utils.ProgressDialogUtils
import com.shadypines.utils.TimeUtils
import com.shadypines.utils.TimeUtils.Companion.getTimeFromTimeStamp
import com.shadypines.utils.TimeUtils.Companion.isConstrainedByTime
import com.validator.easychecker.EasyChecker
import com.validator.easychecker.exceptions.InputErrorException
import com.validator.easychecker.util.PasswordPattern
//import org.jetbrains.anko.support.v4.runOnUiThread
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [ScheduleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScheduleFragment : Fragment(), ScheduleView {

    private var _binding: FragmentScheduleNewBinding? = null
    private val binding get() = _binding!!
    private lateinit var loginDialog: Dialog
    private lateinit var signUpDialog: Dialog
    private lateinit var resetPasswordDialog: Dialog

    private var viewModel: MyViewModel? = null
    val RC_SIGN_IN = 12039
    var presenter: SchedulePresenter? = null
    var adapter: ScheduleAdapter? = null
    var schedule: Schedule? = null
    var googleSignInClient: GoogleSignInClient? = null

    private lateinit var callbackManager: CallbackManager


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentScheduleNewBinding.inflate(inflater, container, false)
        // Obtain the ViewModel instance
        viewModel = ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
        // Observe the LiveData
        viewModel!!.scheduleLiveData.observe(viewLifecycleOwner) { resource ->
            if (resource != null) {
                resource.data?.let { schedule ->
                    resource.day?.let { day ->
                        onGetScheduleSuccess(schedule, day)
                      //  ProgressDialogUtils.on().hideProgressDialog()
                    } ?: run {
                    }
                } ?: resource.error?.let {
                    // Handle error and update UI accordingly
                    onGetScheduleError()
                }
            } else {
                //Toast.makeText(activity, "1st ERROR", Toast.LENGTH_SHORT).show()
            }
        }
        // Observe the isUserLoggedOut LiveData
        viewModel!!.isUserLoggedOut.observe(viewLifecycleOwner) { isLoggedOut ->
            if (isLoggedOut) {
                // Update your UI here upon logout
                if (activity is TempActivity) {
                    (activity as TempActivity?)?.getSchedule(TimeUtils.getCurrentDayName().toUpperCase(Locale.getDefault()))
                }
                viewModel!!.resetUserLoggedOutStatus()
            }
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
       // triggerToCurrentDay()
        initRecyclerView()
        callbackManager = CallbackManager.Factory.create()
        FacebookSdk.sdkInitialize(getApplicationContext())
        presenter = SchedulePresenter(this)


    }

    private fun triggerToCurrentDay() {
        selectDay(TimeUtils.getCurrentDayName())
    }


    private fun init() {

        binding.txtMon.setOnClickListener {
            selectDay(Constants.Days.MON)
        }

        binding.txtTue.setOnClickListener {
            selectDay(Constants.Days.TUE)
        }

        binding.txtWed.setOnClickListener {
            selectDay(Constants.Days.WED)
        }

        binding.txtThu.setOnClickListener {
            selectDay(Constants.Days.THU)
        }

        binding.txtFri.setOnClickListener {
            selectDay(Constants.Days.FRI)
        }

        binding.txtSat.setOnClickListener {
            selectDay(Constants.Days.SAT)
        }

        binding.txtSun.setOnClickListener {
            selectDay(Constants.Days.SUN)
        }

    }


    private fun selectMon() {
        binding.txtMon.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.background_sechdule_days_button)
        binding.txtTue.background = null
        binding.txtWed.background = null
        binding.txtThu.background = null
        binding.txtFri.background = null
        binding.txtSat.background = null
        binding.txtSun.background = null
    }

    private fun selectWed() {
        binding.txtMon.background = null
        binding.txtTue.background = null
        binding.txtWed.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.background_sechdule_days_button)
        binding.txtThu.background = null
        binding.txtFri.background = null
        binding.txtSat.background = null
        binding.txtSun.background = null
    }

    private fun selectTue() {
        binding.txtMon.background = null
        binding.txtTue.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.background_sechdule_days_button)
        binding.txtWed.background = null
        binding.txtThu.background = null
        binding.txtFri.background = null
        binding.txtSat.background = null
        binding.txtSun.background = null
    }

    private fun selectThu() {
        binding.txtMon.background = null
        binding.txtTue.background = null
        binding.txtWed.background = null
        binding.txtThu.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.background_sechdule_days_button)
        binding.txtFri.background = null
        binding.txtSat.background = null
        binding.txtSun.background = null
    }

    private fun selectFri() {
        binding.txtMon.background = null
        binding.txtTue.background = null
        binding.txtWed.background = null
        binding.txtThu.background = null
        binding.txtFri.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.background_sechdule_days_button)
        binding.txtSat.background = null
        binding.txtSun.background = null
    }

    private fun selectSat() {
        binding.txtMon.background = null
        binding.txtTue.background = null
        binding.txtWed.background = null
        binding.txtThu.background = null
        binding.txtFri.background = null
        binding.txtSat.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.background_sechdule_days_button)
        binding.txtSun.background = null
    }

    private fun selectSun() {
        binding.txtMon.background = null
        binding.txtTue.background = null
        binding.txtWed.background = null
        binding.txtThu.background = null
        binding.txtFri.background = null
        binding.txtSat.background = null
        binding.txtSun.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.background_sechdule_days_button)
    }

    private fun getShowRunning(list: List<Day>, day: String): Int {
        var currentPlayingIndex = -1
        if (TimeUtils.Companion.getCurrentDayName().toUpperCase().equals(day)) {
            list.forEachIndexed { index, element ->
                if (isConstrainedByTime(
                        getTimeFromTimeStamp(
                            element.startTimeStamp
                        ), getTimeFromTimeStamp(element.endTimeStamp)
                    )
                ){
                    currentPlayingIndex = index
                }
            }
        }

        return currentPlayingIndex

    }

    fun selectDay(day: String) {
        when (day) {
            Constants.Days.MON -> {
                selectMon()
                //filter monday data and update the list
                if (this.schedule != null) {
                    adapter?.updateList(schedule?.data?.monday, Constants.Days.MON, presenter)
                    var index = getShowRunning(schedule?.data?.monday!!,Constants.Days.MON)
                    if (index>0){
                        binding.recyclerViewSchedule.smoothScrollToPosition(index)
                    }
                }
            }

            Constants.Days.WED -> {
                selectWed()
                //filter wed data and update the list
                if (this.schedule != null) {
                    adapter?.updateList(schedule?.data?.wednesday, Constants.Days.WED, presenter)
                    var index = getShowRunning(schedule?.data?.wednesday!!,Constants.Days.WED)
                    if (index>0){
                        binding.recyclerViewSchedule.smoothScrollToPosition(index)
                    }
                }
            }

            Constants.Days.TUE -> {
                selectTue()
                //filter tue data and update the list
                if (this.schedule != null) {
                    adapter?.updateList(schedule?.data?.tuesday, Constants.Days.TUE, presenter)
                    var index = getShowRunning(schedule?.data?.tuesday!!,Constants.Days.TUE)
                    if (index>0){
                        binding.recyclerViewSchedule.smoothScrollToPosition(index)
                    }
                }
            }

            Constants.Days.THU -> {
                selectThu()
                //filter thu data and update the list
                if (this.schedule != null) {
                    adapter?.updateList(schedule?.data?.thursday, Constants.Days.THU, presenter)
                    var index = getShowRunning(schedule?.data?.thursday!!, Constants.Days.THU)
                    if (index>0){
                        binding.recyclerViewSchedule.smoothScrollToPosition(index)
                    }
                }

            }

            Constants.Days.FRI -> {
                selectFri()
                //filter fri data and update the list
                if (this.schedule != null) {
                    adapter?.updateList(schedule?.data?.friday, Constants.Days.FRI, presenter)
                    var index = getShowRunning(schedule?.data?.friday!!, Constants.Days.FRI)
                    if (index>0){
                        binding.recyclerViewSchedule.smoothScrollToPosition(index)
                    }
                }

            }

            Constants.Days.SAT -> {
                //filter sat data and update the list
                selectSat()
                if (this.schedule != null) {
                    adapter?.updateList(schedule?.data?.saturday, Constants.Days.SAT, presenter)
                    var index = getShowRunning(schedule?.data?.saturday!!, Constants.Days.SAT)
                    if (index>0){
                        binding.recyclerViewSchedule.smoothScrollToPosition(index)
                    }
                }

            }

            Constants.Days.SUN -> {
                //filter the sun day data and update the list
                selectSun()
                if (this.schedule != null) {
                    adapter?.updateList(schedule?.data?.sunday, Constants.Days.SUN, presenter)
                    var index = getShowRunning(schedule?.data?.sunday!!, Constants.Days.SUN)
                    if (index>0){
                        binding.recyclerViewSchedule.smoothScrollToPosition(index)
                    }
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel?.scheduleLiveData?.removeObservers(viewLifecycleOwner)
    }

    fun showLoginDialog() {
        loginDialog = Dialog(requireActivity())
        loginDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        loginDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        loginDialog.setCancelable(false)
        loginDialog.setContentView(R.layout.dialog_sign_in_layout)
        val etEmail = loginDialog.findViewById<EditText>(R.id.et_email_name_sign_in)
        val etPassword = loginDialog.findViewById<EditText>(R.id.et_password_sign_in)
        etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
        val btnSignIn = loginDialog.findViewById<MaterialButton>(R.id.btn_sign_in)
        // val btnFacebookSignIn = loginDialog.findViewById<MaterialButton>(R.id.btn_fb_sign_in)
        // val btnGoogleSignIn = loginDialog.findViewById<MaterialButton>(R.id.btn_google_sign_in)
        val textSignUp = loginDialog.findViewById<TextView>(R.id.text_account_sign_up)
        val textForgotPassword =
                loginDialog.findViewById<TextView>(R.id.text_account_forgot_password)
        val btnCancel = loginDialog.findViewById<ImageView>(R.id.image_view_cancel)

        loginDialog.setOnKeyListener { arg0, keyCode, event -> // TODO Auto-generated method stub
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (loginDialog.isShowing) {
                    loginDialog.dismiss()
                }
            }
            true
        }

        textSignUp?.setOnClickListener {
            if (loginDialog.isShowing) {
                loginDialog.dismiss()
                // showSignUpDialog()
            }
        }

        btnCancel?.setOnClickListener {
            if (loginDialog.isShowing) {
                loginDialog.dismiss()
            }
        }

        btnSignIn.setOnClickListener {
            try {
                EasyChecker.validateInput(
                        requireContext(), 6, PasswordPattern.PASSWORD_PATTERN_NONE, etEmail,
                        etPassword
                )
                ProgressDialogUtils.on().showProgressDialog(requireContext())
                presenter?.signIn(
                        etEmail.text.toString(), etPassword.text.toString(),
                        TimeUtils.getCurrentDayName().toUpperCase(Locale.getDefault())
                )
            } catch (e: InputErrorException) {
                Toast.makeText(requireContext(), e.messageText, Toast.LENGTH_LONG).show()
            }
        }

        textForgotPassword?.setOnClickListener {
            if (loginDialog.isShowing) {
                loginDialog.dismiss()
                showResetPasswordDialog()
            }
        }


        /**
         * todo :  important for facebook login here...
         */
        /* btnFacebookSignIn.setOnClickListener {
             */
        /**
         * facebook sign in
         *//*
            var permissionNeeds: MutableList<String?>? = Arrays.asList("public_profile", "email")
            val manager = LoginManager.getInstance()
            manager.logInWithReadPermissions(this, permissionNeeds)
            manager.registerCallback(callbackManager,
                    object : FacebookCallback<LoginResult?> {
                        override fun onSuccess(loginResult: LoginResult?) {
                            Log.e("RESULT", loginResult.toString())
                            loginResult!!.accessToken
                            val request = GraphRequest.newMeRequest(
                                    loginResult.accessToken
                            ) { `object`, response ->
                                Log.e("FACEBOOK", response.rawResponse)
                                try {
                                    val name = `object`.getString("name")
                                    val email = `object`.getString("email")
                                    val id = `object`.getString("id")
                                    ProgressDialogUtils.on().showProgressDialog(requireActivity())
                                    presenter?.socialSignIn(email, id, name,
                                            TimeUtils.getCurrentDayName().toUpperCase(Locale.getDefault()))
                                } catch (e: JSONException) {
                                    Toast.makeText(requireContext(), e.message, Toast
                                            .LENGTH_LONG).show()
                                } catch (e: Exception) {
                                    Toast.makeText(requireContext(), e.message, Toast
                                            .LENGTH_LONG).show()
                                }
                            }
                            val parameters = Bundle()
                            parameters.putString("fields", "id,name,email")
                            request.parameters = parameters
                            request.executeAsync()
                        }
                        override fun onCancel() {
                            Toast.makeText(requireContext(), "Login Cancel", Toast.LENGTH_LONG).show()
                        }
                        override fun onError(exception: FacebookException) {
                            Toast.makeText(requireContext(), exception.message, Toast
                                    .LENGTH_LONG).show()
                        }
                    })
        }*/


        /**
         *  todo : button google sign in...
         */
        /* btnGoogleSignIn.setOnClickListener {
             */
        /**
         * google sign in
         *//*
            val signInIntent = googleSignInClient?.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }*/
        val window: Window = loginDialog.window!!
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        if (!loginDialog.isShowing) {
            loginDialog.show()
        }
    }

    fun showSignUpDialog() {
        signUpDialog = Dialog(requireActivity())
        signUpDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        signUpDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        signUpDialog.setCancelable(false)
        signUpDialog.setContentView(R.layout.dialog_sign_up_layout)

        val etFirstName = signUpDialog.findViewById<EditText>(R.id.et_first_name_sign_up)
        val etLastName = signUpDialog.findViewById<EditText>(R.id.et_last_name_sign_up)
        val etEmail = signUpDialog.findViewById<EditText>(R.id.et_email_sign_up)
        val etPassword = signUpDialog.findViewById<EditText>(R.id.et_password_sign_up)
        etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
        val etConfirmPassword =
                signUpDialog.findViewById<EditText>(R.id.et_confirm_password_sign_up)
        etConfirmPassword.transformationMethod = PasswordTransformationMethod.getInstance()
        val btnSignUP = signUpDialog.findViewById<MaterialButton>(R.id.btn_sign_up)
        val checkBox = signUpDialog.findViewById<CheckBox>(R.id.check_box)
        val btnCancel = signUpDialog.findViewById<ImageView>(R.id.image_view_back_arrow)

        btnSignUP.setOnClickListener {
            try {
                EasyChecker.validateInput(
                        requireContext(),
                        6,
                        PasswordPattern.PASSWORD_PATTERN_NONE,
                        etFirstName,
                        etEmail,
                        etPassword,
                        etConfirmPassword
                )
                ProgressDialogUtils.on().showProgressDialog(requireContext())
                //call sign up api
                presenter?.registerUser(
                        etFirstName.text.toString(),
                        //etLastName.text.toString(),
                        etEmail.text.toString(),
                        etPassword.text.toString(),
                        etConfirmPassword.text.toString(),isSendEmail = false

                        // checkBox.isChecked
                )
            } catch (e: InputErrorException) {
                Toast.makeText(requireContext(), e.messageText, Toast.LENGTH_LONG).show()
            }
        }

        signUpDialog.setOnKeyListener { arg0, keyCode, event -> // TODO Auto-generated method stub
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (signUpDialog.isShowing) {
                    signUpDialog.dismiss()
                    showLoginDialog()
                }
            }
            true
        }

        btnCancel.setOnClickListener {
            if (signUpDialog.isShowing) {
                signUpDialog.dismiss()
                showLoginDialog()
            }
        }
        val window: Window = signUpDialog.window!!
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        if (!signUpDialog.isShowing) {
            signUpDialog.show()
        }
    }


    fun showResetPasswordDialog() {
        resetPasswordDialog = Dialog(requireActivity())
        resetPasswordDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        resetPasswordDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        resetPasswordDialog.setCancelable(false)
        resetPasswordDialog.setContentView(R.layout.dialog_reset_password_layout)
        val email = resetPasswordDialog.findViewById<EditText>(R.id.et_email_name_reset)
        val btnSubmit = resetPasswordDialog.findViewById<MaterialButton>(R.id.btn_reset_password)
        val btnBack = resetPasswordDialog.findViewById<ImageView>(R.id.image_view_back_arrow)
        btnSubmit.setOnClickListener {
            try {
                EasyChecker.validateInput(
                        requireContext(), 4, PasswordPattern.PASSWORD_PATTERN_NONE,
                        email
                )
                ProgressDialogUtils.on().showProgressDialog(requireContext())
                presenter?.resetPassword(email.text.toString())
            } catch (e: InputErrorException) {
                Toast.makeText(requireContext(), e.messageText, Toast.LENGTH_LONG).show()
            }
        }

        btnBack.setOnClickListener {
            if (resetPasswordDialog.isShowing) {
                resetPasswordDialog.dismiss()
                showLoginDialog()
            }
        }

        resetPasswordDialog.setOnKeyListener { arg0, keyCode, event -> // TODO Auto-generated method stub
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (resetPasswordDialog.isShowing) {
                    resetPasswordDialog.dismiss()
                    // showLoginDialog()
                }
            }
            true
        }

        val window: Window = resetPasswordDialog.window!!
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        if (!resetPasswordDialog.isShowing) {
            resetPasswordDialog.show()
        }
    }

    private fun initRecyclerView() {
        val list = ArrayList<Day>()
        adapter = ScheduleAdapter(list, this, presenter, requireActivity().supportFragmentManager)
        binding.recyclerViewSchedule.setHasFixedSize(true)
        binding.recyclerViewSchedule.layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerViewSchedule.adapter = adapter
    }


 fun onGetScheduleSuccess(schedule: Schedule, day: String) {
        //get current day schedule and also filter by user day click
        //ProgressDialogUtils.on().hideProgressDialog()
        // Toast.makeText(getApplicationContext(), " success click.", Toast.LENGTH_SHORT).show()
        this.schedule = schedule
        selectDay(day)
        ProgressDialogUtils.on().hideProgressDialog()
    }

 fun onGetScheduleError() {
        activity?.runOnUiThread {
            ProgressDialogUtils.on().hideProgressDialog()
            Toast.makeText(
                    requireActivity(), "Can't get schedule, try again!", Toast
                    .LENGTH_LONG
            ).show()
        }
    }


    /**
     * todo: text subscribe or error code...
     */
    override fun onSubscribeSuccess(showId: String, day: String) {
        activity?.runOnUiThread {
            ProgressDialogUtils.on().hideProgressDialog()
            Toast.makeText(
                    requireActivity(), "Subscribed to show!", Toast
                    .LENGTH_LONG
            ).show()

            try {
                val gson: Gson = Gson()
                val json: String? = readString("runnigKey")
                val obj: Day = gson.fromJson(json, Day::class.java)
                if (showId == obj.id.toString()) {
                    obj.isSubscribe = true
                    val gsonOne: String = gson.toJson(obj)
                    write("runnigKey", gsonOne)
                }
            } catch (ex: Exception) {
            }
          //  presenter!!.getSchedule(day)
            if (activity is TempActivity) {
                (activity as TempActivity?)?.getSchedule(day)
            }
        }
    }

    override fun onSubscribeError(message: String, day: String) {
        activity?.runOnUiThread {
            ProgressDialogUtils.on().hideProgressDialog()
            Toast.makeText(
                    requireActivity(), message, Toast
                    .LENGTH_LONG
            ).show()
          //  presenter!!.getSchedule(day)
            if (activity is TempActivity) {
                (activity as TempActivity?)?.getSchedule(day)
            }
        }
    }

    override fun onSignInSuccess(email: String, day: String) {
        /**
         * save email..
         */
        SharedPrefUtils.write(Constants.Preferences.EMAIL, email)
        activity?.runOnUiThread {
            ProgressDialogUtils.on().hideProgressDialog()
            if (loginDialog.isShowing) {
                loginDialog.dismiss()
            }
      /*      presenter!!.getSchedule(day)
*/
            if (activity is TempActivity) {
                (activity as TempActivity?)?.getSchedule(day)
            }
            Toast.makeText(requireContext(), "Sign-in successful!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onSignInError(message: String) {
        activity?.runOnUiThread {
            ProgressDialogUtils.on().hideProgressDialog()
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onSignUpSuccess() {
        activity?.runOnUiThread {
            ProgressDialogUtils.on().hideProgressDialog()
            Toast.makeText(requireContext(), "Sign-up successful", Toast.LENGTH_LONG).show()
            if (signUpDialog.isShowing) {
                signUpDialog.dismiss()
                showLoginDialog()
            }
        }
    }

    override fun onSignUPError(message: String) {
        activity?.runOnUiThread {
            ProgressDialogUtils.on().hideProgressDialog()
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Constants.Default.CAME_FROM_LOGIN_Schedule){
            Constants.Default.CAME_FROM_LOGIN_Schedule = false
            if (activity is TempActivity) {
                (activity as TempActivity?)?.getSchedule(TimeUtils.getCurrentDayName().toUpperCase(Locale.getDefault()))
            }
        }else{
            selectDay(TimeUtils.getCurrentDayName().toUpperCase(Locale.getDefault()))
        }
    }

    override fun onResetPasswordSuccess() {
        activity?.runOnUiThread {
            if (resetPasswordDialog.isShowing) {
                resetPasswordDialog.dismiss()
            }
            ProgressDialogUtils.on().hideProgressDialog()
            Toast.makeText(
                    requireContext(), "We sent you an email, please follow the link there " +
                    "to " +
                    "reset password!", Toast
                    .LENGTH_LONG
            ).show()
        }
    }

    override fun onResetPasswordError(message: String) {
        activity?.runOnUiThread {
            ProgressDialogUtils.on().hideProgressDialog()
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }
    }
}