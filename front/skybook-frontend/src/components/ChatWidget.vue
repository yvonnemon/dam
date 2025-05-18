<template>
  <div>
    <!-- Floating chat bubble -->
    <div class="chat-bubble" @click="toggleChat">
      <span class="material-symbols-outlined">support_agent</span>
    </div>

    <!-- Chat window -->
    <div v-if="isOpen" class="chat-window">
      <div class="chat-header">
        Help Bot
        <span class="close" @click="toggleChat">&times;</span>
      </div>
      <div class="chat-body">
        <div v-for="(msg, index) in messages" :key="index" :class="msg.sender">
          <strong>{{ msg.sender === 'user' ? 'You' : 'Bot' }}:</strong> {{ msg.content }}
        </div>
      </div>
      <input
        v-model="input"
        @keyup.enter="sendMessage"
        placeholder="Type your question..."
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import SockJS from 'sockjs-client/dist/sockjs.min.js'
import { Client } from '@stomp/stompjs'
import { useI18n } from 'vue-i18n'
const { t } = useI18n()

const isOpen = ref(false)
const input = ref('')
const messages = ref([])
let stompClient = null

function toggleChat() {
  isOpen.value = !isOpen.value
}

function sendMessage() {
  if (!input.value.trim()) return
  const msg = { sender: 'user', content: input.value }
  messages.value.push(msg)

  stompClient.publish({
    destination: '/app/chat.send',
    body: JSON.stringify(msg)
  })

  input.value = ''
}

onMounted(() => {
  const socket = new SockJS('http://localhost:8080/helpbot')
  stompClient = new Client({
    webSocketFactory: () => socket,
    reconnectDelay: 5000,
    onConnect: () => {
      stompClient.subscribe('/topic/replies', (message) => {
        const botReply = JSON.parse(message.body)
        messages.value.push({
        sender: 'bot',
        content: t(`bot-translations.${botReply.content}`)
      })

      })
    },
  })
  stompClient.activate()
})

onBeforeUnmount(() => {
  stompClient?.deactivate()
})
</script>

<style scoped>
    .chat-bubble {
        position: fixed;
        bottom: 20px;
        right: 20px;
        background: #10b9ff;
        color: white;
        border-radius: 50%;
        padding: 14px;
        cursor: pointer;
        font-size: 24px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
        z-index: 1000;
        text-align: center;
    }

    .chat-window {
        position: fixed;
        bottom: 80px;
        right: 20px;
        width: 300px;
        background: white;
        border: 1px solid #ccc;
        border-radius: 10px;
        display: flex;
        flex-direction: column;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
        z-index: 1000;
    }

    .chat-header {
        background: #0A3B6C;
        border-radius: 10px;
        color: white;
        padding: 10px;
        font-weight: bold;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    .close {
        cursor: pointer;
        font-size: 18px;
    }

    .chat-body {
        padding: 10px;
        border-radius: 10px;
        max-height: 200px;
        overflow-y: auto;
        font-size: 14px;
        flex-grow: 1;
    }

    input {
        border: none;
        border-radius: 10px;
        border-top: 1px solid #ccc;
        padding: 10px;
        outline: none;
        font-size: 14px;
    }

    .user {
        text-align: right;
        margin-bottom: 5px;
    }

    .bot {
        text-align: left;
        margin-bottom: 5px;
    }
</style>
