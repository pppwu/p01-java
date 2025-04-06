<template>
  <div class="header">
    <h1>Q1. Calculator</h1>
    <router-link to="/">
      <button>Home</button>
    </router-link>
  </div>

  <div id="q1-container">
    <div class="calculator">
      <input type="text" class="result" :value="result" readonly />

      <div class="buttons">
        <button class="number" @click="handleClick('7')">7</button>
        <button class="number" @click="handleClick('8')">8</button>
        <button class="number" @click="handleClick('9')">9</button>
        <button class="operator" @click="handleOperatorClick('/')">/</button>

        <button class="number" @click="handleClick('4')">4</button>
        <button class="number" @click="handleClick('5')">5</button>
        <button class="number" @click="handleClick('6')">6</button>
        <button class="operator" @click="handleOperatorClick('*')">*</button>

        <button class="number" @click="handleClick('1')">1</button>
        <button class="number" @click="handleClick('2')">2</button>
        <button class="number" @click="handleClick('3')">3</button>
        <button class="operator" @click="handleOperatorClick('-')">-</button>

        <button class="number" @click="handleClick('0')">0</button>
        <button class="number" @click="handleClick('.')">.</button>
        <button class="number" @click="handleClick('00')">00</button>
        <button class="operator" @click="handleOperatorClick('+')">+</button>

        <button class="clear" @click="handleClear">C</button>
        <button class="clear" @click="handleClearEntry">CE</button>
        <button class="equal" @click="calculate">=</button>
      </div>
    </div>

    <div class="calculate-history">
      <h3>History</h3>
      <div class="reset-buttons">
        <button class="undo" @click="undo">Undo</button>
        <button class="redo" @click="redo">Redo</button>
      </div>
      <div class="history-list">
        <ul>
          <li v-for="(item, index) in [...history].reverse()" :key="index">{{ item }}</li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const result = ref('')
const calculated = ref(false)
const history = ref([])

const handleClick = (value) => {
  if (calculated.value) {
    result.value = value
    calculated.value = false
  } else {
    result.value += value
  }
}

const handleClear = () => {
  result.value = ''
  calculated.value = false
}

const handleClearEntry = () => {
  if (result.value.length > 0) {
    result.value = result.value.slice(0, -1)
    if (result.value.length === 0) {
      handleClear()
    }
  } else {
    handleClear()
  }
}

const handleOperatorClick = (operator) => {
  if (/[+*/-]$/.test(result.value)) {
    result.value = result.value.slice(0, -1) + operator
  } else {
    result.value += operator
  }
  calculated.value = false
}

const calculate = async () => {
  if (/[+*/-]$/.test(result.value)) {
    result.value = result.value.slice(0, -1)
  }

  try {
    const response = await axios.post(`http://localhost:8080/calculator/calculate`, {
      expression: result.value
    })
    result.value = response.data.value
    calculated.value = true
    fetchHistory()
  } catch (error) {
    console.error(error)
    result.value = 'Error'
  }
}

const undo = async () => {
  try {
    const response = await axios.get(`http://localhost:8080/calculator/history/undo`)
    if (response.data?.value !== undefined) {
      result.value = response.data.value.toString()
    }
    fetchHistory()
  } catch (error) {
    console.error('Undo failed:', error)
  }
}

const redo = async () => {
  try {
    const response = await axios.get(`http://localhost:8080/calculator/history/redo`)
    if (response.data?.value !== undefined) {
      result.value = response.data.value.toString()
    }
    fetchHistory()
  } catch (error) {
    console.error('Redo failed:', error)
  }
}

const fetchHistory = async () => {
  try {
    const response = await axios.get(`http://localhost:8080/calculator/history`)
    history.value = response.data.value || []
  } catch (error) {
    console.error('Fetching history failed:', error)
  }
}

onMounted(() => {
  fetchHistory()
})
</script>

<style src="@/assets/custom-styles.css" />
