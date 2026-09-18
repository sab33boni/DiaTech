document.addEventListener('DOMContentLoaded', () => {
    const contextPath = window.contextPath || '';

    const REGEX_EMAIL = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    const REGEX_NOME = /^[a-zA-ZÀ-ÿ\s']{2,50}$/;
    const REGEX_CAP = /^\d{5}$/;
    const REGEX_TEL = /^(\+39\s?)?3\d{2}[-.\s]?\d{6,7}$/;
    const REGEX_CARD_NUM = /^(\d{4}[\s-]?){3}\d{4}$/;
    const REGEX_CARD_EXP = /^(0[1-9]|1[0-2])\/?([2-9][0-9])$/;
    const REGEX_CARD_CVV = /^\d{3}$/;

    function setError(input, errorSpanId, message) {
        if (!input) return;
        input.classList.add('input-error');
        input.classList.remove('input-valid');
        const span = document.getElementById(errorSpanId);
        if (span) {
            span.textContent = message;
            span.style.display = 'block';
        }
    }

    function clearError(input, errorSpanId) {
        if (!input) return;
        input.classList.remove('input-error');
        input.classList.add('input-valid');
        const span = document.getElementById(errorSpanId);
        if (span) {
            span.textContent = '';
            span.style.display = 'none';
        }
    }

    const formLogin = document.getElementById('form-login');
    if (formLogin) {
        const emailInput = document.getElementById('login-email');
        const passInput = document.getElementById('login-password');

        function validateLoginEmail() {
            const val = emailInput.value.trim();
            if (!val) {
                setError(emailInput, 'error-login-email', 'Inserisci il tuo indirizzo email.');
                return false;
            } else if (!REGEX_EMAIL.test(val)) {
                setError(emailInput, 'error-login-email', 'Formato email non valido (es. nome@dominio.it).');
                return false;
            }
            clearError(emailInput, 'error-login-email');
            return true;
        }

        function validateLoginPass() {
            const val = passInput.value;
            if (!val) {
                setError(passInput, 'error-login-password', 'Inserisci la password.');
                return false;
            }
            clearError(passInput, 'error-login-password');
            return true;
        }

        if (emailInput) {
            emailInput.addEventListener('blur', validateLoginEmail);
            emailInput.addEventListener('input', () => { if (emailInput.classList.contains('input-error')) validateLoginEmail(); });
        }
        if (passInput) {
            passInput.addEventListener('blur', validateLoginPass);
        }

        formLogin.addEventListener('submit', (e) => {
            const isEmailValid = validateLoginEmail();
            const isPassValid = validateLoginPass();

            if (!isEmailValid || !isPassValid) {
                e.preventDefault();
            }
        });
    }

    const formRegister = document.getElementById('form-register');
    if (formRegister) {
        const nome = document.getElementById('reg-nome');
        const cognome = document.getElementById('reg-cognome');
        const email = document.getElementById('reg-email');
        const pass = document.getElementById('reg-password');
        const confPass = document.getElementById('reg-conferma-password');
        const cap = document.getElementById('reg-cap');
        const tel = document.getElementById('reg-telefono');

        let isEmailUnique = true;

        function validateNome() {
            const val = nome.value.trim();
            if (!val) {
                setError(nome, 'error-reg-nome', 'Il nome è obbligatorio.');
                return false;
            } else if (!REGEX_NOME.test(val)) {
                setError(nome, 'error-reg-nome', 'Il nome può contenere solo lettere (min. 2 caratteri).');
                return false;
            }
            clearError(nome, 'error-reg-nome');
            return true;
        }

        function validateCognome() {
            const val = cognome.value.trim();
            if (!val) {
                setError(cognome, 'error-reg-cognome', 'Il cognome è obbligatorio.');
                return false;
            } else if (!REGEX_NOME.test(val)) {
                setError(cognome, 'error-reg-cognome', 'Il cognome può contenere solo lettere (min. 2 caratteri).');
                return false;
            }
            clearError(cognome, 'error-reg-cognome');
            return true;
        }

        function validateEmailFormat() {
            const val = email.value.trim();
            if (!val) {
                setError(email, 'error-reg-email', 'L\'indirizzo email è obbligatorio.');
                return false;
            } else if (!REGEX_EMAIL.test(val)) {
                setError(email, 'error-reg-email', 'Inserisci un formato email valido (es. nome@dominio.it).');
                return false;
            }
            clearError(email, 'error-reg-email');
            return true;
        }

        function checkEmailAjax() {
            if (!validateEmailFormat()) return;
            const val = email.value.trim();

            fetch(`${contextPath}/check-email?email=${encodeURIComponent(val)}`)
                .then(res => res.json())
                .then(data => {
                    if (data.exists) {
                        isEmailUnique = false;
                        setError(email, 'error-reg-email', 'Questo indirizzo email è già registrato.');
                    } else {
                        isEmailUnique = true;
                        clearError(email, 'error-reg-email');
                    }
                })
                .catch(() => {
                    isEmailUnique = true;
                });
        }

        function validatePassword() {
            const val = pass.value;
            if (!val) {
                setError(pass, 'error-reg-password', 'La password è obbligatoria.');
                return false;
            } else if (val.length < 8) {
                setError(pass, 'error-reg-password', 'La password deve avere almeno 8 caratteri.');
                return false;
            }
            clearError(pass, 'error-reg-password');
            return true;
        }

        function validateConfermaPassword() {
            const val = confPass.value;
            if (!val) {
                setError(confPass, 'error-reg-conferma-password', 'Conferma la tua password.');
                return false;
            } else if (val !== pass.value) {
                setError(confPass, 'error-reg-conferma-password', 'Le password inserite non coincidono.');
                return false;
            }
            clearError(confPass, 'error-reg-conferma-password');
            return true;
        }

        function validateCap() {
            const val = cap.value.trim();
            if (val && !REGEX_CAP.test(val)) {
                setError(cap, 'error-reg-cap', 'Il CAP deve essere di 5 cifre.');
                return false;
            }
            clearError(cap, 'error-reg-cap');
            return true;
        }

        function validateTel() {
            const val = tel.value.trim();
            if (val && !REGEX_TEL.test(val)) {
                setError(tel, 'error-reg-telefono', 'Inserisci un numero di cellulare valido.');
                return false;
            }
            clearError(tel, 'error-reg-telefono');
            return true;
        }

        if (nome) nome.addEventListener('blur', validateNome);
        if (cognome) cognome.addEventListener('blur', validateCognome);
        if (email) {
            email.addEventListener('blur', checkEmailAjax);
            email.addEventListener('change', checkEmailAjax);
        }
        if (pass) pass.addEventListener('blur', validatePassword);
        if (confPass) confPass.addEventListener('blur', validateConfermaPassword);
        if (cap) cap.addEventListener('blur', validateCap);
        if (tel) tel.addEventListener('blur', validateTel);

        formRegister.addEventListener('submit', (e) => {
            const v1 = validateNome();
            const v2 = validateCognome();
            const v3 = validateEmailFormat();
            const v4 = validatePassword();
            const v5 = validateConfermaPassword();
            const v6 = validateCap();
            const v7 = validateTel();

            if (!v1 || !v2 || !v3 || !v4 || !v5 || !v6 || !v7 || !isEmailUnique) {
                e.preventDefault();
            }
        });
    }

    const formCheckout = document.getElementById('form-checkout');
    if (formCheckout) {
        const checkNome = document.getElementById('check-nome');
        const checkCognome = document.getElementById('check-cognome');
        const checkIndirizzo = document.getElementById('check-indirizzo');
        const checkCitta = document.getElementById('check-citta');
        const checkCap = document.getElementById('check-cap');
        const checkTel = document.getElementById('check-telefono');

        const cardNum = document.getElementById('card-number');
        const cardExp = document.getElementById('card-expiry');
        const cardCvv = document.getElementById('card-cvv');

        function validateCheckField(input, errorId, message) {
            if (!input.value.trim()) {
                setError(input, errorId, message);
                return false;
            }
            clearError(input, errorId);
            return true;
        }

        function validateCheckCap() {
            const val = checkCap.value.trim();
            if (!val || !REGEX_CAP.test(val)) {
                setError(checkCap, 'error-check-cap', 'Inserisci un CAP valido di 5 cifre.');
                return false;
            }
            clearError(checkCap, 'error-check-cap');
            return true;
        }

        function validateCheckTel() {
            const val = checkTel.value.trim();
            if (!val || !REGEX_TEL.test(val)) {
                setError(checkTel, 'error-check-telefono', 'Inserisci un recapito telefonico valido per il corriere.');
                return false;
            }
            clearError(checkTel, 'error-check-telefono');
            return true;
        }

        function validateCardDetails() {
            const selectedMethod = document.querySelector('input[name="metodoPagamento"]:checked');
            if (selectedMethod && selectedMethod.value === 'Carta di Credito') {
                let valid = true;
                if (cardNum && !REGEX_CARD_NUM.test(cardNum.value.trim().replace(/\s+/g, ''))) {
                    setError(cardNum, 'error-card-number', 'Inserisci un numero di carta valido (16 cifre).');
                    valid = false;
                } else if (cardNum) {
                    clearError(cardNum, 'error-card-number');
                }

                if (cardExp && !REGEX_CARD_EXP.test(cardExp.value.trim())) {
                    setError(cardExp, 'error-card-expiry', 'Formato data non valido (MM/AA).');
                    valid = false;
                } else if (cardExp) {
                    clearError(cardExp, 'error-card-expiry');
                }

                if (cardCvv && !REGEX_CARD_CVV.test(cardCvv.value.trim())) {
                    setError(cardCvv, 'error-card-cvv', 'CVV di 3 cifre.');
                    valid = false;
                } else if (cardCvv) {
                    clearError(cardCvv, 'error-card-cvv');
                }
                return valid;
            }
            return true;
        }

        if (checkNome) checkNome.addEventListener('blur', () => validateCheckField(checkNome, 'error-check-nome', 'Nome destinatario obbligatorio.'));
        if (checkCognome) checkCognome.addEventListener('blur', () => validateCheckField(checkCognome, 'error-check-cognome', 'Cognome destinatario obbligatorio.'));
        if (checkIndirizzo) checkIndirizzo.addEventListener('blur', () => validateCheckField(checkIndirizzo, 'error-check-indirizzo', 'Indirizzo di consegna obbligatorio.'));
        if (checkCitta) checkCitta.addEventListener('blur', () => validateCheckField(checkCitta, 'error-check-citta', 'Città obbligatoria.'));
        if (checkCap) checkCap.addEventListener('blur', validateCheckCap);
        if (checkTel) checkTel.addEventListener('blur', validateCheckTel);

        formCheckout.addEventListener('submit', (e) => {
            const v1 = validateCheckField(checkNome, 'error-check-nome', 'Nome destinatario obbligatorio.');
            const v2 = validateCheckField(checkCognome, 'error-check-cognome', 'Cognome destinatario obbligatorio.');
            const v3 = validateCheckField(checkIndirizzo, 'error-check-indirizzo', 'Indirizzo di consegna obbligatorio.');
            const v4 = validateCheckField(checkCitta, 'error-check-citta', 'Città obbligatoria.');
            const v5 = validateCheckCap();
            const v6 = validateCheckTel();
            const v7 = validateCardDetails();

            if (!v1 || !v2 || !v3 || !v4 || !v5 || !v6 || !v7) {
                e.preventDefault();
            }
        });
    }
});
