package com.example.kotlincalculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity2 : AppCompatActivity() {
private var canAddOperation = false
    private var canAddDecimal = true
    private lateinit var workingsTV: TextView
    private lateinit var resultTV: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        workingsTV = findViewById(R.id.workingsTV)
        resultTV = findViewById(R.id.resultTV)
        }
    fun numberAction(view: View) {
        if(view is Button)
        {
            if(view.text == ".")
            {
                if (canAddDecimal)
                    workingsTV.append(view.text)
                canAddDecimal = false
            }
            else
            workingsTV.append(view.text)
            canAddOperation = true
        }
    }

    fun operationAction(view: View)
    {
        if(view is Button && canAddOperation)
        {
            workingsTV.append(view.text)
            canAddOperation = false
            canAddDecimal= true
        }
    }
    fun removeAllAction(view: View)
    {
        workingsTV.text = ""
        resultTV.text = ""
    }

    fun backSpaceAction(view: View)
    {
        var length = workingsTV.length()
        if (length > 0)
            workingsTV.text = workingsTV.text.subSequence(0, length -1)
    }
    fun equalsAction(view: View)
    {
        resultTV.text = calculateResults()

    }
    private fun calculateResults(): String
    {
        val digitsOperators = digitsOperator()
        if(digitsOperators.isEmpty()) return ""

        val timesDivision = timesDivisionCalculate(digitsOperators)
        if(timesDivision.isEmpty()) return ""

        val result = addSubtractCalculate(timesDivision)
        return result.toString()

        return ""
    }

    private fun addSubtractCalculate(passedlist: MutableList<Any>): Float
    {
        var result = passedlist[0] as Float
        for(i in passedlist.indices)
        {
            if(passedlist[i] is Char && i != passedlist.lastIndex)
            {
                val operator = passedlist[i]
                val nextDigit = passedlist[i + 1] as Float
                if(operator == '+')
                    result += nextDigit
                if(operator == '-')
                    result -= nextDigit
            }
        }
            return result
    }

    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any>
    {
        var list = passedList
        while (list.contains('x') || list.contains('/'))
        {
            list = calcTimesDiv(list)

        }
        return list
    }

    private fun calcTimesDiv(passedlist: MutableList<Any>): MutableList<Any>
    {
      val newList = mutableListOf<Any>()
      var restartIndex = passedlist.size

      for(i in passedlist.indices)
      {
          if(passedlist[i] is Char && i != passedlist.lastIndex && i < restartIndex)
          {
              val operator = passedlist[i]
              val prevDigit = passedlist[i - 1] as Float
              val nextDigit = passedlist[i + 1] as Float
              when(operator)
              {
                  'x' ->
                  {
                      newList.add(prevDigit * nextDigit)
                      restartIndex = i + 1
                  }
                  '/' ->
                  {
                      newList.add(prevDigit / nextDigit)
                      restartIndex = i + 1
                  }
                  else ->
                  {
                      newList.add(prevDigit)
                      newList.add(operator)

                  }

              }
          }
          if(i > restartIndex)
              newList.add(passedlist[i])
      }

      return newList
    }


    private fun digitsOperator(): MutableList<Any>
    {
        val list = mutableListOf<Any>()
        var currentDigit= ""
        for((index, character) in workingsTV.text.withIndex()) {
            if(character.isDigit() || character == '.') {
                currentDigit += character
            }
            else if (character == '-' &&
                (index == 0 || workingsTV.text[index -1]in listOf('+','-','*','/')))
            {
                currentDigit += character
            }

            else
            {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }

        }
        if(currentDigit != "")
            list.add(currentDigit.toFloat())
        return list


    }


}


