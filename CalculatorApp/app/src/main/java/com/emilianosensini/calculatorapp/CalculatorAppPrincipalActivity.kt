package com.emilianosensini.calculatorapp

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import java.text.DecimalFormat
import kotlin.math.pow

lateinit var btnOne: ExtendedFloatingActionButton
lateinit var btnTwo: ExtendedFloatingActionButton
lateinit var btnThree: ExtendedFloatingActionButton
lateinit var btnFour: ExtendedFloatingActionButton
lateinit var btnFive: ExtendedFloatingActionButton
lateinit var btnSix: ExtendedFloatingActionButton
lateinit var btnSeven: ExtendedFloatingActionButton
lateinit var btnEight: ExtendedFloatingActionButton
lateinit var btnNine: ExtendedFloatingActionButton
lateinit var btnZero: ExtendedFloatingActionButton
lateinit var btnPoint: ExtendedFloatingActionButton
lateinit var btnAC: ExtendedFloatingActionButton
lateinit var btnParentheses: ExtendedFloatingActionButton
lateinit var btnPower: ExtendedFloatingActionButton
lateinit var btnDivide: ExtendedFloatingActionButton
lateinit var btnMultiply: ExtendedFloatingActionButton
lateinit var btnSubtract: ExtendedFloatingActionButton
lateinit var btnAdd: ExtendedFloatingActionButton
lateinit var btnEqual: ExtendedFloatingActionButton
lateinit var btnDeleteLast: ExtendedFloatingActionButton
lateinit var tvDisplay: TextView


val expressionList = ArrayList<Char>()
var displayText = ""
val operatorsPrecedence = mapOf('+' to 1, '-' to 1, '*' to 2, '/' to 2, '^' to 3)
var unclosedParentheses = 0

class CalculatorAppPrincipalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_calculator_app_principal)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initComponents()
        initListeners()
    }

    private fun initComponents() {
        btnOne = findViewById(R.id.btnOne)
        btnTwo = findViewById(R.id.btnTwo)
        btnThree = findViewById(R.id.btnThree)
        btnFour = findViewById(R.id.btnFour)
        btnFive = findViewById(R.id.btnFive)
        btnSix = findViewById(R.id.btnSix)
        btnSeven = findViewById(R.id.btnSeven)
        btnEight = findViewById(R.id.btnEight)
        btnNine = findViewById(R.id.btnNine)
        btnZero = findViewById(R.id.btnZero)
        btnPoint = findViewById(R.id.btnPoint)
        btnAC = findViewById(R.id.btnAC)
        btnParentheses = findViewById(R.id.btnParentheses)
        btnPower = findViewById(R.id.btnPower)
        btnDivide = findViewById(R.id.btnDivide)
        btnMultiply = findViewById(R.id.btnMultiply)
        btnSubtract = findViewById(R.id.btnSubtract)
        btnAdd = findViewById(R.id.btnAdd)
        btnEqual = findViewById(R.id.btnEqual)
        btnDeleteLast = findViewById(R.id.btnDeleteLast)
        tvDisplay = findViewById(R.id.tvDisplay)
    }

    private fun initListeners() {
        btnOne.setOnClickListener {
            addCharacterToExpressionList('1')
        }
        btnTwo.setOnClickListener {
            addCharacterToExpressionList('2')
        }
        btnThree.setOnClickListener {
            addCharacterToExpressionList('3')
        }
        btnFour.setOnClickListener {
            addCharacterToExpressionList('4')
        }
        btnFive.setOnClickListener {
            addCharacterToExpressionList('5')
        }
        btnSix.setOnClickListener {
            addCharacterToExpressionList('6')
        }
        btnSeven.setOnClickListener {
            addCharacterToExpressionList('7')
        }
        btnEight.setOnClickListener {
            addCharacterToExpressionList('8')
        }
        btnNine.setOnClickListener {
            addCharacterToExpressionList('9')
        }
        btnZero.setOnClickListener {
            addCharacterToExpressionList('0')
        }
        btnAC.setOnClickListener {
            cleanDisplay()
            refreshDisplay()
            unclosedParentheses = 0
            expressionList.clear()
        }
        btnParentheses.setOnClickListener {
            addParenthesesWithContext()
        }
        btnPower.setOnClickListener {
            addOperatorToExpressionList('^')
        }
        btnDivide.setOnClickListener {
            addOperatorToExpressionList('÷')
        }
        btnMultiply.setOnClickListener {
            addOperatorToExpressionList('*')
        }
        btnSubtract.setOnClickListener {
            if (expressionList.isEmpty()) {
                addCharacterToExpressionList('-')
            } else {
                addOperatorToExpressionList('-')
            }
        }
        btnAdd.setOnClickListener {
            addOperatorToExpressionList('+')
        }
        btnEqual.setOnClickListener {
            val result = calculateExpressionList()
            showResult(result)
        }
        btnDeleteLast.setOnClickListener {
            deleteLast()
        }
        btnPoint.setOnClickListener {
            addCharacterToExpressionList('.')
        }
    }

    private fun addOperatorToExpressionList(operator: Char) {
        if (expressionList.isNotEmpty()) {
            if (isOperator(expressionList.last())) {
                deleteLast()
            }
            if (expressionList.isNotEmpty()) {
                addCharacterToExpressionList(operator)
            }
        }
    }

    private fun addParenthesesWithContext() {
        if (unclosedParentheses == 0 || isOperator(expressionList.last()) || expressionList.last() == '(') {
            if (expressionList.isNotEmpty() && isNumber(expressionList.last().toString())) {
                addCharacterToExpressionList('*')
            }
            addCharacterToExpressionList('(')
            unclosedParentheses++
        } else {
            addCharacterToExpressionList(')')
            unclosedParentheses--
        }
    }

    private fun showResult(result: String) {
        cleanDisplay()
        displayText = result
        refreshDisplay()
    }

    private fun deleteLast() {
        if (expressionList.isNotEmpty()) {
            if (expressionList.last() == '(') {
                unclosedParentheses--
            } else if (expressionList.last() == ')') {
                unclosedParentheses++
            }
            removeLast(expressionList)
            displayText = displayText.dropLast(1)
            refreshDisplay()
        }
    }

    private fun refreshDisplay() {
        tvDisplay.text = displayText
    }

    private fun cleanDisplay() {
        displayText = ""
    }

    private fun addCharacterToExpressionList(c: Char) {
        expressionList.add(c)
        displayText = displayText + c
        refreshDisplay()
    }

    private fun calculateExpressionList(): String {
        addMissingParentheses()
        val ouputQueue: MutableList<String> = convertExpresionListToReversePolish()
        val result = elvaluateReversePolishExpresion(ouputQueue)
        addDoubleToExpresionList(result)
        return formatearNumero(result)
    }

    private fun formatearNumero(numero: Double): String {
        val decimalFormat = DecimalFormat("#.###########") //
        return decimalFormat.format(numero)

    }

    private fun addMissingParentheses() {
        for (i in 1..unclosedParentheses) {
            expressionList.add(')')
        }
    }

    private fun convertExpresionListToReversePolish(): MutableList<String> {
        //Using te Shunting Yard Algorithm with unary operator detection.
        val operatorStack = mutableListOf<Char>()
        val outputQueue = mutableListOf<String>()
        var previousToken: Char? = null

        while (expressionList.isNotEmpty()) {
            var actualChar = removeFirst(expressionList)
            when {
                actualChar.isDigit() -> {
                    outputQueue.add(getNumberInExpresionList(actualChar))
                    previousToken = '0' //Indicates that the previous token was a number
                }

                isOperator(actualChar) -> {
                    // Detect unary operator: if it is '-' and it is at the beginning, or after another operator or a '('.
                    if (actualChar == '-' && (previousToken == null || isOperator(previousToken) || previousToken == '(')) {
                        //We inject a 0 to transform the unary operator into a binary subtraction.
                        outputQueue.add("0")
                    }
                    while (operatorStack.isNotEmpty() && operatorStack.last() != '('
                        && operatorsPrecedence[operatorStack.last()]!! >= operatorsPrecedence[actualChar]!!
                    ) {
                        outputQueue.add(removeLast(operatorStack).toString())
                    }
                    operatorStack.add(actualChar)
                    previousToken = actualChar
                }

                actualChar == '(' -> {
                    operatorStack.add(actualChar)
                    previousToken = actualChar
                }

                actualChar == ')' -> {
                    while (operatorStack.last() != '(') {
                        outputQueue.add(removeLast(operatorStack).toString())
                    }
                    removeLast(operatorStack) //Discard '('
                    previousToken = actualChar
                }
            }
        }
        while (operatorStack.isNotEmpty()) {
            outputQueue.add(removeLast(operatorStack).toString())
        }
        return outputQueue;
    }

    private fun isOperator(character: Char): Boolean {
        return when (character) {
            '+', '-', '÷', '*', '^' -> true
            else -> false
        }
    }

    private fun elvaluateReversePolishExpresion(outputQueue: MutableList<String>): Double {
        val auxiliarStack = mutableListOf<Double>()
        while (outputQueue.isNotEmpty()) {
            var actualString = removeFirst(outputQueue)
            when {
                isNumber(actualString) -> {
                    auxiliarStack.add(actualString.toDouble())
                }

                actualString == "+" -> {
                    var auxDouble = removeLast(auxiliarStack)
                    auxDouble += removeLast(auxiliarStack)
                    auxiliarStack.add(auxDouble)
                }

                actualString == "-" -> {
                    var auxDouble = removeLast(auxiliarStack)
                    auxDouble = removeLast(auxiliarStack) - auxDouble
                    auxiliarStack.add(auxDouble)
                }

                actualString == "÷" -> {
                    var auxDouble = removeLast(auxiliarStack)
                    auxDouble = removeLast(auxiliarStack) / auxDouble
                    auxiliarStack.add(auxDouble)
                }

                actualString == "*" -> {
                    var auxDouble = removeLast(auxiliarStack)
                    auxDouble = removeLast(auxiliarStack) * auxDouble
                    auxiliarStack.add(auxDouble)
                }

                actualString == "^" -> {
                    var auxDouble = removeLast(auxiliarStack)
                    auxDouble = (removeLast(auxiliarStack)).pow(auxDouble)
                    auxiliarStack.add(auxDouble)
                }
            }
        }
        return removeLast(auxiliarStack)
    }

    private fun isNumber(string: String): Boolean {
        return (string.toDoubleOrNull() != null)
    }

    private fun addDoubleToExpresionList(result: Double) {
        var strNumber = result.toString()
        for (character in strNumber) {
            expressionList.add(character)
        }
    }

    private fun getNumberInExpresionList(firstDigit: Char): String {
        var stringNum = firstDigit.toString()
        while (!expressionList.isEmpty() && (expressionList.first()
                .isDigit() || expressionList.first() == '.')
        ) {
            stringNum = stringNum + removeFirst(expressionList)
        }
        return stringNum;
    }

    private fun <T> removeFirst(list: MutableList<T>): T {
        return list.removeAt(0)
    }

    private fun <T> addFirst(list: MutableList<T>, element: T) {
        list.add(0, element)
    }

    private fun <T> removeLast(list: MutableList<T>): T {
        return list.removeAt(list.size - 1)
    }


}