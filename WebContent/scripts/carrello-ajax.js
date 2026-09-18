document.addEventListener('DOMContentLoaded', () => {
    const contextPath = window.contextPath || '';

    function showToast(message) {
        let toast = document.getElementById('cart-toast-notification');
        if (!toast) {
            toast = document.createElement('div');
            toast.id = 'cart-toast-notification';
            toast.className = 'cart-toast';
            document.body.appendChild(toast);
        }
        toast.innerHTML = message;
        toast.classList.add('show');
        setTimeout(() => {
            toast.classList.remove('show');
        }, 2500);
    }

    function updateBadge(count) {
        const badge = document.getElementById('cart-badge');
        if (badge) {
            badge.textContent = count;
            badge.classList.add('badge-bounce');
            setTimeout(() => badge.classList.remove('badge-bounce'), 400);
        }
    }

    document.addEventListener('click', (e) => {
        const addBtn = e.target.closest('.btn-add-cart-ajax');
        if (addBtn) {
            e.preventDefault();
            const idProdotto = addBtn.getAttribute('data-id');
            const nome = addBtn.getAttribute('data-nome') || 'Dispositivo';
            
            const qtyInput = document.getElementById('quantita-input');
            const quantita = qtyInput ? qtyInput.value : 1;

            const formData = new URLSearchParams();
            formData.append('action', 'add');
            formData.append('idProdotto', idProdotto);
            formData.append('quantita', quantita);

            fetch(`${contextPath}/carrello-ajax`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: formData.toString()
            })
            .then(res => res.json())
            .then(data => {
                if (data.success) {
                    updateBadge(data.totalCount);
                    showToast(`🛒 <strong>${nome}</strong> aggiunto al carrello!`);
                }
            })
            .catch(err => console.error('Errore aggiunta carrello AJAX:', err));
        }

        const plusBtn = e.target.closest('.btn-cart-plus');
        const minusBtn = e.target.closest('.btn-cart-minus');
        if (plusBtn || minusBtn) {
            const btn = plusBtn || minusBtn;
            const idProdotto = btn.getAttribute('data-id');
            const row = document.getElementById(`cart-row-${idProdotto}`);
            if (!row) return;

            const input = row.querySelector('.cart-qty-input');
            let currentQty = parseInt(input.value, 10);
            const maxQty = parseInt(input.getAttribute('max'), 10) || 99;

            if (plusBtn && currentQty < maxQty) {
                currentQty++;
            } else if (minusBtn && currentQty > 1) {
                currentQty--;
            } else if (minusBtn && currentQty <= 1) {
                return;
            }

            const formData = new URLSearchParams();
            formData.append('action', 'update');
            formData.append('idProdotto', idProdotto);
            formData.append('quantita', currentQty);

            fetch(`${contextPath}/carrello-ajax`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: formData.toString()
            })
            .then(res => res.json())
            .then(data => {
                if (data.success) {
                    input.value = currentQty;
                    updateBadge(data.totalCount);
                    
                    const subtotalEl = document.getElementById(`subtotal-${idProdotto}`);
                    if (subtotalEl) {
                        subtotalEl.textContent = `${data.rowSubtotal.toFixed(2)} €`;
                    }
                    
                    updateCartSummary(data.totalCount, data.cartTotal);
                }
            })
            .catch(err => console.error('Errore update quantità AJAX:', err));
        }

        const removeBtn = e.target.closest('.btn-remove-cart-ajax');
        if (removeBtn) {
            e.preventDefault();
            const idProdotto = removeBtn.getAttribute('data-id');
            const row = document.getElementById(`cart-row-${idProdotto}`);

            const formData = new URLSearchParams();
            formData.append('action', 'remove');
            formData.append('idProdotto', idProdotto);

            fetch(`${contextPath}/carrello-ajax`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: formData.toString()
            })
            .then(res => res.json())
            .then(data => {
                if (data.success) {
                    if (row) row.remove();
                    updateBadge(data.totalCount);
                    updateCartSummary(data.totalCount, data.cartTotal);

                    if (data.totalCount === 0) {
                        location.reload();
                    }
                }
            })
            .catch(err => console.error('Errore rimozione carrello AJAX:', err));
        }

        const clearBtn = e.target.closest('#btn-clear-cart');
        if (clearBtn) {
            e.preventDefault();
            if (!confirm('Sei sicuro di voler svuotare il carrello?')) return;

            const formData = new URLSearchParams();
            formData.append('action', 'clear');

            fetch(`${contextPath}/carrello-ajax`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: formData.toString()
            })
            .then(res => res.json())
            .then(data => {
                if (data.success) {
                    location.reload();
                }
            })
            .catch(err => console.error('Errore svuotamento carrello AJAX:', err));
        }
    });

    function updateCartSummary(count, total) {
        const itemsCount = document.getElementById('summary-items-count');
        const subtotal = document.getElementById('summary-subtotal');
        const totalEl = document.getElementById('summary-total');

        if (itemsCount) itemsCount.textContent = count;
        if (subtotal) subtotal.textContent = `${total.toFixed(2)} €`;
        if (totalEl) totalEl.textContent = `${total.toFixed(2)} €`;
    }
});
