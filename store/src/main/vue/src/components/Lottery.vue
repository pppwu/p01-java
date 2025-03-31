<!-- src/components/Lottery.vue -->
<template>
  <div>
    <h1>Lottery Spinner</h1>
    <button @click="startLottery">Start Lottery</button>
    <p v-if="lotteryResult">{{ lotteryResult }}</p>
  </div>
</template>

<script>
export default {
  data() {
    return {
      lotteryResult: null
    };
  },
  methods: {
    startLottery() {
      const prizes = ['Prize 1', 'Prize 2', 'Prize 3', 'Thank you for participating'];
      const randomIndex = Math.floor(Math.random() * prizes.length);
      this.lotteryResult = prizes[randomIndex];
    }
  }
};
</script>

<style scoped>
button {
  padding: 10px 20px;
  font-size: 16px;
  cursor: pointer;
}
</style>
