<template>
    <div v-if="visible" class="modal-backdrop">
      <div class="modal-box">
        <h3 class="modal-title">{{ title }}</h3>
        <p class="modal-message">{{ message }}</p>
        <div class="modal-actions">
          <button v-if="showConfirm" class="btn btn-confirm" @click="$emit('confirm')">Confirm</button>
          <button v-if="showCancel" class="btn btn-cancel" @click="$emit('cancel')">Close</button>
        </div>
      </div>
    </div>
  </template>
  
<script setup>
  defineProps({
  visible: Boolean,
  title: String,
  message: String,
  showConfirm: {
    type: Boolean,
    default: true
  },
  showCancel: {
    type: Boolean,
    default: true
  }
});
  defineEmits(['confirm', 'cancel']);
</script>
  
<style scoped>
  .modal-backdrop {
    position: fixed;
    inset: 0;
    background: rgba(0, 0, 0, 0.6);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
    animation: fadeIn 0.3s ease-out;
  }
  
  .modal-box {
    background: #fff;
    padding: 2rem;
    border-radius: 12px;
    width: 90%;
    max-width: 400px;
    box-shadow: 0 15px 30px rgba(0, 0, 0, 0.2);
    animation: slideIn 0.3s ease-out;
  }
  
  .modal-title {
    margin: 0 0 1rem;
    font-size: 1.5rem;
    font-weight: 600;
    color: #333;
  }
  
  .modal-message {
    font-size: 1rem;
    color: #555;
  }
  
  .modal-actions {
    display: flex;
    justify-content: flex-end;
    gap: 1rem;
    margin-top: 2rem;
  }
  
  .btn {
    padding: 0.6rem 1.2rem;
    font-size: 1rem;
    border: none;
    border-radius: 6px;
    cursor: pointer;
    transition: background 0.2s ease-in-out;
  }
  
  .btn-confirm {
    background-color: #4CAF50;
    color: white;
  }
  .btn-confirm:hover {
    background-color: #45A049;
  }
  
  .btn-cancel {
    background-color: #f44336;
    color: white;
  }
  .btn-cancel:hover {
    background-color: #d32f2f;
  }
  
  @keyframes fadeIn {
    from { opacity: 0; }
    to { opacity: 1; }
  }
  
  @keyframes slideIn {
    from {
      opacity: 0;
      transform: translateY(-20px);
    }
    to {
      opacity: 1;
      transform: translateY(0);
    }
  }
</style>
  