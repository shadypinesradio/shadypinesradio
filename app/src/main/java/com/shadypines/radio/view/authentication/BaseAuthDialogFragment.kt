package com.shadypines.radio.view.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.shadypines.radio.R
import com.shadypines.radio.view.authentication.signin.SigninFragment


class BaseAuthDialogFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_base_fragment_holder_auth, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fragmentTransition(SigninFragment())
    }

    companion object{
        fun newInstance(): BaseAuthDialogFragment {
            val wDialog = BaseAuthDialogFragment()
            //We want this Dialog to be a Fragment in fact,
            //otherwise there are problems with showing another fragment, the DeviceListFragment
            wDialog.showsDialog = false
            //wDialog.setStyle(SherlockDialogFragment.STYLE_NORMAL,android.R.style.Theme_Holo_Light_Dialog);
            //We don't want to recreate the instance every time user rotates the phone
            wDialog.retainInstance = true
            //Don't close the dialog when touched outside
            wDialog.isCancelable = false
            return wDialog
        }
    }

    private fun fragmentTransition(fragment: Fragment){
        val fragmentManager: FragmentManager = childFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container_dialog, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commitAllowingStateLoss()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }
}