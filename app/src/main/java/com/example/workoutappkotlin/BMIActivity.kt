package com.example.workoutappkotlin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.workoutappkotlin.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    companion object {
        private const val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW" // Metric Unit View
        private const val US_UNITS_VIEW = "US_UNIT_VIEW" // US Unit View
    }

    private var binding: ActivityBmiBinding? = null
    private var currentVisibleView: String = METRIC_UNITS_VIEW


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbarBmiActivity)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if(supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "CALCULATE BMI"
        }
        binding?.toolbarBmiActivity?.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding?.btnCalculateUnits?.setOnClickListener {
            if (currentVisibleView == METRIC_UNITS_VIEW) {
                calculateMetricUnits()
            } else {
                calculateUsUnits()
            }
        }

        makeVisibleMetricUnitsView()

        binding?.rgUnits?.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbMetricUnits) {
                makeVisibleMetricUnitsView()
            } else {
                makeVisibleUsUnitsView()
            }
        }
    }

    private fun calculateUsUnits() {
        if(validateUsUnits()) {
            val feetValue: Float = binding?.etUsUnitFeet?.text.toString().toFloat()
            val inchValue: Float =  binding?.etUsUnitInch?.text.toString().toFloat()
            val heightValue: Float = (feetValue * 12) + inchValue
            val weightValue: Float = binding?.etUsUnitWeight?.text.toString().toFloat()

            val bmi = (weightValue / (heightValue*heightValue)) * 703
            displayBMIResult(bmi)

        } else {
            Toast.makeText(this@BMIActivity, "Please enter valid values.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun calculateMetricUnits() {
        if(validateMetricUnits()) {
            val heightValue: Float = binding?.etMetricUnitHeight?.text.toString().toFloat() / 100
            val weightValue: Float = binding?.etMetricUnitWeight?.text.toString().toFloat()

            val bmi = weightValue / (heightValue*heightValue)
            displayBMIResult(bmi)

        } else {
            Toast.makeText(this@BMIActivity, "Please enter valid values.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun makeVisibleMetricUnitsView() {
        currentVisibleView = METRIC_UNITS_VIEW
        binding?.llMetricUnits?.visibility = View.VISIBLE
        binding?.llUsUnits?.visibility = View.INVISIBLE

        binding?.etMetricUnitHeight?.text!!.clear()
        binding?.etMetricUnitWeight?.text!!.clear()
        binding?.llDisplayBMIResult?.visibility = View.INVISIBLE
    }

    private fun makeVisibleUsUnitsView() {
        currentVisibleView = US_UNITS_VIEW
        binding?.llUsUnits?.visibility = View.VISIBLE
        binding?.llMetricUnits?.visibility = View.INVISIBLE

        binding?.etUsUnitWeight?.text!!.clear()
        binding?.etUsUnitFeet?.text!!.clear()
        binding?.etUsUnitInch?.text!!.clear()
        binding?.llDisplayBMIResult?.visibility = View.INVISIBLE
    }

    private fun displayBMIResult(bmi: Float) {

        val bmiLabel: String
        val bmiDescription: String

        if(bmi.compareTo(15f) <= 0) {
            bmiLabel = "Very Severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat More!"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat More!"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(16.5f) <= 0) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat More!"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Workout More!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take better care of yourself! Workout More!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        binding?.llDisplayBMIResult?.visibility = View.VISIBLE
        binding?.tvBMIValue?.text = bmiValue
        binding?.tvBMIType?.text = bmiLabel
        binding?.tvBMIDescription?.text = bmiDescription
    }

    private fun validateMetricUnits() : Boolean {
        var isValid = true
        if(binding?.etMetricUnitWeight?.text.toString().isEmpty() || binding?.etMetricUnitHeight?.text.toString().isEmpty()) {
            isValid = false
        }
        return isValid
    }

    private fun validateUsUnits() : Boolean {
        var isValid = true
        if(binding?.etUsUnitWeight?.text.toString().isEmpty() || binding?.etUsUnitFeet?.text.toString().isEmpty() || binding?.etUsUnitInch?.text.toString().isEmpty()){
            isValid = false
        }
        return isValid
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }
}