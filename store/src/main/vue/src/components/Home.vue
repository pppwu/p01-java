<!-- src/components/Home.vue -->
<template>
  <div id="home">
    <h1>Welcome to the App</h1>
    <div class="options">
      <button @click="goToCalculator">Go to Calculator</button>
      <button @click="goToLottery">Go to Lottery</button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Home',
  methods: {
    goToCalculator() {
      this.$router.push('/q1-calculator');
    },
    goToLottery() {
      this.$router.push('/q2-lottery');
    }
  }
};
</script>

<style scoped>
#home {
  text-align: center;
  margin-top: 50px;
}

.options {
  margin-top: 30px;
}

button {
  margin: 10px;
  padding: 10px 20px;
  font-size: 16px;
  cursor: pointer;
}
</style>
