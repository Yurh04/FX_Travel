// src/api/train.js
import axios from 'axios'

const request = axios.create({
    baseURL: '/api',
    timeout: 5000,
    withCredentials: true
})

export const complete = (params) => request.post('/payment/complete', params)

export const fail = (params) => request.post('/payment/fail', params)

export const finish = (params) => request.post('/payment/finish', params)