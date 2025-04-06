<template>
  <div class="header">
    <h1>Q2. Fortune Wheel</h1>
    <router-link to="/">
      <button>Home</button>
    </router-link>
  </div>
  <div id="q2-container">
    <div class="fortune-wheel">
      <div class="wheel-block" v-if="prizesCanvas">
        <FortuneWheel
            style="width: 500px; max-width: 100%;"
            ref="wheelEl"
            :prizes="prizesCanvas"
            :prizeId="prizeId"
            @rotateStart="onCanvasRotateStart"
            @rotateEnd="onRotateEnd"
            :disabled="spinDisable"
        />
        <div
            v-if="spinDisable"
            class="wheel-mask"
        >
          <div class="mask-message">Oops! You're out of spins.</div>
        </div>
      </div>
      <div v-if="statusData">
        <div class="remainingAttempts">
          <h2>You have <span class="attempts-left">{{statusData.remainingAttempts}}</span> spins left.</h2>
        </div>
      </div>
      <div class="spin-result">
        <span class="animate-prize win-prize" v-if="spinResultStockAvailable == true">
          {{ spinResultMessage }}
        </span>
        <span class="animate-prize none-prize" v-else-if="spinResultStockAvailable == false">
          {{ spinResultMessage }}
        </span>
        <h2 v-else>Spin to try your luck!</h2>
      </div>
    </div>
    <div class="fortune-wheel-config">
      <h3>Configuration</h3>
      <div class="reset-buttons">
        <div class="button1">
          <button class="reset" @click="reset">Reset</button>
        </div>
        <div class="button2">
          <input
              type="number"
              v-model.number="attemptInput"
              placeholder="Enter attempts"
              class="attempt-input"
              min="0"
          />
          <button class="submit" @click="setAttempts">Submit</button>
        </div>
      </div>
      <div v-if="statusData">
        <h3>Prizes</h3>
        <table class="table-stats">
          <thead>
          <tr>
            <th>Prize</th>
            <th>Probability (%)</th>
            <th>QTY</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="row in prizeTable" :key="row.name">
            <td>{{ row.name }}</td>
            <td>{{ (row.probability * 100).toFixed(2) }}</td>
            <td>{{ row.quantity }}</td>
          </tr>
          </tbody>
        </table>

      </div>
      <div v-else>Loading...</div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';
import FortuneWheel from 'vue-fortune-wheel';
import 'vue-fortune-wheel/style.css';
import { Fireworks } from 'fireworks-js';

const prizeId = ref(0);
const prizeMessage = ref<string | null>(null);
const prizesCanvas = ref(null);
const statusData = ref<{ remainingAttempts, probabilities: Record<string, number>, prizes: Record<string, number> } | null>(null)
const attemptInput = ref<number | null>(null);
const spinResultPrizeId = ref<string | null>(null);
const spinResultMessage = ref<string | null>(null);
const stockAvailable = ref<boolean | null>(null);
const spinResultStockAvailable = ref<boolean | null>(null);
const spinDisable = ref(true);

let fireworksInstance: Fireworks | null = null;

const prizeTable = computed(() => {
  if (!statusData.value) return [];

  const probabilities = statusData.value.probabilities || {};
  const prizes = statusData.value.prizes || {};

  const allKeys = Array.from(new Set([...Object.keys(probabilities), ...Object.keys(prizes)]));

  return allKeys.map(key => ({
    name: key,
    probability: probabilities[key] ?? 0,
    quantity: key === "None" ? "∞" : (prizes[key] ?? 0)
  }));
});

onMounted(async () => {
  await loadStatus();
});

async function reset() {
  try {
    await axios.post('/fortune-wheel/reset');
    alert('Reset Succeed');
    await loadStatus(); // optional: refresh status
  } catch (e) {
    alert('Reset Failed.');
  }
}

async function setAttempts() {
  if (attemptInput.value == null || attemptInput.value < 0) {
    alert('Invalid Number');
    return;
  }

  try {
    await axios.post('/fortune-wheel/set-attempts', {
      attempts: attemptInput.value
    });
    alert(`Set the number of attempts to ${attemptInput.value}`);
    attemptInput.value = null;
    await loadStatus();
  } catch (e) {
    alert('Set attempts failed');
  }
}

async function onCanvasRotateStart(rotate: Function) {
  try {
    const response = await axios.post('/fortune-wheel/spin');
    prizeId.value = response.data.prize;
    prizeMessage.value = response.data.message;
    stockAvailable.value = response.data.stockAvailable;
  } catch (error) {
    console.error('Spin went wrong with error:', error);
    alert('Please try again later.');
  }
}

async function onRotateEnd() {
  await loadStatus();
  spinResultMessage.value = prizeMessage.value;
  spinResultPrizeId.value = prizeId.value.toString();
  spinResultStockAvailable.value = stockAvailable.value;

  if (spinResultPrizeId.value != 'None' && spinResultStockAvailable.value) {
    startFireworks();
  }
}

function startFireworks() {
  if (fireworksInstance) {
    fireworksInstance.clear();
  }

  const container = document.createElement('div');
  container.classList.add('fireworks-container');

  document.body.appendChild(container);

  fireworksInstance = new Fireworks(container, {
    intensity: 20,
    friction: 0.98,
  });

  fireworksInstance.start();

  setTimeout(() => {
    fireworksInstance?.stop();
    document.body.removeChild(container);
  }, 8000);
}

async function loadStatus() {
  const response = await axios.get('/fortune-wheel/status');
  statusData.value = response.data;
  spinDisable.value = true;

  const prizes = response.data.prizes;
  let totalPrizes = 0;

  for (const key in prizes) {
    totalPrizes += Number(prizes[key]);
  }

  if (statusData.value.remainingAttempts > 0 && totalPrizes > 0) {
    spinDisable.value = false;
    console.log("spinDisable", spinDisable.value);
  }

  prizesCanvas.value = Object.entries(statusData.value.probabilities).map(([name, prob]) => ({
    id: name,
    name: name,
    value: name,
    probability: prob * 100,
    color: '#ffffff',
    bgColor: getColor(name)
  }));
}

function getColor(name: string) {
  const colors: Record<string, string> = {
    A: '#45ace9',
    B: '#dd3832',
    C: '#fef151',
    None: '#ccc'
  };
  return colors[name] || '#fff';
}
</script>

<style src="@/assets/custom-styles.css"/>
