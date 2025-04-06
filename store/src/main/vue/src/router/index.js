import { createRouter, createWebHistory } from 'vue-router';
import Home from '../components/Home.vue';
import Calculator from '../components/Calculator.vue';
import FortuneWheel from '../components/FortuneWheel.vue';

const routes = [
    { path: '/', component: Home },
    { path: '/q1-calculator', component: Calculator },
    { path: '/q2-fortune-wheel', component: FortuneWheel },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
