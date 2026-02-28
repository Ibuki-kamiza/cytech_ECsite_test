// 数量変更時に金額を再計算
document.querySelectorAll('.quantity-select').forEach(function(select) {
    select.addEventListener('change', function() {
        const price = parseInt(this.dataset.price);
        const quantity = parseInt(this.value);
        const itemTotal = price * quantity;
        const totalEl = document.getElementById('item-total-' + this.dataset.cartId);
        if (totalEl) {
            totalEl.textContent = '¥' + itemTotal.toLocaleString();
        }
        updateGrandTotal();
    });
});

function updateGrandTotal() {
    let total = 0;
    document.querySelectorAll('.quantity-select').forEach(function(select) {
        const price = parseInt(select.dataset.price);
        const quantity = parseInt(select.value);
        total += price * quantity;
    });
    const grandTotal = document.getElementById('grand-total');
    if (grandTotal) {
        grandTotal.textContent = '¥' + total.toLocaleString();
    }
}