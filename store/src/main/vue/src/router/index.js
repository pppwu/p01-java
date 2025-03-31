import { createRouter, createWebHistory } from 'vue-router';
import Home from '../components/Home.vue';
import Calculator from '../components/Calculator.vue';
import Lottery from '../components/Lottery.vue';

const routes = [
    { path: '/', component: Home },
    { path: '/q1-calculator', component: Calculator },
    { path: '/q2-lottery', component: Lottery },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
