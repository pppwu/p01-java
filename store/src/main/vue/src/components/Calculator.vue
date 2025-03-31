<template>
  <div id="app">
    <div class="calculator">
      <input type="text" class="result"
             :value="result" readonly />
      <div class="buttons">
        <button class="number"
                @click="handleClick('7')">7</button>
        <button class="number"
                @click="handleClick('8')">8</button>
        <button class="number"
                @click="handleClick('9')">9</button>
        <button class="operator"
                @click="handleOperatorClick('/')">/</button>

        <button class="number"
                @click="handleClick('4')">4</button>
        <button class="number"
                @click="handleClick('5')">5</button>
        <button class="number"
                @click="handleClick('6')">6</button>
        <button class="operator"
                @click="handleOperatorClick('*')">*</button>

        <button class="number"
                @click="handleClick('1')">1</button>
        <button class="number"
                @click="handleClick('2')">2</button>
        <button class="number"
                @click="handleClick('3')">3</button>
        <button class="operator"
                @click="handleOperatorClick('-')">-</button>

        <button class="number"
                @click="handleClick('0')">0</button>
        <button class="number"
                @click="handleClick('.')">.</button>
        <button class="number"
                @click="handleClick('00')">00</button>

        <button class="operator"
                @click="handleOperatorClick('+')">+</button>

        <button class="clear"
                @click="handleClear">C</button>
        <button class="clear"
                @click="handleClearEntry">CE</button>
        <button class="equal"
                @click="calculate">=</button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'App',
  data() {
    return {
      result: '',
      calculated: false,
      operator: '',
      a: null,
      b: null
    };
  },
  methods: {
    handleClick(value) {
      if (this.calculated) {
        // If calculation has been done, start a new expression
        this.result = value;
        this.calculated = false; // Reset flag
      } else {
        this.result += value;
      }
    },
    handleClear() {
      this.result = '';
      this.calculated = false; // Reset flag
    },
    handleClearEntry() {
      if (this.result && this.result.length > 0) {
        this.result = this.result.slice(0, -1);
        if (this.result.length === 0) {
          this.handleClear();
        }
      } else {
        this.handleClear();
      }
    },
    handleOperatorClick(operator) {
      // If the last character is an operator, replace it with the new operator
      if (/[+*/-]$/.test(this.result)) {
        this.result = this.result.slice(0, -1) + operator;
      } else {
        // Otherwise, add the new operator
        this.result += operator;
      }
      this.calculated = false; // Reset flag
    },
    async calculate() {
      if (/[+*/-]$/.test(this.result)) {
        this.result = this.result.slice(0, -1) ;
      }
      try {
        // Call the API to calculate
        const response = await axios.post(`http://localhost:8080/calculator/calculate`, {
          expression: this.result
        });

        // Set result after receiving the response
        this.result = response.data.value;
        this.calculated = true;
      } catch (error) {
        console.error(error);
        this.result = 'Error';
      }
    }
  }
};
</script>

<style src="@/assets/calculator.css">
/* Add your styles here */
</style>
