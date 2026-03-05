'use strict';

// パスワード表示切替
function togglePassword(btnEl) {
    const input = btnEl.previousElementSibling;
    if (input.type === 'password') {
        input.type = 'text';
        btnEl.textContent = '🙈';
    } else {
        input.type = 'password';
        btnEl.textContent = '👁';
    }
}

// 登録フォームのバリデーション
function validateRegisterForm(e) {
    const password = document.getElementById('password').value;
    const email = document.getElementById('email').value;

    if (password.length < 6) {
        e.preventDefault();
        showError('パスワードは6文字以上にしてください');
        return false;
    }

    if (!email.includes('@')) {
        e.preventDefault();
        showError('正しいメールアドレスを入力してください');
        return false;
    }
}

function showError(message) {
    let errorDiv = document.querySelector('.alert-error');
    if (!errorDiv) {
        errorDiv = document.createElement('div');
        errorDiv.className = 'alert-error';
        const form = document.querySelector('form');
        form.parentNode.insertBefore(errorDiv, form);
    }
    errorDiv.textContent = message;
}

// 初期化
document.addEventListener('DOMContentLoaded', function () {
    const registerForm = document.getElementById('register-form');
    if (registerForm) {
        registerForm.addEventListener('submit', validateRegisterForm);
    }
});
