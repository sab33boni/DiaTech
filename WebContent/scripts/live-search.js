document.addEventListener('DOMContentLoaded', () => {
    const searchInput = document.getElementById('live-search-input');
    const resultsContainer = document.getElementById('live-search-results');

    if (!searchInput || !resultsContainer) return;

    let debounceTimer = null;
    const contextPath = window.contextPath || '';

    searchInput.addEventListener('input', (e) => {
        const query = e.target.value.trim();

        clearTimeout(debounceTimer);

        if (query.length < 2) {
            resultsContainer.innerHTML = '';
            resultsContainer.style.display = 'none';
            return;
        }

        debounceTimer = setTimeout(() => {
            fetch(`${contextPath}/live-search?q=${encodeURIComponent(query)}`)
                .then(response => {
                    if (!response.ok) throw new Error('Network response error');
                    return response.json();
                })
                .then(data => {
                    renderResults(data);
                })
                .catch(err => {
                    console.error('Live search error:', err);
                    resultsContainer.style.display = 'none';
                });
        }, 250);
    });

    function renderResults(products) {
        if (!products || products.length === 0) {
            resultsContainer.innerHTML = `
                <div class="search-result-empty">
                    Nessun dispositivo medico trovato
                </div>
            `;
            resultsContainer.style.display = 'block';
            return;
        }

        let html = '<ul class="search-results-list">';
        products.forEach(p => {
            html += `
                <li class="search-result-item">
                    <a href="${contextPath}/prodotto?id=${p.id}" class="search-result-link">
                        <img src="${contextPath}/images/prodotti/${p.immagine}" 
                             alt="${p.nome}" 
                             class="search-result-thumb"
                             onerror="this.src='${contextPath}/images/prodotti/default.png';">
                        <div class="search-result-info">
                            <span class="search-result-brand">${p.brand}</span>
                            <span class="search-result-title">${p.nome}</span>
                            <span class="search-result-category">${p.categoria}</span>
                        </div>
                        <div class="search-result-price">
                            ${Number(p.prezzo).toFixed(2)} €
                        </div>
                    </a>
                </li>
            `;
        });
        html += '</ul>';

        resultsContainer.innerHTML = html;
        resultsContainer.style.display = 'block';
    }

    document.addEventListener('click', (e) => {
        if (!searchInput.contains(e.target) && !resultsContainer.contains(e.target)) {
            resultsContainer.style.display = 'none';
        }
    });

    searchInput.addEventListener('focus', () => {
        if (searchInput.value.trim().length >= 2 && resultsContainer.innerHTML.trim() !== '') {
            resultsContainer.style.display = 'block';
        }
    });
});
