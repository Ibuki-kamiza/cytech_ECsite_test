(function() {
    const MAX = 6;
    const KEY = 'recentlyViewed';

    function getHistory() {
        try { return JSON.parse(localStorage.getItem(KEY)) || []; } catch(e) { return []; }
    }

    function saveHistory(items) {
        localStorage.setItem(KEY, JSON.stringify(items));
    }

    
    const match = location.pathname.match(/\/products\/(\d+)$/);
    if (match) {
        const productId = match[1];
        const title = document.querySelector('.info-table tr:first-child td')?.textContent || '';
        const img = document.querySelector('.product-detail img')?.src || '';
        const price = document.querySelector('.info-table tr:nth-child(4) td')?.textContent || '';

        let history = getHistory().filter(p => p.id !== productId);
        history.unshift({ id: productId, name: title, img: img, price: price });
        if (history.length > MAX) history = history.slice(0, MAX);
        saveHistory(history);
        return;
    }

    
    const section = document.getElementById('recently-viewed-section');
    const grid = document.getElementById('recently-viewed-grid');
    if (!section || !grid) return;

    const history = getHistory();
    if (history.length === 0) return;

    history.forEach(function(p) {
        const card = document.createElement('div');
        card.className = 'product-card';
        card.style.cursor = 'pointer';
        card.onclick = function() { location.href = '/cytech_ECsite_test/products/' + p.id; };
        card.innerHTML =
            '<img src="' + p.img + '" alt="商品画像" onerror="this.src=\'https://placehold.co/300x200\'">' +
            '<div class="product-card-body">' +
            '<p class="product-name">' + p.name + '</p>' +
            '<p class="product-price">' + p.price + '</p>' +
            '</div>';
        grid.appendChild(card);
    });

    section.style.display = 'block';
})();
