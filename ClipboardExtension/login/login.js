import { login, testToken } from '../api.js';

document.addEventListener("DOMContentLoaded", function () {
  testToken()
    .then(result => {
      console.log(result);
      if (result) {
        window.location.href = '../popup.html';
      }
    });
});

document.getElementById('login-form').addEventListener('submit', async (event) => {
  event.preventDefault();

  const username = document.getElementById('username').value;
  const password = document.getElementById('password').value;

  showLoading();
  hideContent();

  try {
    const result = await login(username, password);
    if (result) {
      hideLoading();
      window.location.href = '../popup.html';
    }
  }
  catch (error) {
    hideLoading();
    showContent();
    window.alert('Login failed');
  }
});

document.getElementById('toggle-password').addEventListener('click', function (e) {
  const passwordInput = document.getElementById('password');
  if (passwordInput.type === 'password') {
    passwordInput.type = 'text';
    e.target.classList.remove('fa-eye');
    e.target.classList.add('fa-eye-slash');
  } else {
    passwordInput.type = 'password';
    e.target.classList.remove('fa-eye-slash');
    e.target.classList.add('fa-eye');
  }
});

function hideContent() {
  const content = document.getElementById("content");
  content.style.display = "none";
}

function showContent() {
  const content = document.getElementById("content");
  content.style.display = "flex";
}

function hideLoading() {
  const loading = document.getElementById("loading");
  loading.style.display = "none";
}

function showLoading() {
  const loading = document.getElementById("loading");
  loading.style.display = "block";
}